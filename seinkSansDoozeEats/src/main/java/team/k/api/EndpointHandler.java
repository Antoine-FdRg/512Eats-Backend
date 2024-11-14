package team.k.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Log
public class EndpointHandler implements HttpHandler {
    private final Object controller;
    private final Method method;
    private final String methodType;
    private final List<String> paramNames;
    private Matcher matcher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String path;


    EndpointHandler(Object controller, Method method, String methodType, String path) {
        this.controller = controller;
        this.method = method;
        this.methodType = methodType;
        this.paramNames = extractParamNames(path);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!methodType.equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Méthode non autorisée
            return;
        }

        log.info("Handling "+exchange.getRequestURI().getPath());

        Object[] params;
        try {
            params = resolveParameters(exchange);
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, -1); // Mauvaise requête
            return;
        }

        try {
            String response = (String) method.invoke(controller, params);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Erreur serveur
            e.printStackTrace();
        }
    }

    private Object[] resolveParameters(HttpExchange exchange) throws IOException {
        Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                String paramName = parameter.getAnnotation(PathVariable.class).value();
                int paramIndex = getParamIndex(paramName);
                params[i] = matcher.group(paramIndex + 1); // +1 car les groupes commencent à 1
            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                params[i] = parseRequestBody(exchange.getRequestBody(), parameter.getType());
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                String paramName = parameter.getAnnotation(RequestParam.class).value();
                params[i] = getQueryParam(exchange, paramName);
            }
        }
        return params;
    }

    private Object parseRequestBody(InputStream requestBody, Class<?> targetType) throws IOException {
        return objectMapper.readValue(requestBody, targetType);
    }

    private String getQueryParam(HttpExchange exchange, String paramName) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals(paramName) && keyValue.length == 2) {
                return keyValue[1];
            }
        }
        return null;
    }

    private List<String> extractParamNames(String path) {
        List<String> paramNamesInternal = new ArrayList<>();
        matcher = Pattern.compile("\\{(\\w+)}").matcher(path);
        while (matcher.find()) {
            paramNamesInternal.add(matcher.group(1));
        }
        return paramNamesInternal;
    }

    private int getParamIndex(String paramName) {
        return paramNames.indexOf(paramName);
    }
}

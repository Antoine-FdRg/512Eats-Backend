package team.k.api;

import com.sun.net.httpserver.HttpExchange;
import lombok.extern.java.Log;

import java.io.IOException;
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

@Log
public class EndpointHandler {
    private final Object controller;
    private final Method method;
    private final String methodType;
    private final List<String> paramNames;

    EndpointHandler(Object controller, Method method, String methodType, String originalPath) {
        this.controller = controller;
        this.method = method;
        this.methodType = methodType;
        this.paramNames = extractParamNames(originalPath);
    }

    private List<String> extractParamNames(String path) {
        List<String> paramNames = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{(\\w+)}").matcher(path);
        while (matcher.find()) {
            paramNames.add(matcher.group(1));
        }
        return paramNames;
    }

    public void handle(HttpExchange exchange, Matcher matcher) throws IOException {
        if (!methodType.equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Méthode non autorisée
            return;
        }

        try {
            // Parse les paramètres de la requête
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

            // Crée un tableau d'arguments pour les paramètres de la méthode
            Object[] params = new Object[method.getParameterCount()];
            Parameter[] parameters = method.getParameters();

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(PathVariable.class)) {
                    // Traite les PathVariables
                    PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
                    String paramName = pathVariable.value();
                    int paramIndex = paramNames.indexOf(paramName);
                    if (paramIndex != -1) {
                        params[i] = matcher.group(paramIndex + 1);
                    } else {
                        throw new IllegalArgumentException("Paramètre de chemin manquant : " + paramName);
                    }
                } else if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                    // Traite les RequestParams
                    RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                    String paramName = requestParam.value();
                    if (queryParams.containsKey(paramName)) {
                        params[i] = queryParams.get(paramName);
                    } else {
                        throw new IllegalArgumentException("Paramètre de requête manquant : " + paramName);
                    }
                }
            }

            // Appel de la méthode avec les paramètres extraits
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

    // Méthode pour analyser les paramètres de requête
    private Map<String, String> parseQueryParams(String query) {
        if (query == null || query.isEmpty()) {
            return Map.of();
        }
        return Stream.of(query.split("&"))
                .map(param -> param.split("=", 2))
                .collect(Collectors.toMap(
                        p -> p[0],  // Nom du paramètre
                        p -> p.length > 1 ? p[1] : ""  // Valeur du paramètre ou chaîne vide
                ));
    }
}

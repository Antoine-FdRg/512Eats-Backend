package ssdbrestframework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Setter;
import lombok.extern.java.Log;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Log
public class SSDBHandler implements HttpHandler {
    private final Object controller;
    private final Method method;
    private final HttpMethod methodType;
    private final List<String> paramNames;
    private Matcher matcher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    SSDBHandler(Object controller, Method method, HttpMethod methodType, String path) {
        this.controller = controller;
        this.method = method;
        this.methodType = methodType;
        this.paramNames = extractPathvariablesNames(path);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!methodType.name().equalsIgnoreCase(exchange.getRequestMethod())) {
                throw new SSDBQueryProcessingException(SSDBResponse.METHOD_NOT_ALLOWED, SSDBQueryProcessingException.METHOD_NOT_ALLOWED);
            }

            log.info("Handling " + exchange.getRequestURI().getPath());

            Object[] params;
            params = resolveParameters(exchange);

            Object result;
            result = invokeMethod(params);
            log.info("Result: " + result);

            sendResultToExchange(exchange, result);
        } catch (
                SSDBQueryProcessingException exception) {
            sendException(exchange, exception);
        }
    }

    /**
     * Send the result to the exchange with the appropriate status code
     * @param exchange The exchange to send the response to
     * @param result The result to send
     * @throws IOException If an I/O error occurs
     * @throws SSDBQueryProcessingException If an error occurs during the response writing
     */
    private void sendResultToExchange(HttpExchange exchange, Object result) throws IOException, SSDBQueryProcessingException {
        int statusCode = SSDBResponse.OK; // Code de statut par défaut
        String responseString;
        if (method.isAnnotationPresent(Response.class)) {
            Response responseAnnotation = method.getAnnotation(Response.class);
            statusCode = responseAnnotation.status();
            if (method.getReturnType() == void.class && !"".equals(responseAnnotation.message())) {
                result = new SSDBResponse(responseAnnotation.message());
            } else if (method.getReturnType() != void.class && !"".equals(responseAnnotation.message())) {
                log.warning(controller.getClass().getName() + "." + method.getName() + " is annotated with a Response message but is not void, the message is not sent.");
            }
        }
        responseString = formatResultAsString(result);
        if (responseString != null) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, responseString.getBytes().length);
            writeReponse(exchange, responseString);
        }
        exchange.sendResponseHeaders(statusCode, -1);
    }

    /**
     * Format the result as a string by serializing it with Jackson if necessary
     * @param result The result to format
     * @return The formatted result as a string
     * @throws JsonProcessingException If an error occurs during the serialization
     */
    private String formatResultAsString(Object result) throws JsonProcessingException {
        String responseString;
        if (!Objects.isNull(result) && result instanceof String stringResult) {
            responseString = stringResult;
        } else {
            responseString = objectMapper.writeValueAsString(result);
            log.info("Response: " + responseString);
        }
        return responseString;
    }

    /**
     * Send an exception to the client with the appropriate status code
     *
     * @param exchange  The exchange to send the response to
     * @param exception The exception to send
     * @throws IOException If an I/O error occurs
     */
    private void sendException(HttpExchange exchange, SSDBQueryProcessingException exception) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String errorMessage = objectMapper.writeValueAsString(exception);
        exchange.sendResponseHeaders(exception.getStatusCode(), errorMessage.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(errorMessage.getBytes());
        }
        log.severe(exception.toString());
    }

    /**
     * Write the response in the given exchange
     *
     * @param exchange       The exchange to send the response to
     * @param responseString The response to send
     * @throws SSDBQueryProcessingException If an error occurs during the response writing
     */
    private void writeReponse(HttpExchange exchange, String responseString) throws SSDBQueryProcessingException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseString.getBytes());
        } catch (IOException e) {
            throw new SSDBQueryProcessingException(SSDBResponse.INTERNAL_SERVER_ERROR, SSDBQueryProcessingException.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Invoke the method with the given parameters
     *
     * @param params The parameters to pass to the method
     * @return The result of the method
     * @throws SSDBQueryProcessingException If an error occurs during the method invocation
     */
    private Object invokeMethod(Object[] params) throws SSDBQueryProcessingException {
        Object result;
        try {
            result = method.invoke(controller, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SSDBQueryProcessingException(SSDBResponse.INTERNAL_SERVER_ERROR, SSDBQueryProcessingException.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new SSDBQueryProcessingException(SSDBResponse.BAD_REQUEST, SSDBQueryProcessingException.MAL_FORMED_PARAMS, e.getMessage());
        }
        return result;
    }

    /**
     * Resolve the parameters of received request
     *
     * @param exchange The exchange to get the parameters from
     * @return The resolved parameters
     * @throws SSDBQueryProcessingException If the parameters cannot be resolved
     */
    private Object[] resolveParameters(HttpExchange exchange) throws SSDBQueryProcessingException {
        Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                params[i] = parseRequestBody(exchange.getRequestBody(), parameter.getType());
            } else {
                Object rawParam = null;
                if (parameter.isAnnotationPresent(PathVariable.class)) {
                    String paramName = parameter.getAnnotation(PathVariable.class).value();
                    int paramIndex = paramNames.indexOf(paramName);
                    rawParam = matcher.group(paramIndex + 1); // +1 car les groupes commencent à 1
                } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                    String paramName = parameter.getAnnotation(RequestParam.class).value();
                    rawParam = getQueryParam(exchange, paramName);
                }
                params[i] = convertToExpectedType(rawParam, parameter.getType());
            }
        }
        for (Object param : params) {
            log.info("Param: " + param);
        }
        return params;
    }

    /**
     * Convert the raw parameter to the expected type
     *
     * @param rawParam   The raw parameter
     * @param targetType The expected type
     * @return The converted parameter
     * @throws SSDBQueryProcessingException If the parameter cannot be converted
     */
    private Object convertToExpectedType(Object rawParam, Class<?> targetType) throws SSDBQueryProcessingException {
        if (rawParam == null) {
            return null;
        }
        String rawParamStr = rawParam.toString();
        if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(rawParamStr);
        } else if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(rawParamStr);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(rawParamStr);
        } else if (targetType == String.class) {
            return rawParamStr;
        } else {
            try {
                return objectMapper.readValue(rawParamStr, targetType);
            } catch (IOException e) {
                throw new SSDBQueryProcessingException(SSDBResponse.BAD_REQUEST, SSDBQueryProcessingException.MAL_FORMED_PARAMS);
            }
        }
    }

    /**
     * Parse the request body to the expected type
     *
     * @param requestBody The request body
     * @param targetType  The expected type
     * @return The parsed request body
     * @throws SSDBQueryProcessingException If the request body cannot be parsed
     */
    private Object parseRequestBody(InputStream requestBody, Class<?> targetType) throws SSDBQueryProcessingException {
        try {
            return objectMapper.readValue(requestBody, targetType);
        } catch (IOException e) {
            throw new SSDBQueryProcessingException(SSDBResponse.BAD_REQUEST, SSDBQueryProcessingException.MAL_FORMED_PARAMS, e.getMessage());
        }
    }

    /**
     * Get the query parameter from the exchange
     *
     * @param exchange  The exchange to get the parameter from
     * @param paramName The name of the parameter to get
     * @return The value of the parameter
     */
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

    /**
     * Extract the path variables names from the path
     *
     * @param path The path to extract the names from
     * @return The list of path variables names
     */
    private List<String> extractPathvariablesNames(String path) {
        List<String> paramNamesInternal = new ArrayList<>();
        matcher = Pattern.compile("\\{(\\w+)}").matcher(path);
        while (matcher.find()) {
            paramNamesInternal.add(matcher.group(1));
        }
        return paramNamesInternal;
    }

}

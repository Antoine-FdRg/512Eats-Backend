package ssdbrestframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.java.Log;
import org.reflections.Reflections;
import ssdbrestframework.annotations.RestController;
import ssdbrestframework.annotations.Endpoint;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import java.io.IOException;

/**
 * Classe principale pour le serveur HTTP SeinkSansDoozeBank
 */
@Log
public class SSDBHttpServer {
    private final HttpServer server;
    private final Map<String, Map<Pattern, SSDBHandler>> routesByController = new HashMap<>();

    // Méthode pour démarrer le serveur
    public void start() {
        server.start();
        log.info("Serveur démarré sur le port " + server.getAddress().getPort());
    }

    public SSDBHttpServer(int port, String basePackage) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.registerControllers(basePackage);
    }

    private void registerControllers(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        try {
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(RestController.class);

            for (Class<?> controllerClass : controllers) {
                if (controllerClass.isAnnotationPresent(RestController.class)) {
                    registerController(controllerClass);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void registerController(Class<?> clazz) {
        RestController restController = clazz.getAnnotation(RestController.class);
        String basePath = restController.path();  // Récupère le préfixe de la classe
        routesByController.put(basePath, new HashMap<>());
        Object controllerInstance = null;
        try {
            controllerInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        registerRoutes(controllerInstance, basePath);

        // Définir le gestionnaire pour traiter toutes les requêtes avec les routes enregistrées
        server.createContext(basePath, exchange -> preProcessQuery(exchange, basePath));
    }

    private void preProcessQuery(HttpExchange exchange, String basePath) throws IOException {
        String path = exchange.getRequestURI().getPath();
        boolean found = false;

        for (Map.Entry<Pattern, SSDBHandler> entry : routesByController.get(basePath).entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(path);
            if (matcher.matches()) {
                entry.getValue().setMatcher(matcher);
                entry.getValue().handle(exchange);
                found = true;
                break;
            }
        }

        try {
            if (!found) {
                throw new SSDBQueryProcessingException(404, "Endpoint not found");
            }
        } catch (SSDBQueryProcessingException e) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String errorMessage = new ObjectMapper().writeValueAsString(e);
            exchange.sendResponseHeaders(e.getStatusCode(), errorMessage.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorMessage.getBytes());
            }
            log.severe(e.toString());
        }
    }

    // Enregistre les méthodes annotées avec @WebRoute dans un contrôleur donné
    // Enregistre les méthodes annotées avec @WebRoute dans un contrôleur donné
    private void registerRoutes(Object controller, String basePath) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Endpoint.class)) {
                registerRoute(controller, basePath, method);
            }
        }


    }

    private void registerRoute(Object controller, String basePath, Method method) {
        Endpoint annotation = method.getAnnotation(Endpoint.class);
        String path = basePath + annotation.path();  // Ajoute le préfixe de classe au chemin
        String methodType = annotation.method();

        // Création d'un pattern pour gérer les chemins avec paramètres
        Pattern pattern = Pattern.compile(path.replaceAll("\\{\\w+}", "([^/]+)"));

        // Crée un handler qui appelle la méthode annotée
        SSDBHandler handler = new SSDBHandler(controller, method, methodType, path);
        routesByController.get(basePath).put(pattern, handler);

        // Crée un contexte unique pour cette route et associe le handler
        server.createContext(pattern.pattern(), handler);

        log.info("Route enregistrée : " + methodType + " " + pattern.pattern());
    }
}

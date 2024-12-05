package ssdbrestframework;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.file.Paths;

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
 * Main class to use in the SSDB REST framework.
 * This class is responsible for starting the HTTP server and registering controllers
 */
@Log
public class SSDBHttpServer {
    private final HttpServer server;
    private final Map<String, Map<Pattern, SSDBHandler>> routesByController = new HashMap<>();
    private String folderPath = "";

    /**
     * Constructor for the SSDBHttpServer
     *
     * @param port        the port on which the server should run
     * @param basePackage the base package to scan for controllers. The path should be relative to the project root
     */
    public SSDBHttpServer(int port, String basePackage) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.registerControllers(basePackage);
    }

    public SSDBHttpServer(int port, String basePackage, String folderPath) {
        this(port, basePackage);
        this.folderPath = folderPath;
        this.generateOpenApiDocumentation(basePackage);
    }

    /**
     * Starts the server
     */
    public void start() {
        server.start();
        log.info("Serveur démarré sur le port " + server.getAddress().getPort());
    }

    /**
     * Registers controllers in the given package
     *
     * @param basePackage the base package to scan for controllers
     */
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

    /**
     * Registers a controller
     *
     * @param clazz the controller class to register
     */
    private void registerController(Class<?> clazz) {
        RestController restController = clazz.getAnnotation(RestController.class);
        String basePath = restController.path();  // Récupère le préfixe de la classe
        if (routesByController.containsKey(basePath)) {
            throw new RuntimeException("Un contrôleur avec le chemin de base " + basePath + " est déjà enregistré");
        }
        routesByController.put(basePath, new HashMap<>());
        Object controllerInstance;
        try {
            controllerInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        registerEndpoints(controllerInstance, basePath);

        // Définir le gestionnaire pour traiter toutes les requêtes avec les routes enregistrées
        server.createContext(basePath, exchange -> preProcessQuery(exchange, basePath));
    }

    /**
     * Pre-processes the query and calls the appropriate handler
     *
     * @param exchange the HttpExchange object
     * @param basePath the base path for the controller
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Register endpoints for a given controller
     *
     * @param controller The controller for which the routes should be registered
     * @param basePath   The base path for the controller
     */
    private void registerEndpoints(Object controller, String basePath) {
        for (Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(Endpoint.class)) {
                registerEndpoint(controller, basePath, method);
            }
        }
    }

    /**
     * Register a route for a given controller
     *
     * @param controller The controller for which the route should be registered
     * @param basePath   The base path for the controller
     * @param method     The method to register, that will be called when the endoint is hit
     */
    private void registerEndpoint(Object controller, String basePath, Method method) {
        Endpoint annotation = method.getAnnotation(Endpoint.class);
        String path = basePath + annotation.path();  // Ajoute le préfixe de classe au chemin
        HttpMethod methodType = annotation.method();

        // Création d'un pattern pour gérer les chemins avec paramètres
        Pattern pattern = Pattern.compile(path.replaceAll("\\{\\w+}", "([^/]+)"));
        // ajoute un caracterer de fin de chaine pour éviter les chemins qui ne correspondent pas
        pattern = Pattern.compile(pattern.pattern() + "$");

        // Crée un handler qui appelle la méthode annotée
        SSDBHandler handler = new SSDBHandler(controller, method, methodType, path);
        routesByController.get(basePath).put(pattern, handler);

        // Crée un contexte unique pour cette route et associe le handler
        server.createContext(pattern.pattern(), handler);

        log.info("Route enregistrée : " + methodType + " " + pattern.pattern());
    }

    /**
     * Génère la documentation OpenAPI pour le package donné
     *
     * @param basePackage le package pour lequel générer la documentation
     */
    private void generateOpenApiDocumentation(String basePackage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode openApiSpec = OpenApiGenerator.generate(basePackage);
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(Paths.get(folderPath + "openapi.json").toFile(), openApiSpec);

            log.info("Documentation OpenAPI générée : openapi.json");
        } catch (Exception e) {
            log.severe("Échec de la génération de la documentation OpenAPI : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

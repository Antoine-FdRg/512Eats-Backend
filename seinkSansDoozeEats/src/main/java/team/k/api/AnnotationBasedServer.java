package team.k.api;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.java.Log;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
public class AnnotationBasedServer {
    private static final Map<Pattern, EndpointHandler> routes = new HashMap<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Enregistrer les routes automatiquement
        registerRoutes(new RestController());

        // Créer un contexte pour toutes les routes
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();

            for (Map.Entry<Pattern, EndpointHandler> route : routes.entrySet()) {
                Matcher matcher = route.getKey().matcher(path);
                if (matcher.matches()) {
                    route.getValue().handle(exchange, matcher);
                    return;
                }
            }
            exchange.sendResponseHeaders(404, -1); // 404 Not Found
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    // Enregistrer les routes en utilisant la réflexion pour gérer les paramètres de chemin
    public static void registerRoutes(Object controller) throws Exception {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                WebRoute annotation = method.getAnnotation(WebRoute.class);
                String path = annotation.path();
                String methodType = annotation.method();

                // Création d'un pattern pour gérer les chemins avec paramètres
                Pattern pattern = Pattern.compile(path.replaceAll("\\{\\w+}", "([^/]+)"));

                // Crée un handler qui appelle la méthode annotée, en ajoutant le chemin d'origine
                EndpointHandler handler = new EndpointHandler(controller, method, methodType, path);
                routes.put(pattern, handler);
            }
        }
    }
}

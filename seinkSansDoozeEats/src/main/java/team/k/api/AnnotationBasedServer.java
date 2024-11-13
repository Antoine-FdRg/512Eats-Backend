package team.k.api;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.java.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import java.io.IOException;


@Log
public class AnnotationBasedServer {
    private final HttpServer server;
    private final Map<String, Map<Pattern, EndpointHandler>> routesByController = new HashMap<>();

    public static void main(String[] args) throws Exception {
        AnnotationBasedServer serv =  new AnnotationBasedServer(8080);
        serv.registerControllers("team.k.api");
        serv.start();
    }

    // Méthode pour démarrer le serveur
    public void start() {
        server.start();
        log.info("Serveur démarré sur le port " + server.getAddress().getPort());
    }

    public AnnotationBasedServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void registerControllers(String basePackage) {
        try {
            for (Class<?> clazz : getClasses(basePackage)) {
                if (clazz.isAnnotationPresent(RestController.class)) {
                    registerController(clazz);
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
        server.createContext(basePath, exchange -> {
            String path = exchange.getRequestURI().getPath();
            boolean found = false;

            for (Map.Entry<Pattern, EndpointHandler> entry : routesByController.get(basePath).entrySet()) {
                Pattern pattern = entry.getKey();
                Matcher matcher = pattern.matcher(path);
                if (matcher.matches()) {
                    entry.getValue().setMatcher(matcher);
                    entry.getValue().handle(exchange);
                    found = true;
                    break;
                }
            }

            if (!found) {
                exchange.sendResponseHeaders(404, -1); // Route non trouvée
            }
        });
    }

    // Enregistre les méthodes annotées avec @WebRoute dans un contrôleur donné
    // Enregistre les méthodes annotées avec @WebRoute dans un contrôleur donné
    private void registerRoutes(Object controller, String basePath) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                registerRoute(controller, basePath, method);
            }
        }


    }

    private void registerRoute(Object controller, String basePath, Method method) {
        log.info("registering route "+basePath+ " + "+method.getName());
            WebRoute annotation = method.getAnnotation(WebRoute.class);
            String path = basePath + annotation.path();  // Ajoute le préfixe de classe au chemin
            String methodType = annotation.method();

            // Création d'un pattern pour gérer les chemins avec paramètres
            Pattern pattern = Pattern.compile(path.replaceAll("\\{\\w+}", "([^/]+)"));

            // Crée un handler qui appelle la méthode annotée
            EndpointHandler handler = new EndpointHandler(controller, method, methodType, path);
            routesByController.get(basePath).put(pattern, handler);

            // Crée un contexte unique pour cette route et associe le handler
            server.createContext(pattern.pattern(), handler);

            log.info("Route enregistrée : " + methodType + " " + pattern.pattern());
    }

    // Utilitaire pour obtenir toutes les classes dans un package donné
    private Set<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        Set<Class<?>> classes = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            if (directory.exists() && directory.isDirectory()) {
                for (File file : directory.listFiles()) {
                    if (file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().replace(".class", "");
                        log.info("adding class "+className+" dans la liste des classes trouvées dans le package");
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }
}

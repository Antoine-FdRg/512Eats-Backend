package team.k.restaurantservice;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ssdbrestframework.SSDBHttpServer;

@Service
public class RestaurantAPI {
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(RestaurantConfig.class);
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.restaurantservice", "restaurantservice/", context);
        serv.start();
    }

}

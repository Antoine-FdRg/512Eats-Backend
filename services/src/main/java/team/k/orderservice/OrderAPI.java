package team.k.orderservice;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ssdbrestframework.SSDBHttpServer;

@Service
public class OrderAPI {
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(OrderConfig.class);
        SSDBHttpServer serv = new SSDBHttpServer(8086, "team.k.orderservice", "orderservice/", context);
        serv.start();
    }

}

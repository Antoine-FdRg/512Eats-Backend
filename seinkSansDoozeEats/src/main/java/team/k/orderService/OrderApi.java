package team.k.orderService;

import commonlibrary.model.payment.PaymentProcessor;
import commonlibrary.repository.GroupOrderRepository;
import commonlibrary.repository.IndividualOrderRepository;
import commonlibrary.repository.LocationRepository;
import commonlibrary.repository.RegisteredUserRepository;
import commonlibrary.repository.RestaurantRepository;
import commonlibrary.repository.SubOrderRepository;
import ssdbrestframework.SSDBHttpServer;

public class OrderApi {

    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8086, "team.k.orderService");

        GroupOrderRepository groupOrderRepository = new GroupOrderRepository();
        LocationRepository locationRepository = new LocationRepository();
        SubOrderRepository subOrderRepository = new SubOrderRepository();
        RestaurantRepository restaurantRepository = new RestaurantRepository();
        RegisteredUserRepository registeredUserRepository = new RegisteredUserRepository();
        PaymentProcessor paymentProcessor = new PaymentProcessor();


        OrderService orderService = new OrderService(
                groupOrderRepository,
                locationRepository,
                subOrderRepository,
                restaurantRepository,
                registeredUserRepository,
                paymentProcessor
        );

        //TODO : vérifier que c'est utile
        OrderController orderController = new OrderController(orderService);
        try {
            serv.registerController(OrderController.class);
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de l'enregistrement du contrôleur : " + e.getMessage());
            e.printStackTrace();
        }

        serv.start();
    }
}

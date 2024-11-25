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
        serv.start();
    }
}

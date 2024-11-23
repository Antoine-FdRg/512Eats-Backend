package team.k.restaurantService;

import ssdbrestframework.SSDBHttpServer;

public class RestaurantApi {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.restaurantService");
        serv.start();
    }
}

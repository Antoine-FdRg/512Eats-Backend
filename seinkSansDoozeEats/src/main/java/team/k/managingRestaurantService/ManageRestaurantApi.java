package team.k.managingRestaurantService;

import ssdbrestframework.SSDBHttpServer;


public class ManageRestaurantApi {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.managingRestaurantService");
        serv.start();
    }
}

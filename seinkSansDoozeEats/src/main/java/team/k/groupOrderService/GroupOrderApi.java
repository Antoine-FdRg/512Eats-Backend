package team.k.groupOrderService;

import ssdbrestframework.SSDBHttpServer;

public class GroupOrderApi {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8085, "team.k.groupOrderService");
        serv.start();
    }
}

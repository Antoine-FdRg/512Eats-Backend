package team.k;

import ssdbrestframework.SSDBHttpServer;

public class ServicesAPI {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.controllers");
        serv.start();
    }
}

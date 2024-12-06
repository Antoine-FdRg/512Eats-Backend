package team.k;

import ssdbrestframework.SSDBHttpServer;

public class APIServer {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8081, "team.k","routing/");
        serv.start();
    }
}
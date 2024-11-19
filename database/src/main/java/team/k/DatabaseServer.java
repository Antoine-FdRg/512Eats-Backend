package team.k;

import ssdbrestframework.SSDBHttpServer;

public class DatabaseServer {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8082, "team.k");
        serv.start();
    }
}
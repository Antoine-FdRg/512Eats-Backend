package team.k.api.examples;

import team.k.api.SSDBHttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        SSDBHttpServer serv = new SSDBHttpServer(8080);
        serv.registerControllers("team.k");
        serv.start();
    }
}

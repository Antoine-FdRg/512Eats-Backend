package ssdbrestframework.examples;

import ssdbrestframework.SSDBHttpServer;

public class ExampleServer {

    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8080,"ssdbrestframework");
        serv.start();
    }
}

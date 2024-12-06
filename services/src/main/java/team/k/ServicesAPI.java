package team.k;

import commonlibrary.model.Location;
import ssdbrestframework.SSDBHttpServer;
import team.k.repository.LocationRepository;

public class ServicesAPI {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.controller");
        Location location = new Location.Builder()
                .setNumber("123")
                .setAddress("ABC")
                .setCity("DEF")
                .build();
        LocationRepository.add(location);
        serv.start();
    }
}

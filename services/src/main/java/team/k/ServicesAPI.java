package team.k;

import commonlibrary.model.Location;
import ssdbrestframework.SSDBHttpServer;
import team.k.repository.LocationRepository;

public class ServicesAPI {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.controller");
        Location location = new Location.Builder()
                .setNumber("2400")
                .setAddress("Route des Dollines")
                .setCity("Valbonne")
                .build();
        LocationRepository.add(location);
        serv.start();
    }
}

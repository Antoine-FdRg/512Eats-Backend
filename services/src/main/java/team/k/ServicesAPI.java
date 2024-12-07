package team.k;

import commonlibrary.enumerations.Role;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import ssdbrestframework.SSDBHttpServer;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;

import java.time.LocalDateTime;

public class ServicesAPI {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.controller");
        initDataset();
        serv.start();
    }

    private static void initDataset() {
        initLocationData();
        initRegisteredUsersData();
        initGroupOrderData();
    }

    private static void initGroupOrderData() {
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocationID(3)
                .withDate(LocalDateTime.of(2024, 12, 18, 12, 0))
                .build();
        GroupOrderRepository.add(groupOrder);
        GroupOrder groupOrder2 = new GroupOrder.Builder()
                .withDeliveryLocationID(0)
                .build();
        GroupOrderRepository.add(groupOrder2);
    }

    private static void initRegisteredUsersData() {
        RegisteredUser user1 = new RegisteredUser("Jean Dupont", Role.STUDENT);
        RegisteredUser user2 = new RegisteredUser("Marie Curie", Role.STUDENT);
        RegisteredUser user3 = new RegisteredUser("Pierre Martin", Role.STUDENT);
        RegisteredUser user4 = new RegisteredUser("Sophie Bernard", Role.STUDENT);
        RegisteredUser user5 = new RegisteredUser("Lucie Dujardin", Role.STUDENT);
        RegisteredUser user6 = new RegisteredUser("Paul Durand", Role.RESTAURANT_MANAGER);
        RegisteredUser user7 = new RegisteredUser("Claire Dubois", Role.RESTAURANT_MANAGER);
        RegisteredUser user8 = new RegisteredUser("Marc Petit", Role.CAMPUS_EMPLOYEE);
        RegisteredUser user9 = new RegisteredUser("Nathalie Moreau", Role.CAMPUS_EMPLOYEE);
        RegisteredUser user10 = new RegisteredUser("Julien Robert", Role.CAMPUS_EMPLOYEE);

        RegisteredUserRepository.add(user1);
        RegisteredUserRepository.add(user2);
        RegisteredUserRepository.add(user3);
        RegisteredUserRepository.add(user4);
        RegisteredUserRepository.add(user5);
        RegisteredUserRepository.add(user6);
        RegisteredUserRepository.add(user7);
        RegisteredUserRepository.add(user8);
        RegisteredUserRepository.add(user9);
        RegisteredUserRepository.add(user10);
    }

    private static void initLocationData() {
        Location location = new Location.Builder()
                .setNumber("650")
                .setAddress("Route des Colles")
                .setCity("Biot")
                .build();
        LocationRepository.add(location);
        Location location2 = new Location.Builder()
                .setNumber("2004")
                .setAddress("Route des Lucioles")
                .setCity("Sophia Antipolis")
                .build();
        LocationRepository.add(location2);
        Location location3 = new Location.Builder()
                .setNumber("400")
                .setAddress("Route des Macarons")
                .setCity("Biot")
                .build();
        LocationRepository.add(location3);
        Location location4 = new Location.Builder()
                .setNumber("930 ")
                .setAddress("Route des Colles")
                .setCity("Biot")
                .build();
        LocationRepository.add(location4);
    }
}

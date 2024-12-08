package team.k;

import commonlibrary.enumerations.FoodType;
import commonlibrary.enumerations.Role;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import ssdbrestframework.SSDBHttpServer;
import team.k.repository.DishRepository;

import team.k.repository.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ServicesAPI {
    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.controller", "services/");
        initDataset();
        serv.start();
    }

    private static void initDataset() {
        initLocationData();
        initRegisteredUsersData();
        initGroupOrderData();
        initRestaurantData();
        initDishData();
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

    private static void initDishData() {
        Dish burger = new Dish.Builder()
                .setName("Burger")
                .setDescription("Un bon burger")
                .setPrice(10)
                .setPreparationTime(5)
                .build();

        Dish pizza = new Dish.Builder()
                .setName("Pizza")
                .setDescription("Une bonne pizza")
                .setPrice(12)
                .setPreparationTime(7)
                .build();
        Dish Sushi = new Dish.Builder()
                .setName("Sushi")
                .setDescription("Des sushis frais")
                .setPrice(15)
                .setPreparationTime(10)
                .build();
        DishRepository.add(burger);
        DishRepository.add(pizza);
        DishRepository.add(Sushi);
    }

    private static void initRestaurantData() {
        Restaurant restaurant = new Restaurant.Builder()
                .setName("512EatRestaurant")
                .setDescription("Restaurants de qualité")
                .setFoodTypes(List.of(FoodType.BURGER, FoodType.PIZZA, FoodType.SUSHI))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        new Dish.Builder()
                                .setName("Burger")
                                .setDescription("Un bon burger")
                                .setPrice(10)
                                .setPreparationTime(5)
                                .build(),
                        new Dish.Builder()
                                .setName("Pizza")
                                .setDescription("Une bonne pizza")
                                .setPrice(12)
                                .setPreparationTime(7)
                                .build(),
                        new Dish.Builder()
                                .setName("Sushi")
                                .setDescription("Des sushis frais")
                                .setPrice(15)
                                .setPreparationTime(10)
                                .build()
                ))
                .build();

        TimeSlot timeSlot = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurant, restaurant.getAverageOrderPreparationTime());
        restaurant.addTimeSlot(timeSlot);
        RestaurantRepository.add(restaurant);
        Restaurant restaurant1 = new Restaurant.Builder()
                .setName("512EatBurger")
                .setDescription("Restaurant de burgerf")
                .setFoodTypes(List.of(FoodType.BURGER))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        new Dish.Builder()
                                .setName("Burger")
                                .setDescription("Un bon burger")
                                .setPrice(10)
                                .setPreparationTime(5)
                                .build()
                ))
                .build();

        TimeSlot timeSlot2 = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurant1, restaurant1.getAverageOrderPreparationTime());
        restaurant1.addTimeSlot(timeSlot2);
        RestaurantRepository.add(restaurant1);
        Restaurant restaurant2 = new Restaurant.Builder()
                .setName("512EatSushi")
                .setDescription("Restaurant de sushi")
                .setFoodTypes(List.of(FoodType.SUSHI))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        new Dish.Builder()
                                .setName("Sushi")
                                .setDescription("Un bon sushi")
                                .setPrice(10)
                                .setPreparationTime(5)
                                .build()
                ))
                .build();

        TimeSlot timeSlot3 = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurant2, restaurant2.getAverageOrderPreparationTime());
        restaurant1.addTimeSlot(timeSlot3);
        RestaurantRepository.add(restaurant2);

    }
}

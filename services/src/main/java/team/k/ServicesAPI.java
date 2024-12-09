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
        Location location5 = new Location.Builder()
                .setNumber("2 ")
                .setAddress("Passage Marie Antoinette")
                .setCity("Antibes")
                .build();
        LocationRepository.add(location5);
        Location location6 = new Location.Builder()
                .setNumber("12 ")
                .setAddress("Les Oliviers")
                .setCity("Antibes")
                .build();
        LocationRepository.add(location6);
        Location location7 = new Location.Builder()
                .setNumber("34 ")
                .setAddress("Avenue Saint Augustin")
                .setCity("Nice")
                .build();
        LocationRepository.add(location7);
        Location location8 = new Location.Builder()
                .setNumber("2400 ")
                .setAddress("Route des Dolines")
                .setCity("Valbonne")
                .build();
        LocationRepository.add(location8);
        Location location9 = new Location.Builder()
                .setNumber("1 ")
                .setAddress("Avenue des Templiers")
                .setCity("Cagnes-sur-Mer")
                .build();
        LocationRepository.add(location9);
        Location location10 = new Location.Builder()
                .setNumber("1 ")
                .setAddress("Avenue des Templiers")
                .setCity("Cagnes-sur-Mer")
                .build();
        LocationRepository.add(location10);
    }

    private static List<Dish> initDishData() {
        List<Dish> dishes = List.of(
                new Dish.Builder()
                        .setName("Burger")
                        .setDescription("Un bon burger")
                        .setPrice(10)
                        .setPreparationTime(5)
                        .build(),
                new Dish.Builder()
                        .setName("Vegan Burger")
                        .setDescription("Une bonne pizza tomate mozzarella")
                        .setPrice(10)
                        .setPreparationTime(5)
                        .build(),
                new Dish.Builder()
                        .setName("Chicken Burger")
                        .setDescription("Une bonne pizza tomate mozzarella")
                        .setPrice(12)
                        .setPreparationTime(9)
                        .build(),
                new Dish.Builder()
                        .setName("Margherita")
                        .setDescription("Une bonne pizza tomate mozzarella")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .build(),
                new Dish.Builder()
                        .setName("Napolitaine")
                        .setDescription("Une excellente pizza tomate anchois")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .build(),
                new Dish.Builder()
                        .setName("Sushi")
                        .setDescription("Des sushis frais")
                        .setPrice(15)
                        .setPreparationTime(10)
                        .build(),
                new Dish.Builder()
                        .setName("Pokebowl")
                        .setDescription("Un bon poke rempli de saveurs")
                        .setPrice(13)
                        .setPreparationTime(7)
                        .build(),
                new Dish.Builder()
                        .setName("Pates Carbonara")
                        .setDescription("Des pates à la carbonara (des vraies!)")
                        .setPrice(9)
                        .setPreparationTime(9)
                        .build(),
                new Dish.Builder()
                        .setName("Pates bolognaises")
                        .setDescription("Des pates à la bolognaise")
                        .setPrice(8)
                        .setPreparationTime(7)
                        .build(),
                new Dish.Builder()
                        .setName("Nems")
                        .setDescription("Des nems croustillants")
                        .setPrice(10)
                        .setPreparationTime(4)
                        .build(),
                new Dish.Builder()
                        .setName("Salade Nicoise")
                        .setDescription("Salade composée de thon, tomates, oeufs, olives, haricots verts, anchois, oignons et vinaigrette")
                        .setPrice(14)
                        .setPreparationTime(8)
                        .build(),
                new Dish.Builder()
                        .setName("Salade Cesar")
                        .setDescription("Salade composée de poulet, croûtons, parmesan, tomates, oeufs, vinaigrette")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .build(),
                new Dish.Builder()
                        .setName("Tartiflette")
                        .setDescription("Plat traditionnel savoyard à base de pommes de terre, de lardons, d'oignons et de reblochon")
                        .setPrice(16)
                        .setPreparationTime(10)
                        .build(),
                new Dish.Builder()
                        .setName("Poulet Curry")
                        .setDescription("Poulet au curry et lait de coco")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .build(),
                new Dish.Builder()
                        .setName("Nouilles au poulet")
                        .setDescription("Nouilles sautées au poulet et légumes")
                        .setPrice(12)
                        .setPreparationTime(8)
                        .build(),
                new Dish.Builder()
                        .setName("Bo Bun")
                        .setDescription("Bo Bun au boeuf")
                        .setPrice(15)
                        .setPreparationTime(7)
                        .build(),
                new Dish.Builder()
                        .setName("Tiramisu")
                        .setDescription("Un bon tiramisu")
                        .setPrice(6)
                        .setPreparationTime(8)
                        .build(),
                new Dish.Builder()
                        .setName("Salade de fruits")
                        .setDescription("Salade de fruits frais")
                        .setPrice(6)
                        .setPreparationTime(4)
                        .build(),
                new Dish.Builder()
                        .setName("Brownie")
                        .setDescription("Un bon brownie au chocolat")
                        .setPrice(5)
                        .setPreparationTime(3)
                        .build(),
                new Dish.Builder()
                        .setName("Cheesecake")
                        .setDescription("Un bon cheesecake")
                        .setPrice(6)
                        .setPreparationTime(5)
                        .build(),
                new Dish.Builder()
                        .setName("Mousse au chocolat")
                        .setDescription("Une bonne mousse au chocolat")
                        .setPrice(5)
                        .setPreparationTime(4)
                        .build()
        );
        dishes.forEach(DishRepository::add);
        return dishes;


    }

    private static void initRestaurantData() {
        Restaurant restaurant = new Restaurant.Builder()
                .setName("512EatRestaurant")
                .setDescription("Restaurants de qualité")
                .setFoodTypes(List.of(FoodType.BURGER, FoodType.PIZZA, FoodType.SUSHI, FoodType.CHINESE, FoodType.SALAD, FoodType.VEGAN, FoodType.POKEBOWL))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(initDishData())
                .build();

        TimeSlot timeSlot = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurant, restaurant.getAverageOrderPreparationTime());
        restaurant.addTimeSlot(timeSlot);
        RestaurantRepository.add(restaurant);
        Restaurant restaurantBurger = new Restaurant.Builder()
                .setName("512EatBurger")
                .setDescription("Restaurant de burger")
                .setFoodTypes(List.of(FoodType.BURGER))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        initDishData().get(0),
                        initDishData().get(1),
                        initDishData().get(2),
                        initDishData().get(6),
                        initDishData().get(16),
                        initDishData().get(17),
                        initDishData().get(18),
                        initDishData().get(19),
                        initDishData().get(20)
                ))
                .build();

        TimeSlot timeSlot2 = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurantBurger, restaurantBurger.getAverageOrderPreparationTime());
        restaurantBurger.addTimeSlot(timeSlot2);
        RestaurantRepository.add(restaurantBurger);
        Restaurant restaurantSushi = new Restaurant.Builder()
                .setName("512EatSushi")
                .setDescription("Restaurant de sushi")
                .setFoodTypes(List.of(FoodType.SUSHI))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        initDishData().get(6),
                        initDishData().get(5),
                        initDishData().get(16),
                        initDishData().get(17),
                        initDishData().get(18),
                        initDishData().get(19),
                        initDishData().get(20)

                ))
                .build();

        TimeSlot timeSlot3 = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurantSushi, restaurantSushi.getAverageOrderPreparationTime());
        restaurantSushi.addTimeSlot(timeSlot3);
        RestaurantRepository.add(restaurantSushi);

        Restaurant restaurantPizza = new Restaurant.Builder()
                .setName("512EatPizza")
                .setDescription("Restaurant de Pizza")
                .setFoodTypes(List.of(FoodType.PIZZA))
                .setOpen(LocalTime.of(11, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        initDishData().get(3),
                        initDishData().get(4),
                        initDishData().get(7),
                        initDishData().get(8),
                        initDishData().get(16),
                        initDishData().get(17),
                        initDishData().get(18),
                        initDishData().get(19),
                        initDishData().get(20)

                ))
                .build();


        TimeSlot timeSlot4 = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurantPizza, restaurantPizza.getAverageOrderPreparationTime());
        restaurantPizza.addTimeSlot(timeSlot4);
        RestaurantRepository.add(restaurantPizza);

        Restaurant restaurantChinois = new Restaurant.Builder()
                .setName("512EatChinois")
                .setDescription("Restaurant chinois")
                .setFoodTypes(List.of(FoodType.CHINESE))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(23, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        initDishData().get(9),
                        initDishData().get(13),
                        initDishData().get(14),
                        initDishData().get(15),
                        initDishData().get(16),
                        initDishData().get(17),
                        initDishData().get(18),
                        initDishData().get(19),
                        initDishData().get(20)

                ))
                .build();


        TimeSlot timeSlot5 = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurantPizza, restaurantPizza.getAverageOrderPreparationTime());
        restaurantChinois.addTimeSlot(timeSlot5);
        RestaurantRepository.add(restaurantChinois);

        Restaurant restaurantHealthy = new Restaurant.Builder()
                .setName("512EatSalade")
                .setDescription("Restaurant Healthy")
                .setFoodTypes(List.of(FoodType.SALAD, FoodType.VEGAN, FoodType.POKEBOWL))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        initDishData().get(1),
                        initDishData().get(6),
                        initDishData().get(10),
                        initDishData().get(11),
                        initDishData().get(15),
                        initDishData().get(16),
                        initDishData().get(17),
                        initDishData().get(18),
                        initDishData().get(19),
                        initDishData().get(20)

                ))
                .build();


        TimeSlot timeSlot6 = new TimeSlot(LocalDateTime.of(2024, 12, 18, 12, 0), restaurantPizza, restaurantPizza.getAverageOrderPreparationTime());
        restaurantHealthy.addTimeSlot(timeSlot6);
        RestaurantRepository.add(restaurantHealthy);

    }
}

package team.k;

import commonlibrary.enumerations.FoodType;
import commonlibrary.enumerations.Role;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ssdbrestframework.SSDBHttpServer;
import team.k.repository.DishRepository;

import team.k.repository.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class ServicesAPI {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
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
        initTimeSlot();
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
                        .setPicture("https://cdn.stoneline.de/media/b0/c4/37/1721744157/Smash-Burger.png")
                        .build(),
                new Dish.Builder()
                        .setName("Vegan Burger")
                        .setDescription("Une bonne pizza tomate mozzarella")
                        .setPrice(10)
                        .setPreparationTime(5)
                        .setPicture("https://resize-elle.ladmedia.fr/rcrop/638,,forcex/img/var/plain_site/storage/images/elle-a-table/les-dossiers-de-la-redaction/dossier-de-la-redac/burger-veggie/92821415-3-fre-FR/10-burgers-vegetariens-qui-reduisent-l-empreinte-carbone.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Chicken Burger")
                        .setDescription("Une bonne pizza tomate mozzarella")
                        .setPrice(12)
                        .setPreparationTime(9)
                        .setPicture("https://vikalinka.com/wp-content/uploads/2021/07/Greek-Chicken-Burger-1CR2-Edit.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Margherita")
                        .setDescription("Une bonne pizza tomate mozzarella")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://fr.ooni.com/cdn/shop/articles/Margherita-9920.jpg?crop=center&height=800&v=1644590066&width=800")
                        .build(),
                new Dish.Builder()
                        .setName("Napolitaine")
                        .setDescription("Une excellente pizza tomate anchois")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://giuliz.com/cdn/shop/articles/recette_four_a_pizza_giuliz_anchois_olives_sicile_1200x1200.jpg?v=1632908868")
                        .build(),
                new Dish.Builder()
                        .setName("Sushi")
                        .setDescription("Des sushis frais")
                        .setPrice(15)
                        .setPreparationTime(10)
                        .setPicture("https://japanathome.fr/cdn/shop/articles/sushi-2853382_1920.jpg?crop=center&height=1200&v=1586628738&width=1200")
                        .build(),
                new Dish.Builder()
                        .setName("Pokebowl")
                        .setDescription("Un bon poke rempli de saveurs")
                        .setPrice(13)
                        .setPreparationTime(7)
                        .setPicture("https://fyooyzbm.filerobot.com/v7/https://static01.nicematin.com/media/npo/xlarge/2020/12/istock-1203557795.jpg?w=480&h=382&gravity=auto&func=crop")
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
                        .setPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQdnT57wAp4uOZI2MSS9BRceBeQ0oI8Q1BQqQ&s")
                        .build(),
                new Dish.Builder()
                        .setName("Nems")
                        .setDescription("Des nems croustillants")
                        .setPrice(10)
                        .setPreparationTime(4)
                        .setPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS447Bc01IHHM8kG7Fv4i0MZBchDdx1uplq1A&s")
                        .build(),
                new Dish.Builder()
                        .setName("Salade Nicoise")
                        .setDescription("Salade composée de thon, tomates, oeufs, olives, haricots verts, anchois, oignons et vinaigrette")
                        .setPrice(14)
                        .setPreparationTime(8)
                        .setPicture("https://img.cuisineaz.com/1024x576/2013/12/20/i34581-salade-nicoise-rapide.webp")
                        .build(),
                new Dish.Builder()
                        .setName("Salade Cesar")
                        .setDescription("Salade composée de poulet, croûtons, parmesan, tomates, oeufs, vinaigrette")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://assets.afcdn.com/recipe/20200212/107449_w1024h1024c1cx1060cy707cxt0cyt0cxb2121cyb1414.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Tartiflette")
                        .setDescription("Plat traditionnel savoyard à base de pommes de terre, de lardons, d'oignons et de reblochon")
                        .setPrice(16)
                        .setPreparationTime(10)
                        .setPicture("https://img.cuisineaz.com/660x660/2023/05/26/i194047-tartiflette-oignons-confits-reblochons-et-lardons-fait-maison.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Poulet Curry")
                        .setDescription("Poulet au curry et lait de coco")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://img.freepik.com/photos-premium/bol-poulet-au-curry-du-riz-fond-blanc_658005-392.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Nouilles au poulet")
                        .setDescription("Nouilles sautées au poulet et légumes")
                        .setPrice(12)
                        .setPreparationTime(8)
                        .setPicture("https://www.1001recettes.net/wp-content/uploads/2024/06/1719011545_recette-facile-de-nouilles-chinoises-au-poulet-saveurs-asiatiques-a-la-maison.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Bo Bun")
                        .setDescription("Bo Bun au boeuf")
                        .setPrice(15)
                        .setPreparationTime(7)
                        .setPicture("https://www.grazia.fr/wp-content/uploads/grazia/2022/11/bo-bun-500x500-1-410x410.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Tiramisu")
                        .setDescription("Un bon tiramisu")
                        .setPrice(6)
                        .setPreparationTime(8)
                        .setPicture("https://ffcuisine.fr/wp-content/uploads/2024/06/1717801337_recette-facile-de-tiramisu-sans-mascarpone-decouvrez-comment-le-reussir.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Salade de fruits")
                        .setDescription("Salade de fruits frais")
                        .setPrice(6)
                        .setPreparationTime(4)
                        .setPicture("https://www.le-caucase.com/wp-content/uploads/2024/07/1720172719_salade-de-fruits-dete-recette-facile-et-rafraichissante.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Brownie")
                        .setDescription("Un bon brownie au chocolat")
                        .setPrice(5)
                        .setPreparationTime(3)
                        .setPicture("https://assets.afcdn.com/recipe/20161114/26634_w1024h576c1cx2736cy1824.webp")
                        .build(),
                new Dish.Builder()
                        .setName("Cheesecake")
                        .setDescription("Un bon cheesecake")
                        .setPrice(6)
                        .setPreparationTime(5)
                        .setPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZtBlgj42aG4M_dTpTA13Vvh1HiJZfZvzAEw&s")
                        .build(),
                new Dish.Builder()
                        .setName("Mousse au chocolat")
                        .setDescription("Une bonne mousse au chocolat")
                        .setPrice(5)
                        .setPreparationTime(4)
                        .setPicture("https://kissmychef.com/wp-content/uploads/2021/04/mousse.png")
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

        RestaurantRepository.add(restaurantHealthy);

    }

    private static void initTimeSlot() {
        List<Restaurant> restaurants = RestaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            LocalTime open = restaurant.getOpen();
            LocalTime close = restaurant.getClose();
            LocalDateTime currentSlotStart = LocalDateTime.of(2024, 12, 18, open.getHour(), open.getMinute());

            while (currentSlotStart.toLocalTime().isBefore(close)) {
                Random random = new Random();
                int randomNumber = random.nextInt(7) + 1;
                TimeSlot timeSlot = new TimeSlot(currentSlotStart, restaurant, randomNumber);
                restaurant.addTimeSlot(timeSlot);

                // Passer au prochain créneau de 30 minutes
                currentSlotStart = currentSlotStart.plusMinutes(30);
            }
        }
    }

}

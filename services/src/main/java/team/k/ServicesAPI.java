package team.k;

import commonlibrary.enumerations.FoodType;
import commonlibrary.enumerations.Role;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import commonlibrary.repository.DishJPARepository;
import commonlibrary.repository.GroupOrderJPARepository;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.TimeSlotJPARepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ssdbrestframework.SSDBHttpServer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class ServicesAPI {

    public static LocationJPARepository locationJPARepository;
    public static GroupOrderJPARepository groupOrderJPARepository;
    public static RegisteredUserJPARepository registeredUserJPARepository;
    public static DishJPARepository dishJPARepository;
    public static RestaurantJPARepository restaurantJPARepository;
    public static TimeSlotJPARepository timeSlotJPARepository;
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        locationJPARepository = context.getBean(LocationJPARepository.class);
        groupOrderJPARepository = context.getBean(GroupOrderJPARepository.class);
        registeredUserJPARepository = context.getBean(RegisteredUserJPARepository.class);
        dishJPARepository = context.getBean(DishJPARepository.class);
        restaurantJPARepository = context.getBean(RestaurantJPARepository.class);
        timeSlotJPARepository = context.getBean(TimeSlotJPARepository.class);
        SSDBHttpServer serv = new SSDBHttpServer(8083, "team.k.controller", "services/", context);
//        initDataset();
        serv.start();
    }

    private static void initDataset() {
        initLocationData();
        initRegisteredUsersData();
        initDishData();
        initGroupOrderData();
        initRestaurantData();
        initTimeSlot();
    }

    private static void initLocationData() {
        Location location = new Location.Builder()
                .setNumber("650")
                .setAddress("Route des Colles")
                .setCity("Biot")
                .build();
        locationJPARepository.save(location);
        Location location2 = new Location.Builder()
                .setNumber("2004")
                .setAddress("Route des Lucioles")
                .setCity("Sophia Antipolis")
                .build();
        locationJPARepository.save(location2);
        Location location3 = new Location.Builder()
                .setNumber("400")
                .setAddress("Route des Macarons")
                .setCity("Biot")
                .build();
        locationJPARepository.save(location3);
        Location location4 = new Location.Builder()
                .setNumber("930 ")
                .setAddress("Route des Colles")
                .setCity("Biot")
                .build();
        locationJPARepository.save(location4);
        Location location5 = new Location.Builder()
                .setNumber("2 ")
                .setAddress("Passage Marie Antoinette")
                .setCity("Antibes")
                .build();
        locationJPARepository.save(location5);
        Location location6 = new Location.Builder()
                .setNumber("12 ")
                .setAddress("Les Oliviers")
                .setCity("Antibes")
                .build();
        locationJPARepository.save(location6);
        Location location7 = new Location.Builder()
                .setNumber("34 ")
                .setAddress("Avenue Saint Augustin")
                .setCity("Nice")
                .build();
        locationJPARepository.save(location7);
        Location location8 = new Location.Builder()
                .setNumber("2400 ")
                .setAddress("Route des Dolines")
                .setCity("Valbonne")
                .build();
        locationJPARepository.save(location8);
        Location location9 = new Location.Builder()
                .setNumber("1 ")
                .setAddress("Avenue des Templiers")
                .setCity("Cagnes-sur-Mer")
                .build();
        locationJPARepository.save(location9);
        Location location10 = new Location.Builder()
                .setNumber("1 ")
                .setAddress("Avenue des Templiers")
                .setCity("Cagnes-sur-Mer")
                .build();
        locationJPARepository.save(location10);
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

        registeredUserJPARepository.save(user1);
        registeredUserJPARepository.save(user2);
        registeredUserJPARepository.save(user3);
        registeredUserJPARepository.save(user4);
        registeredUserJPARepository.save(user5);
        registeredUserJPARepository.save(user6);
        registeredUserJPARepository.save(user7);
        registeredUserJPARepository.save(user8);
        registeredUserJPARepository.save(user9);
        registeredUserJPARepository.save(user10);
    }

    private static void initDishData() {
        List<Dish> dishes = List.of(
                new Dish.Builder()
                        .setName("Burger")
                        .setDescription("A classic burger")
                        .setPrice(10)
                        .setPreparationTime(5)
                        .setPicture("https://cdn.stoneline.de/media/b0/c4/37/1721744157/Smash-Burger.png")
                        .build(),
                new Dish.Builder()
                        .setName("Vegan Burger")
                        .setDescription("A vegan burger with a plant-based patty")
                        .setPrice(10)
                        .setPreparationTime(5)
                        .setPicture("https://resize-elle.ladmedia.fr/rcrop/638,,forcex/img/var/plain_site/storage/images/elle-a-table/les-dossiers-de-la-redaction/dossier-de-la-redac/burger-veggie/92821415-3-fre-FR/10-burgers-vegetariens-qui-reduisent-l-empreinte-carbone.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Chicken Burger")
                        .setDescription("A chicken burger with a crispy chicken patty")
                        .setPrice(12)
                        .setPreparationTime(9)
                        .setPicture("https://vikalinka.com/wp-content/uploads/2021/07/Greek-Chicken-Burger-1CR2-Edit.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Margherita")
                        .setDescription("A classic margherita pizza with tomato sauce, mozzarella and basil")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://fr.ooni.com/cdn/shop/articles/Margherita-9920.jpg?crop=center&height=800&v=1644590066&width=800")
                        .build(),
                new Dish.Builder()
                        .setName("Neapolitan")
                        .setDescription("A neapolitan pizza with tomato sauce, mozzarella, anchovies and olives")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://giuliz.com/cdn/shop/articles/recette_four_a_pizza_giuliz_anchois_olives_sicile_1200x1200.jpg?v=1632908868")
                        .build(),
                new Dish.Builder()
                        .setName("Sushi")
                        .setDescription("A plate of sushi with salmon, tuna, avocado and cucumber")
                        .setPrice(15)
                        .setPreparationTime(10)
                        .setPicture("https://japanathome.fr/cdn/shop/articles/sushi-2853382_1920.jpg?crop=center&height=1200&v=1586628738&width=1200")
                        .build(),
                new Dish.Builder()
                        .setName("Pokebowl")
                        .setDescription("A pokebowl with salmon, avocado, cucumber, edamame, radish and sesame seeds")
                        .setPrice(13)
                        .setPreparationTime(7)
                        .setPicture("https://fyooyzbm.filerobot.com/v7/https://static01.nicematin.com/media/npo/xlarge/2020/12/istock-1203557795.jpg?w=480&h=382&gravity=auto&func=crop")
                        .build(),
                new Dish.Builder()
                        .setName("Carbonara")
                        .setDescription("Pasta carbonara with bacon, eggs and parmesan")
                        .setPrice(9)
                        .setPreparationTime(9)
                        .setPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQdnT57wAp4uOZI2MSS9BRceBeQ0oI8Q1BQqQ&s")
                        .build(),
                new Dish.Builder()
                        .setName("Pastas Bolognaise")
                        .setDescription("Pasta with bolognaise sauce")
                        .setPrice(8)
                        .setPreparationTime(7)
                        .setPicture("https://img.cuisineaz.com/660x660/2013/12/20/i71303-recette-pate-spaghettis-bolognaise-maison.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Nems")
                        .setDescription("Nems with pork, shrimp, vermicelli, mushrooms, carrots and lettuce")
                        .setPrice(10)
                        .setPreparationTime(4)
                        .setPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS447Bc01IHHM8kG7Fv4i0MZBchDdx1uplq1A&s")
                        .build(),
                new Dish.Builder()
                        .setName("Salad Nicoise")
                        .setDescription("Salad with tuna, tomatoes, eggs, olives, green beans, potatoes and vinaigrette")
                        .setPrice(14)
                        .setPreparationTime(8)
                        .setPicture("https://img.cuisineaz.com/1024x576/2013/12/20/i34581-salade-nicoise-rapide.webp")
                        .build(),
                new Dish.Builder()
                        .setName("Salad Cesar")
                        .setDescription("Salad with chicken, lettuce, croutons, parmesan and caesar dressing")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://assets.afcdn.com/recipe/20200212/107449_w1024h1024c1cx1060cy707cxt0cyt0cxb2121cyb1414.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Tartiflette")
                        .setDescription("Tartiflette with reblochon, onions, bacon and potatoes")
                        .setPrice(16)
                        .setPreparationTime(10)
                        .setPicture("https://img.cuisineaz.com/660x660/2023/05/26/i194047-tartiflette-oignons-confits-reblochons-et-lardons-fait-maison.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Chicken Curry")
                        .setDescription("Chicken curry with rice")
                        .setPrice(12)
                        .setPreparationTime(7)
                        .setPicture("https://img.freepik.com/photos-premium/bol-poulet-au-curry-du-riz-fond-blanc_658005-392.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Chicken Noodles")
                        .setDescription("Chicken noodles with vegetables")
                        .setPrice(12)
                        .setPreparationTime(8)
                        .setPicture("https://www.1001recettes.net/wp-content/uploads/2024/06/1719011545_recette-facile-de-nouilles-chinoises-au-poulet-saveurs-asiatiques-a-la-maison.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Bo Bun")
                        .setDescription("Bo bun with beef, vermicelli, lettuce, cucumber, carrots, peanuts and nuoc mam sauce")
                        .setPrice(15)
                        .setPreparationTime(7)
                        .setPicture("https://www.grazia.fr/wp-content/uploads/grazia/2022/11/bo-bun-500x500-1-410x410.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Tiramisu")
                        .setDescription("A classic tiramisu")
                        .setPrice(6)
                        .setPreparationTime(8)
                        .setPicture("https://ffcuisine.fr/wp-content/uploads/2024/06/1717801337_recette-facile-de-tiramisu-sans-mascarpone-decouvrez-comment-le-reussir.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Salad of fruits")
                        .setDescription("A fresh fruit salad with strawberries, kiwis, bananas and oranges")
                        .setPrice(6)
                        .setPreparationTime(4)
                        .setPicture("https://www.le-caucase.com/wp-content/uploads/2024/07/1720172719_salade-de-fruits-dete-recette-facile-et-rafraichissante.jpg")
                        .build(),
                new Dish.Builder()
                        .setName("Brownie")
                        .setDescription("A delicious brownie with chocolate chips")
                        .setPrice(5)
                        .setPreparationTime(3)
                        .setPicture("https://assets.afcdn.com/recipe/20161114/26634_w1024h576c1cx2736cy1824.webp")
                        .build(),
                new Dish.Builder()
                        .setName("Cheesecake")
                        .setDescription("A creamy cheesecake with a biscuit base")
                        .setPrice(6)
                        .setPreparationTime(5)
                        .setPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZtBlgj42aG4M_dTpTA13Vvh1HiJZfZvzAEw&s")
                        .build(),
                new Dish.Builder()
                        .setName("Chocolate Mousse")
                        .setDescription("A light and fluffy chocolate mousse")
                        .setPrice(5)
                        .setPreparationTime(4)
                        .setPicture("https://kissmychef.com/wp-content/uploads/2021/04/mousse.png")
                        .build()
        );
        dishJPARepository.saveAll(dishes);
    }

    private static void initGroupOrderData() {
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocationID(3)
                .withDate(LocalDateTime.of(2024, 12, 18, 12, 0))
                .build();
        groupOrderJPARepository.save(groupOrder);
        GroupOrder groupOrder2 = new GroupOrder.Builder()
                .withDeliveryLocationID(0)
                .build();
        groupOrderJPARepository.save(groupOrder2);
    }

    private static void initRestaurantData() {
        Restaurant restaurant = new Restaurant.Builder()
                .setName("512EatRestaurant")
                .setDescription("The best restaurant in Sophia Antipolis")
                .setFoodTypes(List.of(FoodType.BURGER, FoodType.PIZZA, FoodType.SUSHI, FoodType.CHINESE, FoodType.SALAD, FoodType.VEGAN, FoodType.POKEBOWL))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(dishJPARepository.findAll())
                .build();

        restaurantJPARepository.save(restaurant);


        Restaurant restaurantBurger = new Restaurant.Builder()
                .setName("512EatBurger")
                .setDescription("The best burger in Sophia Antipolis !")
                .setFoodTypes(List.of(FoodType.BURGER))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        dishJPARepository.findById((long)0).orElseThrow(),
                        dishJPARepository.findById((long)1).orElseThrow(),
                        dishJPARepository.findById((long)2).orElseThrow(),
                        dishJPARepository.findById((long)6).orElseThrow(),
                        dishJPARepository.findById((long)16).orElseThrow(),
                        dishJPARepository.findById((long)17).orElseThrow(),
                        dishJPARepository.findById((long)18).orElseThrow(),
                        dishJPARepository.findById((long)19).orElseThrow(),
                        dishJPARepository.findById((long)20).orElseThrow()
                ))
                .build();

        restaurantJPARepository.save(restaurantBurger);

        Restaurant restaurantSushi = new Restaurant.Builder()
                .setName("512EatSushi")
                .setDescription("Sushi restaurant in Sophia Antipolis for sushi lovers")
                .setFoodTypes(List.of(FoodType.SUSHI))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        dishJPARepository.findById((long)6).orElseThrow(),
                        dishJPARepository.findById((long)5).orElseThrow(),
                        dishJPARepository.findById((long)16).orElseThrow(),
                        dishJPARepository.findById((long)17).orElseThrow(),
                        dishJPARepository.findById((long)18).orElseThrow(),
                        dishJPARepository.findById((long)19).orElseThrow(),
                        dishJPARepository.findById((long)20).orElseThrow()

                ))
                .build();

        restaurantJPARepository.save(restaurantSushi);


        Restaurant restaurantPizza = new Restaurant.Builder()
                .setName("512EatPizza")
                .setDescription("Pizza restaurant in Sophia Antipolis")
                .setFoodTypes(List.of(FoodType.PIZZA))
                .setOpen(LocalTime.of(11, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        dishJPARepository.findById((long)3).orElseThrow(),
                        dishJPARepository.findById((long)4).orElseThrow(),
                        dishJPARepository.findById((long)7).orElseThrow(),
                        dishJPARepository.findById((long)8).orElseThrow(),
                        dishJPARepository.findById((long)16).orElseThrow(),
                        dishJPARepository.findById((long)17).orElseThrow(),
                        dishJPARepository.findById((long)18).orElseThrow(),
                        dishJPARepository.findById((long)19).orElseThrow(),
                        dishJPARepository.findById((long)20).orElseThrow()

                ))
                .build();

        restaurantJPARepository.save(restaurantPizza);


        Restaurant restaurantChinois = new Restaurant.Builder()
                .setName("512EatChinese")
                .setDescription("Chinese restaurant in Sophia Antipolis")
                .setFoodTypes(List.of(FoodType.CHINESE))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(23, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        dishJPARepository.findById((long)9).orElseThrow(),
                        dishJPARepository.findById((long)13).orElseThrow(),
                        dishJPARepository.findById((long)14).orElseThrow(),
                        dishJPARepository.findById((long)15).orElseThrow(),
                        dishJPARepository.findById((long)16).orElseThrow(),
                        dishJPARepository.findById((long)17).orElseThrow(),
                        dishJPARepository.findById((long)18).orElseThrow(),
                        dishJPARepository.findById((long)19).orElseThrow(),
                        dishJPARepository.findById((long)20).orElseThrow()

                ))
                .build();

        restaurantJPARepository.save(restaurantChinois);


        Restaurant restaurantHealthy = new Restaurant.Builder()
                .setName("512EatSalad")
                .setDescription("Healthy restaurant in Sophia Antipolis")
                .setFoodTypes(List.of(FoodType.SALAD, FoodType.VEGAN, FoodType.POKEBOWL))
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .setDishes(List.of(
                        dishJPARepository.findById((long)1).orElseThrow(),
                        dishJPARepository.findById((long)6).orElseThrow(),
                        dishJPARepository.findById((long)10).orElseThrow(),
                        dishJPARepository.findById((long)11).orElseThrow(),
                        dishJPARepository.findById((long)15).orElseThrow(),
                        dishJPARepository.findById((long)16).orElseThrow(),
                        dishJPARepository.findById((long)17).orElseThrow(),
                        dishJPARepository.findById((long)18).orElseThrow(),
                        dishJPARepository.findById((long)19).orElseThrow(),
                        dishJPARepository.findById((long)20).orElseThrow()

                ))
                .build();

        restaurantJPARepository.save(restaurantHealthy);
}
    private static void initTimeSlot() {
        List<Restaurant> restaurants = restaurantJPARepository.findAll();
        for (Restaurant restaurant : restaurants) {
            LocalTime open = restaurant.getOpen();
            LocalTime close = restaurant.getClose();
            LocalDateTime currentSlotStart = LocalDateTime.of(2024, 12, 10, open.getHour(), open.getMinute());

            Random random = new Random();
            while (currentSlotStart.toLocalTime().isBefore(close)) {
                int randomNumber = random.nextInt(7) + 1;
                TimeSlot timeSlot = new TimeSlot(currentSlotStart, restaurant, randomNumber);
                saveTimeSlot(timeSlot);
                restaurant.addTimeSlot(timeSlot);
                // Passer au prochain créneau de 30 minutes
                currentSlotStart = currentSlotStart.plusMinutes(30);
            }
        }
    }

    public static void saveTimeSlot(TimeSlot timeSlot) {
        timeSlotJPARepository.save(timeSlot);
    }

}

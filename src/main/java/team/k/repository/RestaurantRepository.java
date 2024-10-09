package team.k.repository;

import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {


    private final Dish dishStrawberry = new Dish(1, "Salade de fraise", "Salade fraise de saison", 5, 3, "");
    private final Dish dishApple = new Dish(2, "Pomme d'amour", "Pomme enrobée de sucre", 10, 10, "");
    private final Dish dishBurger = new Dish(3, "cheeseburger", "Burger avec du fromage en tranche", 30, 3, "");

    private Restaurant fruitsRestaurant = new Restaurant("512BankFruitsRestaurants", 1, LocalTime.of(8, 0, 0), LocalTime.of(22, 0, 0, 0), List.of(dishApple, dishStrawberry), List.of(FoodType.VEGAN), null);
    private Restaurant burgerRestaurant = new Restaurant("512BankBurger", 1, LocalTime.of(8, 0, 0), LocalTime.of(22, 0, 0, 0), List.of(dishBurger), List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL), null);
    List<Restaurant> restaurants = new ArrayList<>(List.of(
            fruitsRestaurant,
            burgerRestaurant));


    /**
     * Find a restaurant by its name.
     *
     * @param name the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant findByName(String name) {
        return restaurants.stream().filter(restaurant -> restaurant.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Find all restaurants.
     *
     * @return the list of all restaurants
     */
    public List<Restaurant> findAll() {
        return restaurants;
    }

    /**
     * Add a restaurant to the repository.
     *
     * @param restaurant the restaurant to add
     */
    public void add(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    /**
     * Delete a restaurant from the repository.
     *
     * @param restaurant the restaurant to delete
     */
    public void delete(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

}

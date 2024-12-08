package team.k.repository;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantRepository {
    private RestaurantRepository() {
        throw new IllegalStateException("Utility class");
    }

    private static final List<Restaurant> restaurants = new ArrayList<>();

    /**
     * Find a restaurant by its name.
     *
     * @param name the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public static Restaurant findByName(String name) {
        return restaurants.stream().filter(restaurant -> restaurant.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Find a restaurant by its id.
     * @param id the id of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public static Restaurant findById(int id) {
        return restaurants.stream().filter(restaurant -> restaurant.getId() == id).findFirst().orElse(null);
    }

    /**
     * Find all restaurants.
     *
     * @return the list of all restaurants
     */
    public static List<Restaurant> findAll() {
        return restaurants;
    }

    /**
     * Add a restaurant to the repository.
     *
     * @param restaurant the restaurant to add
     */
    public static void add(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    /**
     * Delete a restaurant from the repository.
     *
     * @param restaurantId the restaurant to delete
     */
    public static void delete(int restaurantId) {
        Restaurant restaurant = findById(restaurantId);
        restaurants.remove(restaurant);
    }

    public static List<Restaurant> findRestaurantByFoodType(List<FoodType> foodTypes) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getFoodTypes().stream().anyMatch(foodTypes::contains))
                .toList();
    }

    public static List<Restaurant> findRestaurantsByAvailability(LocalDateTime timeChosen) {
        return findAll().stream().filter(restaurant -> restaurant.isAvailable(timeChosen)).toList();
    }

    public static List<Restaurant> findRestaurantByName(String name) {
        return findAll().stream().filter(restaurant -> Objects.equals(restaurant.getName(), name)).toList();
    }

    public static void clear() {
        restaurants.clear();
    }

}

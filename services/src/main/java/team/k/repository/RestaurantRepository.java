package team.k.repository;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();

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
     * Find a restaurant by its id.
     * @param id the id of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant findById(int id) {
        return restaurants.stream().filter(restaurant -> restaurant.getId() == id).findFirst().orElse(null);
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

    public List<Restaurant> findRestaurantByFoodType(List<FoodType> foodTypes) {
        return this.restaurants.stream()
                .filter(restaurant -> restaurant.getFoodTypes().stream().anyMatch(foodTypes::contains))
                .toList();
    }

    public List<Restaurant> findRestaurantsByAvailability(LocalDateTime timeChosen) {
        return this.findAll().stream().filter(restaurant -> restaurant.isAvailable(timeChosen)).toList();
    }

    public List<Restaurant> findRestaurantByName(String name) {
        return this.findAll().stream().filter(restaurant -> Objects.equals(restaurant.getName(), name)).toList();
    }


}
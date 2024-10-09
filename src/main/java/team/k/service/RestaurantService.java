package team.k.service;

import team.k.common.Dish;
import team.k.repository.RestaurantRepository;
import team.k.repository.TimeSlotRepository;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;

import java.util.List;

public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private TimeSlotRepository timeSlotRepository;

    public List<Restaurant> getAllRestaurants() {
        return this.restaurantRepository.findAll();
    }

    /**
     * Get a restaurant by its name
     *
     * @param restaurantName the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant getRestaurantByName(String restaurantName) {
        return this.restaurantRepository.findByName(restaurantName);
    }

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantName the name of the restaurant
     * @return the list of dishes if the restaurant is found, null otherwise
     */
    public List<Dish> getAllDishesFromRestaurant(String restaurantName) {
        Restaurant restaurant = this.getRestaurantByName(restaurantName);
        return restaurant.getDishes();
    }

    /**
     * Add a restaurant to the repository
     *
     * @param restaurant the restaurant to add
     */
    public void addRestaurant(Restaurant restaurant) {
        this.restaurantRepository.add(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        this.restaurantRepository.delete(restaurant);
    }


    public void addTimeSlotToRestaurant(int restaurantId, int timeSlotId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        TimeSlot ts = timeSlotRepository.findById(timeSlotId);
        restaurant.addTimeSlot(ts);
    }

    public void addDishToRestaurant(Restaurant restaurant, Dish dish) {
        restaurant.addDish(dish);
    }

    public List<Restaurant> getRestaurantsByFoodType(List<String> foodTypes) {
        return this.restaurantRepository.findRestaurantByFoodType(foodTypes);
    }

    public List<Restaurant> getRestaurantsByAvailability() {
        return this.restaurantRepository.findRestaurantsByAvailability();
    }

    public List<Restaurant> getRestaurantsByName(String name) {
        return this.restaurantRepository.findRestaurantByName(name);
    }
}

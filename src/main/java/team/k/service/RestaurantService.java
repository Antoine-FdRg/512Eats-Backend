package team.k.service;

import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.repository.RestaurantRepository;
import team.k.repository.TimeSlotRepository;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private TimeSlotRepository timeSlotRepository;

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantName the name of the restaurant
     * @return the list of dishes if the restaurant is found, null otherwise
     */
    public List<Dish> getAllDishesFromRestaurant(String restaurantName) throws NoSuchElementException {
        Restaurant restaurant = this.getRestaurantByName(restaurantName);
        if (restaurant == null) {
            throw new NoSuchElementException("Restaurant not found");
        }
        List<Dish> dishes = restaurant.getDishes();
        if (dishes.isEmpty()) {
            throw new NoSuchElementException("No dishes available");
        }
        return dishes;
    }

    /**
     * Get all available delivery times of a restaurant on a specific day
     *
     * @param restaurantId the id of the restaurant
     * @param day          the day to check
     * @return the list of available delivery times
     */
    public List<LocalDateTime> getAllAvailableDeliveryTimesOfRestaurantOnDay(int restaurantId, LocalDate day) throws NoSuchElementException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new NoSuchElementException("Restaurant not found");
        }
        List<LocalDateTime> availableTimes = restaurant.getAvailableDeliveryTimesOnDay(day);
        if (availableTimes.isEmpty()) {
            throw new NoSuchElementException("No available delivery times");
        }
        return availableTimes;
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

    /***** Update *****/
    public void addTimeSlotToRestaurant(int restaurantId, int timeSlotId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        TimeSlot ts = timeSlotRepository.findById(timeSlotId);
        restaurant.addTimeSlot(ts);
    }

    public void addDishToRestaurant(Restaurant restaurant, Dish dish) {
        restaurant.addDish(dish);
    }

    /***** Searching methods *****/
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

    public List<Restaurant> getRestaurantsByFoodType(List<FoodType> foodTypes) throws NoSuchElementException {
        List<Restaurant> restaurants = this.restaurantRepository.findRestaurantByFoodType(foodTypes);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with the food types: " + foodTypes);
        }
        return restaurants;
    }

    public List<Restaurant> getRestaurantsByAvailability(LocalDateTime timeChosen) throws NoSuchElementException {
        List<Restaurant> restaurants = this.restaurantRepository.findRestaurantsByAvailability(timeChosen);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with availability at: " + timeChosen);
        }
        return restaurants;
    }

    public List<Restaurant> getRestaurantsByName(String name) throws NoSuchElementException {
        List<Restaurant> restaurants = this.restaurantRepository.findRestaurantByName(name);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with the name: " + name);
        }
        return restaurants;
    }
}

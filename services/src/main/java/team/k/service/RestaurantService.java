package team.k.service;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import team.k.repository.RestaurantRepository;
import team.k.repository.TimeSlotRepository;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class RestaurantService {

    private RestaurantService() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantId the name of the restaurant
     * @return the list of dishes if the restaurant is found, null otherwise
     */
    public static List<Dish> getAllDishesFromRestaurant(int restaurantId) throws NoSuchElementException {
        Restaurant restaurant = getRestaurantOrThrowIfNull(RestaurantRepository.findById(restaurantId));
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
    public static List<LocalDateTime> getAllAvailableDeliveryTimesOfRestaurantOnDay(int restaurantId, LocalDate day) throws NoSuchElementException {
        Restaurant restaurant = getRestaurantOrThrowIfNull(RestaurantRepository.findById(restaurantId));
        List<LocalDateTime> availableTimes = restaurant.getAvailableDeliveryTimesOnDay(day);
        if (availableTimes.isEmpty()) {
            throw new NoSuchElementException("No available delivery times");
        }
        return availableTimes;
    }



    /***** Update *****/
    public static void addTimeSlotToRestaurant(int restaurantId, int timeSlotId) {
        Restaurant restaurant = getRestaurantOrThrowIfNull(RestaurantRepository.findById(restaurantId));
        TimeSlot ts = TimeSlotRepository.findById(timeSlotId);
        if (ts == null) {
            throw new NoSuchElementException("Time slot not found");
        }
        restaurant.addTimeSlot(ts);
    }

    private static Restaurant getRestaurantOrThrowIfNull(Restaurant restaurantFound) {
        if (restaurantFound == null) {
            throw new NoSuchElementException("Restaurant not found");
        }
        return restaurantFound;
    }

    public static void addDishToRestaurant(Restaurant restaurant, Dish dish) {
        restaurant.addDish(dish);
    }


    /***** Searching methods *****/
    public static List<Restaurant> getAllRestaurants() {
        return RestaurantRepository.findAll();
    }

    /**
     * Get a restaurant by its name
     *
     * @param restaurantName the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public static Restaurant getRestaurantByName(String restaurantName) {
        return RestaurantRepository.findByName(restaurantName);
    }

    public static List<Restaurant> getRestaurantsByFoodType(List<FoodType> foodTypes) throws NoSuchElementException {
        List<Restaurant> restaurants = RestaurantRepository.findRestaurantByFoodType(foodTypes);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with the food types: " + foodTypes);
        }
        return restaurants;
    }

    public static List<Restaurant> getRestaurantsByAvailability(LocalDateTime timeChosen) throws NoSuchElementException {
        List<Restaurant> restaurants = RestaurantRepository.findRestaurantsByAvailability(timeChosen);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with availability at: " + timeChosen);
        }
        return restaurants;
    }

    public static List<Restaurant> getRestaurantsByName(String name) throws NoSuchElementException {
        List<Restaurant> restaurants = RestaurantRepository.findRestaurantByName(name);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with the name: " + name);
        }
        return restaurants;
    }

    /**
     * Get all food types
     *
     * @return the list of food types
     */
    public static List<FoodType> getFoodTypes() {
        return List.of(FoodType.values());
    }
}

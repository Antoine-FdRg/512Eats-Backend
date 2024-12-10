package team.k.restaurantservice;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.repository.RestaurantJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.k.repository.TimeSlotRepository;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RestaurantService {

    private RestaurantJPARepository restaurantJPARepository;
    
    @Autowired
    public RestaurantService(RestaurantJPARepository restaurantJPARepository) {
        this.restaurantJPARepository = restaurantJPARepository;
    }
    
    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantId the name of the restaurant
     * @return the list of dishes if the restaurant is found, null otherwise
     */
    public List<Dish> getAllDishesFromRestaurant(int restaurantId) throws NoSuchElementException {
        Restaurant restaurant = getRestaurantOrThrowIfNull(restaurantJPARepository.findById((long)restaurantId).orElse(null));
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
        Restaurant restaurant = getRestaurantOrThrowIfNull(restaurantJPARepository.findById((long)restaurantId).orElse(null));
        List<LocalDateTime> availableTimes = restaurant.getAvailableDeliveryTimesOnDay(day);
        if (availableTimes.isEmpty()) {
            throw new NoSuchElementException("No available delivery times");
        }
        return availableTimes;
    }



    /***** Update *****/
    @Transactional
    public void addTimeSlotToRestaurant(int restaurantId, int timeSlotId) {
        Restaurant restaurant = getRestaurantOrThrowIfNull(restaurantJPARepository.findById((long)restaurantId).orElse(null));
        TimeSlot ts = TimeSlotRepository.findById(timeSlotId);
        if (ts == null) {
            throw new NoSuchElementException("Time slot not found");
        }
        restaurant.addTimeSlot(ts);
    }

    private Restaurant getRestaurantOrThrowIfNull(Restaurant restaurantFound) {
        if (restaurantFound == null) {
            throw new NoSuchElementException("Restaurant not found");
        }
        return restaurantFound;
    }

    public void addDishToRestaurant(Restaurant restaurant, Dish dish) {
        restaurant.addDish(dish);
    }


    /***** Searching methods *****/
    public List<Restaurant> getAllRestaurants() {
        return restaurantJPARepository.findAll();
    }

    /**
     * Get a restaurant by its name
     *
     * @param restaurantName the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant getRestaurantByName(String restaurantName) {
        return restaurantJPARepository.findAll().stream().filter(r -> r.getName().equals(restaurantName)).findFirst().orElse(null);
    }

    public List<Restaurant> getRestaurantsByFoodType(List<FoodType> foodTypes) throws NoSuchElementException {
        List<Restaurant> restaurants = restaurantJPARepository.findAll().stream()
                .filter(restaurant -> restaurant.getFoodTypes().stream().anyMatch(foodTypes::contains))
                .toList();
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with the food types: " + foodTypes);
        }
        return restaurants;
    }

    public List<Restaurant> getRestaurantsByAvailability(LocalDateTime timeChosen) throws NoSuchElementException {
        List<Restaurant> restaurants = restaurantJPARepository.findAll().stream().filter(restaurant -> restaurant.isAvailable(timeChosen)).toList();
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with availability at: " + timeChosen);
        }
        return restaurants;
    }

    public List<Restaurant> getRestaurantsByName(String name) throws NoSuchElementException {
        List<Restaurant> restaurants = restaurantJPARepository.findAll().stream().filter(restaurant -> restaurant.getName().toLowerCase().contains(name.toLowerCase())).toList();
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
    public List<FoodType> getFoodTypes() {
        return List.of(FoodType.values());
    }
}

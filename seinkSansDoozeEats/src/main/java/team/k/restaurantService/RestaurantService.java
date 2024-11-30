package team.k.restaurantService;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import commonlibrary.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantId the name of the restaurant
     * @return the list of dishes if the restaurant is found, null otherwise
     */
    public List<Dish> getAllDishesFromRestaurant(int restaurantId) throws NoSuchElementException, IOException, InterruptedException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
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
    public List<LocalDateTime> getAllAvailableDeliveryTimesOfRestaurantOnDay(int restaurantId, LocalDate day) throws NoSuchElementException, IOException, InterruptedException {
        Restaurant restaurant = getRestaurantOrThrowIfNull(restaurantRepository.findById(restaurantId));
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
    public void addRestaurant(Restaurant restaurant) throws IOException, InterruptedException {
        this.restaurantRepository.add(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) throws IOException, InterruptedException {
        this.restaurantRepository.delete(restaurant.getId());
    }

    /***** Update *****/
    public void addTimeSlotToRestaurant(int restaurantId, LocalDateTime start, int productionCapacity) throws IOException, InterruptedException {
        Restaurant restaurant = getRestaurantOrThrowIfNull(restaurantRepository.findById(restaurantId));
        TimeSlot ts = new TimeSlot(start, restaurant, productionCapacity);
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
    public List<Restaurant> getAllRestaurants() throws IOException, InterruptedException {
        return this.restaurantRepository.findAll();
    }

    /**
     * Get a restaurant by its name
     *
     * @param restaurantName the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public List<Restaurant> getRestaurantByName(String restaurantName) throws IOException, InterruptedException {
        return this.restaurantRepository.findRestaurantByName(restaurantName);
    }

    public List<Restaurant> getRestaurantsByFoodType(List<FoodType> foodTypes) throws NoSuchElementException, IOException, InterruptedException {
        List<Restaurant> restaurants = this.restaurantRepository.findRestaurantByFoodType(foodTypes);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with the food types: " + foodTypes);
        }
        return restaurants;
    }

    public List<Restaurant> getRestaurantsByAvailability(LocalDateTime timeChosen) throws NoSuchElementException, IOException, InterruptedException {
        List<Restaurant> restaurants = this.restaurantRepository.findRestaurantsByAvailability(timeChosen);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with availability at: " + timeChosen);
        }
        return restaurants;
    }

    public List<Restaurant> getRestaurantsByName(String name) throws NoSuchElementException, IOException, InterruptedException {
        List<Restaurant> restaurants = this.restaurantRepository.findRestaurantByName(name);
        if (restaurants.isEmpty()) {
            throw new NoSuchElementException("No restaurants found with the name: " + name);
        }
        return restaurants;
    }
}

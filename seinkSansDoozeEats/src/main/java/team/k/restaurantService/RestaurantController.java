package team.k.restaurantService;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController(path = "/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantName the name of the restaurant
     * @return the list of dishes
     */
    @Endpoint(path = "/{restaurantName}/dishes", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 201)
    public List<Dish> getAllDishes(@PathVariable("restaurantName") String restaurantName) {
        try {
            return restaurantService.getAllDishesFromRestaurant(restaurantName);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No dishes available");
        }
    }

    /**
     * Get all food types
     *
     * @return the list of food types
     */
    @Endpoint(path = "/food-types", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 201)
    public List<FoodType> getFoodTypes() {
        return restaurantService.getFoodTypes();
    }


    /**
     * Get all available delivery times of a restaurant on a specific day
     *
     * @param restaurantId the id of the restaurant
     * @return the list of available delivery times
     */
    @Endpoint(path = "/{restaurantId}/delivery-times/{day}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 201)
    public List<LocalDateTime> getAvailableDeliveryTimes(
            @PathVariable("restaurantId") int restaurantId, @PathVariable("day") LocalDate day
    ) {
        try {
            return restaurantService.getAllAvailableDeliveryTimesOfRestaurantOnDay(restaurantId, day);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No available delivery times");
        }
    }

    /**
     * Add a new restaurant
     *
     * @param restaurant the restaurant to add
     * @return the restaurant id
     */
    @Endpoint(path = "/", method = ssdbrestframework.HttpMethod.POST)
    @Response(status = 201)
    public int addRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.addRestaurant(restaurant);
        return restaurant.getId();
    }

    /**
     * Delete a restaurant
     *
     * @param restaurant the restaurant to delete
     * @return success message
     */
    @Endpoint(path = "/", method = ssdbrestframework.HttpMethod.DELETE)
    @Response(status = 201)
    public void deleteRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.deleteRestaurant(restaurant);
    }


    /**
     * Get all restaurants
     *
     * @return list of restaurants
     */
    @Endpoint(path = "/", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 201)
    public List<Restaurant> getAllRestaurants() {
        //TODO: Créer un restaurant dto pour envoyer uniquement le nom du restaurant
        return restaurantService.getAllRestaurants();
    }

    /**
     * Get restaurants by name
     *
     * @param restaurantName the name of the restaurant
     * @return list of matching restaurants
     */
    @Endpoint(path = "/{restaurantName}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 201)
    public List<Restaurant> getRestaurantsByName(@RequestParam("restaurantName") String restaurantName) {
        try {
            return restaurantService.getRestaurantsByName(restaurantName);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No restaurants found with the name: " + restaurantName);
        }
    }

    /**
     * Get restaurants by food types
     *
     * @param foodTypes list of food types
     * @return list of matching restaurants
     */
    @Endpoint(path = "/by/food-types", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 201)
    public List<Restaurant> getRestaurantsByFoodType(@RequestBody List<FoodType> foodTypes) {
        try {
            return restaurantService.getRestaurantsByFoodType(foodTypes);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No restaurants found with the food types: " + foodTypes);
        }
    }

    /**
     * Get restaurants by availability
     *
     * @param availability the chosen time
     * @return list of matching restaurants
     */
    @Endpoint(path = "/by/availability/{availability}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 201)
    public List<Restaurant> getRestaurantsByAvailability(@PathVariable("availability") Boolean availability) {
        if (availability) {
            try {
                return restaurantService.getRestaurantsByAvailability(LocalDateTime.now());
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("No restaurants found with availability at: " + LocalDateTime.now());
            }
        } else {
            return restaurantService.getAllRestaurants();
        }

    }
}

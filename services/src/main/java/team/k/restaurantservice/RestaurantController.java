package team.k.restaurantservice;

import commonlibrary.dto.DishDTO;
import commonlibrary.dto.RestaurantDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@RestController(path = "/restaurants")
@Component
public class RestaurantController {

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService RestaurantService) {
        this.restaurantService = RestaurantService;
    }

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantId the id of the restaurant
     * @return the list of dishes
     */
    @Endpoint(path = "/dishes", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<DishDTO> getAllDishes(@RequestParam("restaurant-id") int restaurantId) throws SSDBQueryProcessingException {
        try {
            List<Dish> dishes = restaurantService.getAllDishesFromRestaurant(restaurantId);
            return dishes.stream().map(Dish::convertDishToDishDto).toList();
        } catch (NoSuchElementException e) {
            throw new  SSDBQueryProcessingException(404, "Les dishes du restaurant id"+ restaurantId+"sont introuvables");
        }
    }

    /**
     * Get all food types
     *
     * @return the list of food types
     */
    @Endpoint(path = "/food-types", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<FoodType> getFoodTypes() {
        return restaurantService.getFoodTypes();
    }

    /**
     * Get all available delivery times of a restaurant on a specific day
     *
     * @param restaurantId the id of the restaurant
     * @param day the specific day
     * @return the list of available delivery times
     */
    @Endpoint(path = "/get/delivery-times/{restaurantId}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<LocalDateTime> getAvailableDeliveryTimes(
            @PathVariable("restaurantId") int restaurantId, @RequestParam("day") LocalDate day
    ) throws SSDBQueryProcessingException {
        try {
            return restaurantService.getAllAvailableDeliveryTimesOfRestaurantOnDay(restaurantId, day);
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404,"No available delivery times");
        }
    }



    /**
     * Get all restaurants
     *
     * @return list of restaurants
     */
    @Endpoint(path = "", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return restaurants.stream().map(Restaurant::convertRestaurantToRestaurantDTO).toList();
    }

    /**
     * Get restaurants by name
     *
     * @param restaurantName the name of the restaurant
     * @return list of matching restaurants
     */
    @Endpoint(path = "/by/name/{restaurantName}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<RestaurantDTO> getRestaurantsByName(@PathVariable("restaurantName") String restaurantName) throws SSDBQueryProcessingException {
        try {
            List<Restaurant> restaurants = restaurantService.getRestaurantsByName(restaurantName);
            return restaurants.stream().map(Restaurant::convertRestaurantToRestaurantDTO).toList();
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404,"No restaurants found with the name: " + restaurantName);
        }
    }

    /**
     * Get restaurants by food types
     *
     * @param foodTypes list of food types
     * @return list of matching restaurants
     */
    @Endpoint(path = "/by/food-types", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<RestaurantDTO> getRestaurantsByFoodType(@RequestParam("food-types") List<FoodType> foodTypes) throws SSDBQueryProcessingException {
        try {
            List<Restaurant> restaurants = restaurantService.getRestaurantsByFoodType(foodTypes);
            return restaurants
                    .stream()
                    .map(Restaurant::convertRestaurantToRestaurantDTO)
                    .toList();
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404,"No restaurants found with the food types: " + foodTypes);
        }
    }

    /**
     * Get restaurants by availability
     *
     * @return list of matching restaurants
     */
    @Endpoint(path = "/by/availability", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<RestaurantDTO> getRestaurantsByAvailability() throws SSDBQueryProcessingException {
        try {
            List<Restaurant> restaurants = restaurantService.getRestaurantsByAvailability(LocalDateTime.now());
            return restaurants
                    .stream()
                    .map(Restaurant::convertRestaurantToRestaurantDTO)
                    .toList();
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404,"No restaurants found with availability at: " + LocalDateTime.now());
        }
    }

}

package team.k.controller;

import commonlibrary.dto.DishDTO;
import commonlibrary.dto.RestaurantDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import lombok.AllArgsConstructor;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.service.RestaurantService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController(path = "/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantId the id of the restaurant
     * @return the list of dishes
     */
    @Endpoint(path = "/dishes", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<DishDTO> getAllDishes(@RequestParam("restaurant-id") int restaurantId) {
        try {
            List<Dish> dishes = restaurantService.getAllDishesFromRestaurant(restaurantId);
            return dishes.stream().map(Dish::convertDishToDishDto).toList();
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
    @Endpoint(path = "/{restaurantId}/delivery-times/{day}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
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
     * @param restaurantDto the restaurant to add
     * @return the restaurant id
     */
    @Endpoint(path = "/", method = ssdbrestframework.HttpMethod.POST)
    @Response(status = 201) // Created
    public int addRestaurant(@RequestBody RestaurantDTO restaurantDto) {
        Restaurant restaurant = restaurantDto.convertRestaurantDtoToRestaurant();
        restaurantService.addRestaurant(restaurant);
        return restaurant.getId();
    }

    /**
     * Delete a restaurant
     *
     * @param restaurantId the restaurant to delete
     */
    @Endpoint(path = "/", method = ssdbrestframework.HttpMethod.DELETE)
    @Response(status = 204) // No Content
    public void deleteRestaurant(@RequestBody int restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
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
    @Endpoint(path = "/{restaurantName}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<RestaurantDTO> getRestaurantsByName(@RequestParam("restaurantName") String restaurantName) {
        try {
            List<Restaurant> restaurants = restaurantService.getRestaurantsByName(restaurantName);
            return restaurants.stream().map(Restaurant::convertRestaurantToRestaurantDTO).toList();
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
    @Response(status = 200) // OK
    public List<RestaurantDTO> getRestaurantsByFoodType(@RequestParam("food-types") List<FoodType> foodTypes) {
        try {
            List<Restaurant> restaurants = restaurantService.getRestaurantsByFoodType(foodTypes);
            return restaurants
                    .stream()
                    .map(Restaurant::convertRestaurantToRestaurantDTO)
                    .toList();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No restaurants found with the food types: " + foodTypes);
        }
    }

    /**
     * Get restaurants by availability
     *
     * @return list of matching restaurants
     */
    @Endpoint(path = "/by/availability", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200) // OK
    public List<RestaurantDTO> getRestaurantsByAvailability() {
        try {
            List<Restaurant> restaurants = restaurantService.getRestaurantsByAvailability(LocalDateTime.now());
            return restaurants
                    .stream()
                    .map(Restaurant::convertRestaurantToRestaurantDTO)
                    .toList();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No restaurants found with availability at: " + LocalDateTime.now());
        }
    }
}

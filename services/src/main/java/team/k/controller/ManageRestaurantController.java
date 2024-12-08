package team.k.controller;

import commonlibrary.dto.RestaurantDTO;
import commonlibrary.model.restaurant.Restaurant;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.service.ManageRestaurantService;
import team.k.service.RestaurantService;

@RestController(path = "/manage-restaurants")
public class ManageRestaurantController {

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
        ManageRestaurantService.addRestaurant(restaurant);
        return restaurant.getId();
    }

    /**
     * Delete a restaurant
     *
     * @param restaurantId the restaurant to delete
     */
    @Endpoint(path = "/delete/{restaurantId}", method = ssdbrestframework.HttpMethod.DELETE)
    @Response(status = 204) // No Content
    public void deleteRestaurant(@PathVariable("restaurantId") int restaurantId) {
        ManageRestaurantService.deleteRestaurant(restaurantId);
    }
}

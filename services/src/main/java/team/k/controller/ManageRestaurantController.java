package team.k.controller;

import commonlibrary.dto.RestaurantDTO;
import commonlibrary.model.restaurant.Restaurant;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.service.ManageRestaurantService;
import commonlibrary.dto.DishDTO;
import commonlibrary.dto.ManagingRestaurantDTO;

import commonlibrary.model.Dish;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.*;


@RestController(path = "/management")
public class ManageRestaurantController {


    /**
     * Update the restaurant infos
     *
     * @param restaurantId          the id of the restaurant
     * @param ManagingRestaurantDTO the new infos
     * @return the updated restaurant
     */
    @Endpoint(path = "/update-restaurant-infos", method = HttpMethod.POST)
    @Response(status = 204)
    public static RestaurantDTO updateRestaurantInfos(@RequestParam("restaurant-id") int restaurantId, @RequestBody ManagingRestaurantDTO ManagingRestaurantDTO) throws SSDBQueryProcessingException {
        return ManageRestaurantService.updateRestaurantInfos(
                restaurantId,
                ManagingRestaurantDTO.openTime(),
                ManagingRestaurantDTO.closeTime()
        ).restaurantToRestaurantDTO();

    }

    /**
     * Add a dish to the restaurant
     *
     * @param restaurantId
     * @param dishDTO
     * @return the updated restaurant
     */
    @Endpoint(path = "/add-dish", method = HttpMethod.POST)
    @Response(status = 201)
    public static RestaurantDTO addDish(@RequestParam("restaurant-id") int restaurantId, @RequestBody DishDTO dishDTO) throws SSDBQueryProcessingException {
        Dish dish = dishDTO.convertDishDtoToDish();
        return ManageRestaurantService.addDish(
                restaurantId,
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getPreparationTime()
        ).restaurantToRestaurantDTO();
    }

    /**
     * Remove a dish from the restaurant
     *
     * @param restaurantId
     * @param dishId
     * @return the updated restaurant
     */
    @Endpoint(path = "/remove-dish", method = HttpMethod.DELETE)
    @Response(status = 200)
    public static RestaurantDTO removeDish(@RequestParam("restaurant-id") int restaurantId, @RequestParam("dish-id") int dishId) throws SSDBQueryProcessingException {
        return ManageRestaurantService.removeDish(
                restaurantId,
                dishId
        ).restaurantToRestaurantDTO();
    }

    /**
     * Update a dish from the restaurant
     *
     * @param restaurantId
     * @param dishDTO
     * @return the updated restaurant
     */
    @Endpoint(path = "/update-dish", method = HttpMethod.PUT)
    @Response(status = 204)
    public static RestaurantDTO updateDish(@RequestParam("restaurant-id") int restaurantId, @RequestBody DishDTO dishDTO) throws SSDBQueryProcessingException {
        Dish dish = dishDTO.convertDishDtoToDish();
        return ManageRestaurantService.updateDish(
                restaurantId,
                dish.getId(),
                dish.getPrice(),
                dish.getPreparationTime()
        ).restaurantToRestaurantDTO();
    }

    /**
     * Add a new restaurant
     *
     * @param restaurantDto the restaurant to add
     * @return the restaurant id
     */
    @Endpoint(path = "/add/restaurant", method = ssdbrestframework.HttpMethod.POST)
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

package team.k.controller;

import commonlibrary.dto.RestaurantDTO;
import commonlibrary.model.restaurant.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class ManageRestaurantController {

    private ManageRestaurantService manageRestaurantService;

    @Autowired
    public ManageRestaurantController(ManageRestaurantService ManageRestaurantService) {
        this.manageRestaurantService = ManageRestaurantService;
    }


    /**
     * Update the restaurant infos
     *
     * @param restaurantId          the id of the restaurant
     * @param ManagingRestaurantDTO the new infos
     * @return the updated restaurant
     * @throws SSDBQueryProcessingException if the restaurant is not found
     */
    @Endpoint(path = "/update-restaurant-infos", method = HttpMethod.POST)
    @Response(status = 204)
    public RestaurantDTO updateRestaurantInfos(@RequestParam("restaurant-id") int restaurantId, @RequestBody ManagingRestaurantDTO ManagingRestaurantDTO) throws SSDBQueryProcessingException {
        return manageRestaurantService.updateRestaurantInfos(
                restaurantId,
                ManagingRestaurantDTO.openTime(),
                ManagingRestaurantDTO.closeTime()
        ).restaurantToRestaurantDTO();

    }

    /**
     * Add a dish to the restaurant
     *
     * @param restaurantId the id of the restaurant
     * @param dishDTO   the dish to add
     * @return the updated restaurant
     * @throws SSDBQueryProcessingException if the restaurant is not found
     */
    @Endpoint(path = "/add-dish", method = HttpMethod.POST)
    @Response(status = 201)
    public RestaurantDTO addDish(@RequestParam("restaurant-id") int restaurantId, @RequestBody DishDTO dishDTO) throws SSDBQueryProcessingException {
        Dish dish = dishDTO.convertDishDtoToDish();
        return manageRestaurantService.addDish(
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
     * @param restaurantId the id of the restaurant
     * @param dishId  the id of the dish to remove
     * @return the updated restaurant
     * @throws SSDBQueryProcessingException if the dish is not found
     */
    @Endpoint(path = "/remove-dish", method = HttpMethod.DELETE)
    @Response(status = 200)
    public RestaurantDTO removeDish(@RequestParam("restaurant-id") int restaurantId, @RequestParam("dish-id") int dishId) throws SSDBQueryProcessingException {
        return manageRestaurantService.removeDish(
                restaurantId,
                dishId
        ).restaurantToRestaurantDTO();
    }

    /**
     * Update a dish from the restaurant
     *
     * @param restaurantId the id of the restaurant
     * @param dishDTO  the dish to update
     * @return the updated restaurant
     * @throws SSDBQueryProcessingException if the dish is not found
     */
    @Endpoint(path = "/update-dish", method = HttpMethod.PUT)
    @Response(status = 204)
    public RestaurantDTO updateDish(@RequestParam("restaurant-id") int restaurantId, @RequestBody DishDTO dishDTO) throws SSDBQueryProcessingException {
        Dish dish = dishDTO.convertDishDtoToDish();
        return manageRestaurantService.updateDish(
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
        manageRestaurantService.addRestaurant(restaurant);
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
        manageRestaurantService.deleteRestaurant(restaurantId);
    }
}

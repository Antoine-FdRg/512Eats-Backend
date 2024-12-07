package team.k.controller;

import commonlibrary.dto.DishDTO;
import commonlibrary.dto.ManagingRestaurantDTO;
import commonlibrary.dto.RestaurantDTO;
import commonlibrary.model.Dish;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.*;
import team.k.service.ManageRestaurantService;

@RestController(path = "/manage-restaurant")
public class ManageRestaurantController {


    @Endpoint(path = "/update-restaurant-infos", method = HttpMethod.POST)
    @Response(status = 204)
    public static RestaurantDTO updateRestaurantInfos(@RequestParam("restaurant-id") int restaurantId, @RequestBody ManagingRestaurantDTO ManagingRestaurantDTO) throws SSDBQueryProcessingException {
        return ManageRestaurantService.updateRestaurantInfos(
                restaurantId,
                ManagingRestaurantDTO.openTime(),
                ManagingRestaurantDTO.closeTime()
        ).restaurantToRestaurantDTO();

    }

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

    @Endpoint(path = "/remove-dish", method = HttpMethod.DELETE)
    @Response(status = 200)
    public static RestaurantDTO removeDish(@RequestParam("restaurant-id") int restaurantId, @RequestParam("dish-id") int dishId) throws SSDBQueryProcessingException {
        return ManageRestaurantService.removeDish(
                restaurantId,
                dishId
        ).restaurantToRestaurantDTO();
    }

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
}

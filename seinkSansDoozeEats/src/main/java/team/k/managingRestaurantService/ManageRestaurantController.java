package team.k.managingRestaurantService;

import commonlibrary.dto.DishDTO;
import commonlibrary.model.Dish;
import lombok.RequiredArgsConstructor;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.*;

@RequiredArgsConstructor
@RestController(path = "/manage")
public class ManageRestaurantController {

    public record ManagingRestaurantDTO(int id, String openTime, String closeTime) {
    }

    private final ManageRestaurantService manageRestaurantService;

    @Endpoint(path = "/updateRestaurantInfos", method = HttpMethod.POST)
    @Response(status = 204)
    public void updateRestaurantInfos(@RequestBody ManagingRestaurantDTO ManagingRestaurantDTO) {
        manageRestaurantService.updateRestaurantInfos(ManagingRestaurantDTO.id(), ManagingRestaurantDTO.openTime(), ManagingRestaurantDTO.closeTime());
    }

    @Endpoint(path = "/addDish", method = HttpMethod.POST)
    @Response(status = 201)
    public void addDish(@RequestParam("restaurantId") int restaurantId, @RequestBody DishDTO dishDTO) {
        Dish dish = dishDTO.convertDishDtoToDish();
        manageRestaurantService.addDish(restaurantId, dish.getName(), dish.getDescription(), dish.getPrice(), dish.getPreparationTime());
    }

    @Endpoint(path = "/removeDish", method = HttpMethod.DELETE)
    @Response(status = 200)
    public void removeDish(@RequestParam("restaurantId") int restaurantId, @RequestParam("dishId") int dishId) {
        manageRestaurantService.removeDish(restaurantId, dishId);
    }

    @Endpoint(path = "/updateDish", method = HttpMethod.PUT)
    @Response(status = 204)
    public void updateDish(@RequestParam("restaurantId") int restaurantId, @RequestBody DishDTO dishDTO) {
        Dish dish = dishDTO.convertDishDtoToDish();
        manageRestaurantService.updateDish(restaurantId, dish.getId(), dish.getPrice(), dish.getPreparationTime());
    }
}


package team.k;

import ssdbrestframework.annotations.RestController;
import team.k.entities.restaurant.RestaurantEntity;

@RestController(path = "/restaurants")
public class RestaurantController extends GenericController<RestaurantEntity> {
    public RestaurantController() {
        super(RestaurantEntity.class);
    }
}

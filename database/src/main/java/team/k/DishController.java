package team.k;

import ssdbrestframework.annotations.RestController;
import team.k.entities.restaurant.DishEntity;

@RestController(path = "/dishes")
public class DishController extends GenericController<DishEntity> {
    public DishController() {
        super(DishEntity.class);
    }
}

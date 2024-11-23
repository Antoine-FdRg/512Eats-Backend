package team.k;

import ssdbrestframework.annotations.RestController;

@RestController(path = "/dishes")
public class DishController extends GenericController<Dish> {
    public DishController() {
        super(Dish.class);
    }
}

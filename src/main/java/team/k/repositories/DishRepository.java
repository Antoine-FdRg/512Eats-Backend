package team.k.repositories;

import team.k.common.Dish;

import java.util.ArrayList;
import java.util.List;


public class DishRepository {
    private List<Dish> dishes = new ArrayList<>();

    public Dish findById(int dishId) {
        return dishes.get(dishId);
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        dishes.remove(dish);
    }

    public List<Dish> findAll() {
        return dishes;
    }

    public void clear() {
        dishes.clear();
    }
}

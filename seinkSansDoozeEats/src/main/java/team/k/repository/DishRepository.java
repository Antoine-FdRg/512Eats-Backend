package team.k.repository;

import commonlibrary.model.Dish;

import java.util.ArrayList;
import java.util.List;


public class DishRepository {
    private final List<Dish> dishes = new ArrayList<>();

    public Dish findById(int dishId) {
        return dishes.stream().filter(restaurant -> restaurant.getId() == dishId).findFirst().orElse(null);
    }

    public void add(Dish dish) {
        dishes.add(dish);
    }

    public void remove(Dish dish) {
        dishes.remove(dish);
    }

    public List<Dish> findAll() {
        return dishes;
    }

    public void clear() {
        dishes.clear();
    }
}

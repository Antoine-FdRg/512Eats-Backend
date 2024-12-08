package team.k.repository;


import commonlibrary.model.Dish;

import java.util.ArrayList;
import java.util.List;


public class DishRepository {
    private static final List<Dish> dishes = new ArrayList<>();

    private DishRepository() {
        throw new IllegalStateException("Utility class");
    }

    public static Dish findById(int dishId) {
        return dishes.stream().filter(restaurant -> restaurant.getId() == dishId).findFirst().orElse(null);
    }

    public static void add(Dish dish) {
        dishes.add(dish);
    }

    public static void remove(Dish dish) {
        dishes.remove(dish);
    }

    public static List<Dish> findAll() {
        return dishes;
    }

    public static void clear() {
        dishes.clear();
    }
}

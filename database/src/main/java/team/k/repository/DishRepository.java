package team.k.repository;

import commonlibrary.model.Dish;


public class DishRepository extends GenericRepository<Dish> {

    private static DishRepository instance;

    private DishRepository() {
        super();
    }

    public static DishRepository getInstance() {
        if (instance == null) {
            instance = new DishRepository();
        }
        return instance;
    }

    public Dish findById(int dishId) {
        return findAll().stream().filter(restaurant -> restaurant.getId() == dishId).findFirst().orElse(null);
    }
}

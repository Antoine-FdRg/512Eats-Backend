package team.k.repository;

import commonlibrary.model.Dish;
import ssdbrestframework.SSDBQueryProcessingException;

import java.util.List;


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

    public static void throwIfDishIdDoesNotExist(int dishId) throws SSDBQueryProcessingException {
        if (DishRepository.getInstance().findById(dishId) == null) {
            throw new SSDBQueryProcessingException(404, "Dish with ID " + dishId + " not found.");
        }
    }

    public static void throwIfDishesDoNotExist(List<Dish> dishes) throws SSDBQueryProcessingException {
        for (Dish dish : dishes) {
            throwIfDishIdDoesNotExist(dish.getId());
        }
    }

    public static void throwIfDishIdsDoNotExist(List<Integer> dishesIds) throws SSDBQueryProcessingException {
        for (Integer dishID : dishesIds) {
            throwIfDishIdDoesNotExist(dishID);
        }
    }
}

package team.k.repository;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RestaurantRepository extends GenericRepository<Restaurant> {
    private static RestaurantRepository instance;

    private RestaurantRepository() {
        super();
    }

    public static RestaurantRepository getInstance() {
        if (instance == null) {
            instance = new RestaurantRepository();
        }
        return instance;
    }

    /**
     * Find a restaurant by its name.
     *
     * @param name the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant findByName(String name) {
        return findAll().stream().filter(restaurant -> restaurant.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Find a restaurant by its id.
     *
     * @param id the id of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant findById(int id) {
        return findAll().stream().filter(restaurant -> restaurant.getId() == id).findFirst().orElse(null);
    }

    public List<Restaurant> findRestaurantByFoodType(List<FoodType> foodTypes) {
        return findAll().stream().filter(restaurant -> restaurant.getFoodTypes().stream().anyMatch(foodTypes::contains)).toList();
    }

    public List<Restaurant> findRestaurantsByAvailability(LocalDateTime timeChosen) {
        return findAll().stream().filter(restaurant -> restaurant.isAvailable(timeChosen)).toList();
    }

    public List<Restaurant> findRestaurantByName(String name) {
        return findAll().stream().filter(restaurant -> Objects.equals(restaurant.getName(), name)).toList();
    }
}

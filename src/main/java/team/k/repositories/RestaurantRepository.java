package team.k.repositories;

import team.k.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {

    private final List<Restaurant> restaurants = new ArrayList<>();

    public void save(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public Restaurant findById(int id) {
        return restaurants.stream().filter(restaurant -> restaurant.getId() == id).findFirst().orElse(null);
    }

    public List<Restaurant> findAll() {
        return restaurants;
    }

    public void delete(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

    public void clear() {
        restaurants.clear();
    }
}

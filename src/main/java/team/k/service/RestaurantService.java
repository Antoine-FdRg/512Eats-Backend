package team.k.service;

import team.k.common.Dish;
import team.k.repository.RestaurantRepository;
import team.k.restaurant.Restaurant;

import java.util.List;

public class RestaurantService {


    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return this.restaurantRepository.findAll();
    }

    /**
     * Get a restaurant by its name
     *
     * @param restaurantName the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant getRestaurantByName(String restaurantName) {
        return restaurantRepository.findByName(restaurantName);
    }

    /**
     * Get all dishes from a restaurant
     *
     * @param restaurantName the name of the restaurant
     * @return the list of dishes if the restaurant is found, null otherwise
     */
    public List<Dish> getAllDishesFromRestaurant(String restaurantName) {
        Restaurant restaurant = this.getRestaurantByName(restaurantName);
        return restaurant.getDishes();
    }

    /**
     * Add a restaurant to the repository
     *
     * @param restaurant the restaurant to add
     */
    public void addRestaurant(Restaurant restaurant) {
        restaurantRepository.add(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        restaurantRepository.delete(restaurant);
    }


}

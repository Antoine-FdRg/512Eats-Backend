package team.k.service;

import commonlibrary.model.Dish;
import team.k.repository.RestaurantRepository;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalTime;


public class ManageRestaurantService {
    private static final String RESTAURANT_NOT_FOUND = "Restaurant not found";

    public static Restaurant updateRestaurantInfos(int restaurantId, String openTime, String closeTime) {
        Restaurant restaurant = restaurantValidator(restaurantId);
        if (openTime != null) {
            restaurant.setOpen(LocalTime.parse(openTime));
        }
        if (closeTime != null) {
            restaurant.setClose(LocalTime.parse(closeTime));
        }
        return restaurant;
    }

    public static Restaurant addDish(int restaurantId, String dishName, String dishDescription, double dishPrice, int dishPreparationTime) {
        Restaurant restaurant = restaurantValidator(restaurantId);
        restaurant.addDish(new Dish.Builder()
                .setName(dishName)
                .setDescription(dishDescription)
                .setPrice(dishPrice)
                .setPreparationTime(dishPreparationTime)
                .build()
        );
        return restaurant;
    }

    public static Restaurant removeDish(int restaurantId, int dishId) {
        Restaurant restaurant = restaurantValidator(restaurantId);
        restaurant.removeDish(dishId);
        return restaurant;
    }

    public static Restaurant updateDish(int restaurantId, int dishId, double newDishPrice, int newDishPreparationTime) {
        Restaurant restaurant = restaurantValidator(restaurantId);
        Dish dish = restaurant.getDishes().stream().filter(d -> d.getId() == dishId).findFirst().orElse(null);
        if (dish == null) {
            throw new IllegalArgumentException("Dish not found");
        }
        if (newDishPrice != 0) {
            dish.setPrice(newDishPrice);
        }
        if (newDishPreparationTime != 0) {
            dish.setPreparationTime(newDishPreparationTime);
        }
        return restaurant;

    }

    private static Restaurant restaurantValidator(int restaurantId) {
        Restaurant restaurant = RestaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException(RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }

    /**
     * Add a restaurant to the repository
     *
     * @param restaurant the restaurant to add
     */
    public static void addRestaurant(Restaurant restaurant) {
        RestaurantRepository.add(restaurant);
    }

    public static void deleteRestaurant(int restaurantId) {
        RestaurantRepository.delete(restaurantId);
    }
}

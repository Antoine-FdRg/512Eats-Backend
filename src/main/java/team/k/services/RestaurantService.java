package team.k.services;

import team.k.common.Dish;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;

public class RestaurantService {

    public void addTimeSlotToRestaurant(Restaurant restaurant, TimeSlot timeSlot) {
        restaurant.addTimeSlot(timeSlot);
    }

    public void addDishToRestaurant(Restaurant restaurant, Dish dish) {
        restaurant.addDish(dish);
    }
}

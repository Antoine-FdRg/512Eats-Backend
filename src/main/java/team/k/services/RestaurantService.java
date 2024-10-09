package team.k.services;

import team.k.common.Dish;
import team.k.repositories.RestaurantRepository;
import team.k.repositories.TimeSlotRepository;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;

public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private TimeSlotRepository timeSlotRepository;

    public void addTimeSlotToRestaurant(int restaurantId, int timeSlotId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        TimeSlot ts = timeSlotRepository.findById(timeSlotId);
        restaurant.addTimeSlot(ts);
    }

    public void addDishToRestaurant(Restaurant restaurant, Dish dish) {
        restaurant.addDish(dish);
    }
}

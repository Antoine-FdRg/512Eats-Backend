package team.k.services;

import team.k.RegisteredUser;
import team.k.common.Dish;
import team.k.repositories.DishRepository;
import team.k.repositories.RegisteredUserRepository;
import team.k.repositories.RestaurantRepository;
import team.k.repositories.SubOrderRepository;
import team.k.restaurant.Restaurant;

public class OrderService {
    private RestaurantRepository restaurantRepository;
    private DishRepository dishRepository;
    private SubOrderRepository subOrderRepository;

    private RegisteredUserRepository registeredUserRepository;

    public void addDishToRegisteredUserBasket(int registeredUserID, int dishId, int restaurantId) {
        Dish dish = dishRepository.findById(dishId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if(!restaurant.getDishes().contains(dish)) {
            throw new IllegalArgumentException("Dish not found in restaurant");
        }
        RegisteredUser registeredUser  = registeredUserRepository.findById(registeredUserID);
        if (registeredUser.getCurrentOrder()!= null && registeredUser.getCurrentOrder().getRestaurant() != restaurant) {
            throw new IllegalArgumentException("Dishes of an order can only be from the same restaurant");
        }
        registeredUser.addDishToBasket(dish, restaurant);
        subOrderRepository.save(registeredUser.getCurrentOrder());

    }
}

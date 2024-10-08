package team.k.services;

import team.k.RegisteredUser;
import team.k.common.Dish;
import team.k.order.GroupOrder;
import team.k.restaurant.Restaurant;

public class OrderService {
    public void addDishToRegisteredUserBasket(RegisteredUser registeredUser, Dish dish, Restaurant restaurant) {
        if (registeredUser.getCurrentOrder()!= null && registeredUser.getCurrentOrder().getRestaurant() != restaurant) {
            throw new IllegalArgumentException("Dishes of an order can only be from the same restaurant");
        }
        registeredUser.addDishToBasket(dish, restaurant);
    }

    public void addRegisteredUserToGroupOrder(RegisteredUser registeredUser, GroupOrder groupOrder) {
        registeredUser.joinGroupOrder(groupOrder);
        groupOrder.addSubOrder(registeredUser.getCurrentOrder());
    }
}

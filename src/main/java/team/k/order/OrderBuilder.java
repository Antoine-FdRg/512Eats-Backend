package team.k.order;

import team.k.RegisteredUser;
import team.k.common.Dish;
import team.k.common.Location;
import team.k.enumerations.OrderStatus;
import team.k.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    private final int id;
    private double price;
    private GroupOrder groupOrder;
    private Restaurant restaurant;
    private RegisteredUser user;
    private final List<Dish> dishes;
    private LocalDateTime deliveryTime;
    private Location deliveryLocation;
    private static int idCounter = 0;

    public OrderBuilder() {
        id = idCounter++;
        dishes = new ArrayList<>();
    }

    public OrderBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public OrderBuilder setGroupOrder(GroupOrder groupOrder) {
        this.groupOrder = groupOrder;
        return this;
    }

    public OrderBuilder setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public OrderBuilder setUser(RegisteredUser user) {
        this.user = user;
        return this;
    }

    public OrderBuilder setDeliveryLocation(Location deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
        return this;
    }

    public OrderBuilder setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
        return this;
    }

    public SubOrder build() {
        if(groupOrder != null && deliveryLocation != null) {
            throw new IllegalArgumentException("The builder has both a group order and a delivery location, it should have only one of them.\n" +
                    "It needs a groupOrder to create a SubOrder or a deliveryLocation to create an IndividualOrder");
        }
        if(this.groupOrder == null) {
            return new IndividualOrder(id, price, null, restaurant, user, dishes, OrderStatus.CREATED, null, deliveryTime, deliveryLocation);

        }
        return new SubOrder(id, price, groupOrder, restaurant, user, dishes, OrderStatus.CREATED, null, deliveryTime);
    }
}

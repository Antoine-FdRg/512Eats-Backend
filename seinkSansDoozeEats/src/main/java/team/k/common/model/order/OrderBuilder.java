package team.k.common.model.order;

import team.k.common.model.RegisteredUser;
import team.k.common.model.Dish;
import team.k.common.model.Location;
import team.k.common.enumerations.OrderStatus;
import team.k.common.model.payment.Payment;
import team.k.common.model.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    final int id;
    OrderStatus status;
    double price;
    GroupOrder groupOrder;
    Restaurant restaurant;
    RegisteredUser user;
    final List<Dish> dishes;
    LocalDateTime deliveryTime;
    private Location deliveryLocation;
    LocalDateTime placedDate;
    Payment payment;

    private static int idCounter = 0;

    public OrderBuilder() {
        id = idCounter++;
        dishes = new ArrayList<>();
        status = OrderStatus.CREATED;
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

    public OrderBuilder setPlacedDate(LocalDateTime placedDate) {
        this.placedDate = placedDate;
        return this;
    }

    public OrderBuilder setDishes(List<Dish> dishes) {
        this.dishes.addAll(dishes);
        return this;
    }

    public OrderBuilder setPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public SubOrder build() {
        if(groupOrder != null && deliveryLocation != null) {
            throw new IllegalArgumentException("The builder has both a group order and a delivery location, it should have only one of them.\n" +
                    "It needs a groupOrder to create a SubOrder or a deliveryLocation to create an IndividualOrder");
        }
        if(this.groupOrder == null) {
            return new IndividualOrder(this);
        }
        return new SubOrder(this);
    }


}

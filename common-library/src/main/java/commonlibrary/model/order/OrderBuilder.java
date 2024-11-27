package commonlibrary.model.order;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.payment.Payment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    int id;
    OrderStatus status;
    double price;
    GroupOrder groupOrder;
    int restaurantID;
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

    public OrderBuilder setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
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

    public OrderBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public OrderBuilder setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public SubOrder build() {
        if (groupOrder != null && deliveryLocation != null) {
            throw new IllegalArgumentException("The builder has both a group order and a delivery location, it should have only one of them.\n" +
                    "It needs a groupOrder to create a SubOrder or a deliveryLocation to create an IndividualOrder");
        }
        if (this.groupOrder == null) {
            return new IndividualOrder(this);
        }
        return new SubOrder(this);
    }


}

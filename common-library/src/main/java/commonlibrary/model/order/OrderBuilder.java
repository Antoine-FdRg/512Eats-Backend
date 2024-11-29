package commonlibrary.model.order;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.payment.Payment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    int id;
    OrderStatus status;
    double price;
    int restaurantID;
    int userID;
    final List<Dish> dishes;
    LocalDateTime deliveryTime;
    Location deliveryLocation;
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

    public OrderBuilder setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
        return this;
    }

    public OrderBuilder setUserID(int userID) {
        this.userID = userID;
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
        if (deliveryLocation != null) {
            return new IndividualOrder(this);
        }
        return new SubOrder(this);
    }


}

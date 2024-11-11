package team.k.order;


import lombok.Getter;
import lombok.Setter;
import team.k.common.Dish;
import team.k.RegisteredUser;
import team.k.restaurant.Restaurant;
import team.k.enumerations.OrderStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class SubOrder {
    private int id;
    private double price;
    private GroupOrder groupOrder;
    private Restaurant restaurant;
    private RegisteredUser user;
    private List<Dish> dishes;
    private OrderStatus status;
    private LocalDateTime placedDate;
    private LocalDateTime deliveryDate;
    private Payment payment;

    SubOrder(OrderBuilder orderBuilder) {
        this.id = orderBuilder.id;
        this.price = orderBuilder.price;
        this.groupOrder = orderBuilder.groupOrder;
        this.restaurant = orderBuilder.restaurant;
        this.user = orderBuilder.user;
        this.dishes = orderBuilder.dishes;
        this.status = orderBuilder.status;
        this.placedDate = orderBuilder.placedDate;
        this.deliveryDate = orderBuilder.deliveryTime;
        this.payment = orderBuilder.payment;
    }

    public Dish getCheaperDish() {
        return dishes.stream().min(Comparator.comparingDouble(Dish::getPrice)).orElse(null);
    }

    public boolean addDish(Dish dish) {
        this.price += dish.getPrice();
        return dishes.add(dish);
    }

    public int getPreparationTime() {
        return dishes.stream().mapToInt(Dish::getPreparationTime).sum();
    }

    public void cancel() {
        status = OrderStatus.CANCELED;
    }

    public void place(LocalDateTime now) {
        this.setStatus(OrderStatus.PLACED);
        this.setPlacedDate(now);
        this.user.addOrderToHistory(this);
    }

    public void pay(LocalDateTime now) {
        if(restaurant.getDiscountStrategy()!=null){
            this.price= this.restaurant.getDiscountStrategy().applyDiscount(this); //Appliquer la discount
        }
        this.setStatus(OrderStatus.PAID);
    }
}

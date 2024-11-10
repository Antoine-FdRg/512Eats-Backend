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

    SubOrder(int id, double price, GroupOrder groupOrder, Restaurant restaurant, RegisteredUser user, List<Dish> dish, OrderStatus orderStatus, LocalDateTime placedDate, LocalDateTime deliveryDate) {
        this.id = id;
        this.price = price;
        this.groupOrder = groupOrder;
        this.restaurant = restaurant;
        this.user = user;
        this.dishes = dish;
        this.status = orderStatus;
        this.placedDate = placedDate;
        this.deliveryDate = deliveryDate;
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

    public void place(LocalDateTime localDateTime) {
        this.setStatus(OrderStatus.PLACED);
        this.user.addOrderToHistory(this);

    }

    public void pay() {
        if(restaurant.getDiscountStrategy()!=null){
            this.price= this.restaurant.getDiscountStrategy().applyDiscount(this); //Appliquer la discount
        }
        this.setStatus(OrderStatus.PAID);

    }
}

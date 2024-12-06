package commonlibrary.model.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.DishDTO;
import commonlibrary.dto.PaymentDTO;
import commonlibrary.dto.SubOrderDTO;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commonlibrary.model.payment.Payment;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class SubOrder {
    private int id;
    private double price;
    private int restaurantID;
    private int userID;
    private List<Dish> dishes;
    private OrderStatus status;
    private LocalDateTime placedDate;
    private LocalDateTime deliveryDate;
    private Payment payment;

    SubOrder(OrderBuilder orderBuilder) {
        this.id = orderBuilder.id;
        this.price = orderBuilder.price;
        this.restaurantID = orderBuilder.restaurantID;
        this.userID = orderBuilder.userID;
        this.dishes = orderBuilder.dishes;
        this.status = orderBuilder.status;
        this.placedDate = orderBuilder.placedDate;
        this.deliveryDate = orderBuilder.deliveryTime;
        this.payment = orderBuilder.payment;
        if (this.price == 0 && !dishes.isEmpty()) {
            this.price = dishes.stream().mapToDouble(Dish::getPrice).sum();
        }
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

    public void place(LocalDateTime now, RegisteredUser orderOwner) {
        this.setStatus(OrderStatus.PLACED);
        this.setPlacedDate(now);
        orderOwner.addOrderToHistory(this);
    }

    public void pay(LocalDateTime now, Restaurant restaurant, RegisteredUser user) {
        if (restaurant.getDiscountStrategy() != null) {
            this.price = restaurant.getDiscountStrategy().applyDiscount(this, user); //Appliquer la discount
        }
        this.setStatus(OrderStatus.PAID);
    }

    public SubOrderDTO convertSubOrderToSubOrderDto() {
        List<DishDTO> convertedDishes = dishes.stream()
                .map(Dish::convertDishToDishDto)
                .toList();

        PaymentDTO convertedPayment = payment.convertPaymentToPaymentDto();

        return new SubOrderDTO(id, String.valueOf(price), restaurantID, userID,
                convertedDishes, status.toString(), placedDate.toString(), deliveryDate.toString(), convertedPayment);
    }
}

package commonlibrary.model.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import commonlibrary.dto.DishDTO;
import commonlibrary.dto.SubOrderDTO;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.payment.Payment;
import commonlibrary.model.restaurant.Restaurant;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "sub_order")
public class SubOrder {
    @Id
    private int id;
    private double price;
    private int restaurantID;
    private int userID;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Dish> dishes;
    private OrderStatus status;
    private LocalDateTime placedDate;
    private LocalDateTime deliveryDate;
    @OneToOne(fetch = FetchType.EAGER)
    private Payment payment;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private GroupOrder groupOrder;

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

    /**
     * Remove a dish from the order and decrease the price of the order
     *
     * @param dishID the id of the dish to remove
     * @return true if the dish was removed, false otherwise
     */
    public boolean removeDish(int dishID) {
        Dish dishToRemove = dishes.stream().filter(dish -> dish.getId() == dishID).findFirst().orElse(null);
        if (dishToRemove == null) {
            return false;
        }
        this.price -= dishToRemove.getPrice();
        return dishes.remove(dishToRemove);
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

        return new SubOrderDTO(id, String.valueOf(price), restaurantID, userID,
                convertedDishes,
                status != null ? status.toString() : null,
                placedDate != null ? placedDate.toString() : null,
                deliveryDate != null ? deliveryDate.toString() : null,
                payment != null ? payment.convertPaymentToPaymentDto() : null
        );
    }
}

package team.k.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class SubOrder {
    private int id;
    private double price;
    private GroupOrder groupOrder;
    private Restaurant restaurant;
    private RegisteredUser user;
    private List<Dish> dishes;
    private OrderStatus status;
    private LocalDateTime date;

    public Dish getCheaperDish() {
        return dishes.stream().min(Comparator.comparingDouble(Dish::getPrice)).orElse(null);
    }

    public boolean addDish(Dish dish) {
        return dishes.add(dish);
    }

    public int getPreparationTime() {
        return dishes.stream().mapToInt(Dish::getPreparationTime).sum();
    }

    public void cancel() {
        status = OrderStatus.CANCELED;
    }
}

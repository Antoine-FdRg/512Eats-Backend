package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Dish;
import team.k.enumerations.Role;
import team.k.order.GroupOrder;
import team.k.order.SubOrder;
import team.k.order.OrderBuilder;
import team.k.restaurant.Restaurant;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RegisteredUser {
    private int id;
    private String name;
    private Role role;
    private SubOrder currentOrder;
    private List<SubOrder> orders;
    private static int idCounter = 0;

    public RegisteredUser(String name, Role role) {
        this.id = idCounter++;
        this.name = name;
        this.role = role;
    }

    public boolean addOrderToHistory(SubOrder order) {
        return orders.add(order);
    }

    public void addDishToBasket(Dish dish, Restaurant restaurant) {
        if (currentOrder == null) {
            currentOrder = new OrderBuilder()
                    .setUser(this)
                    .setRestaurant(restaurant)
                    .build();
        }
        currentOrder.addDish(dish);
    }

    public void joinGroupOrder(GroupOrder groupOrder) {
        if (currentOrder == null) {
            currentOrder = new OrderBuilder()
                    .setUser(this)
                    .setGroupOrder(groupOrder)
                    .build();
        }
        currentOrder.setGroupOrder(groupOrder);
    }
}

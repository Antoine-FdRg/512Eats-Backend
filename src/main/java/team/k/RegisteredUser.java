package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Location;
import team.k.enumerations.Role;
import team.k.order.SubOrder;
import team.k.order.OrderBuilder;
import team.k.restaurant.Restaurant;

import java.time.LocalDateTime;
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

    public void initializeOrder(Restaurant restaurant, Location deliveryLocation, LocalDateTime deliveryTime) {
        currentOrder = new OrderBuilder()
                .setUser(this)
                .setRestaurant(restaurant)
                .setDeliveryLocation(deliveryLocation)
                .setDeliveryTime(deliveryTime)
                .build();
    }
}

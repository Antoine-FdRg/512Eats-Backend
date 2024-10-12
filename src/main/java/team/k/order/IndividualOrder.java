package team.k.order;

import lombok.Getter;
import lombok.Setter;
import team.k.RegisteredUser;
import team.k.common.Dish;
import team.k.common.Location;
import team.k.enumerations.OrderStatus;
import team.k.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class IndividualOrder extends SubOrder {
    private Location deliveryLocation;

    IndividualOrder(int id, double price, GroupOrder groupOrder, Restaurant restaurant, RegisteredUser user, List<Dish> dish, OrderStatus orderStatus, LocalDateTime placedDate, LocalDateTime deliveryDate, Location deliveryLocation) {
        super(id, price, groupOrder, restaurant, user, dish, orderStatus, placedDate, deliveryDate);
        this.deliveryLocation = deliveryLocation;
    }


}

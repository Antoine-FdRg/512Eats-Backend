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

    IndividualOrder(OrderBuilder orderBuilder) {
        super(orderBuilder);
    }

    @Override
    public void pay(LocalDateTime now) {
        super.pay(now);
        super.place(now);
    }
}

package team.k.common.model.order;

import lombok.Getter;
import lombok.Setter;
import team.k.common.model.Location;

import java.time.LocalDateTime;

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

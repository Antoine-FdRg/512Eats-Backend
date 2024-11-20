package commonlibrary.model.order;

import commonlibrary.model.Location;
import lombok.Getter;
import lombok.Setter;

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

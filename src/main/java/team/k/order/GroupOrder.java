package team.k.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Location;
import team.k.enumerations.OrderStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupOrder {
    private int id;
    private Date date;
    private OrderStatus status;
    private List<SubOrder> subOrders;
    private Location deliveryLocation;

    public boolean addSubOrder(SubOrder subOrder) {
        return subOrders.add(subOrder);
    }

    public void close() {
        status = OrderStatus.CANCELED;
        this.subOrders.forEach(subOrder -> subOrder.cancel());
    }
}

package team.k.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Location;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupOrder {
    private int id;
    private Date date;
    private List<SubOrder> subOrders;
    private Location deliveryLocation;

    public boolean addSubOrder(SubOrder subOrder) {
        return subOrders.add(subOrder);
    }

}

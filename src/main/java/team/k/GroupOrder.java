package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupOrder {
    private int id;
    private Date date;
    private List<SubOrder> subOrders;

    public boolean addSubOrder(SubOrder subOrder) {
        return subOrders.add(subOrder);
    }

}

package team.k.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import commonlibrary.model.order.GroupOrder;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GroupOrderRepository {
    List<GroupOrder> groupOrders = new ArrayList<>();

    public GroupOrder findGroupOrderById(int id) {
        return groupOrders.stream().filter(groupOrder -> groupOrder.getId() == id).findFirst().orElse(null);
    }

    public void add(GroupOrder groupOrder) {
        groupOrders.add(groupOrder);
    }
}

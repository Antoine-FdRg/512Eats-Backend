package team.k.repository;

import commonlibrary.model.order.GroupOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

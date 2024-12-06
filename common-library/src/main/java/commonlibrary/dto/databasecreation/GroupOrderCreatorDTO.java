package commonlibrary.dto.databasecreation;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.SubOrder;

import java.time.LocalDateTime;
import java.util.List;

public record GroupOrderCreatorDTO(LocalDateTime deliveryDateTime,
                                   OrderStatus status,
                                   List<Integer> subOrderIDs,
                                   int deliveryLocationID) {

    public GroupOrderCreatorDTO(GroupOrder groupOrder) {
        this(groupOrder.getDeliveryDateTime(),
                groupOrder.getStatus(),
                groupOrder.getSubOrders().stream().map(SubOrder::getId).toList(),
                groupOrder.getDeliveryLocationID());
    }
}
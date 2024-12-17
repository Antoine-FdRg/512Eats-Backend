package commonlibrary.dto.databaseupdator;

import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.SubOrder;

import java.time.LocalDateTime;
import java.util.List;

public record GroupOrderUpdatorDTO(int id,
                                   LocalDateTime deliveryDateTime,
                                   String status,
                                   List<Integer> subOrderIDs,
                                   int deliveryLocationID) {
    public GroupOrderUpdatorDTO(GroupOrder groupOrder) {
        this(groupOrder.getId(),
                groupOrder.getDeliveryDateTime(),
                groupOrder.getStatus().name(),
                groupOrder.getSubOrders().stream().map(SubOrder::getId).toList(),
                groupOrder.getDeliveryLocationID());
    }
}
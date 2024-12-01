package team.k.models;

import commonlibrary.dto.databaseupdator.GroupOrderUpdatorDTO;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.SubOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PersistedGroupOrder {
    private int id;
    private LocalDateTime deliveryDateTime;
    private OrderStatus status;
    private List<Integer> suborderIDs;
    private final int deliveryLocationID;

    public PersistedGroupOrder(GroupOrder groupOrder) {
        this.id = groupOrder.getId();
        this.deliveryDateTime = groupOrder.getDeliveryDateTime();
        this.status = groupOrder.getStatus();
        this.suborderIDs = groupOrder.getSubOrders().stream().map(SubOrder::getId).toList();
        this.deliveryLocationID = groupOrder.getDeliveryLocationID();
    }

    public PersistedGroupOrder(GroupOrderUpdatorDTO groupOrderUpdatorDTO) {
        this.id = groupOrderUpdatorDTO.id();
        this.deliveryDateTime = groupOrderUpdatorDTO.deliveryDateTime();
        this.status = OrderStatus.valueOf(groupOrderUpdatorDTO.status());
        this.suborderIDs = groupOrderUpdatorDTO.subOrderIDs();
        this.deliveryLocationID = groupOrderUpdatorDTO.deliveryLocationID();
    }
}

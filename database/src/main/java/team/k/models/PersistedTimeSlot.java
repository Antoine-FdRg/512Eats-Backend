package team.k.models;

import commonlibrary.dto.databaseupdator.TimeSlotUpdatorDTO;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.restaurant.TimeSlot;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PersistedTimeSlot {
    private int id;
    private List<Integer> orderIDs;
    private LocalDateTime start;
    private int productionCapacity; //number of cooks
    private int maxNumberOfOrders;

    public PersistedTimeSlot(TimeSlot timeSlot) {
        this.id = timeSlot.getId();
        this.orderIDs = timeSlot.getOrders().stream().map(SubOrder::getId).toList();
        this.start = timeSlot.getStart();
        this.productionCapacity = timeSlot.getProductionCapacity();
        this.maxNumberOfOrders = timeSlot.getMaxNumberOfOrders();
    }

    public PersistedTimeSlot(TimeSlotUpdatorDTO timeSlotUpdatorDTO) {
        this.id = timeSlotUpdatorDTO.id();
        this.orderIDs = timeSlotUpdatorDTO.orderIDs();
        this.start = timeSlotUpdatorDTO.start();
        this.productionCapacity = timeSlotUpdatorDTO.productionCapacity();
        this.maxNumberOfOrders = timeSlotUpdatorDTO.maxNumberOfOrders();
    }
}

package commonlibrary.dto.databaseupdator;

import commonlibrary.model.restaurant.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;

public record TimeSlotUpdatorDTO(int id, List<Integer> orderIDs, LocalDateTime start, int productionCapacity,
                                 int maxNumberOfOrders) {
    public TimeSlotUpdatorDTO(TimeSlot timeSlot) {
        this(timeSlot.getId(), timeSlot.getOrders().stream().map(order -> order.getId()).toList(), timeSlot.getStart(),
                timeSlot.getProductionCapacity(), timeSlot.getMaxNumberOfOrders());
    }
}

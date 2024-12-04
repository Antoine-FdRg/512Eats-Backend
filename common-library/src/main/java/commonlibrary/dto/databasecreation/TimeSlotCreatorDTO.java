package commonlibrary.dto.databasecreation;

import commonlibrary.model.restaurant.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;

public record TimeSlotCreatorDTO(List<Integer> orderIDs, LocalDateTime start, int productionCapacity,
                                 int maxNumberOfOrders) {
    public TimeSlotCreatorDTO(TimeSlot timeSlot) {
        this(timeSlot.getOrders().stream().map(order -> order.getId()).toList(), timeSlot.getStart(),
                timeSlot.getProductionCapacity(), timeSlot.getMaxNumberOfOrders());
    }
}

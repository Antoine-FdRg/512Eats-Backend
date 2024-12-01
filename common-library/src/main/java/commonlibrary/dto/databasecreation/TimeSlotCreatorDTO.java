package commonlibrary.dto.databasecreation;

import java.time.LocalDateTime;
import java.util.List;

public record TimeSlotCreatorDTO(List<Integer> orderIDs, LocalDateTime start, int productionCapacity, int maxNumberOfOrders) {
}

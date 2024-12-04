package commonlibrary.dto.databaseupdator;

import java.time.LocalDateTime;
import java.util.List;

public record TimeSlotUpdatorDTO(int id, List<Integer> orderIDs, LocalDateTime start, int productionCapacity,
                                 int maxNumberOfOrders) {
}

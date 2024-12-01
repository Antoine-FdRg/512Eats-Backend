package commonlibrary.dto.databaseupdator;

import java.time.LocalDateTime;
import java.util.List;

public record GroupOrderUpdatorDTO(int id,
                                   LocalDateTime deliveryDateTime,
                                   String status,
                                   List<Integer> subOrderIDs,
                                   int deliveryLocationID) {
}
package commonlibrary.dto.databasecreation;

import commonlibrary.enumerations.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record GroupOrderCreatorDTO(LocalDateTime deliveryDateTime,
                                   OrderStatus status,
                                   List<Integer> subOrderIDs,
                                   int deliveryLocationID) {
}
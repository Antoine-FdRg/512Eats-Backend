package commonlibrary.dto.databaseupdator;

import commonlibrary.dto.PaymentDTO;

import java.time.LocalDateTime;
import java.util.List;

public record SubOrderUpdatorDTO(int id, int restaurantID, int userID,
                                 List<Integer> dishIDs, String status, LocalDateTime placedDate,
                                 LocalDateTime deliveryDateTime,
                                 PaymentDTO payment) {
}

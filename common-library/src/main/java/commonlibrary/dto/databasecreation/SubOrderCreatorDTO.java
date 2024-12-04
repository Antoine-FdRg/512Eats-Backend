package commonlibrary.dto.databasecreation;

import commonlibrary.dto.PaymentDTO;

import java.time.LocalDateTime;
import java.util.List;

public record SubOrderCreatorDTO(int restaurantId, int userId,
                                 List<Integer> dishIDs, String status, LocalDateTime placedDate,
                                 LocalDateTime deliveryDateTime,
                                 PaymentDTO payment) {

}

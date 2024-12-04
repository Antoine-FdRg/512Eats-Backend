package commonlibrary.dto.databaseupdator;

import commonlibrary.dto.PaymentDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.order.SubOrder;

import java.time.LocalDateTime;
import java.util.List;

public record SubOrderUpdatorDTO(int id, int restaurantID, int userID,
                                 List<Integer> dishIDs, String status, LocalDateTime placedDate,
                                 LocalDateTime deliveryDateTime,
                                 PaymentDTO payment) {
    public SubOrderUpdatorDTO(SubOrder subOrder) {
        this(subOrder.getId(),
                subOrder.getRestaurantID(),
                subOrder.getUserID(),
                subOrder.getDishes().stream().map(Dish::getId).toList(),
                subOrder.getStatus().name(),
                subOrder.getPlacedDate(),
                subOrder.getDeliveryDate(),
                subOrder.getPayment() != null ? new PaymentDTO(subOrder.getPayment().getAmount()) : null);
    }
}

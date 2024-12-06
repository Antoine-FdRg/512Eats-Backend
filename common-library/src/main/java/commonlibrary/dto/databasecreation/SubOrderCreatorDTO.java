package commonlibrary.dto.databasecreation;

import commonlibrary.dto.PaymentDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.order.SubOrder;

import java.time.LocalDateTime;
import java.util.List;

public record SubOrderCreatorDTO(int restaurantId, int userId,
                                 List<Integer> dishIDs, String status, LocalDateTime placedDate,
                                 LocalDateTime deliveryDateTime,
                                 PaymentDTO payment) {
    public SubOrderCreatorDTO(SubOrder subOrder) {
        this(subOrder.getRestaurantID(), subOrder.getUserID(), subOrder.getDishes().stream().map(Dish::getId).toList(), subOrder.getStatus().name(),
                subOrder.getPlacedDate(), subOrder.getDeliveryDate(), subOrder.getPayment() != null ? subOrder.getPayment().convertPaymentToPaymentDto() : null);

    }

}

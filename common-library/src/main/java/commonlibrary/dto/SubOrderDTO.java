package commonlibrary.dto;

import java.util.List;

public record SubOrderDTO(int id, String price, int restaurantId, int userId,
                          List<DishCreationDTO> dishes, String status, String placedDate, String deliveryDateTime,
                          PaymentDTO payment) {
}

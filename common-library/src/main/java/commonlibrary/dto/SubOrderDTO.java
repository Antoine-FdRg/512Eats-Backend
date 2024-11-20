package commonlibrary.dto;

import java.util.List;

public record SubOrderDTO(int id, String price, GroupOrderDTO groupOrder, int restaurantId, int userId,
                          List<DishDTO> dishes, String status, String placedDate, String deliveryDateTime,
                          PaymentDTO payment) {
}

package commonlibrary.dto;

import java.util.List;

public record IndividualOrderDTO(int id, String price, int restaurantId, int userId,
                                 List<DishDTO> dishes, String status, String placedDate, String deliveryDateTime,
                                 PaymentDTO payment, LocationDTO deliveryLocation) {

}

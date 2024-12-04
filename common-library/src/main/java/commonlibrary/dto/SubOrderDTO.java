package commonlibrary.dto;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.payment.Payment;
import commonlibrary.repository.RegisteredUserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public record SubOrderDTO(int id, String price, int restaurantId, int userId,
                          List<DishDTO> dishes, String status, String placedDate, String deliveryDateTime,
                          PaymentDTO payment) {

    public SubOrder convertSubOrderDtoToSubOrder() throws IOException, InterruptedException {
        List<Dish> convertedDishes = dishes.stream()
                .map(DishDTO::convertDishDtoToDish)
                .toList();

        Payment convertedPayment = payment.convertPaymentDtoToPayment();
        RegisteredUser user = RegisteredUserRepository.findById(userId);
        return new OrderBuilder()
                .setId(id)
                .setPrice(Double.parseDouble(price))
                .setRestaurantID(restaurantId)
                .setUserID(user.getId())
                .setDishes(convertedDishes)
                .setStatus(OrderStatus.valueOf(status))
                .setPlacedDate(LocalDateTime.parse(placedDate))
                .setDeliveryTime(LocalDateTime.parse(deliveryDateTime))
                .setPayment(convertedPayment)
                .build();
    }
}

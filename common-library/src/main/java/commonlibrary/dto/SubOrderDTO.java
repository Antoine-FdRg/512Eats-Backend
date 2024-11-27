package commonlibrary.dto;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.payment.Payment;
import commonlibrary.repository.RegisteredUserRepository;

import java.time.LocalDateTime;
import java.util.List;

public record SubOrderDTO(int id, String price, GroupOrderDTO groupOrder, int restaurantId, int userId,
                          List<DishDTO> dishes, String status, String placedDate, String deliveryDateTime,
                          PaymentDTO payment) {

    public SubOrder convertSubOrderDtoToSubOrder() {
        List<Dish> convertedDishes = dishes.stream()
                .map(DishDTO::convertDishDtoToDish)
                .toList();

        Payment convertedPayment = payment.convertPaymentDtoToPayment();
        GroupOrder convertedGroupOrder = groupOrder.convertGroupOrderDtoToGroupOrder();
        RegisteredUserRepository registeredUserRepository = new RegisteredUserRepository();
        RegisteredUser user = registeredUserRepository.findById(userId);
        return new OrderBuilder()
                .setId(id)
                .setPrice(Double.parseDouble(price))
                .setGroupOrder(convertedGroupOrder)
                .setRestaurantID(restaurantId)
                .setUser(user)
                .setDishes(convertedDishes)
                .setStatus(OrderStatus.valueOf(status))
                .setPlacedDate(LocalDateTime.parse(placedDate))
                .setDeliveryTime(LocalDateTime.parse(deliveryDateTime))
                .setPayment(convertedPayment)
                .build();
    }
}

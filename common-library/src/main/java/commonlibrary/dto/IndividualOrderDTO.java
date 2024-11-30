package commonlibrary.dto;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.IndividualOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.payment.Payment;
import commonlibrary.repository.RegisteredUserRepository;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public record IndividualOrderDTO(int id, String price, int restaurantId, int userId,
                                 List<DishDTO> dishes, String status, String placedDate, String deliveryDateTime,
                                 PaymentDTO payment, LocationDTO deliveryLocation) {

    /*
     * Converts an IndividualOrderDTO object to an IndividualOrder object
     */
    public IndividualOrder convertIndividualOrderDtoToIndividualOrder() throws IOException, InterruptedException {
        List<Dish> convertedDishes = dishes.stream()
                .map(DishDTO::convertDishDtoToDish)
                .toList();

        Payment convertedPayment = payment.convertPaymentDtoToPayment();
        Location convertedLocation = deliveryLocation.convertLocationDtoToLocation();
        RegisteredUserRepository registeredUserRepository = new RegisteredUserRepository();
        RegisteredUser user = registeredUserRepository.findById(userId);

        return (IndividualOrder) new OrderBuilder()
                .setId(id)
                .setPrice(Double.parseDouble(price))
                .setRestaurantID(restaurantId)
                .setUserID(user.getId())
                .setDishes(convertedDishes)
                .setStatus(OrderStatus.valueOf(status))
                .setPlacedDate(LocalDateTime.parse(placedDate))
                .setDeliveryTime(LocalDateTime.parse(deliveryDateTime))
                .setPayment(convertedPayment)
                .setDeliveryLocation(convertedLocation)
                .build();
    }
}

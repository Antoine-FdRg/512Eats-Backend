package team.k.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.k.RegisteredUser;
import team.k.common.Dish;
import team.k.external.PaymentProcessor;
import team.k.order.SubOrder;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.restaurant.Restaurant;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.restaurant.TimeSlot;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
public class OrderService {

    @Getter
    final GroupOrderRepository groupOrderRepository;
    final LocationRepository locationRepository;
    final SubOrderRepository subOrderRepository;
    final RestaurantRepository restaurantRepository;
    final RegisteredUserRepository registeredUserRepository;
    PaymentProcessor paymentProcessor;

    public void createIndividualOrder(int registeredUserID, int restaurantId, int deliveryLocationId, LocalDateTime deliveryTime, LocalDateTime now) {
        RegisteredUser registeredUser = registeredUserRepository.findById(registeredUserID);
        if (registeredUser.getCurrentOrder() != null) {
            throw new NoSuchElementException("User already has an active order");
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new NoSuchElementException("Restaurant not found");
        }
        Location deliveryLocation = locationRepository.findLocationById(deliveryLocationId);
        if (deliveryLocation == null) {
            throw new NoSuchElementException("Location not found");
        }
        if (Objects.isNull(deliveryTime)) {
            throw new IllegalArgumentException("Delivery time cannot be null when creating an individual order");
        }
        if (deliveryTime.isBefore(now)) {
            throw new IllegalArgumentException("Delivery time cannot be in the past");
        }
        if (!restaurant.isAvailable(deliveryTime.toLocalTime())) {
            throw new IllegalArgumentException("Restaurant is not available at the chosen time");
        }
        SubOrder order = registeredUser.initializeIndividualOrder(restaurant, deliveryLocation, deliveryTime);
        subOrderRepository.add(order);
        restaurant.addOrderToTimeslot(order);
    }

    public void createGroupOrder(int deliveryLocation) {
        Location location = locationRepository.findLocationById(deliveryLocation);
        if (location == null) {
            throw new IllegalArgumentException("Location not found");
        }
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocation(location)
                .build();

        groupOrderRepository.add(groupOrder);
    }

    public void addDishToOrder(int orderId, Dish dish) {
        //Il faut que le total des ingrédients du plat soit inférieur à DURATION
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException("SubOrder not found");
        }
        int preparationTime = subOrder.getPreparationTime();
        if (preparationTime >= TimeSlot.DURATION) {
            throw new IllegalArgumentException("The total preparation time of the dishes is greater than 30 minutes");
        }
        subOrder.addDish(dish);
    }

    public void placeSubOrder(int orderId) throws NoSuchElementException {
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException("SubOrder not found");
        }
        subOrder.place();

    }

    public void paySubOrder(int registeredUserID, int orderId) throws IllegalStateException {
        RegisteredUser registeredUser = registeredUserRepository.findById(registeredUserID);
        SubOrder currentOrder = registeredUser.getCurrentOrder();
        if (currentOrder == null) {
            throw new IllegalArgumentException("User has no current order");
        }
        if (currentOrder.getId() != orderId) {
            throw new IllegalArgumentException("User can only pay for his current order");
        }
        if (!currentOrder.getRestaurant().isAvailable(LocalTime.now())) {
            throw new IllegalArgumentException("Restaurant is not available");
        }
        if (currentOrder.getDishes().isEmpty()) {
            throw new IllegalArgumentException("Basket is empty");
        }
        if (paymentProcessor.processPayment()) {
            subOrderRepository.findById(orderId).pay();
        } else {
            throw new IllegalStateException("Payment failed");
        }
    }
}

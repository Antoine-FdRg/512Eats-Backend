package team.k.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.k.RegisteredUser;
import team.k.common.Dish;
import team.k.external.PaymentFailedException;
import team.k.external.PaymentProcessor;
import team.k.order.Payment;
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
import java.util.List;
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

    /**
     * Create an individual order
     *
     * @param registeredUserID   the id of the user who wants to create the order
     * @param restaurantId       the id of the restaurant where the order is placed
     * @param deliveryLocationId the id of the location where the order will be delivered
     * @param deliveryTime       the time at which the order will be delivered
     * @param now                the time at which the order is created (should be the current time in REST controller but can be changed as parameter for testing)
     */
    public void createIndividualOrder(int registeredUserID, int restaurantId, int deliveryLocationId, LocalDateTime deliveryTime, LocalDateTime now) {
        RegisteredUser registeredUser = this.registeredUserValidator(registeredUserID);
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
        if (!restaurant.isAvailable(deliveryTime)) {
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

    public void addDishToOrder(int orderId, int dishId) {
        //Il faut que le total des ingrédients du plat soit inférieur à DURATION
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException("SubOrder not found");
        }
        Dish dish = subOrder.getRestaurant().getDishById(dishId);
        if (dish == null) {
            throw new NoSuchElementException("Dish not found");
        }
        int preparationTimePredicted = subOrder.getPreparationTime() + dish.getPreparationTime();
        if (preparationTimePredicted > TimeSlot.DURATION) {
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

    public void paySubOrder(int registeredUserID, int orderId, LocalDateTime currentDateTime) throws PaymentFailedException {
        RegisteredUser registeredUser = this.registeredUserValidator(registeredUserID);
        SubOrder currentOrder = registeredUser.getCurrentOrder();
        if (currentOrder == null) {
            throw new IllegalArgumentException("User has no current order");
        }
        if (currentOrder.getId() != orderId) {
            throw new IllegalArgumentException("User can only pay for his current order");
        }
        if (!currentOrder.getRestaurant().isAvailable(currentDateTime)) {
            throw new IllegalArgumentException("Restaurant is not available");
        }
        if (currentOrder.getDishes().isEmpty()) {
            throw new IllegalArgumentException("Basket is empty");
        }
        if (paymentProcessor.processPayment()) {
            SubOrder subOrder = subOrderRepository.findById(orderId);
            subOrder.pay();
            subOrder.setPayment(new Payment(subOrder.getPrice(), LocalDateTime.now()));

        } else {
            throw new PaymentFailedException("Payment failed");
        }
    }

    public List<Dish> getAvailableDishes(int restaurantId, int orderId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        SubOrder order = subOrderRepository.findById(orderId);
        if (restaurant == null) {
            throw new NoSuchElementException("Restaurant not found");
        }
        if (order == null) {
            throw new NoSuchElementException("Order not found");
        }
        return restaurant.getDishesReadyInLessThan(TimeSlot.DURATION - order.getPreparationTime());
    }

    public RegisteredUser registeredUserValidator(int registeredUserID) {
        RegisteredUser registeredUser = registeredUserRepository.findById(registeredUserID);
        if (registeredUser == null) {
            throw new NoSuchElementException("User not found");
        }
        if (!registeredUser.getRole().canOrder()) {
            throw new IllegalArgumentException("User cannot order");
        }
        return registeredUser;
    }
}

package team.k.service;

import commonlibrary.external.PaymentFailedException;
import commonlibrary.external.PaymentProcessor;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import commonlibrary.model.restaurant.Restaurant;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import commonlibrary.model.restaurant.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
public class OrderService {

    @Getter
    private final GroupOrderRepository groupOrderRepository;
    private final LocationRepository locationRepository;
    private final SubOrderRepository subOrderRepository;
    private final RestaurantRepository restaurantRepository;
    private final RegisteredUserRepository registeredUserRepository;
    private PaymentProcessor paymentProcessor;
    private static final String RESTAURANT_NOT_FOUND = "Restaurant not found";

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
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
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
        SubOrder order = new OrderBuilder()
                .setUserID(registeredUser.getId())
                .setRestaurantID(restaurant.getId())
                .setDeliveryLocationID(deliveryLocation.getId())
                .setDeliveryTime(deliveryTime)
                .build();
        registeredUser.setCurrentOrder(order);
        subOrderRepository.add(order);
        restaurant.addOrderToTimeslot(order);
    }

    public void addDishToOrder(int orderId, int dishId) {
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException("SubOrder not found");
        }
        Restaurant restaurant = restaurantRepository.findById(subOrder.getRestaurantID());
        Dish dish = restaurant.getDishById(dishId);
        if (dish == null) {
            throw new NoSuchElementException("Dish not found");
        }
        int preparationTimePredicted = subOrder.getPreparationTime() + dish.getPreparationTime();
        if (preparationTimePredicted > TimeSlot.DURATION) {
            throw new IllegalArgumentException("The total preparation time of the dishes is greater than 30 minutes");
        }
        subOrder.addDish(dish);
    }

    public void placeSubOrder(int orderId, LocalDateTime now) throws NoSuchElementException {
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException("SubOrder not found");
        }
        RegisteredUser orderOwner = registeredUserRepository.findById(subOrder.getUserID());
        subOrder.place(now, orderOwner);

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
        if (currentDateTime == null) {
            throw new IllegalArgumentException("Current datetime cant be null");
        }
        Restaurant restaurant = restaurantRepository.findById(currentOrder.getRestaurantID());
        if (!restaurant.isAvailable(currentDateTime)) {
            throw new IllegalArgumentException("Restaurant is not available");
        }
        if (currentOrder.getDishes().isEmpty()) {
            throw new IllegalArgumentException("Basket is empty");
        }
        if (!paymentProcessor.processPayment(currentOrder.getPrice())) {
            throw new PaymentFailedException("Payment failed");
        }
        currentOrder.pay(currentDateTime, restaurant, registeredUser);
        currentOrder.setPayment(new Payment(currentOrder.getPrice(), currentDateTime));
        registeredUser.addOrderToHistory(currentOrder);
        registeredUser.setCurrentOrder(null);
    }

    public List<Dish> getAvailableDishes(int restaurantId, int orderId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        SubOrder order = subOrderRepository.findById(orderId);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        if (order == null) {
            throw new NoSuchElementException("Order not found");
        }
        return restaurant.getDishesReadyInLessThan(TimeSlot.DURATION - order.getPreparationTime());
    }

    private RegisteredUser registeredUserValidator(int registeredUserID) {
        RegisteredUser registeredUser = registeredUserRepository.findById(registeredUserID);
        if (registeredUser == null) {
            throw new NoSuchElementException("User not found");
        }
        if (!registeredUser.getRole().canOrder()) {
            throw new IllegalArgumentException("User cannot order");
        }
        return registeredUser;
    }

    /**
     * Create a suborder in a group order and set the current order of the user
     *
     * @param registeredUserID
     * @param restaurantId
     * @param groupOrderId
     */
    public void createSuborder(int registeredUserID, int restaurantId, int groupOrderId) {
        RegisteredUser registeredUser = this.registeredUserValidator(registeredUserID);
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        GroupOrder groupOrder = groupOrderRepository.findGroupOrderById(groupOrderId);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        if (groupOrder == null) {
            throw new NoSuchElementException("GroupOrder does not exist");
        }
        SubOrder suborder = new OrderBuilder()
                .setUserID(registeredUser.getId())
                .setRestaurantID(restaurant.getId())
                .build();
        registeredUser.setCurrentOrder(suborder);
        groupOrder.addSubOrder(suborder);
    }
}

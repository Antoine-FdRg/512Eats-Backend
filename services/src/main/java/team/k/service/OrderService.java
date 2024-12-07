package team.k.service;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.external.PaymentFailedException;
import commonlibrary.external.PaymentProcessor;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.IndividualOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.payment.Payment;
import team.k.repository.*;
import lombok.NoArgsConstructor;
import ssdbrestframework.SSDBQueryProcessingException;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OrderService {
    private static final String RESTAURANT_NOT_FOUND = "Restaurant not found";
    public static final String SUB_ORDER_NOT_FOUND = "SubOrder not found";
    public static final String INDIVIDUAL_ORDER_NOT_FOUND = "Individual Order not found";
    public static final String USER_NOT_FOUND = "User not found";

    private OrderService() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create an individual order
     *
     * @param registeredUserID   the id of the user who wants to create the order
     * @param restaurantId       the id of the restaurant where the order is placed
     * @param deliveryLocationId the id of the location where the order will be delivered
     * @param deliveryTime       the time at which the order will be delivered
     * @param now                the time at which the order is created (should be the current time in REST controller but can be changed as parameter for testing)
     */
    public static int createIndividualOrder(int registeredUserID, int restaurantId, int deliveryLocationId, LocalDateTime deliveryTime, LocalDateTime now) {
        RegisteredUser registeredUser = registeredUserValidator(registeredUserID);
        Restaurant restaurant = RestaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        Location deliveryLocation = LocationRepository.findLocationById(deliveryLocationId);
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
                .setDeliveryLocation(deliveryLocation)
                .setDeliveryTime(deliveryTime)
                .build();
        registeredUser.setCurrentOrder(order);
        SubOrderRepository.add(order);
        restaurant.addOrderToTimeslot(order);
        return order.getId();
    }

    public static void addDishToOrder(int orderId, int dishId) {
        SubOrder subOrder = getSubOrder(orderId);
        Restaurant restaurant = RestaurantRepository.findById(subOrder.getRestaurantID());
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
    public static void removeDishFromOrder(int orderId, int dishId) throws SSDBQueryProcessingException {
        SubOrder subOrder = SubOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new SSDBQueryProcessingException(404, SUB_ORDER_NOT_FOUND);
        }
        if (subOrder.getStatus() != OrderStatus.CREATED) {
            throw new SSDBQueryProcessingException(400, "Order is already placed");
        }
        if(subOrder.getDishes().stream().noneMatch(dish -> dish.getId() == dishId)){
            throw new SSDBQueryProcessingException(404, "Dish not found in order");
        }
        subOrder.removeDish(dishId);
    }

    public static void placeIndividualOrder(int orderId, LocalDateTime now) throws NoSuchElementException {
        IndividualOrder individualOrder = IndividualOrderRepository.findById(orderId);
        if (individualOrder == null) {
            throw new NoSuchElementException(INDIVIDUAL_ORDER_NOT_FOUND);
        }
        RegisteredUser orderOwner = RegisteredUserRepository.findById(individualOrder.getUserID());
        if (orderOwner == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }
        individualOrder.place(now, orderOwner);

    }

    public static void paySubOrder(int registeredUserID, int orderId, LocalDateTime currentDateTime, PaymentProcessor paymentProcessor) throws PaymentFailedException {
        RegisteredUser registeredUser = registeredUserValidator(registeredUserID);
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
        Restaurant restaurant = RestaurantRepository.findById(currentOrder.getRestaurantID());
        if (!restaurant.isAvailable(currentOrder.getDeliveryDate())) {
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

    public static List<Dish> getAvailableDishes(int orderId) {
        SubOrder order = SubOrderRepository.findById(orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found");
        }
        int restaurantId = order.getRestaurantID();
        Restaurant restaurant = RestaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        return restaurant.getDishesReadyInLessThan(TimeSlot.DURATION - order.getPreparationTime());
    }

    public static SubOrder getSubOrder(int orderId) {
        SubOrder subOrder = SubOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException(SUB_ORDER_NOT_FOUND);
        }
        return subOrder;
    }

    private static RegisteredUser registeredUserValidator(int registeredUserID) {
        RegisteredUser registeredUser = RegisteredUserRepository.findById(registeredUserID);
        if (registeredUser == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }
        if (!registeredUser.getRole().canOrder()) {
            throw new IllegalArgumentException("User cannot order");
        }
        return registeredUser;
    }

    /**
     * Create a suborder in a group order and set the current order of the user
     *
     * @param registeredUserID the id of the user who wants to create the order
     * @param restaurantId     the id of the restaurant where the order is placed
     * @param groupOrderId     the id of the group order
     */
    public static int createSuborder(int registeredUserID, int restaurantId, int groupOrderId) {
        RegisteredUser registeredUser = registeredUserValidator(registeredUserID);
        Restaurant restaurant = RestaurantRepository.findById(restaurantId);
        GroupOrder groupOrder = GroupOrderRepository.findGroupOrderById(groupOrderId);
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
        SubOrderRepository.add(suborder);
        groupOrder.addSubOrder(suborder);
        return suborder.getId();
    }
}

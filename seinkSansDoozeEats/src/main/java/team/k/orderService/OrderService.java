package team.k.orderService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.Dish;
import commonlibrary.model.payment.PaymentFailedException;
import commonlibrary.model.payment.PaymentProcessor;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.payment.Payment;
import commonlibrary.model.order.SubOrder;

import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.Location;

import commonlibrary.model.restaurant.TimeSlot;
import commonlibrary.repository.LocationRepository;
import commonlibrary.repository.RegisteredUserRepository;
import commonlibrary.repository.RestaurantRepository;
import commonlibrary.repository.SubOrderRepository;
import commonlibrary.repository.GroupOrderRepository;

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
    public int createIndividualOrder(int registeredUserID, int restaurantId, int deliveryLocationId, LocalDateTime deliveryTime, LocalDateTime now) {
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
                .setUser(registeredUser)
                .setRestaurant(restaurant)
                .setDeliveryLocation(deliveryLocation)
                .setDeliveryTime(deliveryTime)
                .build();
        registeredUser.setCurrentOrder(order);
        subOrderRepository.add(order);
        restaurant.addOrderToTimeslot(order);
        return order.getId();
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

    public void placeSubOrder(int orderId, LocalDateTime now) throws NoSuchElementException {
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException("SubOrder not found");
        }
        subOrder.place(now);

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
        if (!currentOrder.getRestaurant().isAvailable(currentDateTime)) {
            throw new IllegalArgumentException("Restaurant is not available");
        }
        if (currentOrder.getDishes().isEmpty()) {
            throw new IllegalArgumentException("Basket is empty");
        }
        if (paymentProcessor.processPayment(currentOrder.getPrice())) {
            SubOrder subOrder = subOrderRepository.findById(orderId);
            subOrder.pay(currentDateTime);
            subOrder.setPayment(new Payment(subOrder.getPrice(), currentDateTime));
        } else {
            throw new PaymentFailedException("Payment failed");
        }
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
                .setUser(registeredUser)
                .setRestaurant(restaurant)
                .setGroupOrder(groupOrder)
                .build();
        registeredUser.setCurrentOrder(suborder);
        groupOrder.addSubOrder(suborder);
    }
}

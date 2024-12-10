package team.k.orderservice;

import commonlibrary.dto.DishDTO;
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
import commonlibrary.repository.GroupOrderJPARepository;
import commonlibrary.repository.IndividualOrderJPARepository;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.SubOrderJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdbrestframework.SSDBQueryProcessingException;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final String RESTAURANT_NOT_FOUND = "Restaurant not found";
    public static final String SUB_ORDER_NOT_FOUND = "SubOrder not found";
    public static final String INDIVIDUAL_ORDER_NOT_FOUND = "Individual Order not found";
    public static final String USER_NOT_FOUND = "User not found";

    private LocationJPARepository locationJPARepository;
    private RestaurantJPARepository restaurantJPARepository;
    private SubOrderJPARepository subOrderJPARepository;
    private RegisteredUserJPARepository registeredUserJPARepository;
    private GroupOrderJPARepository groupOrderJPARepository;
    private IndividualOrderJPARepository individualOrderJPARepository;

    @Autowired
    public OrderService(LocationJPARepository locationJPARepository,
                        RestaurantJPARepository restaurantJPARepository,
                        SubOrderJPARepository subOrderJPARepository,
                        RegisteredUserJPARepository registeredUserJPARepository,
                        GroupOrderJPARepository groupOrderJPARepository,
                        IndividualOrderJPARepository individualOrderJPARepository) {
        this.locationJPARepository = locationJPARepository;
        this.restaurantJPARepository = restaurantJPARepository;
        this.subOrderJPARepository = subOrderJPARepository;
        this.registeredUserJPARepository = registeredUserJPARepository;
        this.groupOrderJPARepository = groupOrderJPARepository;
        this.individualOrderJPARepository = individualOrderJPARepository;
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
    @Transactional
    public int createIndividualOrder(int registeredUserID, int restaurantId, int deliveryLocationId, LocalDateTime deliveryTime, LocalDateTime now) {
        RegisteredUser registeredUser = registeredUserValidator(registeredUserID);
        Restaurant restaurant = restaurantJPARepository.findById((long)restaurantId).orElse(null);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        Location deliveryLocation = locationJPARepository.findById((long)deliveryLocationId).orElse(null);
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
        IndividualOrder order = (IndividualOrder) new OrderBuilder()
                .setUserID(registeredUser.getId())
                .setRestaurantID(restaurant.getId())
                .setDeliveryLocation(deliveryLocation)
                .setDeliveryTime(deliveryTime)
                .build();
        registeredUser.setCurrentOrder(order);
        individualOrderJPARepository.save(order);
        restaurant.addOrderToTimeslot(order);
        return order.getId();
    }

    @Transactional
    public void addDishToOrder(int orderId, int dishId) {
        SubOrder subOrder = getSubOrder(orderId);
        Restaurant restaurant = restaurantJPARepository.findById((long)subOrder.getRestaurantID()).orElse(null);
        if(restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
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

    @Transactional
    public void removeDishFromOrder(int orderId, int dishId) throws SSDBQueryProcessingException {
        SubOrder subOrder = subOrderJPARepository.findById((long)orderId).orElse(null);
        if (subOrder == null) {
            throw new SSDBQueryProcessingException(404, SUB_ORDER_NOT_FOUND);
        }
        if (subOrder.getStatus() != OrderStatus.CREATED) {
            throw new SSDBQueryProcessingException(400, "Order is already placed");
        }
        if (subOrder.getDishes().stream().noneMatch(dish -> dish.getId() == dishId)) {
            throw new SSDBQueryProcessingException(404, "Dish not found in order");
        }
        subOrder.removeDish(dishId);
    }

    @Transactional
    public void placeIndividualOrder(int orderId, LocalDateTime now) throws NoSuchElementException {
        IndividualOrder individualOrder = individualOrderJPARepository.findById((long)orderId).orElse(null);
        if (individualOrder == null) {
            throw new NoSuchElementException(INDIVIDUAL_ORDER_NOT_FOUND);
        }
        RegisteredUser orderOwner = registeredUserJPARepository.findById((long)individualOrder.getUserID()).orElse(null);
        if (orderOwner == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }
        individualOrder.place(now, orderOwner);

    }

    @Transactional
    public void paySubOrder(int registeredUserID, int orderId, LocalDateTime currentDateTime, PaymentProcessor paymentProcessor) throws PaymentFailedException {
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
        Restaurant restaurant = restaurantJPARepository.findById((long)currentOrder.getRestaurantID()).orElse(null);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        if (currentOrder.getDeliveryDate()!= null && !restaurant.isAvailable(currentOrder.getDeliveryDate())) {
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

    public List<Dish> getAvailableDishes(int orderId) {
        SubOrder order = subOrderJPARepository.findById((long)orderId).orElse(null);
        if (order == null) {
            throw new NoSuchElementException("Order not found");
        }
        int restaurantId = order.getRestaurantID();
        Restaurant restaurant = restaurantJPARepository.findById((long)restaurantId).orElse(null);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        return restaurant.getDishesReadyInLessThan(TimeSlot.DURATION - order.getPreparationTime());
    }

    public List<DishDTO> getAvailableDishesDTO(int orderId) {
        int restaurantId = this.getSubOrder(orderId).getRestaurantID();

        // Récupération des plats disponibles et du restaurant
        List<Dish> availableDishes = this.getAvailableDishes(orderId);
        Set<Integer> availableDishIds = availableDishes.stream()
                .map(Dish::getId)
                .collect(Collectors.toSet());

        // Conversion des plats désactivés et marquage
        return RestaurantRepository.findById(restaurantId).getDishes().stream()
                .map(dish -> {
                    boolean isAvailable = availableDishIds.contains(dish.getId());
                    return isAvailable ? dish.convertDishToDishDto() : dish.convertDishDisabledToDishDto();
                })
                .toList();
    }

    public SubOrder getSubOrder(int orderId) {
        SubOrder subOrder = subOrderJPARepository.findById((long)orderId).orElse(null);
        if (subOrder == null) {
            throw new NoSuchElementException(SUB_ORDER_NOT_FOUND);
        }
        return subOrder;
    }

    private RegisteredUser registeredUserValidator(int registeredUserID) {
        RegisteredUser registeredUser = registeredUserJPARepository.findById((long)registeredUserID).orElse(null);
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
    @Transactional
    public int createSuborder(int registeredUserID, int restaurantId, int groupOrderId) {
        RegisteredUser registeredUser = registeredUserValidator(registeredUserID);
        if(registeredUser.getCurrentOrder() != null){
            throw new UnsupportedOperationException("User already has an order");
        }
        Restaurant restaurant = restaurantJPARepository.findById((long)restaurantId).orElse(null);
        GroupOrder groupOrder = groupOrderJPARepository.findById((long)groupOrderId).orElse(null);
        if (restaurant == null) {
            throw new NoSuchElementException(RESTAURANT_NOT_FOUND);
        }
        if (groupOrder == null) {
            throw new NoSuchElementException("GroupOrder does not exist");
        }
        if(groupOrder.getStatus() != OrderStatus.CREATED){
            throw new UnsupportedOperationException("Group order is already placed");
        }
        SubOrder suborder = new OrderBuilder()
                .setUserID(registeredUser.getId())
                .setGroupOrder(groupOrder)
                .setRestaurantID(restaurant.getId())
                .build();
        registeredUser.setCurrentOrder(suborder);
        subOrderJPARepository.save(suborder);
        groupOrder.addSubOrder(suborder);
        return suborder.getId();
    }
}

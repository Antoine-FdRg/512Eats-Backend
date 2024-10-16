package team.k.service;

import lombok.Getter;
import team.k.RegisteredUser;
import team.k.common.Dish;
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
import java.util.NoSuchElementException;
import java.util.Objects;

public class OrderService {

    LocationRepository locationRepository;
    SubOrderRepository subOrderRepository;
    private RestaurantRepository restaurantRepository;
    @Getter
    GroupOrderRepository groupOrderRepository = new GroupOrderRepository();
    private RegisteredUserRepository registeredUserRepository;


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
        registeredUser.initializeIndividualOrder(restaurant, deliveryLocation, deliveryTime);
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
        int preparationTime = subOrder.getDishes().stream().map(Dish::getPreparationTime).reduce(0, Integer::sum);
        if (preparationTime >= TimeSlot.DURATION) {
            throw new IllegalArgumentException("The total preparation time of the dishes is greater than 30 minutes");
        }
        subOrder.addDish(dish);
    }

    public void placeSubOrder(int orderId) throws NoSuchElementException {
        // TODO : verify restaurant availibity, create a Payment, call PaymentProcessor to make the user pay, place if the payment is successful
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null) {
            throw new NoSuchElementException("SubOrder not found");
        }
        subOrder.place();
        addOrderInTheCorrectTimeSlot(subOrder);
    }


    public void addOrderInTheCorrectTimeSlot(SubOrder subOrder) {
        //TODO : add the order in the correct time slot
        //Prendre le bon timeSlot
        //Ajouter l'order dans le timeSlot
    }
}

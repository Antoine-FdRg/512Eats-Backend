package team.k.service;

import lombok.Getter;
import team.k.RegisteredUser;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.restaurant.Restaurant;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class OrderService {

    LocationRepository locationRepository;
    SubOrderRepository subOrderRepository;
    private RestaurantRepository restaurantRepository;
    @Getter
    GroupOrderRepository groupOrderRepository = new GroupOrderRepository();
    private RegisteredUserRepository registeredUserRepository;


    public void createIndividualOrder(int registeredUserID, int restaurantId, int deliveryLocationId, LocalDateTime deliveryTime) {
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

    public void placeSubOrder(int orderId){
    // TODO : verify restaurant availibity, create a Payment, call PaymentProcessor to make the user pay, place if the payment is successful
        subOrderRepository.findById(orderId).place();
    }
}

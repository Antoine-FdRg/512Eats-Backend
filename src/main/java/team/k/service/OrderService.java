package team.k.service;

import lombok.Getter;
import team.k.RegisteredUser;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.restaurant.Restaurant;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;

import java.time.LocalDateTime;

public class OrderService {

    LocationRepository locationRepository;
    private RestaurantRepository restaurantRepository;
    @Getter
    GroupOrderRepository groupOrderRepository = new GroupOrderRepository();

    private RegisteredUserRepository registeredUserRepository;


    public void createIndividualOrder(int registeredUserID, int restaurantId, int deliveryLocationId, LocalDateTime deliveryTime) {
        RegisteredUser registeredUser = registeredUserRepository.findById(registeredUserID);
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        Location deliveryLocation = locationRepository.findLocationById(deliveryLocationId);
        if (registeredUser.getCurrentOrder() != null) {
            throw new IllegalArgumentException("User already has an active order");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found");
        }
        if (deliveryLocation == null) {
            throw new IllegalArgumentException("Location not found");
        }
        registeredUser.initializeOrder(restaurant, deliveryLocation, deliveryTime);
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

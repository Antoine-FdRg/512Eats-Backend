package team.k.service;

import lombok.Getter;
import team.k.RegisteredUser;
import team.k.common.Dish;
import team.k.order.SubOrder;
import team.k.repository.DishRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.restaurant.Restaurant;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;

import java.util.NoSuchElementException;

public class OrderService {

    LocationRepository locationRepository;
    private RestaurantRepository restaurantRepository;
    private DishRepository dishRepository;
    private SubOrderRepository subOrderRepository;
    @Getter
    GroupOrderRepository groupOrderRepository = new GroupOrderRepository();

    private RegisteredUserRepository registeredUserRepository;

    public void addDishToRegisteredUserBasket(int registeredUserID, int dishId, int restaurantId) {
        Dish dish = dishRepository.findById(dishId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if(!restaurant.getDishes().contains(dish)) {
            throw new IllegalArgumentException("Dish not found in restaurant");
        }
        RegisteredUser registeredUser  = registeredUserRepository.findById(registeredUserID);
        if (registeredUser.getCurrentOrder()!= null && registeredUser.getCurrentOrder().getRestaurant() != restaurant) {
            throw new IllegalArgumentException("Dishes of an order can only be from the same restaurant");
        }
        registeredUser.addDishToBasket(dish, restaurant);
        subOrderRepository.add(registeredUser.getCurrentOrder());
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

    public void placeSubOrder(int orderId) throws NoSuchElementException {
        // TODO : verify restaurant availibity, create a Payment, call PaymentProcessor to make the user pay, place if the payment is successful
        SubOrder subOrder = subOrderRepository.findById(orderId);
        if (subOrder == null){
            throw new NoSuchElementException("SubOrder not found");
        }
        subOrder.place();



}

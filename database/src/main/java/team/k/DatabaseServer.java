package team.k;

import commonlibrary.enumerations.FoodType;
import commonlibrary.enumerations.Role;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import ssdbrestframework.SSDBHttpServer;
import team.k.repository.DishRepository;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DatabaseServer {
    public static void main(String[] args) {
        initDataSet();
        SSDBHttpServer serv = new SSDBHttpServer(8082, "team.k");
        serv.start();
    }

    private static void initDataSet() {
        Dish pizza = new Dish.Builder()
                .setName("Pizza")
                .setDescription("A delicious pizza")
                .setPrice(10.0)
                .setPreparationTime(10)
                .setPicture("pizza.jpg")
                .build();
        DishRepository.getInstance().add(pizza);
        Restaurant restaurant = new Restaurant.Builder()
                .setName("Pizzeria")
                .setAverageOrderPreparationTime(15)
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setFoodTypes(List.of(FoodType.PIZZA, FoodType.FAST_FOOD))
                .build();
        restaurant.setDishes(List.of(
                pizza
        ));
        TimeSlot ts = new TimeSlot(LocalDateTime.of(2025, 1, 1, 10, 0), restaurant, 2);
        restaurant.addTimeSlot(ts);
        Location location = new Location.Builder().setId(1).setAddress("Via Roma 1").setCity("Trento").build();
        RegisteredUser user = new RegisteredUser("John", Role.STUDENT);
        SubOrder subOrder = new OrderBuilder()
                .setRestaurantID(restaurant.getId())
                .setDishes(List.of(pizza))
                .setId(1)
                .setUserID(user.getId())
                .setDeliveryTime(LocalDateTime.of(2025, 1, 1, 10, 50))
                .setDeliveryLocation(location)
                .build();
        user.setCurrentOrder(subOrder);
        SubOrderRepository.getInstance().add(subOrder);
        restaurant.addOrderToTimeslot(subOrder);
        RestaurantRepository.getInstance().add(restaurant);
        RegisteredUserRepository.getInstance().add(user);
        LocationRepository.getInstance().add(location);
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocation(location)
                .build();
        GroupOrderRepository.getInstance().add(groupOrder);
        user.addOrderToHistory(subOrder);
    }
}
package team.k;

import commonlibrary.enumerations.FoodType;
import commonlibrary.enumerations.Role;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.IndividualOrder;
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
import team.k.repository.TimeSlotRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalDateTime.now;

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
        Restaurant r = new Restaurant.Builder()
                .setName("Pizzeria")
                .setAverageOrderPreparationTime(15)
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setFoodTypes(List.of(FoodType.PIZZA, FoodType.FAST_FOOD))
                .build();
        r.setDishes(List.of(
                pizza
        ));
        TimeSlot ts = new TimeSlot(LocalDateTime.of(2025, 1, 1, 10, 0), r, 2);
        TimeSlotRepository.getInstance().add(ts);
        r.addTimeSlot(ts);
        RestaurantRepository.getInstance().add(r);
        RegisteredUser user = new RegisteredUser("John", Role.STUDENT);

        RegisteredUserRepository.getInstance().add(user);
        Location location = new Location.Builder().setId(1).setAddress("Via Roma 1").setCity("Trento").build();
        LocationRepository.getInstance().add(location);
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocation(location)
                .build();
        GroupOrderRepository.getInstance().add(groupOrder);
        SubOrder subOrder = new OrderBuilder()
                .setRestaurant(r)
                .setDishes(List.of(pizza))
                .setId(1)
                .setUser(user)
                .setDeliveryTime(now())
                .build();
        SubOrderRepository.getInstance().add(subOrder);

        user.addOrderToHistory(subOrder);
        RegisteredUserRepository.getInstance().update(user);
    }
}
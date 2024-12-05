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
import lombok.extern.java.Log;
import ssdbrestframework.SSDBHttpServer;
import team.k.models.PersistedGroupOrder;
import team.k.models.PersistedRegisteredUser;
import team.k.models.PersistedRestaurant;
import team.k.models.PersistedSubOrder;
import team.k.repository.DishRepository;
import team.k.repository.GroupOrderRepository;
import team.k.repository.IndividualOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Log
public class InitDataSet {
    public static void main(String[] args) {
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
        TimeSlot ts2 = new TimeSlot(LocalDateTime.of(2025, 1, 1, 10, 30), restaurant, 3);
        restaurant.addTimeSlot(ts);
        restaurant.addTimeSlot(ts2);
        Location location = new Location.Builder(true).setNumber("13").setAddress("Via Roma 1").setCity("Trento").build();
        RegisteredUser user = new RegisteredUser("John", Role.STUDENT);
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocationID(location.getId())
                .withDate(LocalDateTime.of(2025, 1, 1, 10, 50))
                .build();
        SubOrder subOrder = new OrderBuilder()
                .setRestaurantID(restaurant.getId())
                .setDishes(List.of(pizza))
                .setUserID(user.getId())
                .setDeliveryTime(groupOrder.getDeliveryDateTime())
                .setDeliveryLocationID(location.getId()) //inclure pour avoir une individual order, exclure pour avoir une suborder
                .build();
        groupOrder.addSubOrder(subOrder);
        user.setCurrentOrder(subOrder);
        SubOrderRepository.getInstance().add(new PersistedSubOrder(subOrder));
//        IndividualOrderRepository.getInstance().add((IndividualOrder) subOrder);
        restaurant.addOrderToTimeslot(subOrder);
        RestaurantRepository.getInstance().add(new PersistedRestaurant(restaurant));
        LocationRepository.getInstance().add(location);
        GroupOrderRepository.getInstance().add(new PersistedGroupOrder(groupOrder));
        user.addOrderToHistory(subOrder);
    }
}
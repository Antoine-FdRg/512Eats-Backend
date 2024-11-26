package team.k;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import ssdbrestframework.SSDBHttpServer;
import team.k.repository.DishRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.TimeSlotRepository;

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
    }
}
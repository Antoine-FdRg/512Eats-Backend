package team.k.controllers;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RestController;
import team.k.repository.DishRepository;
import team.k.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController(path = "/restaurants")
public class RestaurantController {

    public RestaurantController() {
        Restaurant r = new Restaurant.Builder()
                .setName("Pizzeria")
                .setAverageOrderPreparationTime(15)
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setFoodTypes(List.of(FoodType.PIZZA, FoodType.FAST_FOOD))
                .build();
        r.setDishes(List.of(
                new Dish.Builder()
                        .setName("Pizza")
                        .setDescription("A delicious pizza")
                        .setPrice(10.0)
                        .setPreparationTime(10)
                        .setPicture("pizza.jpg")
                        .build()
        ));
        r.addTimeSlot(new TimeSlot(LocalDateTime.of(2025, 1, 1, 10, 0), r, 2));
        RestaurantRepository.getInstance().add(r);
    }

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<Restaurant> findAll() {
        return RestaurantRepository.getInstance().findAll();
    }
}

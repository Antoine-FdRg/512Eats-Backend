package team.k.restaurant;

import lombok.Getter;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.restaurant.discount.DiscountStrategy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RestaurantBuilder {
    private final int id;
    private String name;
    private LocalTime open;
    private LocalTime close;
    private final List<TimeSlot> timeSlots;
    private final List<Dish> dishes;
    private final List<FoodType> foodTypes;
    private DiscountStrategy discountStrategy;
    private static int idCounter = 0;

    public RestaurantBuilder() {
        id = idCounter++;
        timeSlots = new ArrayList<>();
        dishes = new ArrayList<>();
        foodTypes = new ArrayList<>();
    }

    public RestaurantBuilder addTimeSlot(TimeSlot timeSlot) {
        timeSlots.add(timeSlot);
        return this;
    }

    public RestaurantBuilder addDish(Dish dish) {
        dishes.add(dish);
        return this;
    }

    public RestaurantBuilder addFoodType(FoodType foodType) {
        foodTypes.add(foodType);
        return this;
    }

    public RestaurantBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RestaurantBuilder setOpen(LocalTime open) {
        this.open = open;
        return this;
    }

    public RestaurantBuilder setClose(LocalTime close) {
        this.close = close;
        return this;
    }

    public RestaurantBuilder setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
        return this;
    }
    public RestaurantBuilder setFoodTypes(List<FoodType> foodTypes) {
        this.foodTypes.addAll(foodTypes);
        return this;
    }

    public Restaurant build() {
        return new Restaurant(name, id, open, close, timeSlots, dishes, foodTypes, discountStrategy);
    }
}

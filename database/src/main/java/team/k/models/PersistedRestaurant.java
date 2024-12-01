package team.k.models;

import commonlibrary.dto.databaseupdator.RestauranUpdatorDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import commonlibrary.model.restaurant.discount.DiscountStrategy;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class PersistedRestaurant {
    private int id;
    private String name;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private List<TimeSlot> timeSlots;
    private List<Integer> dishes;
    private List<FoodType> foodTypes;
    private DiscountStrategy discountStrategy;
    private int averageOrderPreparationTime;

    public PersistedRestaurant(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.description = restaurant.getDescription();
        this.openTime = restaurant.getOpen();
        this.closeTime = restaurant.getClose();
        this.timeSlots = restaurant.getTimeSlots();
        this.dishes = restaurant.getDishes().stream().map(Dish::getId).toList();
        this.foodTypes = restaurant.getFoodTypes();
        this.discountStrategy = restaurant.getDiscountStrategy();
        this.averageOrderPreparationTime = restaurant.getAverageOrderPreparationTime();
    }

    public PersistedRestaurant(RestauranUpdatorDTO restaurantUpdatorDTO) {
        this.id = restaurantUpdatorDTO.id();
        this.name = restaurantUpdatorDTO.name();
        this.description = restaurantUpdatorDTO.description();
        this.openTime = restaurantUpdatorDTO.openTime();
        this.closeTime = restaurantUpdatorDTO.closeTime();
        this.timeSlots = restaurantUpdatorDTO.timeSlots();
        this.dishes = restaurantUpdatorDTO.disheIDs();
        this.foodTypes = restaurantUpdatorDTO.foodTypeList().stream().map(String::toUpperCase).map(FoodType::valueOf).toList();
        this.discountStrategy = restaurantUpdatorDTO.discountStrategy();
        this.averageOrderPreparationTime = restaurantUpdatorDTO.averageOrderPreparationTime();
    }
}

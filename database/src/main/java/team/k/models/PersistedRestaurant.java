package team.k.models;

import commonlibrary.dto.databaseupdator.RestauranUpdatorDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
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
    private List<PersistedTimeSlot> timeSlots;
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
        this.timeSlots = restaurant.getTimeSlots().stream().map(PersistedTimeSlot::new).toList();
        this.dishes = restaurant.getDishes().stream().map(Dish::getId).toList();
        this.foodTypes = restaurant.getFoodTypes();
        this.discountStrategy = restaurant.getDiscountStrategy();
        this.averageOrderPreparationTime = restaurant.getAverageOrderPreparationTime();
    }

    public PersistedRestaurant(RestauranUpdatorDTO restaurantUpdatorDTO) {
        this.id = restaurantUpdatorDTO.id();
        this.name = restaurantUpdatorDTO.name();
        this.description = restaurantUpdatorDTO.description();
        this.openTime = restaurantUpdatorDTO.open();
        this.closeTime = restaurantUpdatorDTO.close();
        this.timeSlots = restaurantUpdatorDTO.timeSlots().stream().map(PersistedTimeSlot::new).toList();
        this.dishes = restaurantUpdatorDTO.dishIDs();
        this.foodTypes = restaurantUpdatorDTO.foodTypes().stream().map(String::toUpperCase).map(FoodType::valueOf).toList();
        this.discountStrategy = restaurantUpdatorDTO.discountStrategy();
        this.averageOrderPreparationTime = restaurantUpdatorDTO.averageOrderPreparationTime();
    }
}

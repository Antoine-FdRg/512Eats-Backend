package commonlibrary.dto.databaseupdator;

import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.discount.DiscountStrategy;

import java.time.LocalTime;
import java.util.List;

public record RestauranUpdatorDTO(int id,
                                  String name,
                                  String description,
                                  LocalTime open,
                                  LocalTime close,
                                  List<TimeSlotUpdatorDTO> timeSlots,
                                  List<Integer> dishIDs,
                                  List<String> foodTypes,
                                  int averageOrderPreparationTime,
                                  DiscountStrategy discountStrategy) {
    public RestauranUpdatorDTO(Restaurant restaurant) {
        this(restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getOpen(),
                restaurant.getClose(),
                restaurant.getTimeSlots().stream().map(TimeSlotUpdatorDTO::new).toList(),
                restaurant.getDishes().stream().map(dish -> dish.getId()).toList(),
                restaurant.getFoodTypes().stream().map(Enum::name).toList(),
                restaurant.getAverageOrderPreparationTime(),
                restaurant.getDiscountStrategy());
    }

}

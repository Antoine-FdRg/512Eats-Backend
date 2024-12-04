package commonlibrary.dto.databasecreation;

import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.discount.DiscountStrategy;

import java.time.LocalTime;
import java.util.List;

public record RestaurantCreatorDTO(String name,
                                   String description,
                                   LocalTime open,
                                   LocalTime close,
                                   List<TimeSlotCreatorDTO> timeSlots,
                                   List<Integer> dishIDs,
                                   List<String> foodTypes,
                                   int averageOrderPreparationTime,
                                   DiscountStrategy discountStrategy) {

    public RestaurantCreatorDTO(Restaurant restaurant) {
        this(restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getOpen(),
                restaurant.getClose(),
                restaurant.getTimeSlots().stream().map(TimeSlotCreatorDTO::new).toList(),
                restaurant.getDishes().stream().map(dish -> dish.getId()).toList(),
                restaurant.getFoodTypes().stream().map(Enum::name).toList(),
                restaurant.getAverageOrderPreparationTime(),
                restaurant.getDiscountStrategy());

    }
}

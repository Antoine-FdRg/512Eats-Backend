package commonlibrary.dto.databasecreation;

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
}

package commonlibrary.dto.databasecreation;

import commonlibrary.model.restaurant.TimeSlot;
import commonlibrary.model.restaurant.discount.DiscountStrategy;

import java.time.LocalTime;
import java.util.List;

public record RestaurantCreatorDTO(String name,
                                   String description,
                                   LocalTime openTime,
                                   LocalTime closeTime,
                                   List<TimeSlot> timeSlots,
                                   List<Integer> dishes,
                                   List<String> foodTypeList,
                                   int averageOrderPreparationTime,
                                   DiscountStrategy discountStrategy) {
}

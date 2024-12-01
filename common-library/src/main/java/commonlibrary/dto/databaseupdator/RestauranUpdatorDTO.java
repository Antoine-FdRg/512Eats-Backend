package commonlibrary.dto.databaseupdator;

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

}

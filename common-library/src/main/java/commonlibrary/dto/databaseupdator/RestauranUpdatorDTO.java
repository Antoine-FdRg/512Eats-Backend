package commonlibrary.dto.databaseupdator;

import commonlibrary.model.restaurant.TimeSlot;
import commonlibrary.model.restaurant.discount.DiscountStrategy;

import java.time.LocalTime;
import java.util.List;

public record RestauranUpdatorDTO(int id,
                                  String name,
                                  String description,
                                  LocalTime openTime,
                                  LocalTime closeTime,
                                  List<TimeSlot> timeSlots,
                                  List<Integer> disheIDs,
                                  List<String> foodTypeList,
                                  int averageOrderPreparationTime,
                                  DiscountStrategy discountStrategy) {

}

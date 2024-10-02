package team.k.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.order.SubOrder;
import team.k.restaurant.discount.DiscountStrategy;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Restaurant {
    private String name;
    private int id;
    private LocalTime open;
    private LocalTime close;
    private List<TimeSlot> timeSlots;
    private List<Dish> dishes;
    private List<FoodType> foodTypes;
    private DiscountStrategy discountStrategy;

    /**
     * Check if the restaurant is available by checking if it is open and if there is a time slot available.
     *
     * @return true if the restaurant is available, false otherwise
     */
    public boolean isAvailable() {
        LocalTime now = LocalTime.now();
        if (!now.isAfter(open) || !now.isBefore(close)) {
            return false;
        }
        TimeSlot currentTimeSlot = searchForCurrentTimeSlot();
        if (currentTimeSlot == null) {
            return false;
        }
        return currentTimeSlot.getOrders().size() < currentTimeSlot.getMaxNumberOfOrders();
    }

    /**
     * Search for the current time slot.
     *
     * @return the current time slot if found, null otherwise
     */
    private TimeSlot searchForCurrentTimeSlot() {
        LocalTime now = LocalTime.now();
        for (TimeSlot timeSlot : timeSlots) {
            LocalTime timeSlotStart = timeSlot.getStart().toLocalTime();
            LocalTime timeSlotEnd = timeSlotStart.plusMinutes(TimeSlot.DURATION);
            if (now.isAfter(timeSlotStart) && now.isBefore(timeSlotEnd)) {
                return timeSlot;
            }
        }
        return null;
    }


    public void updateRestaurantInfos(String name, String open, String close) {
        this.name = name;
        this.open = LocalTime.parse(open);
        this.close = LocalTime.parse(close);
    }
}

package team.k.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
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

    public Restaurant(String name, int id, LocalTime open, LocalTime close, List<Dish> dishes, List<FoodType> foodTypes, DiscountStrategy discountStrategy) {
        this.name = name;
        this.id = id;
        this.open = open;
        this.close = close;
        this.dishes = dishes;
        this.foodTypes = foodTypes;
        this.discountStrategy = discountStrategy;
    }

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


    /**
     * Update the restaurant's information.
     *
     * @param name  the new name
     * @param open  the new opening time
     * @param close the new closing time
     */
    public void updateRestaurantInfos(String name, String open, String close) {
        this.name = name;
        this.open = LocalTime.parse(open);
        this.close = LocalTime.parse(close);
    }

    public List<Dish> getAvailableDishesInTimeSlot(TimeSlot timeSlot) {
        if(timeSlot == null) {
            return List.of();
        }
        if(timeSlot.isFull()) {
            return List.of();
        }
        int freeProductionCapacity = timeSlot.getFreeProductionCapacity();
        return getDishesReadyInLessThan(freeProductionCapacity);
    }

    private List<Dish> getDishesReadyInLessThan(int time) {
        return dishes.stream().filter(dish -> dish.getPreparationTime() <= time).toList();
    }
}

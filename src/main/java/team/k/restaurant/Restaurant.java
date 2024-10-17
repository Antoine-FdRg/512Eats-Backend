package team.k.restaurant;

import lombok.Getter;
import lombok.Setter;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.order.SubOrder;
import team.k.restaurant.discount.DiscountStrategy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Restaurant {
    private String name;
    private int id;
    private LocalTime open;
    private LocalTime close;
    private List<TimeSlot> timeSlots;
    private List<Dish> dishes;
    private List<FoodType> foodTypes;
    private DiscountStrategy discountStrategy;
    private int averageOrderPreparationTime;

    private Restaurant(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.open = builder.open;
        this.close = builder.close;
        this.timeSlots = builder.timeSlots;
        this.dishes = builder.dishes;
        this.foodTypes = builder.foodTypes;
        this.averageOrderPreparationTime = builder.averageOrderPreparationTime;
        this.discountStrategy = builder.discountStrategy;
    }

    /**
     * Check if the restaurant is available by checking if it is open and if there is a time slot available.
     *
     * @return true if the restaurant is available, false otherwise
     */
    public boolean isAvailable(LocalTime deliveryTimeWanted) {
        // Check if the order is created after opening time + 50 minutes (30min for the timeslot and 20min for the delivery ) and before closing time - 20 minutes (for the delivery)
        if (!deliveryTimeWanted.isAfter(open.plusMinutes(50)) || !deliveryTimeWanted.isBefore(close.plusMinutes(20))) {
            return false;
        }
        // Check if the restaurant has a time slot available 20 minutes (of delivery) before the chosen time
        TimeSlot currentTimeSlot = getPreviousTimeSlot(deliveryTimeWanted.minusMinutes(20));
        if (currentTimeSlot == null) {
            return false;
        }
        // Check that the time slot is not full
        return !currentTimeSlot.isFull();
    }

    /**
     * Search for the current time slot.
     *
     * @return the current time slot if found, null otherwise
     */
    private TimeSlot getCurrentTimeSlot(LocalTime now) {
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
     * Search for the time slot that precedes the time slto containing the given time.
     * @param time the time to search for
     * @return the time slot that precedes the time slot containing the given time
     */
    public TimeSlot getPreviousTimeSlot(LocalTime time) {
        return getCurrentTimeSlot(time.minusMinutes(TimeSlot.DURATION));
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

    public void addTimeSlot(TimeSlot timeSlot) {
        timeSlots.add(timeSlot);
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void addFoodType(FoodType foodType) {
        foodTypes.add(foodType);
    }

    public void addOrderToTimeslot(SubOrder order) {
        TimeSlot currentTimeSlot = getPreviousTimeSlot(order.getDeliveryDate().toLocalTime().minusMinutes(20));
        if (currentTimeSlot != null && !currentTimeSlot.isFull()) {
            currentTimeSlot.addOrder(order);
        }
    }

    public static class Builder {
        private final int id;
        private String name;
        private LocalTime open;
        private LocalTime close;
        private int averageOrderPreparationTime;
        private final List<TimeSlot> timeSlots;
        private final List<Dish> dishes;
        private final List<FoodType> foodTypes;
        private DiscountStrategy discountStrategy;
        private static int idCounter = 0;

        public Builder() {
            id = idCounter++;
            timeSlots = new ArrayList<>();
            dishes = new ArrayList<>();
            foodTypes = new ArrayList<>();
        }

        public Builder addTimeSlot(TimeSlot timeSlot) {
            timeSlots.add(timeSlot);
            return this;
        }

        public Builder addDish(Dish dish) {
            dishes.add(dish);
            return this;
        }

        public Builder addFoodType(FoodType foodType) {
            foodTypes.add(foodType);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setOpen(LocalTime open) {
            this.open = open;
            return this;
        }

        public Builder setClose(LocalTime close) {
            this.close = close;
            return this;
        }

        public Builder setAverageOrderPreparationTime(int averageOrderPreparationTime) {
            this.averageOrderPreparationTime = averageOrderPreparationTime;
            return this;
        }

        public Builder setDiscountStrategy(DiscountStrategy discountStrategy) {
            this.discountStrategy = discountStrategy;
            return this;
        }
        public Builder setFoodTypes(List<FoodType> foodTypes) {
            this.foodTypes.addAll(foodTypes);
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}

package team.k.restaurant;

import lombok.Getter;
import lombok.Setter;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.order.SubOrder;
import team.k.restaurant.discount.DiscountStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public boolean isAvailable(LocalDateTime deliveryTimeWanted) {
        // Check if the order is created after opening time + 50 minutes (30min for the timeslot and 20min for the delivery ) and before closing time - 20 minutes (for the delivery)
        if (!deliveryTimeWanted.toLocalTime().isAfter(open.plusMinutes(50))
                || !deliveryTimeWanted.toLocalTime().isBefore(close.plusMinutes(20))) {
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
     * @param now the current time (should be the current time in REST controller but can be changed as parameter for testing)
     * @return the current time slot if found, null otherwise
     */
    public TimeSlot getCurrentTimeSlot(LocalDateTime now) {
        for (TimeSlot timeSlot : timeSlots) {
            LocalDateTime timeSlotStart = timeSlot.getStart();
            LocalDateTime timeSlotEnd = timeSlot.getEnd();
            if (now.isAfter(timeSlotStart) && now.isBefore(timeSlotEnd) || now.equals(timeSlotStart)) {
                return timeSlot;
            }
        }
        return null;
    }

    /**
     * Search for the time slot that precedes the time slot containing the given time.
     *
     * @param time the time to search for
     * @return the time slot that precedes the time slot containing the given time
     */
    public TimeSlot getPreviousTimeSlot(LocalDateTime time) {
        return getCurrentTimeSlot(time.minusMinutes(TimeSlot.DURATION));
    }

    public List<Dish> getDishesReadyInLessThan(int time) {
        return dishes.stream().filter(dish -> dish.getPreparationTime() <= time).toList();
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        if (this.timeSlots.stream().noneMatch(ts -> ts.getStart().equals(timeSlot.getStart()))) {
            timeSlots.add(timeSlot);
        }
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void addOrderToTimeslot(SubOrder order) {
        TimeSlot currentTimeSlot = getPreviousTimeSlot(order.getDeliveryDate().minusMinutes(20));
        if (currentTimeSlot != null && !currentTimeSlot.isFull()) {
            currentTimeSlot.addOrder(order);
        }
    }

    public List<LocalDateTime> getAvailableDeliveryTimesOnDay(LocalDate day) {
        List<LocalDateTime> availableTimes = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            if (!timeSlot.isFull()) {
                LocalTime deliveryTime = LocalTime.of(timeSlot.getEnd().getHour(), timeSlot.getEnd().getMinute() + 20);
                LocalDateTime deliveryDateTime = LocalDateTime.of(day, deliveryTime);
                availableTimes.add(deliveryDateTime);
            }
        }
        return availableTimes;
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

        public Builder setFoodTypes(List<FoodType> foodTypes) {
            this.foodTypes.addAll(foodTypes);
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}

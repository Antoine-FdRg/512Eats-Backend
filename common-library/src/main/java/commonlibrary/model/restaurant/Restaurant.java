package commonlibrary.model.restaurant;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.RestaurantDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.restaurant.discount.DiscountStrategy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "restaurant")
public class Restaurant {

    public static final long DELIVERY_DURATION = 20;
    /**
     * Time to prepare and deliver an order
     */
    public static final long ORDER_PROCESSING_TIME_MINUTES = DELIVERY_DURATION + TimeSlot.DURATION;
    private String name;
    @Id
    private int id;
    private LocalTime open;
    private LocalTime close;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TimeSlot> timeSlots;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Dish> dishes;
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "food_types", joinColumns = @JoinColumn(name = "food_type_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    private List<FoodType> foodTypes;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DiscountStrategy discountStrategy;
    private int averageOrderPreparationTime;
    private String description;
    @CollectionTable(name = "url_picture", joinColumns = @JoinColumn(name = "url_picture_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> urlPicture;

    private Restaurant(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.open = builder.open;
        this.close = builder.close;
        this.timeSlots = builder.timeSlots;
        this.dishes = builder.dishes;
        this.foodTypes = builder.foodTypes;
        this.averageOrderPreparationTime = builder.averageOrderPreparationTime;
        this.discountStrategy = builder.discountStrategy;
        this.urlPicture = new ArrayList<>();
        for (int i = 0; i < Math.min(3, dishes.size()); i++) {
            urlPicture.add(dishes.get(i).getPicture());
        }
    }

    /**
     * Check if the restaurant is available by checking if it is open and if there is a time slot available.
     *
     * @return true if the restaurant is available, false otherwise
     */
    public boolean isAvailable(LocalDateTime deliveryTimeWanted) {
        // Check if the order is created after opening time + 50 minutes (30min for the timeslot and 20min for the delivery ) and before closing time - 20 minutes (for the delivery)
        if (deliveryTimeWanted.toLocalTime().isBefore(open.plusMinutes(ORDER_PROCESSING_TIME_MINUTES))
                || deliveryTimeWanted.toLocalTime().isAfter(close.plusMinutes(DELIVERY_DURATION))) {
            return false;
        }
        // Check if the restaurant has a time slot available 20 minutes (of delivery) before the chosen time
        TimeSlot currentTimeSlot = getPreviousTimeSlot(deliveryTimeWanted.minusMinutes(DELIVERY_DURATION));
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
            if ((now.isAfter(timeSlotStart) && now.isBefore(timeSlotEnd)) || now.equals(timeSlotStart)) {
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
        TimeSlot currentTimeSlot = getPreviousTimeSlot(order.getDeliveryDate().minusMinutes(DELIVERY_DURATION));
        if (currentTimeSlot != null && !currentTimeSlot.isFull()) {
            currentTimeSlot.addOrder(order);
        }
    }

    public List<LocalDateTime> getAvailableDeliveryTimesOnDay(LocalDate day) {
        List<LocalDateTime> availableTimes = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            if (!timeSlot.isFull()) {
                LocalTime deliveryTime = LocalTime.of(timeSlot.getEnd().getHour(), (int) (timeSlot.getEnd().getMinute() + DELIVERY_DURATION));
                LocalDateTime deliveryDateTime = LocalDateTime.of(day, deliveryTime);
                availableTimes.add(deliveryDateTime);
            }
        }
        return availableTimes;
    }

    public void removeDish(int dishId) {
        dishes.removeIf(dish -> dish.getId() == dishId);
    }

    public Dish getDishById(int dishId) {
        return dishes.stream().filter(dish -> dish.getId() == dishId).findFirst().orElse(null);
    }

    public RestaurantDTO restaurantToRestaurantDTO() {
        List<String> foodTypes = this.foodTypes.stream().map(Enum::name).toList();
        return new RestaurantDTO(this.id, this.name, this.open.toString(), this.close.toString(), foodTypes, this.getAveragePrice(), this.description, this.getFirstDishPictureURLList());
    }

    public double getAveragePrice() {
        return dishes.stream().mapToDouble(Dish::getPrice).average().orElse(0);
    }

    private List<String> getFirstDishPictureURLList() {
        return dishes.stream().map(Dish::getPicture).limit(3).toList();
    }

    public RestaurantDTO convertRestaurantToRestaurantDTO() {
        List<String> foodTypes = this.foodTypes.stream().map(Enum::name).toList();
        double averagePrice = this.getAveragePrice();
        return new RestaurantDTO(this.id, this.name, this.open.toString(), this.close.toString(), foodTypes, (averagePrice <= 10 ? 1 : averagePrice <= 20 ? 2 : 3), this.description, this.urlPicture);
    }

    public static class Builder {
        private int id;
        private String name;
        private LocalTime open;
        private LocalTime close;
        private int averageOrderPreparationTime;
        private final List<TimeSlot> timeSlots;
        private final List<Dish> dishes;
        private final List<FoodType> foodTypes;
        private String description;
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

        public Builder setDescription(String description) {
            this.description = description;
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

        public Builder setDishes(List<Dish> dishes) {
            this.dishes.addAll(dishes);
            return this;
        }

        public Builder setDiscountStrategy(DiscountStrategy discountStrategy) {
            this.discountStrategy = discountStrategy;
            return this;
        }

        public Builder setTimeSlots(List<TimeSlot> timeSlots) {
            this.timeSlots.addAll(timeSlots);
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
    }
}

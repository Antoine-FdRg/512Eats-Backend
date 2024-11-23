package team.k.entities.restaurant;

import io.ebean.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.k.entities.discount.DiscountStrategyEntity;
import team.k.entities.enumeration.FoodTypeEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "restaurant")
@NoArgsConstructor
public class RestaurantEntity extends Model {

    public static final long DELIVERY_DURATION = 20;
    /**
     * Time to prepare and deliver an order
     */
    public static final long ORDER_PROCESSING_TIME_MINUTES = DELIVERY_DURATION + TimeSlotEntity.DURATION;
    private String name;
    @Id
    private int id;
    private LocalTime open;
    private LocalTime close;
    @OneToMany
    @JoinColumn(name = "id")
    private List<TimeSlotEntity> timeSlots;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    public List<DishEntity> dishes;
    private List<FoodTypeEntity> foodTypes;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private DiscountStrategyEntity discountStrategy;
    private int averageOrderPreparationTime;
    private String description;

    private RestaurantEntity(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.open = builder.open;
        this.close = builder.close;
        this.timeSlots = builder.timeSlots;
        this.dishes = builder.dishes;
        this.description = builder.description;
        this.foodTypes = builder.foodTypes;
        this.averageOrderPreparationTime = builder.averageOrderPreparationTime;
        this.discountStrategy = builder.discountStrategy;
    }

    public static class Builder {
        private int id;
        private String name;
        private LocalTime open;
        private LocalTime close;
        private int averageOrderPreparationTime;
        private final List<TimeSlotEntity> timeSlots;
        private final List<DishEntity> dishes;
        private final List<FoodTypeEntity> foodTypes;
        private String description;
        private DiscountStrategyEntity discountStrategy;
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

        public Builder setFoodTypes(List<FoodTypeEntity> foodTypes) {
            this.foodTypes.addAll(foodTypes);
            return this;
        }

        public RestaurantEntity build() {
            return new RestaurantEntity(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
    }

    public void addDish(DishEntity dish) {
        dishes.add(dish);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RestaurantEntity restaurant)) {
            return false;
        }
        return id == restaurant.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

package commonlibrary.model;

import commonlibrary.dto.DishCreationDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a dish in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dish")
public class Dish {
    @Id
    @GeneratedValue(generator = "increment")
    private int id;
    private String name;
    private String description;
    private double price;
    private int preparationTime;
    private String picture;

    private Dish(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.preparationTime = builder.preparationTime;
        this.picture = builder.picture;
    }

    public String toString() {
        return "Dish [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", preparationTime=" + preparationTime + ", picture=" + picture + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Dish dish)) {
            return false;
        }
        return id == dish.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public DishCreationDTO convertDishToDishDto() {
        return new DishCreationDTO(name, description, price, preparationTime, picture, false);
    }

    public DishCreationDTO convertDishDisabledToDishDto() {
        return new DishCreationDTO(name, description, price, preparationTime, picture, true);
    }

    public static class Builder {
        private String name;
        private String description;
        private double price;
        private int preparationTime;
        private String picture;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setPreparationTime(int preparationTime) {
            this.preparationTime = preparationTime;
            return this;
        }

        public Builder setPicture(String picture) {
            this.picture = picture;
            return this;
        }

        public Dish build() {
            return new Dish(this);
        }


    }
}

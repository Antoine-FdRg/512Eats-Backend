package commonlibrary.model;

import commonlibrary.dto.DishDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a dish in the database.
 */
@Getter
@Setter
public class Dish {

    private int id;
    private String name;
    private String description;
    private double price;
    private int preparationTime;
    private String picture;

    private Dish(Builder builder) {
        this.id = builder.id;
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

    public DishDTO convertDishToDishDto() {
        return new DishDTO(id, name, description, price, preparationTime, picture);
    }

    public static class Builder {
        private int id;
        private String name;
        private String description;
        private double price;
        private int preparationTime;
        private String picture;
        private static int idCounter = 0;

        public Builder() {
            this.id = idCounter++;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

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

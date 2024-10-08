package team.k.common;

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

    Dish(int id, String name, String description, double price, int preparationTime, String picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.preparationTime = preparationTime;
        this.picture = picture;
    }

    public String toString() {
        return "Dish [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", preparationTime=" + preparationTime + ", picture=" + picture + "]";
    }
}

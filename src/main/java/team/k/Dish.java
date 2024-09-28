package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a dish in the database.
 */
@Getter
@Setter
@AllArgsConstructor
public class Dish {

    private int id;
    private String name;
    private String description;
    private double price;
    private int preparationTime;
    private String picture;

    public String toString() {
        return "Dish [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", preparationTime=" + preparationTime + ", picture=" + picture + "]";
    }
}

package team.k;

/**
 * Represents a dish in the database.
 */
public class Dish {

    private int id;
    private String name;
    private String description;
    private double price;
    private int preparationTime;
    private String picture;

    public Dish(int id, String name, String description, double price, int preparationTime, String picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.preparationTime = preparationTime;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String toString() {
        return "Dish [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", preparationTime=" + preparationTime + ", picture=" + picture + "]";
    }
}

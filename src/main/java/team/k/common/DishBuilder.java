package team.k.common;

public class DishBuilder {
    private int id;
    private String name;
    private String description;
    private double price;
    private int preparationTime;
    private String picture;
    private static int idCounter = 0;

    public DishBuilder() {
        this.id = idCounter++;
    }

    public DishBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public DishBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DishBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public DishBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public DishBuilder setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
        return this;
    }

    public DishBuilder setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public Dish build() {
        return new Dish(id, name, description, price, preparationTime, picture);
    }
}

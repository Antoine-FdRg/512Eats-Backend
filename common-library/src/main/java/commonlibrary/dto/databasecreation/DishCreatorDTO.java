package commonlibrary.dto.databasecreation;

import commonlibrary.model.Dish;

public record DishCreatorDTO(String name, String description, double price, int preparationTime, String pictureURL) {

    public DishCreatorDTO(Dish dish) {
        this(dish.getName(), dish.getDescription(), dish.getPrice(), dish.getPreparationTime(), dish.getPicture());
    }

    /**
     * Convert DishDTO to Dish
     *
     * @return un Dish
     */
    public Dish toDish() {
        return new Dish.Builder()
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setPreparationTime(preparationTime)
                .setPicture(pictureURL)
                .build();
    }
}

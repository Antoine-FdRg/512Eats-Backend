package commonlibrary.dto.databasecreation;

import commonlibrary.model.Dish;

public record DishCreatorDTO(String name, String description, double price, int preparationTime, String pictureURL) {

    /**
     * Convert DishDTO to Dish
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

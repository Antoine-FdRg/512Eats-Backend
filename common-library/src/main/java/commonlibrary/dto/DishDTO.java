package commonlibrary.dto;

import commonlibrary.model.Dish;

public record DishDTO(int id, String name, String description, double price, int preparationTime, String pictureURL) {

    /**
     * Convert DishDTO to Dish
     * @return un Dish
     */
    public Dish convertDishDtoToDish() {
        return new Dish.Builder()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setPreparationTime(preparationTime)
                .setPicture(pictureURL)
                .build();
    }
}

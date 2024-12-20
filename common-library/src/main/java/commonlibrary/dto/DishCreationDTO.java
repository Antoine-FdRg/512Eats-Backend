package commonlibrary.dto;

import commonlibrary.model.Dish;

public record DishCreationDTO(String name, String description, double price, int preparationTime, String pictureURL, boolean disabled) {

    /**
     * Convert DishDTO to Dish
     *
     * @return un Dish
     */
    public Dish convertDishDtoToDish() {
        return new Dish.Builder()
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setPreparationTime(preparationTime)
                .setPicture(pictureURL)
                .build();
    }
}

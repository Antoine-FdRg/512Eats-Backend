package commonlibrary.dto;

import commonlibrary.model.Dish;

public record DishDTO(int id, String name, String description, double price, int preparationTime, String pictureURL) {

    /**
     * Convertit un DishDTO en Dish
     *
     * @return un Dish
     */
    public Dish convertDishDtoToDish() {
        return new Dish.Builder()
                .setId(id) // Conserve l'ID du DTO
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setPreparationTime(preparationTime)
                .setPicture(pictureURL) // Associer pictureURL au champ picture
                .build();
    }
}

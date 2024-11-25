package commonlibrary.dto;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalTime;
import java.util.List;

public record RestaurantDTO(int id, String name, String openTime, String closeTime, List<String> foodTypeList,
                            double averagePrice, String description, List<String> dishPictureURLListSample) {

    public Restaurant convertRestaurantDtoToRestaurant() {
        List<FoodType> foodtypes = foodTypeList.stream().map(FoodType::valueOf).toList();

        return new Restaurant.Builder()
                .setId(id)
                .setName(name)
                .setOpen(LocalTime.parse(openTime))
                .setClose(LocalTime.parse(closeTime))
                .setFoodTypes(foodtypes)
                .setDescription(description)
                .build();
    }
}

package team.k.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import team.k.repository.RestaurantRepository;
import team.k.restaurant.Restaurant;

import java.time.LocalTime;

@RequiredArgsConstructor
@AllArgsConstructor
public class ManageRestaurantService {
    private RestaurantRepository restaurantRepository;

    public void updateRestaurantInfos(int restaurantId, String openTime, String closeTime) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found");
        }
        if (openTime != null) {
            restaurant.setOpen(LocalTime.parse(openTime));
        }
        if (closeTime != null) {
            restaurant.setClose(LocalTime.parse(closeTime));
        }
    }
}

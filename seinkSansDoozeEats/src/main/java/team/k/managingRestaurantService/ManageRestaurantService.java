package team.k.managingRestaurantService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import commonlibrary.model.Dish;

import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.repository.RestaurantRepository;

import java.io.IOException;
import java.time.LocalTime;

@RequiredArgsConstructor
@AllArgsConstructor
public class ManageRestaurantService {
    private static final String RESTAURANT_NOT_FOUND = "Restaurant not found";
    private RestaurantRepository restaurantRepository;

    public void updateRestaurantInfos(int restaurantId, String openTime, String closeTime) throws IOException, InterruptedException {
        Restaurant restaurant = this.restaurantValidator(restaurantId);
        if (openTime != null) {
            restaurant.setOpen(LocalTime.parse(openTime));
        }
        if (closeTime != null) {
            restaurant.setClose(LocalTime.parse(closeTime));
        }
    }

    public void addDish(int restaurantId, String dishName, String dishDescription, double dishPrice, int dishPreparationTime) throws IOException, InterruptedException {
        Restaurant restaurant = this.restaurantValidator(restaurantId);
        restaurant.addDish(new Dish.Builder()
                .setName(dishName)
                .setDescription(dishDescription)
                .setPrice(dishPrice)
                .setPreparationTime(dishPreparationTime)
                .build()
        );
    }

    public void removeDish(int restaurantId, int dishId) throws IOException, InterruptedException {
        Restaurant restaurant = this.restaurantValidator(restaurantId);
        restaurant.removeDish(dishId);
    }

    public void updateDish(int restaurantId, int dishId, double newDishPrice, int newDishPreparationTime) throws IOException, InterruptedException {
        Restaurant restaurant = this.restaurantValidator(restaurantId);
        Dish dish = restaurant.getDishes().stream().filter(d -> d.getId() == dishId).findFirst().orElse(null);
        if (dish == null) {
            throw new IllegalArgumentException("Dish not found");
        }
        if (newDishPrice != 0) {
            dish.setPrice(newDishPrice);
        }
        if (newDishPreparationTime != 0) {
            dish.setPreparationTime(newDishPreparationTime);
        }

    }

    private Restaurant restaurantValidator(int restaurantId) throws IOException, InterruptedException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException(RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }
}

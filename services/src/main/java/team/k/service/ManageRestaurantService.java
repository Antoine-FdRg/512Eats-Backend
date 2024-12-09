package team.k.service;

import commonlibrary.model.Dish;
import commonlibrary.repository.RestaurantJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdbrestframework.SSDBQueryProcessingException;
import team.k.repository.RestaurantRepository;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalTime;

@Service
public class ManageRestaurantService {
    private final String RESTAURANT_NOT_FOUND = "Restaurant not found";
    private RestaurantJPARepository restaurantJPARepository;
    
    @Autowired
    public ManageRestaurantService(RestaurantJPARepository restaurantJPARepository) {
        this.restaurantJPARepository = restaurantJPARepository;
    }

    @Transactional
    public Restaurant updateRestaurantInfos(int restaurantId, String openTime, String closeTime) throws SSDBQueryProcessingException {
        Restaurant restaurant = restaurantValidator(restaurantId);
        if (openTime != null) {
            restaurant.setOpen(LocalTime.parse(openTime));
        }
        if (closeTime != null) {
            restaurant.setClose(LocalTime.parse(closeTime));
        }
        return restaurant;
    }

    @Transactional
    public Restaurant addDish(int restaurantId, String dishName, String dishDescription, double dishPrice, int dishPreparationTime) throws SSDBQueryProcessingException {
        Restaurant restaurant = restaurantValidator(restaurantId);
        restaurant.addDish(new Dish.Builder()
                .setName(dishName)
                .setDescription(dishDescription)
                .setPrice(dishPrice)
                .setPreparationTime(dishPreparationTime)
                .build()
        );
        return restaurant;
    }

    @Transactional
    public Restaurant removeDish(int restaurantId, int dishId) throws SSDBQueryProcessingException {
        Restaurant restaurant = restaurantValidator(restaurantId);
        restaurant.removeDish(dishId);
        return restaurant;
    }

    @Transactional
    public Restaurant updateDish(int restaurantId, int dishId, double newDishPrice, int newDishPreparationTime) throws SSDBQueryProcessingException {
        Restaurant restaurant = restaurantValidator(restaurantId);
        Dish dish = restaurant.getDishes().stream().filter(d -> d.getId() == dishId).findFirst().orElse(null);
        if (dish == null) {
            throw new SSDBQueryProcessingException(404, "Dish not found");
        }
        if (newDishPrice != 0) {
            dish.setPrice(newDishPrice);
        }
        if (newDishPreparationTime != 0) {
            dish.setPreparationTime(newDishPreparationTime);
        }
        return restaurant;

    }

    private Restaurant restaurantValidator(int restaurantId) throws SSDBQueryProcessingException {
        Restaurant restaurant = restaurantJPARepository.findById((long)restaurantId).orElse(null);
        if (restaurant == null) {
            throw new SSDBQueryProcessingException(404, RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }

    /**
     * Add a restaurant to the repository
     *
     * @param restaurant the restaurant to add
     */
    @Transactional
    public void addRestaurant(Restaurant restaurant) {
        restaurantJPARepository.save(restaurant);
    }

    @Transactional
    public void deleteRestaurant(int restaurantId) {
        restaurantJPARepository.deleteById((long)restaurantId);
    }
}

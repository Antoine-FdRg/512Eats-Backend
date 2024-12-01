package team.k.controllers;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.restaurant.Restaurant;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.RestaurantRepository;
import team.k.repository.DishRepository;


import java.time.LocalDateTime;
import java.util.List;

@RestController(path = "/restaurants")
public class RestaurantController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<Restaurant> findAll(@RequestParam("name") String name) {
        if (name != null) {
            return RestaurantRepository.getInstance().findByName(name);
        }
        return RestaurantRepository.getInstance().findAll();
    }

    @Endpoint(path = "/by/foodtypes", method = HttpMethod.GET)
    public List<Restaurant> findByFoodTypes(@RequestParam("food-types") List<String> foodTypesAsString) throws SSDBQueryProcessingException {
        List<FoodType> foodTypes;
        try {
            foodTypes = foodTypesAsString.stream().map(String::toUpperCase).map(FoodType::valueOf).toList();
        } catch (IllegalArgumentException e) {
            throw new SSDBQueryProcessingException(400, "Invalid food type provided.");
        }
        return RestaurantRepository.getInstance().findRestaurantByFoodType(foodTypes);
    }

    @Endpoint(path = "/by/availability", method = HttpMethod.GET)
    public List<Restaurant> findByAvailability(@RequestParam("date") LocalDateTime dateTime) {
        return RestaurantRepository.getInstance().findRestaurantsByAvailability(dateTime);
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public Restaurant findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(id);
        return RestaurantRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Restaurant created successfully")
    public void add(@RequestBody Restaurant restaurant) throws SSDBQueryProcessingException {
        if (RestaurantRepository.getInstance().findById(restaurant.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Restaurant with ID " + restaurant.getId() + " already exists, try updating it instead.");
        }
        DishRepository.throwIfDishesDoNotExist(restaurant.getDishes());
        RestaurantRepository.getInstance().add(restaurant);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Restaurant updated successfully")
    public void update(@RequestBody Restaurant restaurant) throws SSDBQueryProcessingException {
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(restaurant.getId());
        DishRepository.throwIfDishesDoNotExist(restaurant.getDishes());
        Restaurant existingRestaurant = RestaurantRepository.getInstance().findById(restaurant.getId());
        RestaurantRepository.getInstance().update(restaurant, existingRestaurant);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Restaurant deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(id);
        RestaurantRepository.getInstance().remove(id);
    }
}

package team.k.controllers;

import commonlibrary.dto.databasecreation.RestaurantCreatorDTO;
import commonlibrary.dto.databaseupdator.RestauranUpdatorDTO;
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
import team.k.models.PersistedRestaurant;
import team.k.repository.DishRepository;
import team.k.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController(path = "/restaurants")
public class RestaurantController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<Restaurant> findAll(@RequestParam("name") String name) {
        if (name != null) {
            return RestaurantRepository.getInstance().findByName(name)
                    .stream()
                    .map(RestaurantController::mapPersistedRestaurantToRestaurant)
                    .toList();
        }
        return RestaurantRepository.getInstance().findAll()
                .stream()
                .map(RestaurantController::mapPersistedRestaurantToRestaurant)
                .toList();
    }

    @Endpoint(path = "/by/foodtypes", method = HttpMethod.GET)
    public List<Restaurant> findByFoodTypes(@RequestBody List<String> foodTypesAsString) throws SSDBQueryProcessingException {
        List<FoodType> foodTypes;
        try {
            foodTypes = foodTypesAsString.stream().map(String::toUpperCase).map(FoodType::valueOf).toList();
        } catch (IllegalArgumentException e) {
            throw new SSDBQueryProcessingException(400, "Invalid food type provided.");
        }
        return RestaurantRepository.getInstance().findRestaurantByFoodType(foodTypes)
                .stream()
                .map(RestaurantController::mapPersistedRestaurantToRestaurant)
                .toList();
    }

    @Endpoint(path = "/by/availability", method = HttpMethod.GET)
    public List<Restaurant> findByAvailability(@RequestBody LocalDateTime dateTime) {
        List<Restaurant> restaurantList =  RestaurantRepository.getInstance().findAll().stream()
                .map(RestaurantController::mapPersistedRestaurantToRestaurant)
                .toList();
        return restaurantList.stream().filter(restaurant -> restaurant.isAvailable(dateTime)).toList();

    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public Restaurant findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(id);
        return mapPersistedRestaurantToRestaurant(RestaurantRepository.getInstance().findById(id));
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Restaurant created successfully")
    public Restaurant add(@RequestBody RestaurantCreatorDTO rcDTO) throws SSDBQueryProcessingException {
        DishRepository.throwIfDishIdsDoNotExist(rcDTO.dishes());
        Restaurant restaurant = new Restaurant.Builder()
                .setName(rcDTO.name())
                .setDescription(rcDTO.description())
                .setOpen(rcDTO.openTime())
                .setClose(rcDTO.closeTime())
                .setTimeSlots(rcDTO.timeSlots())
                .setDishes(rcDTO.dishes().stream()
                        .map(DishRepository.getInstance()::findById)
                        .toList())
                .setFoodTypes(rcDTO.foodTypeList().stream()
                        .map(String::toUpperCase)
                        .map(FoodType::valueOf)
                        .toList())
                .setAverageOrderPreparationTime(rcDTO.averageOrderPreparationTime())
                .setDiscountStrategy(rcDTO.discountStrategy())
                .build();
        RestaurantRepository.getInstance().add(new PersistedRestaurant(restaurant));
        return restaurant;
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Restaurant updated successfully")
    public Restaurant update(@RequestBody RestauranUpdatorDTO restaurantUpdatorDTO) throws SSDBQueryProcessingException {
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(restaurantUpdatorDTO.id());
        DishRepository.throwIfDishIdsDoNotExist(restaurantUpdatorDTO.disheIDs());
        PersistedRestaurant existingRestaurant = RestaurantRepository.getInstance().findById(restaurantUpdatorDTO.id());
        PersistedRestaurant newRestaurant = new PersistedRestaurant(restaurantUpdatorDTO);
        RestaurantRepository.getInstance().update(newRestaurant,existingRestaurant);
        return mapPersistedRestaurantToRestaurant(newRestaurant);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Restaurant deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(id);
        RestaurantRepository.getInstance().remove(id);
    }

    private static Restaurant mapPersistedRestaurantToRestaurant(PersistedRestaurant pr) {
        return new Restaurant.Builder()
                .setId(pr.getId())
                .setName(pr.getName())
                .setDescription(pr.getDescription())
                .setOpen(pr.getOpenTime())
                .setClose(pr.getCloseTime())
                .setTimeSlots(pr.getTimeSlots())
                .setDishes(pr.getDishes().stream()
                        .map(DishRepository.getInstance()::findById)
                        .toList())
                .setFoodTypes(pr.getFoodTypes())
                .setAverageOrderPreparationTime(pr.getAverageOrderPreparationTime())
                .setDiscountStrategy(pr.getDiscountStrategy())
                .build();
    }
}

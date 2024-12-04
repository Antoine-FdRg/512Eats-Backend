package team.k.repository;

import commonlibrary.enumerations.FoodType;
import ssdbrestframework.SSDBQueryProcessingException;
import team.k.models.PersistedRestaurant;

import java.util.List;
import java.util.Objects;

public class RestaurantRepository extends GenericRepository<PersistedRestaurant> {
    private static RestaurantRepository instance;

    private RestaurantRepository() {
        super();
    }

    public static RestaurantRepository getInstance() {
        if (instance == null) {
            instance = new RestaurantRepository();
        }
        return instance;
    }

    /**
     * Find a restaurant by its name.
     *
     * @param name the name of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public List<PersistedRestaurant> findByName(String name) {
        return findAll().stream().filter(restaurant -> restaurant.getName().toLowerCase().contains(name.toLowerCase())).toList();
    }

    /**
     * Find a restaurant by its id.
     *
     * @param id the id of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public PersistedRestaurant findById(int id) {
        return findAll().stream().filter(restaurant -> restaurant.getId() == id).findFirst().orElse(null);
    }

    public List<PersistedRestaurant> findRestaurantByFoodType(List<FoodType> foodTypes) {
        return findAll().stream().filter(restaurant -> restaurant.getFoodTypes().stream().anyMatch(foodTypes::contains)).toList();
    }

    public List<PersistedRestaurant> findRestaurantByName(String name) {
        return findAll().stream().filter(restaurant -> Objects.equals(restaurant.getName(), name)).toList();
    }

    public static void throwIfRestaurantIdDoesNotExist(int restaurantID) throws SSDBQueryProcessingException {
        if (Objects.isNull(RestaurantRepository.getInstance().findById(restaurantID))) {
            throw new SSDBQueryProcessingException(404, "Restaurant with ID " + restaurantID + " not found.");
        }
    }
}

package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.repository.RestaurantRepository;
import team.k.restaurant.Restaurant;
import team.k.service.RestaurantService;

import java.time.LocalTime;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class InternetUserFiltersRestaurantStepdefs {

    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private RestaurantService restaurantService;
    List<Restaurant> restaurantsByFoodType;
    List<Restaurant> restaurantsAvailable;
    List<Restaurant> restaurantsByName;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Given("a list of restaurants {string} and {string} with registered dishes")
    public void aListOfRestaurantsAndWithRegisteredDishes(String restaurantNameA, String restaurantNameB) {
        Restaurant restaurantA = new Restaurant.Builder().setName(restaurantNameA).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(22, 0, 0)).setFoodTypes(List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL)).build();
        Restaurant restaurantB = new Restaurant.Builder().setName(restaurantNameB).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(12, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).build();
        Dish dishA = new Dish.Builder().setName("sushi").setDescription("Description").setPrice(5).setPreparationTime(3).build();
        Dish dishB = new Dish.Builder().setName("burger").setDescription("Description").setPrice(5).setPreparationTime(3).build();
        restaurantA.addDish(dishA);
        restaurantB.addDish(dishB);
        when(this.restaurantRepository.findAll()).thenReturn(List.of(restaurantA, restaurantB));

    }


    @When("Registered user selects a food type : {string}")
    public void registeredUserSelectsAFoodType(String foodType) {
        restaurantsByFoodType = this.restaurantService.getRestaurantsByFoodType(List.of(foodType));
    }

    @Then("Registered user should see the restaurant that serves that food type")
    public void registeredUserShouldSeeTheRestaurantThatServesThatFoodType() {
        assertEquals(1, restaurantsByFoodType.size());
    }

    @When("Registered user selects open restaurants")
    public void registeredUserSelectsOpenRestaurants() {
        try (MockedStatic<LocalTime> mockedLocalTime = mockStatic(LocalTime.class)) {
            mockedLocalTime.when(LocalTime::now).thenReturn(LocalTime.of(14, 0));
            restaurantsAvailable = this.restaurantService.getRestaurantsByAvailability();
        }
    }

    @Then("Registered user should see the restaurant that are open")
    public void registeredUserShouldSeeTheRestaurantThatAreOpen() {
        assertEquals(1, restaurantsAvailable.size());
    }


    @When("Registered user searches for a restaurant with name {string}")
    public void registeredUserSearchesForARestaurantWithName(String restaurantName) {
        restaurantsByName = this.restaurantService.getRestaurantsByName(restaurantName);
    }

    @Then("Registered user should see the restaurant that matches the name {string}")
    public void registeredUserShouldSeeTheRestaurantThatMatchesTheName(String restaurantName) {
        assertEquals(1, restaurantsByName.size());
    }
}

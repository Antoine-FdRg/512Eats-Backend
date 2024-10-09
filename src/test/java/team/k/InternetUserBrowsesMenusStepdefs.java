package team.k;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.common.DishBuilder;
import team.k.enumerations.FoodType;
import team.k.repository.DishRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.TimeSlotRepository;
import team.k.restaurant.Restaurant;
import team.k.service.RestaurantService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class InternetUserBrowsesMenusStepdefs {


    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    TimeSlotRepository timeSlotRepository;
    @Mock
    DishRepository dishRepository;

    @InjectMocks
    private RestaurantService restaurantService;
    private Restaurant restaurant;
    List<Dish> dishes;

    // Create a restaurant before each scenario
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Remove the restaurant after each scenario
    @After
    public void tearDown() {
        if (restaurant != null) {
            restaurantService.deleteRestaurant(restaurant);
        }
    }

    @Given("A restaurant {string} with a dish {string} and a dish {string}")
    public void aRestaurantExistInTheListOfRestaurantsWithADishAndADish(String restaurantName, String dishNameA, String dishNameB) {
        dishes = new ArrayList<>();
        Dish dishA = new DishBuilder().setName(dishNameA).setDescription("Description").setPrice(5).setPreparationTime(3).build();
        when(dishRepository.findById(dishA.getId())).thenReturn(dishA);
        dishes.add(dishA);
        Dish dishB = new DishBuilder().setName(dishNameB).setDescription("Description").setPrice(5).setPreparationTime(3).build();
        when(dishRepository.findById(dishB.getId())).thenReturn(dishB);
        dishes.add(dishB);
        when(dishRepository.findAll()).thenReturn(dishes);
        Restaurant restaurantA = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(22, 0, 0)).setFoodTypes(List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL)).build();
        restaurantA.addDish(dishA);
        restaurantA.addDish(dishB);
        when(restaurantRepository.findByName(restaurantName)).thenReturn(restaurantA);
        when(restaurantRepository.findById(restaurantA.getId())).thenReturn(restaurantA);
        restaurantService.addRestaurant(restaurantA);
    }

    @When("The user wants to have dishes of the restaurant {string}")
    public void theUserWantsToHaveDishesOfTheRestaurant(String restaurantName) {
        restaurant = restaurantService.getRestaurantByName(restaurantName);
    }

    @Then("the user gets the dishes {string} and {string} registered in the restaurant selected")
    public void theUserGetsTheDishesAndRegisteredInTheRestaurantSelected(String dishNameA, String dishNameB) {
        List<Dish> restaurantDishes = restaurant.getDishes();
        assertEquals(2, restaurantDishes.size());
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameA)).count());
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameB)).count());
    }
}

package team.k;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;
import team.k.service.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class InternetUserBrowsesMenusStepdefs {

    private RestaurantService restaurantService;
    private Restaurant restaurant;
    List<Dish> dishes;

    // Create a restaurant before each scenario
    @Before
    public void setUp() {
        restaurantService = new RestaurantService();
    }

    // Remove the restaurant after each scenario
    @After
    public void tearDown() {
        if (restaurant != null) {
            restaurantService.deleteRestaurant(restaurant);
        }
    }

    @Given("A restaurant {string} exists in the list of restaurants with a dish {string} and a dish {string}")
    public void aRestaurantExistInTheListOfRestaurantsWithADishAndADish(String restaurantName, String dishNameA, String dishNameB) {
        dishes = new ArrayList<>();
        dishes.add(new Dish(1, dishNameA, "Description", 5, 3, ""));
        dishes.add(new Dish(2, dishNameB, "Description", 5, 3, ""));
        Restaurant restaurantA = new Restaurant(restaurantName, 1, LocalTime.of(8, 0, 0), LocalTime.of(22, 0, 0, 0), dishes, List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL), null);
        restaurantService.addRestaurant(restaurantA);
    }

    @When("The user wants to have dishes of the restaurant {string}")
    public void theUserWantsToHaveDishesOfTheRestaurant(String restaurantName) {
        restaurant = restaurantService.getRestaurantByName(restaurantName);
    }

    @Then("the user gets the dishes {string} and {string} registered in the restaurant selected")
    public void theUserGetsTheDishesAndRegisteredInTheRestaurantSelected(String dishNameA, String dishNameB) {
        List<Dish> restaurantDishes = restaurant.getDishes();
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameA)).count());
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameB)).count());
    }
}

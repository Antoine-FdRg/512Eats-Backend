package team.k;

import commonlibrary.repository.TimeSlotRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.Dish;
import commonlibrary.enumerations.FoodType;
import commonlibrary.repository.DishRepository;
import commonlibrary.repository.RestaurantRepository;
import commonlibrary.model.restaurant.Restaurant;
import team.k.restaurantService.RestaurantService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class InternetUserBrowsesMenusStepdefs {



    RestaurantRepository restaurantRepository;
    DishRepository dishRepository;

    TimeSlotRepository timeSlotRepository;
    private RestaurantService restaurantService;
    private Restaurant restaurant;
    Restaurant restaurantB;

    List<Dish> dishes;
    List<Dish> restaurantDishes;

    private NoSuchElementException errorMessage;

    @Before
    public void setUp() {
        timeSlotRepository = new TimeSlotRepository();
        restaurantRepository = new RestaurantRepository();
        dishRepository = new DishRepository();
        restaurantService = new RestaurantService(restaurantRepository, timeSlotRepository);
        restaurant = new Restaurant.Builder().setName("restaurant").setId(1).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(22, 0, 0)).setFoodTypes(List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL)).build();
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
        Dish dishA = new Dish.Builder().setName(dishNameA).setDescription("Description").setPrice(5).setPreparationTime(3).build();
        dishes.add(dishA);
        Dish dishB = new Dish.Builder().setName(dishNameB).setDescription("Description").setPrice(5).setPreparationTime(3).build();
        dishes.add(dishB);
        Restaurant restaurantA = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(22, 0, 0)).setFoodTypes(List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL)).build();
        restaurantA.addDish(dishA);
        restaurantA.addDish(dishB);
        restaurantRepository.add(restaurantA);
        dishRepository.add(dishA);
        dishRepository.add(dishB);
        restaurant.setDishes(dishes);
    }

    @When("The user wants to have dishes non registered of the restaurant {string}")
    public void theUserWantsToHaveDishesNonRegisteredOfTheRestaurant(String restaurantName) {
        restaurantRepository.add(restaurant);
        try {
            restaurantDishes = restaurantService.getAllDishesFromRestaurant(restaurant.getId());
        } catch (NoSuchElementException e) {
            this.errorMessage = e;
        }

    }

    @When("The user wants to have dishes of the restaurant {string}")
    public void theUserWantsToHaveDishesOfTheRestaurant(String restaurantName) {
        restaurant = restaurantService.getRestaurantByName(restaurantName);

        try {
            restaurantDishes = restaurant.getDishes();
        } catch (NoSuchElementException e) {
            this.errorMessage = e;
        }

    }

    @Then("the user gets the dishes {string} and {string} registered in the restaurant selected")
    public void theUserGetsTheDishesAndRegisteredInTheRestaurantSelected(String dishNameA, String dishNameB) {
        List<Dish> restaurantDishes = restaurant.getDishes();
        assertEquals(2, restaurantDishes.size());
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameA)).count());
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameB)).count());
    }

    @Given("A restaurant {string} with no dishes registered")
    public void aRestaurantWithNoDishesRegistered(String restaurantName) {
        restaurantB = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(22, 0, 0)).setFoodTypes(List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL)).build();
        restaurantService.addRestaurant(restaurantB);
    }

    @Then("The user gets no dishes registered in the restaurant selected and have this message :{string}")
    public void theUserGetsNoDishesRegisteredInTheRestaurantSelectedAndHaveThisMessage(String expectedMessage) {
        assertEquals(this.errorMessage.getMessage(), expectedMessage);
    }


}

package team.k;

import io.cucumber.java.After;
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
import team.k.restaurant.TimeSlot;
import team.k.service.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class InternetUserFiltersRestaurantStepdefs {


    private RestaurantRepository restaurantRepository;

    private RestaurantService restaurantService;
    List<Restaurant> restaurantsByFoodType;
    List<Restaurant> restaurantsAvailable;
    List<Restaurant> restaurantsByName;


    Restaurant restaurantA;

    Restaurant restaurantB;

    private NoSuchElementException exception;

    @Before
    public void setUp() {
        this.restaurantRepository = new RestaurantRepository();
        this.restaurantService = new RestaurantService(restaurantRepository);
    }

    @After
    public void tearDown() {
        this.restaurantRepository.delete(restaurantA);
        this.restaurantRepository.delete(restaurantB);

    }


    @Given("a list of restaurants {string} a {string} restaurant which is open from {int} to {int} and {string} a {string} restaurant opened from {int} to {int} with registered dishes")
    public void aListOfRestaurantsARestaurantWhichIsOpenFromToAndARestaurantOpenedFromToWithRegisteredDishes(String restaurantNameA, String restaurantTypeA, int openningA, int closingA, String restaurantNameB, String restaurantTypeB, int openningB, int closingB) {
        restaurantA = new Restaurant.Builder().setName(restaurantNameA).setOpen(LocalTime.of(openningA, 0, 0)).setClose(LocalTime.of(closingA, 0, 0)).setFoodTypes(List.of(FoodType.SUSHI)).build();
        restaurantB = new Restaurant.Builder().setName(restaurantNameB).setOpen(LocalTime.of(openningB, 0, 0)).setClose(LocalTime.of(closingB, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).build();
        Dish dishA = new Dish.Builder().setName("sushi").setDescription("Description").setPrice(5).setPreparationTime(0).build();
        Dish dishB = new Dish.Builder().setName("burger").setDescription("Description").setPrice(5).setPreparationTime(0).build();
        restaurantA.addTimeSlot(new TimeSlot(LocalDateTime.of(2024, 10, 12, openningA, 0, 0), restaurantA, 500, 10));
        restaurantB.addTimeSlot(new TimeSlot(LocalDateTime.of(2024, 10, 12, openningB, 0, 0), restaurantB, 500, 10));
        restaurantA.addDish(dishA);
        restaurantB.addDish(dishB);
        this.restaurantRepository.add(restaurantA);
        this.restaurantRepository.add(restaurantB);
    }

    //By Name//
    @When("Registered user searches for a restaurant with name {string}")
    public void registeredUserSearchesForARestaurantWithNameButDoesnTExist(String restaurantName) {
        try {
            restaurantsByName = this.restaurantService.getRestaurantsByName(restaurantName);
        } catch (NoSuchElementException e) {
            this.exception = e;
        }
    }

    @Then("Registered user should see the restaurant that matches the name {string} for his name choice")
    public void registeredUserShouldSeeTheRestaurantThatMatchesTheName(String restaurantName) {
        assertEquals(1, restaurantsByName.size());
    }

    //By Type of food//

    @When("Registered user selects a food type : {string}")
    public void registeredUserSelectsAFoodType(String type) {
        try {
            restaurantsByFoodType = this.restaurantService.getRestaurantsByFoodType(List.of(type));
        } catch (NoSuchElementException e) {
            this.exception = e;
        }
    }
    @Then("Registered user should see the restaurant that serves that food type")
    public void registeredUserShouldSeeTheRestaurantThatServesThatFoodType() {
        assertEquals(1, restaurantsByFoodType.size());
    }

    //By Availability//

    @When("Registered user selects restaurants that are open at {int}: {int}")
    public void registeredUserSelectsRestaurantsThatAreOpenAtOClock(int currentTime, int min) {
        try {
            restaurantsAvailable = this.restaurantService.getRestaurantsByAvailability(LocalTime.of(currentTime, min, 0));
        } catch (NoSuchElementException e) {
            this.exception = e;
        }
    }
    @Then("Registered user should see the restaurant that are open")
    public void registeredUserShouldSeeTheRestaurantThatAreOpen() {
        assertEquals(1, restaurantsAvailable.size());
    }


    //Exceptions//

    @Then("Registered user should see a message {string}")
    public void registeredUserShouldSeeAMessage(String expectedMessage) {
        assertEquals(expectedMessage, this.exception.getMessage());
    }


}

package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.Dish;
import commonlibrary.enumerations.FoodType;
import team.k.repository.RestaurantRepository;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.restaurantservice.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class InternetUserFiltersRestaurantStepdefs {


    List<Restaurant> restaurantsByFoodType;
    List<Restaurant> restaurantsAvailable;
    List<Restaurant> restaurantsByName;


    Restaurant restaurantA;

    Restaurant restaurantB;

    private NoSuchElementException exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a list of restaurants {string} a {string} restaurant which is open from {int} to {int} and {string} a {string} restaurant opened from {int} to {int} with registered dishes")
    public void aListOfRestaurantsARestaurantWhichIsOpenFromToAndARestaurantOpenedFromToWithRegisteredDishes(String restaurantNameA, String restaurantTypeA, int openningA, int closingA, String restaurantNameB, String restaurantTypeB, int openningB, int closingB) {
        restaurantA = new Restaurant.Builder().setName(restaurantNameA).setOpen(LocalTime.of(openningA, 0, 0)).setClose(LocalTime.of(closingA, 0, 0)).setFoodTypes(List.of(FoodType.valueOf(restaurantTypeA))).setAverageOrderPreparationTime(15).build();
        restaurantB = new Restaurant.Builder().setName(restaurantNameB).setOpen(LocalTime.of(openningB, 0, 0)).setClose(LocalTime.of(closingB, 0, 0)).setFoodTypes(List.of(FoodType.valueOf(restaurantTypeB))).setAverageOrderPreparationTime(15).build();
        Dish dishA = new Dish.Builder().setName("sushi").setDescription("Description").setPrice(5).setPreparationTime(0).build();
        Dish dishB = new Dish.Builder().setName("burger").setDescription("Description").setPrice(5).setPreparationTime(0).build();
        restaurantA.addTimeSlot(new TimeSlot(LocalDateTime.of(2025, 1, 1, openningA, 0, 0), restaurantA, 5));
        restaurantB.addTimeSlot(new TimeSlot(LocalDateTime.of(2025, 1, 1, openningB, 0, 0), restaurantB, 5));
        restaurantA.addDish(dishA);
        restaurantB.addDish(dishB);
        RestaurantRepository.add(restaurantA);
        RestaurantRepository.add(restaurantB);
    }

    //By Name//
    @When("Internet User searches for a restaurant with name {string}")
    public void internetUserSearchesForARestaurantWithNameButDoesnTExist(String restaurantName) {
        try {
            restaurantsByName = RestaurantService.getRestaurantsByName(restaurantName);
        } catch (NoSuchElementException e) {
            this.exception = e;
        }
    }

    @Then("Internet User should see the restaurant that matches the name {string} for his name choice")
    public void internetUserShouldSeeTheRestaurantThatMatchesTheName(String restaurantName) {
        assertEquals(1, restaurantsByName.size());
    }

    //By Type of food//

    @When("Internet User selects a food type : {string}")
    public void internetUserSelectsAFoodType(String type) {
        try {
            restaurantsByFoodType = RestaurantService.getRestaurantsByFoodType(List.of(FoodType.valueOf(type)));
        } catch (NoSuchElementException e) {
            this.exception = e;
        }
    }

    @Then("Internet User should see the restaurant that serves that food type")
    public void internetUserShouldSeeTheRestaurantThatServesThatFoodType() {
        assertEquals(2, restaurantsByFoodType.size());
    }

    //By Availability//

    @When("Internet User selects restaurants that are open for a delivery {int}:{int} on {int}-{int}-{int}")
    public void internetUserSelectsRestaurantsThatAreOpenAtOClock(int hours, int minutes, int day, int month, int year) {
        try {
            restaurantsAvailable = RestaurantService.getRestaurantsByAvailability(LocalDateTime.of(year, month, day, hours, minutes, 0));
        } catch (NoSuchElementException e) {
            this.exception = e;
        }
    }

    @Then("Internet User should see the restaurant that are open")
    public void internetUserShouldSeeTheRestaurantThatAreOpen() {
        assertNotNull(restaurantsAvailable);
        assertEquals(2, restaurantsAvailable.size());
    }


    //Exceptions//

    @Then("Internet User should see a message {string}")
    public void internetUserShouldSeeAMessage(String expectedMessage) {
        assertEquals(expectedMessage, this.exception.getMessage());
    }


}

package team.k;

import commonlibrary.repository.DishJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.TimeSlotJPARepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import commonlibrary.model.Dish;
import commonlibrary.enumerations.FoodType;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.restaurantservice.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;

public class InternetUserFiltersRestaurantStepdefs {
    @Mock
    private RestaurantJPARepository restaurantRepository;
    private List<Restaurant> restaurantsInDatabase;
    @Mock
    private TimeSlotJPARepository timeSlotRepository;
    private List<TimeSlot> timeSlotsInDatabase;
    @Mock
    private DishJPARepository dishRepository;
    private List<Dish> dishesInDatabase;
    private RestaurantService restaurantService;

    List<Restaurant> restaurantsByFoodType;
    List<Restaurant> restaurantsAvailable;
    List<Restaurant> restaurantsByName;
    
    private NoSuchElementException exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantService = new RestaurantService(restaurantRepository, timeSlotRepository);
        restaurantsInDatabase = new ArrayList<>();
        dishesInDatabase = new ArrayList<>();
        timeSlotsInDatabase = new ArrayList<>();
    }

    @Given("The restaurant {string} a {string} restaurant open from {int} to {int} with the dish {string} registered")
    public void theRestaurantARestaurantWhichIsOpenFromToWitheTheDishRegistered(String restaurantName, String restaurantType, int openning, int closing, String dishName) {
        Restaurant restaurant = new Restaurant.Builder()
                .setName(restaurantName)
                .setOpen(LocalTime.of(openning, 0, 0))
                .setClose(LocalTime.of(closing, 0, 0))
                .setFoodTypes(List.of(FoodType.valueOf(restaurantType)))
                .setAverageOrderPreparationTime(15).build();
        restaurantsInDatabase.add(restaurant);
        Dish dish = new Dish.Builder().setName(dishName).setDescription("Description").setPrice(5).setPreparationTime(0).build();
        dishesInDatabase.add(dish);
        restaurant.addDish(dish);
        TimeSlot timeSlot = new TimeSlot(LocalDateTime.of(2025, 1, 1, openning, 0, 0), restaurant, 5);
        restaurant.addTimeSlot(timeSlot);
        timeSlotsInDatabase.add(timeSlot);
        fillMock();
    }
    
    private void fillMock(){
        when(restaurantRepository.findAll()).thenReturn(restaurantsInDatabase);
        when(dishRepository.findAll()).thenReturn(dishesInDatabase);
        when(timeSlotRepository.findAll()).thenReturn(timeSlotsInDatabase);
    }

    //By Name//

    @When("Internet User searches for a restaurant with name {string}")
    public void internetUserSearchesForARestaurantWithNameButDoesnTExist(String restaurantName) {
        try {
            restaurantsByName = restaurantService.getRestaurantsByName(restaurantName);
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
            restaurantsByFoodType = restaurantService.getRestaurantsByFoodType(List.of(FoodType.valueOf(type)));
        } catch (NoSuchElementException e) {
            this.exception = e;
        }
    }

    @Then("Internet User should see the {int} restaurants that serves that food type")
    public void internetUserShouldSeeTheRestaurantThatServesThatFoodType(int numberOfRestaurants) {
        assertEquals(numberOfRestaurants, restaurantsByFoodType.size());
    }

    //By Availability//

    @When("Internet User selects restaurants that are open for a delivery {int}:{int} on {int}-{int}-{int}")
    public void internetUserSelectsRestaurantsThatAreOpenAtOClock(int hours, int minutes, int day, int month, int year) {
        try {
            restaurantsAvailable = restaurantService.getRestaurantsByAvailability(LocalDateTime.of(year, month, day, hours, minutes, 0));
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

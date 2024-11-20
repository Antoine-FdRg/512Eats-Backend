package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.Dish;
import team.k.repository.RestaurantRepository;
import commonlibrary.model.restaurant.Restaurant;
import team.k.managingRestaurantService.ManageRestaurantService;

import java.time.LocalTime;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class ManagingRestaurant {
    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private ManageRestaurantService manageRestaurantService;
    private Restaurant nagaRestaurant;
    private int dishAdded;
    private int dishUpdated;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("the restaurant {string} has the following information")
    public void theRestaurantHasTheFollowingInformation(String restaurantName, Map<String, String> restaurantInfo) {
        nagaRestaurant = new Restaurant.Builder()
                .setName(restaurantName)
                .setAverageOrderPreparationTime(10)
                .setOpen(LocalTime.parse(restaurantInfo.get("open")))
                .setClose(LocalTime.parse(restaurantInfo.get("closed")))
                .build();
        Dish dishA = new Dish.Builder().setName("sushi").setDescription("Description").setPrice(5).setPreparationTime(0).build();
        Dish dishB = new Dish.Builder().setName("burger").setDescription("Description").setPrice(5).setPreparationTime(0).build();
        nagaRestaurant.addDish(dishA);
        nagaRestaurant.addDish(dishB);
        when(restaurantRepository.findById(1)).thenReturn(nagaRestaurant);

    }

    @When("the restaurant manager updates the open time to {string}")
    public void theRestaurantManagerUpdatesTheOpenTimeTo(String open) {
        manageRestaurantService.updateRestaurantInfos(1, open, null);
    }

    @When("the restaurant manager updates the closed time to {string}")
    public void theRestaurantManagerUpdatesTheClosedTimeTo(String closed) {
        manageRestaurantService.updateRestaurantInfos(1, null, closed);
    }

    @Then("the restaurant Naga should have the following information")
    public void theRestaurantShouldHaveTheFollowingInformation(Map<String, String> expectedInfo) {
        assertEquals(LocalTime.parse(expectedInfo.get("open")), nagaRestaurant.getOpen());
        assertEquals(LocalTime.parse(expectedInfo.get("closed")), nagaRestaurant.getClose());
    }

    @When("the restaurant manager adds a new dish {string} with price {double}")
    public void theRestaurantManagerAddsANewDishWithPrice(String dishName, double dishPrice) {
        manageRestaurantService.addDish(1, dishName, "Description", dishPrice, 0);
        this.dishAdded = nagaRestaurant.getDishes().stream().filter(d -> d.getName().equals(dishName)).findFirst().orElse(null).getId();
    }

    @Then("the restaurant Naga should have the new dish {string} with price {double}")
    public void theRestaurantNagaShouldHaveTheNewDishWithPrice(String dishName, double dishPrice) {
        assertEquals(dishName, nagaRestaurant.getDishes().getLast().getName());
        assertEquals(dishPrice, nagaRestaurant.getDishes().getLast().getPrice(), 0);
    }

    @And("the restaurant manager removes the dish recently added")
    public void theRestaurantManagerRemovesTheDish() {
        manageRestaurantService.removeDish(1, this.dishAdded);
    }

    @Then("the restaurant Naga should not have the dish {string}")
    public void theRestaurantNagaShouldNotHaveTheDish(String dishName) {
        assertNull(nagaRestaurant.getDishes().stream().filter(d -> d.getName().equals(dishName)).findFirst().orElse(null));
    }

    @And("the restaurant manager updates the dish {int} price to {double}")
    public void theRestaurantManagerUpdatesTheDishPriceTo(int dishId, double newPrice) {
        manageRestaurantService.updateDish(1, dishId, newPrice, 0);
    }

    @And("the restaurant manager updates a dish with price to {double} with preparation time {int}")
    public void theRestaurantManagerUpdatesTheDishPriceToWithPreparationTime(double price, int preparationTime) {
        Dish dish = nagaRestaurant.getDishes().stream().findFirst().orElse(null);
        assertNotNull(dish);
        this.dishUpdated = dish.getId();
        manageRestaurantService.updateDish(1, dishUpdated, price, preparationTime);
    }

    @Then("the restaurant Naga should have the dish updated with price {double} and preparation time {int}")
    public void theRestaurantNagaShouldHaveTheDishWithPriceAndPreparationTime(double price, int preparatioTime) {
        Dish dish = nagaRestaurant.getDishes().stream().filter(d -> d.getId() == dishUpdated).findFirst().orElse(null);
        assertNotNull(dish);
        assertEquals(price, dish.getPrice(), 0);
        assertEquals(preparatioTime, dish.getPreparationTime());
    }
}

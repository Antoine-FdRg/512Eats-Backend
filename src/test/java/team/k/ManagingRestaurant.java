package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.repository.RestaurantRepository;
import team.k.restaurant.Restaurant;
import team.k.service.ManageRestaurantService;

import java.time.LocalTime;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ManagingRestaurant {
    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private ManageRestaurantService manageRestaurantService;
    private Restaurant nagaRestaurant;

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
}

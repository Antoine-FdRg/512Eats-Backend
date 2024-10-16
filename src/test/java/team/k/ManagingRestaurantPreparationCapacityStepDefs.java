package team.k;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.order.IndividualOrder;
import team.k.order.SubOrder;
import team.k.repository.RestaurantRepository;
import team.k.restaurant.Restaurant;

import java.time.LocalTime;
import java.util.List;

public class ManagingRestaurantPreparationCapacityStepDefs {

    @Mock
    RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("an order with the status {string} in the restaurant {string} with a chosen dish {string}")
    public void anOrderWithTheStatusInTheRestaurantWithAChosenDish(String statusCreated, String restaurantName, String dishName) {
        Restaurant restaurant = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).build();
        Dish dish = new Dish.Builder().setName(dishName).setDescription("Cheeseburger").setPrice(5).setPreparationTime(10).build();

    }


    @When("a registered user places the command")
    public void aRegisteredUserPlacesTheCommand() {

    }


    @Then("the preparation capacity of the order has increased")
    public void thePreparationCapacityOfTheOrderHasIncreased() {

    }
}

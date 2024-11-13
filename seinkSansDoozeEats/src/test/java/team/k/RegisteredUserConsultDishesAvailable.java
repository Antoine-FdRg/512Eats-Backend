package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.model.Dish;
import team.k.common.model.RegisteredUser;
import team.k.common.enumerations.Role;
import team.k.common.model.order.OrderBuilder;
import team.k.common.model.order.SubOrder;
import team.k.common.repository.RestaurantRepository;
import team.k.common.repository.SubOrderRepository;
import team.k.common.model.restaurant.Restaurant;
import team.k.common.model.restaurant.TimeSlot;
import team.k.orderService.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RegisteredUserConsultDishesAvailable {

    Restaurant nagaRestaurant;

    RegisteredUser registeredUser;
    SubOrder order;
    List<Dish> availableDishes;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    SubOrderRepository subOrderRepository;
    @InjectMocks
    OrderService orderService;

    @DataTableType
    public Dish dishEntry(Map<String, String> entry) {
        return new Dish.Builder()
                .setId(Integer.parseInt(entry.get("id")))
                .setName(entry.get("name"))
                .setPrice(Double.parseDouble(entry.get("price")))
                .setPreparationTime(Integer.parseInt(entry.get("preparationTime")))
                .build();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("the restaurant {string} has the following dishes with preparation time")
    public void theRestaurantNagaHasTheFollowingDishesWithPreparationTime(String restaurantName, List<Dish> dishes) {

        nagaRestaurant = new Restaurant.Builder()
                .setName(restaurantName)
                .setAverageOrderPreparationTime(10)
                .build();
        for (Dish dish : dishes) {
            nagaRestaurant.addDish(dish);
        }
        when(restaurantRepository.findById(1)).thenReturn(nagaRestaurant);
    }

    @And("User {string} has a currentOrder for the restaurant Naga in the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void userHasACurrentOrderForTheRestaurantNagaInTheTimeslotBeginningAtOn(String userName, int min, int hour, int day, int month, int year) {
        registeredUser = new RegisteredUser(userName, Role.STUDENT);
        order = new OrderBuilder()
                .setRestaurant(nagaRestaurant)
                .setUser(registeredUser)
                .setDeliveryTime(LocalDateTime.of(year, month, day, hour, min))
                .build();
        registeredUser.setCurrentOrder(order);
        TimeSlot timeSlot = new TimeSlot(LocalDateTime.of(year, month, day, hour, min), nagaRestaurant, nagaRestaurant.getAverageOrderPreparationTime());
        nagaRestaurant.addTimeSlot(timeSlot);
        when(subOrderRepository.findById(1)).thenReturn(order);
    }

    @When("Jack consults the available dishes of the restaurant Naga")
    public void jackConsultsTheAvailableDishesOfTheRestaurantNagaForTheTimeslotBeginningAtOn() {
        availableDishes = orderService.getAvailableDishes(1, 1);
    }

    @Then("he can see only the following dishes")
    public void heCanSeeOnlyTheFollowingDishes(List<Dish> expectedDishes) {
        assertTrue(availableDishes.containsAll(expectedDishes) && expectedDishes.containsAll(availableDishes));
    }

    @And("Jack adds the dish {string} to his basket")
    public void jackAddsTheDishToHisBasket(String dishName) {
        Dish dish = nagaRestaurant.getDishes().stream().filter(d -> d.getName().equals(dishName)).findFirst().orElse(null);
        assertNotNull(dish);
        order.addDish(dish);
    }

}

package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.common.Location;
import team.k.enumerations.OrderStatus;
import team.k.enumerations.Role;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;
import team.k.service.OrderService;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static junit.framework.TestCase.assertEquals;

public class RegisteredUserCreatesSuborderStepDefs {

    RegisteredUser registeredUser;
    GroupOrder groupOrder;

    Restaurant restaurant;
    Dish dish;
    RestaurantRepository restaurantRepository;
    RegisteredUserRepository registeredUserRepository;
    GroupOrderRepository groupOrderRepository;
    LocationRepository locationRepository;
    SubOrderRepository subOrderRepository;
    OrderService orderService;

    @Before
    public void setUp() {
        registeredUser = new RegisteredUser("John Doe", Role.STUDENT);
        registeredUserRepository = new RegisteredUserRepository();
        registeredUserRepository.add(registeredUser);
        restaurantRepository = new RestaurantRepository();
        groupOrderRepository = new GroupOrderRepository();
        locationRepository = new LocationRepository();
        subOrderRepository = new SubOrderRepository();
        orderService = new OrderService(
                groupOrderRepository,
                locationRepository,
                subOrderRepository,
                restaurantRepository,
                registeredUserRepository);
    }

    @Given("a groupOrder with the id {int} and without any suborder")
    public void aGroupOrderWithTheId5AndWithoutAnySuborderARegisteredUserJoinTheGroupOrder(int groupOrderId) {
        groupOrder = new GroupOrder.Builder().build();
        groupOrderRepository.add(groupOrder);
    }

    @Given("a restaurant {string}  with a dish {string} an opening time {string} and closing time {string}")
    public void aRestaurantWithADish(String restaurantName, String dishName, String open, String closed) {
        restaurant = new Restaurant.Builder()
                .setName(restaurantName)
                .setAverageOrderPreparationTime(10)
                .setOpen(LocalTime.parse(open))
                .setClose(LocalTime.parse(closed))
                .build();
        restaurantRepository.add(restaurant);
        dish = new Dish.Builder().setName(dishName).setDescription("buger").setPrice(5).setPreparationTime(0).build();
        restaurant.addDish(dish);
    }

    @When("the user order a {string} in the restaurant {string}")
    public void theUserOrderAInTheRestaurant(String dishName, String restaurantName) {
        int idrestaurant = restaurantRepository.findRestaurantByName(restaurantName).getFirst().getId();
        int registerId = registeredUser.getId();
        orderService.createSuborder(registerId, idrestaurant, groupOrder.getId());
        registeredUser.getCurrentOrder().addDish(dish);
    }

    @Then("the suborder has the status created")
    public void theSuborderHasTheStatusCreated() {

        assertEquals(registeredUser.getCurrentOrder().getStatus(), OrderStatus.CREATED);
    }

    @And("the groupe order with the id {int} has a suborder with the status created")
    public void theGroupeOrderWithTheIdHasASuborderWithTheStatusCreated(int grpOrderId) {
        assertEquals(groupOrder.getSubOrders().get(0).getStatus(), OrderStatus.CREATED);
    }

    @When("the user orders a {string} in the restaurant {string} for the location : {string}")
    public void theUserOrdersAInTheRestaurantForTheLocation(String dishName, String restaurantName, String location) {
        int idrestaurant = restaurantRepository.findRestaurantByName(restaurantName).getFirst().getId();
        int registerId = registeredUser.getId();
        Location loc = new Location.Builder().setAddress(location).build();
        locationRepository.add(loc);
        restaurant.addTimeSlot(new TimeSlot(LocalDateTime.parse("2024-10-12T11:20:00"), restaurant, 20));
        orderService.createIndividualOrder(registerId, idrestaurant, loc.getId(), LocalDateTime.parse("2024-10-12T12:13:20"), LocalDateTime.parse("2024-10-12T12:11:18"));
        registeredUser.getCurrentOrder().addDish(dish);
    }

    @Then("the order has the status created")
    public void theOrderHasTheStatusCreated() {
        assertEquals(registeredUser.getCurrentOrder().getStatus(), OrderStatus.CREATED);
    }
}

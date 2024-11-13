package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import team.k.common.model.Dish;
import team.k.common.model.Location;
import team.k.common.model.RegisteredUser;
import team.k.common.enumerations.OrderStatus;
import team.k.common.enumerations.Role;
import team.k.common.model.order.GroupOrder;
import team.k.common.repository.GroupOrderRepository;
import team.k.common.repository.LocationRepository;
import team.k.common.repository.RegisteredUserRepository;
import team.k.common.repository.RestaurantRepository;
import team.k.common.repository.SubOrderRepository;
import team.k.common.model.restaurant.Restaurant;
import team.k.common.model.restaurant.TimeSlot;
import team.k.orderService.OrderService;

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

    @Given("a groupOrder without any suborder")
    public void aGroupOrderWithTheId5AndWithoutAnySuborderARegisteredUserJoinTheGroupOrder() {
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
        dish = new Dish.Builder().setName(dishName).setDescription("buger").setPrice(5).setPreparationTime(3).build();
        restaurant.addDish(dish);
    }

    @When("the user order a {string} in the restaurant {string}")
    public void theUserOrderAInTheRestaurant(String dishName, String restaurantName) {
        int idrestaurant = restaurantRepository.findRestaurantByName(restaurantName).getFirst().getId();
        int registerId = registeredUser.getId();
        orderService.createSuborder(registerId, idrestaurant, groupOrder.getId());
        registeredUser.getCurrentOrder().addDish(dish);
    }

    @Then("the suborder has the status {status}")
    public void theSuborderHasTheStatusCreated(OrderStatus wantedStatus) {

        assertEquals(registeredUser.getCurrentOrder().getStatus(), wantedStatus);
    }

    @Then("the groupe order has a suborder with the status {status}")
    public void theGroupeOrderWithTheIdHasASuborderWithTheStatusCreated(OrderStatus wantedStatus) {
        assertEquals(groupOrder.getSubOrders().get(0).getStatus(), wantedStatus);
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

    @Then("the order has the status {status}")
    public void theOrderHasTheStatusCreated(OrderStatus wantedStatus) {
        assertEquals(wantedStatus, OrderStatus.CREATED);
    }
}

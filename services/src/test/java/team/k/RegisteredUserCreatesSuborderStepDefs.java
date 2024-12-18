package team.k;

import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.repository.GroupOrderJPARepository;
import commonlibrary.repository.IndividualOrderJPARepository;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.SubOrderJPARepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import commonlibrary.model.Dish;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.orderservice.OrderService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class RegisteredUserCreatesSuborderStepDefs {
    @Mock
    private RestaurantJPARepository restaurantRepository;
    @Mock
    private SubOrderJPARepository subOrderRepository;
    @Mock
    private IndividualOrderJPARepository individualOrderRepository;
    @Mock
    private RegisteredUserJPARepository registeredUserRepository;
    @Mock
    private LocationJPARepository locationRepository;
    @Mock
    private GroupOrderJPARepository groupOrderRepository;

    private OrderService orderService;

    RegisteredUser registeredUser;
    GroupOrder groupOrder;

    Restaurant restaurant;
    Dish dish;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService =new OrderService(
                locationRepository,
                restaurantRepository,
                subOrderRepository,
                registeredUserRepository,
                groupOrderRepository,
                individualOrderRepository);
    }

    @Given("a groupOrder without any suborder")
    public void aGroupOrderWithTheId5AndWithoutAnySuborderARegisteredUserJoinTheGroupOrder() {
        groupOrder = new GroupOrder.Builder().build();
        when(groupOrderRepository.findAll()).thenReturn(List.of(groupOrder));
        when(groupOrderRepository.findById((long)groupOrder.getId())).thenReturn(Optional.ofNullable(groupOrder));
    }

    @Given("a restaurant {string}  with a dish {string} an opening time {string} and closing time {string}")
    public void aRestaurantWithADish(String restaurantName, String dishName, String open, String closed) {
        restaurant = new Restaurant.Builder()
                .setName(restaurantName)
                .setAverageOrderPreparationTime(10)
                .setOpen(LocalTime.parse(open))
                .setClose(LocalTime.parse(closed))
                .build();
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(restaurantRepository.findByName(restaurantName)).thenReturn(List.of(restaurant));
        when(restaurantRepository.findById((long)restaurant.getId())).thenReturn(Optional.ofNullable(restaurant));
        dish = new Dish.Builder().setName(dishName).setDescription("buger").setPrice(5).setPreparationTime(3).build();
        restaurant.addDish(dish);
    }

    @When("the user order a {string} in the restaurant {string}")
    public void theUserOrderAInTheRestaurant(String dishName, String restaurantName) {
        int idrestaurant = restaurantRepository.findByName(restaurantName).getFirst().getId();
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
        assertEquals(groupOrder.getSubOrders().getFirst().getStatus(), wantedStatus);
    }

    @When("the user orders a {string} in the restaurant {string} for the location : {string}")
    public void theUserOrdersAInTheRestaurantForTheLocation(String dishName, String restaurantName, String location) {
        int idrestaurant = restaurantRepository.findByName(restaurantName).getFirst().getId();
        int registerId = registeredUser.getId();
        Location loc = new Location.Builder().setAddress(location).build();
        when(locationRepository.findAll()).thenReturn(List.of(loc));
        when(locationRepository.findById((long)loc.getId())).thenReturn(Optional.of(loc));
        restaurant.addTimeSlot(new TimeSlot(LocalDateTime.parse("2024-10-12T11:20:00"), restaurant, 20));
        orderService.createIndividualOrder(registerId, idrestaurant, loc.getId(), LocalDateTime.parse("2024-10-12T12:13:20"), LocalDateTime.parse("2024-10-12T12:11:18"));
        registeredUser.getCurrentOrder().addDish(dish);
    }

    @Then("the order has the status {status}")
    public void theOrderHasTheStatusCreated(OrderStatus wantedStatus) {
        assertEquals(wantedStatus, OrderStatus.CREATED);
    }

    @Given("a registered user named {string} with the role STUDENT")
    public void aRegisteredUserNamedWithTheRoleSTUDENT(String name) {
        registeredUser = new RegisteredUser(name, Role.STUDENT);
        when(registeredUserRepository.findAll()).thenReturn(List.of(registeredUser));
        when(registeredUserRepository.findById((long)registeredUser.getId())).thenReturn(Optional.ofNullable(registeredUser));
    }
}

package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.common.DishBuilder;
import team.k.enumerations.OrderStatus;
import team.k.enumerations.Role;
import team.k.repository.DishRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.repository.TimeSlotRepository;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;
import team.k.service.OrderService;
import team.k.service.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class IndividualOrderStepdefs {
    RegisteredUser registeredUser;
    Restaurant restaurant;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    DishRepository dishRepository;
    @Mock
    RegisteredUserRepository registeredUserRepository;
    @Mock
    TimeSlotRepository timeSlotRepository;
    @Mock
    SubOrderRepository subOrderRepository;

    @InjectMocks
    RestaurantService restaurantService;

    @InjectMocks
    OrderService orderService;

    @ParameterType("STUDENT|CAMPUS_EMPLOYEE")
    public Role role(String role) {
        return Role.valueOf(role);
    }

    @ParameterType("CREATED|PAID|PLACED|DELIVERING|COMPLETED|DISCOUNT_USED|CANCELED")
    public OrderStatus status(String status) {
        return OrderStatus.valueOf(status);}

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a registeredUser named {string} with the role {role}")
    public void aRegisteredUsernamedWithTheRole(String name, Role role) {
        registeredUser = new RegisteredUser(name, role);
        when(registeredUserRepository.findById(registeredUser.getId())).thenReturn(registeredUser);
    }

    @And("a restaurant named {string} open from {int}:{int} to {int}:{int}")
    public void aRestaurantNamedOpenFromTo(String name, int openHours, int openMinutes, int closeHours, int closeMinutes) {
        LocalTime openTime = LocalTime.of(openHours, openMinutes);
        LocalTime closeTime = LocalTime.of(closeHours, closeMinutes);
        restaurant = new Restaurant.Builder().setName(name).setOpen(openTime).setClose(closeTime).build();
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(restaurant);
    }

    @And("with a productionCapacity of {int} for the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void withAProduvtionCapacityOfForTheTimeslotAtOn(int productionCapacity, int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, productionCapacity, 10);
        when(timeSlotRepository.findById(timeSlot.getId())).thenReturn(timeSlot);
        restaurantService.addTimeSlotToRestaurant(restaurant.getId(), timeSlot.getId());
    }
    
    @And("a dish named {string} that costs {double} and takes {int} minutes to be prepared")
    public void aDishNamedThatCostsAndTakesMinutesToBePrepared(String dishName, double price, int preparationTime) {
        Dish dish = new DishBuilder().setName(dishName).setPrice(price).setPreparationTime(preparationTime).build();
        when(dishRepository.findById(dish.getId())).thenReturn(dish);
        restaurantService.addDishToRestaurant(restaurant, dish);
    }

    @When("a registeredUser adds {string} to his basket")
    public void aRegisteredUserAddsToHisBasket(String dishName) {
        Dish dish = restaurant.getDishes().stream().filter(d -> d.getName().equals(dishName)).findFirst().orElseThrow();
        orderService.addDishToRegisteredUserBasket(registeredUser.getId(), dish.getId(), restaurant.getId());
        when(subOrderRepository.findById(registeredUser.getCurrentOrder().getId())).thenReturn(registeredUser.getCurrentOrder());
    }

    @Then("his current order contains {int} dishes")
    public void hisCurrentOrderContainsDishes(int nbOfDishes) {
        assertEquals(nbOfDishes,registeredUser.getCurrentOrder().getDishes().size());
    }

    @And("his current order has the status {status}")
    public void hisCurrentOrderHasTheStatus(OrderStatus status) {
        assertEquals(status, registeredUser.getCurrentOrder().getStatus());
    }
}

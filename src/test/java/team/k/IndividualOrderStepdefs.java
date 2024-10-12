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
import team.k.common.Location;
import team.k.enumerations.OrderStatus;
import team.k.enumerations.Role;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
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
    Location deliveryLocation;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    RegisteredUserRepository registeredUserRepository;
    @Mock
    TimeSlotRepository timeSlotRepository;
    @Mock
    LocationRepository locationRepository;

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

    @And("a delivery location with the number {string}, the street {string} and the city {string}")
    public void aDeliveryLocationWithTheNumberTheStreetAndTheCity(String streetNumber, String street, String city) {
        deliveryLocation = new Location.Builder()
                .setNumber(String.valueOf(streetNumber))
                .setAddress(street)
                .setCity(city)
                .build();
        when(locationRepository.findLocationById(deliveryLocation.getId())).thenReturn(deliveryLocation);
    }

    @When("a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for {int}h on {int}-{int}-{int}")
    public void aRegisteredUserCreatesAnOrderForTheRestaurantNagaWithTheDeliveryPlaceOfIdForH(int deliveryTimeHours, int deliveryTimeDay, int deliveryTimeMonth, int deliveryTimeYear) {
        LocalDateTime deliveryTime = LocalDateTime.of(deliveryTimeYear, deliveryTimeMonth, deliveryTimeDay, deliveryTimeHours, 0);
        orderService.createIndividualOrder(registeredUser.getId(), restaurant.getId(), deliveryLocation.getId(), deliveryTime);
    }

    @Then("the registeredUser should have his currentOrder with the status CREATED")
    public void theRegisteredUserShouldHaveACurrentOrderWithTheStatusCREATED() {
        assertEquals(OrderStatus.CREATED, registeredUser.getCurrentOrder().getStatus());
    }

    @Then("the registeredUser should have his currentOrder with no dishes")
    public void theRegisteredUserShouldHaveHisCurrentOrderWithNoDished() {
        assertEquals(0, registeredUser.getCurrentOrder().getDishes().size());
    }
}

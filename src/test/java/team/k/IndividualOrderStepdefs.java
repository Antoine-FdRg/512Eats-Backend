package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import team.k.common.Location;
import team.k.enumerations.OrderStatus;
import team.k.enumerations.Role;
import team.k.order.SubOrder;
import team.k.repository.LocationRepository;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndividualOrderStepdefs {
    RegisteredUser registeredUser;
    Restaurant restaurant;
    Location deliveryLocation;
    @Mock
    TimeSlotRepository timeSlotRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    LocationRepository locationRepository;
    @Mock
    SubOrderRepository subOrderRepository;
    @Mock
    RegisteredUserRepository registeredUserRepository;
    @InjectMocks
    OrderService orderService;

    private IllegalArgumentException orderNotCreatedException;

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
        restaurant = new Restaurant.Builder().setName(name).setOpen(openTime).setClose(closeTime).setAverageOrderPreparationTime(30).build();
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(restaurant);
    }

    @And("with a productionCapacity of {int} for the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void withAProduvtionCapacityOfForTheTimeslotAtOn(int productionCapacity, int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, productionCapacity);
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

    @When("a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for {int}h{int} on {int}-{int}-{int} the current date being {int}-{int}-{int} {int}:{int}")
    public void aRegisteredUserCreatesAnOrderForTheRestaurantNagaWithTheDeliveryPlaceCreatedForHOnTheCurrentDateBeing(int deliveryTimeHours, int deliveryTimeMinutes, int deliveryTimeDay, int deliveryTimeMonth, int deliveryTimeYear, int currentDay, int currentMonth, int currentYear, int currentHours, int currentMinutes) {
        LocalDateTime deliveryTime = LocalDateTime.of(deliveryTimeYear, deliveryTimeMonth, deliveryTimeDay, deliveryTimeHours, deliveryTimeMinutes);
        LocalDateTime now = LocalDateTime.of(currentYear, currentMonth, currentDay, currentHours, currentMinutes);
        try {
            orderService.createIndividualOrder(registeredUser.getId(), restaurant.getId(), deliveryLocation.getId(), deliveryTime, now);
        } catch (IllegalArgumentException e) {
            orderNotCreatedException = e;
        }
    }

    @Then("the registeredUser should have his currentOrder with the status CREATED")
    public void theRegisteredUserShouldHaveACurrentOrderWithTheStatusCREATED() {
        assertNotNull(registeredUser.getCurrentOrder());
        assertEquals(OrderStatus.CREATED, registeredUser.getCurrentOrder().getStatus());
    }

    @And("the registeredUser should have his currentOrder with no dishes")
    public void theRegisteredUserShouldHaveHisCurrentOrderWithNoDished() {
        assertEquals(0, registeredUser.getCurrentOrder().getDishes().size());
    }

    @And("the restaurant should have {int} order with the status CREATED")
    public void theRestaurantShouldHaveAnOrderWithTheStatusCREATED(int numberOfOrders) {
        TimeSlot timeSlot = restaurant.getPreviousTimeSlot(registeredUser.getCurrentOrder().getDeliveryDate().toLocalTime());
        System.out.println(timeSlot.toString());
        int numberOfCreatedOrders = timeSlot.getNumberOfCreatedOrders();
        assertEquals(numberOfOrders, numberOfCreatedOrders);
    }

    @And("the order should have been added to the suborder repository")
    public void theOrderShouldHaveBeenAddedToTheSuborderRepository() {
        verify(subOrderRepository).add(registeredUser.getCurrentOrder());
    }

    @When("a registeredUser creates an order for the restaurant Naga with the deliveryPlace created but without delivery date the current date being {int}-{int}-{int} {int}:{int}")
    public void aRegisteredUserCreatesAnOrderForTheRestaurantNagaWithTheDeliveryPlaceCreatedButWithoutDeliveryDateTheCurrentDateBeing(int currentDay, int currentMonth, int currentYear, int currentHours, int currentMinutes) {
        LocalDateTime now = LocalDateTime.of(currentYear, currentMonth, currentDay, currentHours, currentMinutes);
        try {
            orderService.createIndividualOrder(registeredUser.getId(), restaurant.getId(), deliveryLocation.getId(), null, now);
        } catch (IllegalArgumentException e) {
            orderNotCreatedException = e;
        }
    }

    @Then("the registeredUser should not have any currentOrder")
    public void theRegisteredUserShouldNotHaveAnyCurrentOrder() {
        assertEquals(IllegalArgumentException.class, orderNotCreatedException.getClass());
        assertNull(registeredUser.getCurrentOrder());
    }


    @Given("another timeslot at Naga beginning at {int}h{int} on {int}-{int}-{int} but to many order already created on this timeslot")
    public void anotherTimeslotAtNagaBeginningAtHOnButToManyOrderAlreadyCreatedOnThisTimeslot(int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, 0);
        SubOrder orderToFillTimeSlot1 = Mockito.mock(SubOrder.class);
        SubOrder orderToFillTimeSlot2 = Mockito.mock(SubOrder.class);
        timeSlot.addOrder(orderToFillTimeSlot1);
        timeSlot.addOrder(orderToFillTimeSlot2);
        when(timeSlotRepository.findById(timeSlot.getId())).thenReturn(timeSlot);
        restaurantService.addTimeSlotToRestaurant(restaurant.getId(), timeSlot.getId());
    }
}

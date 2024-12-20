package team.k;

import commonlibrary.external.PaymentFailedException;
import commonlibrary.external.PaymentProcessor;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.IndividualOrder;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.Dish;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;

import commonlibrary.model.order.OrderBuilder;

import commonlibrary.model.restaurant.Restaurant;
import team.k.orderservice.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RegisteredUserPlacesAnOrderStepdefs {
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
    IndividualOrder order;

    @Mock
    Dish dish;

    @Mock
    Restaurant restaurant;

    @Mock
    PaymentProcessor paymentProcessor;

    Exception exception;

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


    @Given("an individual order is created by a registered user whose name is {string} and his role is {role}")
    public void anIndividualOrderIsCreatedByARegisteredUserWhoseNameIsAndHisRoleIsSTUDENT(String name, Role role) {
        registeredUser = spy(new RegisteredUser(name, role));
        when(registeredUserRepository.findAll()).thenReturn(List.of(registeredUser));
        when(registeredUserRepository.findById((long) registeredUser.getId())).thenReturn(Optional.ofNullable(registeredUser));
        when(restaurant.isAvailable(any())).thenReturn(true);
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(restaurantRepository.findById((long) restaurant.getId())).thenReturn(Optional.ofNullable(restaurant));
        Location loc = new Location.Builder().setAddress("Antibes").build();
        order = (IndividualOrder) new OrderBuilder().setId(1).setUserID(registeredUser.getId()).setRestaurantID(restaurant.getId()).setDeliveryLocation(loc).build();
        registeredUser.setCurrentOrder(order);
        order.addDish(dish);
        when(individualOrderRepository.findAll()).thenReturn(List.of(order));
        when(individualOrderRepository.findById((long) order.getId())).thenReturn(Optional.ofNullable(order));
    }

    @When("The user places the order at {int}:{int} on {int}-{int}-{int}")
    public void placeTheOrder(int hour, int minute, int day, int month, int year) {
        LocalDateTime placedTime = LocalDateTime.of(year, month, day, hour, minute);
        orderService.placeIndividualOrder(order.getId(), placedTime);
    }

    @Then("the status of the order is placed now")
    public void theStatusOfTheOrderIsNow() {
        assertEquals(OrderStatus.PLACED, order.getStatus());
    }


    @When("The user pays the order at {int}:{int} on {int}-{int}-{int}")
    public void theUserPaysTheOrder(int hour, int minute, int day, int month, int year) {
        LocalDateTime paymentTime = LocalDateTime.of(year, month, day, hour, minute);
        when(paymentProcessor.processPayment(anyDouble())).thenReturn(true);
        orderService.paySubOrder(registeredUser.getId(), order.getId(), paymentTime, paymentProcessor);
    }

    @Then("the order appears in the user's history")
    public void theOrderAppearsInTheUserSHistory() {
        assertEquals(registeredUser.getOrders().getFirst(), order);
        verify(paymentProcessor, times(1)).processPayment(anyDouble());
    }

    @When("The user pays the order and the payment fails at {int}:{int} on {int}-{int}-{int}")
    public void theUserPaysTheOrderAndThePaymentFails(int hour, int minute, int day, int month, int year) {
        when(paymentProcessor.processPayment(anyDouble())).thenReturn(false);
        try {
            orderService.paySubOrder(registeredUser.getId(), order.getId(), LocalDateTime.of(year, month, day, hour, minute), paymentProcessor);
        } catch (PaymentFailedException e) {
            exception = e;
        }
    }


    @Then("the order does not appears in the user's history")
    public void theOrderDoesNotAppearsInTheUserSHistory() {
        verify(registeredUser, never()).addOrderToHistory(order);
        assertEquals(PaymentFailedException.class, exception.getClass());
        verify(paymentProcessor, times(1)).processPayment(anyDouble());
    }
}

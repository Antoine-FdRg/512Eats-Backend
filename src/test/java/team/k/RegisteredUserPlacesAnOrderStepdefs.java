package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.enumerations.OrderStatus;
import team.k.enumerations.Role;

import team.k.external.PaymentFailedException;
import team.k.external.PaymentProcessor;
import team.k.order.OrderBuilder;
import team.k.order.SubOrder;

import team.k.repository.RegisteredUserRepository;
import team.k.repository.SubOrderRepository;
import team.k.restaurant.Restaurant;
import team.k.service.OrderService;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RegisteredUserPlacesAnOrderStepdefs {

    RegisteredUser registeredUser;
    SubOrder order;

    @Mock
    SubOrderRepository subOrderRepository;

    @Mock
    Dish dish;

    @Mock
    RegisteredUserRepository registeredUserRepository;

    @Mock
    Restaurant restaurant;

    @Mock
    PaymentProcessor paymentProcessor;

    Exception exception;

    @InjectMocks
    OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Given("an order is created by a registered user whose name is {string} and his role is {role}")
    public void anOrderIsCreatedByARegisteredUserWhoseNameIsAndHisRoleIsSTUDENT(String name, Role role) {
        registeredUser = spy(new RegisteredUser(name, role));
        when(registeredUserRepository.findById(registeredUser.getId())).thenReturn(registeredUser);
        when(restaurant.isAvailable(any())).thenReturn(true);
        order = new OrderBuilder().setUser(registeredUser).setRestaurant(restaurant).build();
        registeredUser.setCurrentOrder(order);
        order.addDish(dish);
        when(subOrderRepository.findById(order.getId())).thenReturn(order);
    }

    @When("The user places the order")
    public void placeTheOrder() {
        orderService.placeSubOrder(order.getId());
    }

    @Then("the status of the order is placed now")
    public void theStatusOfTheOrderIsNow() {
        assertEquals(OrderStatus.PLACED, order.getStatus());
    }


    @When("The user pays the order at {int}:{int} on {int}-{int}-{int}")
    public void theUserPaysTheOrder(int hour, int minute, int day, int month, int year) {
        LocalDateTime paymentTime = LocalDateTime.of(year, month, day, hour, minute);
        when(paymentProcessor.processPayment(anyDouble())).thenReturn(true);
        orderService.paySubOrder(registeredUser.getId(), order.getId(), paymentTime);
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
            orderService.paySubOrder(registeredUser.getId(), order.getId(), LocalDateTime.of(year, month, day, hour, minute));
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

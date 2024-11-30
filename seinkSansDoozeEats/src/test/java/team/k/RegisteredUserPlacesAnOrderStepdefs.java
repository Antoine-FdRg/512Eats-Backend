package team.k;

import commonlibrary.model.Location;
import commonlibrary.repository.GroupOrderRepository;
import commonlibrary.repository.LocationRepository;
import commonlibrary.repository.RestaurantRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;

import commonlibrary.model.payment.PaymentFailedException;
import commonlibrary.model.payment.PaymentProcessor;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;

import commonlibrary.repository.RegisteredUserRepository;
import commonlibrary.repository.SubOrderRepository;
import commonlibrary.model.restaurant.Restaurant;
import team.k.orderService.OrderService;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RegisteredUserPlacesAnOrderStepdefs {

    RegisteredUser registeredUser;
    SubOrder order;

    SubOrderRepository subOrderRepository;
    @Mock
    Dish dish;
    RegisteredUserRepository registeredUserRepository;
    @Mock
    Restaurant restaurant;
    @Mock
    PaymentProcessor paymentProcessor;
    GroupOrderRepository groupOrderRepository;
    LocationRepository locationRepository;
    RestaurantRepository restaurantRepository;

    Exception exception;
    OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registeredUserRepository = new RegisteredUserRepository();
        subOrderRepository = new SubOrderRepository();
        groupOrderRepository = new GroupOrderRepository();
        locationRepository = new LocationRepository();
        restaurantRepository = new RestaurantRepository();
        orderService = new OrderService(groupOrderRepository,
                locationRepository,
                subOrderRepository,
                restaurantRepository,
                registeredUserRepository,
                paymentProcessor);
    }


    @Given("an order is created by a registered user whose name is {string} and his role is {role}")
    public void anOrderIsCreatedByARegisteredUserWhoseNameIsAndHisRoleIsSTUDENT(String name, Role role) throws IOException, InterruptedException {
        registeredUser = spy(new RegisteredUser(name, role));
        registeredUserRepository.add(registeredUser);
        when(restaurant.isAvailable(any())).thenReturn(true);
        restaurantRepository.add(restaurant);
        Location mockLocation = mock(Location.class);
        order = new OrderBuilder().setUserID(registeredUser.getId())
                .setRestaurantID(restaurant.getId())
                .setDeliveryLocation(mockLocation) //ajout de la location pour créer une IndividualOrder
                .build();
        registeredUser.setCurrentOrder(order);
        order.addDish(dish);
        subOrderRepository.add(order);
    }

    @When("The user places the order at {int}:{int} on {int}-{int}-{int}")
    public void placeTheOrder(int hour, int minute, int day, int month, int year) throws IOException, InterruptedException {
        LocalDateTime placedTime = LocalDateTime.of(year, month, day, hour, minute);
        orderService.placeSubOrder(order.getId(), placedTime);
    }

    @Then("the status of the order is placed now")
    public void theStatusOfTheOrderIsNow() {
        assertEquals(OrderStatus.PLACED, order.getStatus());
    }


    @When("The user pays the order at {int}:{int} on {int}-{int}-{int}")
    public void theUserPaysTheOrder(int hour, int minute, int day, int month, int year) throws IOException, InterruptedException {
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
        } catch (PaymentFailedException | IOException | InterruptedException e) {
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

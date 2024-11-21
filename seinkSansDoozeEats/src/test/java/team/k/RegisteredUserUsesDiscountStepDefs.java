package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.enumerations.Role;
import commonlibrary.model.payment.PaymentProcessor;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.repository.RegisteredUserRepository;
import commonlibrary.repository.SubOrderRepository;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.discount.FreeDishAfterXOrders;
import commonlibrary.model.restaurant.discount.RoleDiscount;
import commonlibrary.model.restaurant.discount.UnconditionalDiscount;
import team.k.orderService.OrderService;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class RegisteredUserUsesDiscountStepDefs {

    RegisteredUser registeredUser;
    SubOrder order;
    FreeDishAfterXOrders freeDiscount;
    UnconditionalDiscount unconditionalDiscount;
    RoleDiscount roleDiscount;
    @Mock
    SubOrderRepository subOrderRepository;

    @Mock
    Dish dish;
    @Mock
    Dish dishCheapest;

    @Mock
    SubOrder previousOrder;

    @Mock
    PaymentProcessor paymentProcessor;

    @Mock
    RegisteredUserRepository registeredUserRepository;

    @Mock
    Restaurant restaurant;

    @InjectMocks
    OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Given("an order containing {int} dishes is created by a registered user whose name is {string} and his role is {role}")
    public void anOrderContainingDishesIsCreatedByARegisteredUserWhoseNameIsAndHisRoleIsSTUDENT(int number, String name, Role role) {
        registeredUser = spy(new RegisteredUser(name, role));
        when(registeredUserRepository.findById(registeredUser.getId())).thenReturn(registeredUser);
        when(previousOrder.getRestaurant()).thenReturn(restaurant);
        for (int i = 0; i < 10; i++) {
            registeredUser.addOrderToHistory(previousOrder);
        }
        when(restaurant.isAvailable(any())).thenReturn(true);
        order = new OrderBuilder().setUser(registeredUser).setRestaurant(restaurant).build();
        registeredUser.setCurrentOrder(order);
        when(subOrderRepository.findById(order.getId())).thenReturn(order);
        when(dish.getPrice()).thenReturn(10.0);
        when(dishCheapest.getPrice()).thenReturn(5.0);
        for (int i = 0; i < number - 1; i++) {
            order.addDish(dish);
        }
        order.addDish(dishCheapest);
    }

    @And("the restaurant have freeDishAfterXOrders discount")
    public void theRestaurantHaveAUnconditionalDiscount() {
        freeDiscount = new FreeDishAfterXOrders(restaurant, 10);
        when(restaurant.getDiscountStrategy()).thenReturn(freeDiscount);
    }

    @When("The user pays the order with the discount")
    public void theUserPaysTheOrderWithTheDiscount() {
        when(paymentProcessor.processPayment(anyDouble())).thenReturn(true);
        orderService.paySubOrder(registeredUser.getId(), order.getId(), LocalDateTime.now());
    }

    @Then("the cheapest dish is free in the order")
    public void theCheapestDishIsFreeInTheOrder() {
        assertEquals(90.0, order.getPrice(), 0);
        assertEquals(90.0, order.getPayment().getAmount(), 0);
    }


    @And("the restaurant have unconditional discount")
    public void theRestaurantHaveUnconditionalDiscount() {
        unconditionalDiscount = new UnconditionalDiscount(restaurant, 0.25);
        when(restaurant.getDiscountStrategy()).thenReturn(unconditionalDiscount);
    }

    @Then("the price is lower than before")
    public void thePriceIsLowerThanBefore() {
        assertEquals(71.25, order.getPrice(), 0);
        assertEquals(71.25, order.getPayment().getAmount(), 0);
    }

    @And("the restaurant have Role discount")
    public void theRestaurantHaveRoleDiscount() {
        roleDiscount = new RoleDiscount(restaurant, 0.2, Role.STUDENT);
        when(restaurant.getDiscountStrategy()).thenReturn(roleDiscount);
    }

    @Then("the price is lower than the previous one")
    public void thePriceIsLowerThanThePreviousOne() {
        assertEquals(76, order.getPrice(), 0);
        assertEquals(76, order.getPayment().getAmount(), 0);
    }


    @Then("the price does not change")
    public void thePriceDoesNotChange() {
        assertEquals(95.0, order.getPrice(), 0);
    }
}

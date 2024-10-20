package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.enumerations.Role;
import team.k.external.PaymentProcessor;
import team.k.order.OrderBuilder;
import team.k.order.SubOrder;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.SubOrderRepository;
import team.k.restaurant.Restaurant;
import team.k.restaurant.discount.FreeDishAfterXOrders;
import team.k.service.OrderService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class RegisteredUserUsesDiscountStepDefs {

    RegisteredUser registeredUser;
    SubOrder order;

    @Mock
    SubOrderRepository subOrderRepository;

    @Mock
    Dish dish;
    @Mock
    Dish dishCheapest;

    @Mock
    SubOrder previousOrder;

    @Mock
    RegisteredUserRepository registeredUserRepository;

    @Mock
    Restaurant restaurant;

    @Mock
    PaymentProcessor paymentProcessor;

    FreeDishAfterXOrders freeDiscount;


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
        for (int i=0; i<9; i++){
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

    @And("the restaurant have a unconditional discount")
    public void theRestaurantHaveAUnconditionalDiscount() {
        freeDiscount = new FreeDishAfterXOrders(restaurant, 10);
        when(restaurant.getDiscountStrategy()).thenReturn(freeDiscount);
    }

    @When("The user places the order with the discount")
    public void theUserPlacesTheOrderWithTheDiscount() {
        orderService.placeSubOrder(order.getId());
    }

    @Then("the cheapest dish is free in the order")
    public void theCheapestDishIsFreeInTheOrder() {
        assertEquals(90.0, order.getPrice(), 0);
    }
}

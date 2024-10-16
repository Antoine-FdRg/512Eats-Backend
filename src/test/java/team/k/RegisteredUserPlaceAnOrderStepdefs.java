package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.enumerations.OrderStatus;
import team.k.enumerations.Role;

import team.k.order.OrderBuilder;
import team.k.order.SubOrder;

import team.k.repository.RegisteredUserRepository;
import team.k.repository.SubOrderRepository;
import team.k.service.OrderService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class RegisteredUserPlaceAnOrderStepdefs {

    RegisteredUser registeredUser;
    SubOrder order;

    @Mock
    SubOrderRepository subOrderRepository;

    @Mock
    RegisteredUserRepository registeredUserRepository;


    @InjectMocks
    OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Given("an order is created by a registered user whose name is {string} and his role is {role}")
    public void anOrderIsCreatedByARegisteredUserWhoseNameIsAndHisRoleIsSTUDENT(String name, Role role) {
        registeredUser = new RegisteredUser(name, role);
        when(registeredUserRepository.findById(registeredUser.getId())).thenReturn(registeredUser);
        order = new OrderBuilder().setUser(registeredUser).build();
        when(subOrderRepository.findById(order.getId())).thenReturn(order);
    }

    @When("The user places the order")
    public void placeTheOrder() {
        orderService.placeSubOrder(order.getId());
    }

    @Then("the status of the order is placed now")
    public void theStatusOfTheOrderIsNow() {
        assertEquals(order.getStatus(), OrderStatus.PLACED);
    }


}

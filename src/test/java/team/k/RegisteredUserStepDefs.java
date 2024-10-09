package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.LocationRepository;
import team.k.service.OrderService;

import static org.junit.Assert.assertEquals;

public class RegisteredUserStepDefs {
    @Mock
    LocationRepository locationRepository;
    @InjectMocks
    OrderService orderService = new OrderService();
    GroupOrder groupOrder;
    Location location;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a delivery location")
    public void aDeliveryLocation() {
        location = new Location(1, 123, "123 Main St", "Springfield");
        Mockito.when(locationRepository.getLocationById(1)).thenReturn(location);
    }


    @When("the user creates a group order by initializing the delivery location")
    public void theUserCreatesAGroupOrderByInitializingTheDeliveryLocation() {
        orderService.createGroupOrder(1);

    }

    @Then("the group order is created and the delivery location is initialized")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationIsInitialized() {
        groupOrder = orderService.getGroupOrderRepository().getGroupOrders().getFirst();
        assertEquals(1, groupOrder.getDeliveryLocation().getId());
        assertEquals(location, groupOrder.getDeliveryLocation());
    }


}

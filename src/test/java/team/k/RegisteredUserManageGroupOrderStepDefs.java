package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.service.GroupOrderService;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class RegisteredUserManageGroupOrderStepDefs {
    LocationRepository locationRepository;
    GroupOrderRepository groupOrderRepository;
    @Mock
    GroupOrderService groupOrderService;
    int codeToShare;
    Location location;
    Exception exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        locationRepository = Mockito.mock(LocationRepository.class);
        groupOrderRepository = new GroupOrderRepository();
        groupOrderService = new GroupOrderService(
                groupOrderRepository,
                locationRepository);
    }

    @Given("a delivery location")
    public void aDeliveryLocation() {
        location = new Location.Builder()
                .setNumber("123")
                .setAddress("123 Main St")
                .setCity("Springfield")
                .build();
        when(locationRepository.findLocationById(location.getId())).thenReturn(location);
    }


    @When("the user creates a group order with the delivery location")
    public void theUserCreatesAGroupOrderWithTheDeliveryLocation() {
        codeToShare = groupOrderService.createGroupOrder(location.getId());
    }

    @Then("the group order is created and the delivery location is initialized")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationIsInitialized() {
        GroupOrder groupOrder = groupOrderService.findGroupOrderById(codeToShare);
        assertEquals(location.getId(), groupOrder.getDeliveryLocation().getId());
        assertEquals(location, groupOrder.getDeliveryLocation());
    }


    @When("the user creates a group order without the delivery location")
    public void theUserCreatesAGroupOrderWithoutTheDeliveryLocation() {
        try {
            codeToShare = groupOrderService.createGroupOrder(-1);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the group order is not created")
    public void theGroupOrderIsNotCreated() {
        assertNull(groupOrderService.findGroupOrderById(codeToShare));
        assertNotNull(exception);
        assertEquals(NoSuchElementException.class, exception.getClass());
    }
}

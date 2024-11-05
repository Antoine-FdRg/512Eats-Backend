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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @When("the user creates a group order with the delivery location for the {string} at {string} on {string} at {string}")
    public void theUserCreatesAGroupOrderWithTheDeliveryLocationForTheAtOnAt(String orderDate, String orderTime, String currentDate, String currentTime) {
        LocalDateTime deliveryDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );
        LocalDateTime currentDateTime = LocalDateTime.of(
                LocalDate.parse(currentDate),
                LocalTime.parse(currentTime)
        );
        codeToShare = groupOrderService.createGroupOrder(location.getId(), deliveryDateTime, currentDateTime);
    }




    @Then("the group order is created and the delivery location is initialized and the delivery date time is the {string} at {string}")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationIsInitializedAndTheDeliveryDateTimeIsTheAt(String orderDate, String orderTime) {
        LocalDateTime deliveryDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );

        GroupOrder groupOrder = groupOrderService.findGroupOrderById(codeToShare);
        assertEquals(location.getId(), groupOrder.getDeliveryLocation().getId());
        assertEquals(location, groupOrder.getDeliveryLocation());
        assertEquals(deliveryDateTime, groupOrder.getDeliveryDateTime());
    }
    @Then("the group order is created and the delivery location and delivery date time are initialized")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationAndDeliveryDateTimeAreInitialized() {
        GroupOrder groupOrder = groupOrderService.findGroupOrderById(codeToShare);
        assertEquals(location.getId(), groupOrder.getDeliveryLocation().getId());
        assertEquals(location, groupOrder.getDeliveryLocation());
    }

    @When("the user creates a group order without the delivery location for the {string} at {string} on {string} at {string}")
    public void theUserCreatesAGroupOrderWithoutTheDeliveryLocationForTheAtOnAt(String orderDate, String orderTime, String currentDate, String currentTime) {
        LocalDateTime deliveryDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );
        LocalDateTime currentDateTime = LocalDateTime.of(
                LocalDate.parse(currentDate),
                LocalTime.parse(currentTime)
        );
        try {
            codeToShare = groupOrderService.createGroupOrder(-1, deliveryDateTime, currentDateTime);
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

    @When("the user creates a group order with the delivery location on {string} at {string}")
    public void theUserCreatesAGroupOrderWithTheDeliveryLocationOnAt(String currentDate, String currentTime) {
        LocalDateTime currentDateTime = LocalDateTime.of(
                LocalDate.parse(currentDate),
                LocalTime.parse(currentTime)
        );
        try {
            codeToShare = groupOrderService.createGroupOrder(location.getId(), null, currentDateTime);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the group order is created and the delivery location is initialized but the delivery date time is not")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationIsInitializedButTheDeliveryDateTimeIsNot() {
        GroupOrder groupOrder = groupOrderService.findGroupOrderById(codeToShare);
        assertEquals(location.getId(), groupOrder.getDeliveryLocation().getId());
        assertEquals(location, groupOrder.getDeliveryLocation());
        assertNull(groupOrder.getDeliveryDateTime());
    }
}

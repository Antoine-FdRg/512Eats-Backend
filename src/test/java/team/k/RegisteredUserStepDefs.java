package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.service.OrderService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class RegisteredUserStepDefs {
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    LocationRepository locationRepository;
    @Mock
    SubOrderRepository subOrderRepository;
    GroupOrderRepository groupOrderRepository = new GroupOrderRepository();
    @Mock
    RegisteredUserRepository registeredUserRepository;
    @InjectMocks
    OrderService orderService;
    GroupOrder groupOrder;
    Location location;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(
                groupOrderRepository,
                locationRepository,
                subOrderRepository,
                restaurantRepository,
                registeredUserRepository);
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


    @When("the user creates a group order by initializing the delivery location")
    public void theUserCreatesAGroupOrderByInitializingTheDeliveryLocation() {
        orderService.createGroupOrder(location.getId());

    }

    @Then("the group order is created and the delivery location is initialized")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationIsInitialized() {
        groupOrder = orderService.getGroupOrderRepository().getGroupOrders().getFirst();
        assertEquals(location.getId(), groupOrder.getDeliveryLocation().getId());
        assertEquals(location, groupOrder.getDeliveryLocation());
    }


}

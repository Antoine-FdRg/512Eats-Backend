package team.k;

import commonlibrary.model.RegisteredUser;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.mockito.MockitoAnnotations;
import commonlibrary.model.Location;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.service.GroupOrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class RegisteredUserManageGroupOrderStepDefs {
    LocationRepository locationRepository;
    GroupOrderRepository groupOrderRepository;
    GroupOrderService groupOrderService;
    RegisteredUserRepository registeredUserRepository;
    int codeToShare;
    Location location;
    Exception exception;

    SubOrder PaidSuborder;

    SubOrder UnpaidSuborder;

    GroupOrder groupOrder;
    RegisteredUser user1;
    RegisteredUser user2;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        locationRepository = new LocationRepository();
        groupOrderRepository = new GroupOrderRepository();

        groupOrderService = new GroupOrderService(
                groupOrderRepository,
                locationRepository,
                registeredUserRepository);
    }

    @Given("a delivery location")
    public void aDeliveryLocation() {
        location = new Location.Builder()
                .setNumber("123")
                .setAddress("123 Main St")
                .setCity("Springfield")
                .build();
        locationRepository.add(location);
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
        assertEquals(location.getId(), groupOrder.getDeliveryLocationID());
        assertEquals(location, groupOrder.getDeliveryLocationID());
        assertEquals(deliveryDateTime, groupOrder.getDeliveryDateTime());
    }
    @Then("the group order is created and the delivery location and delivery date time are initialized")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationAndDeliveryDateTimeAreInitialized() {
        GroupOrder groupOrder = groupOrderService.findGroupOrderById(codeToShare);
        assertEquals(location.getId(), groupOrder.getDeliveryLocationID());
        assertEquals(location, groupOrder.getDeliveryLocationID());
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
        assertEquals(location.getId(), groupOrder.getDeliveryLocationID());
        assertEquals(location, groupOrder.getDeliveryLocationID());
        assertNull(groupOrder.getDeliveryDateTime());
    }

    @Given("a group order created without a delivery datetime")
    public void aGroupOrderCreatedWithoutADeliveryDatetime() {
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocationID(location.getId())
                .build();
        codeToShare = groupOrder.getId();
        groupOrderRepository.add(groupOrder);
    }

    @When("the user modifies the delivery datetime to set {string} at {string} on {string} at {string}")
    public void theUserModifiesTheDeliveryDatetimeToSetAtOnAt(String orderDate, String orderTime, String currentDate, String currentTime) {
        LocalDateTime deliveryDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );
        LocalDateTime currentDateTime = LocalDateTime.of(
                LocalDate.parse(currentDate),
                LocalTime.parse(currentTime)
        );
        try {
            groupOrderService.modifyGroupOrderDeliveryDateTime(codeToShare, deliveryDateTime, currentDateTime);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the group order delivery datetime is {string} at {string}")
    public void theGroupOrderIsModifiedAndTheDeliveryDateTimeIsAt(String orderDate, String orderTime) {
        LocalDateTime deliveryDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );
        GroupOrder groupOrder = groupOrderRepository.findGroupOrderById(codeToShare);
        assertEquals(deliveryDateTime,groupOrder.getDeliveryDateTime());
    }

    @Then("the group order is not modified and the delivery datetime is still null")
    public void theGroupOrderIsNotModifiedAndTheDeliveryDatetimeIsStillNull() {
        GroupOrder groupOrder = groupOrderRepository.findGroupOrderById(codeToShare);
        assertNull(groupOrder.getDeliveryDateTime());
    }

    @Given("a group order created with {string} at {string} as delivery datetime")
    public void aGroupOrderCreatedWithAtAsDeliveryDatetime(String orderDate, String orderTime) {
        LocalDateTime deliveryDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );
        groupOrder = new GroupOrder.Builder()
                .withDeliveryLocationID(location.getId())
                .withDate(deliveryDateTime)
                .build();
        codeToShare = groupOrder.getId();
        groupOrderRepository.add(groupOrder);
    }


    @And("a suborder of the user {string} with the status {status} added in the group order")
    public void aSuborderWithTheStatusAddedInTheGroupOrder(String name, OrderStatus status) {
        user1 = new RegisteredUser(name, Role.STUDENT);
        PaidSuborder = new OrderBuilder().setUserID(user1.getId()).build();
        PaidSuborder.setStatus(status);
        groupOrder.addSubOrder(PaidSuborder);
    }

    @And("a suborder of the user {string} not already placed with the status {status} added in the group order")
    public void aSuborderNotAlreadyPlacedWithTheStatusCREATEDAddedInTheGroupOrder(String name, OrderStatus status) {
        user2 = new RegisteredUser(name, Role.STUDENT);
        UnpaidSuborder = new OrderBuilder().setUserID(user2.getId()).build();
        UnpaidSuborder.setStatus(status);
        groupOrder.addSubOrder(UnpaidSuborder);

    }

    @When("a group is placed at {string} at {string}")
    public void aGroupIsPlacedAtAt(String orderDate, String orderTime) {
        LocalDateTime placedDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );
        groupOrderService.place(groupOrder.getId(), placedDateTime);
    }


    @Then("one suborder is placed and the other one is canceled")
    public void oneSuborderIsPlacedAndTheOtherOneIsCanceled() {
        assertEquals(OrderStatus.PLACED, groupOrder.getStatus());
        assertEquals(OrderStatus.PLACED, PaidSuborder.getStatus());
        assertEquals(OrderStatus.CANCELED, UnpaidSuborder.getStatus());
    }


    @Then("the suborder and the groupOrder are canceled")
    public void theSuborderAndTheGroupOrderIsCanceled() {
        assertEquals(OrderStatus.CANCELED, UnpaidSuborder.getStatus());
        assertEquals(OrderStatus.CANCELED, groupOrder.getStatus());
    }

    @Then("the suborder and the groupOrder are placed")
    public void theSuborderAndTheGroupOrderIsPlaced() {
        assertEquals(OrderStatus.PLACED, groupOrder.getStatus());
        assertEquals(OrderStatus.PLACED, PaidSuborder.getStatus());
    }


}
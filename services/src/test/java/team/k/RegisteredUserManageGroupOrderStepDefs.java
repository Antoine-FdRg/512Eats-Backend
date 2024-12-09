package team.k;

import commonlibrary.model.RegisteredUser;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
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
import commonlibrary.model.order.SubOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.service.GroupOrderService;
import team.k.service.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@
public class RegisteredUserManageGroupOrderStepDefs {
    Exception exception;

    Location location;
    GroupOrder groupOrder;
    int codeToShare;

    RegisteredUser user1;
    SubOrder paidSuborder;
    private Restaurant restaurantTom;

    RegisteredUser user2;
    SubOrder unpaidSuborder;
    private Restaurant restaurantLeo;


    GroupOrderService groupOrderService;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a delivery location")
    public void aDeliveryLocation() {
        location = new Location.Builder()
                .setNumber("123")
                .setAddress("123 Main St")
                .setCity("Springfield")
                .build();
        LocationRepository.add(location);
    }


    @Given("two restaurants available at {string} and at {string} on {string}")
    public void twoRestaurantsAvailableFromToOn(String timeSlotStart1, String timeSlotStart2, String timeSlotDate) {
        LocalDateTime timeSlotStartTime1 = LocalDateTime.of(
                LocalDate.parse(timeSlotDate),
                LocalTime.parse(timeSlotStart1)
        );
        LocalDateTime timeSlotStartTime2 = LocalDateTime.of(
                LocalDate.parse(timeSlotDate),
                LocalTime.parse(timeSlotStart2)
        );

        restaurantTom = new Restaurant.Builder()
                .setName("Chez Joe")
                .setDescription("Le restaurant préféré de Tom")
                .setOpen(LocalTime.of(11, 0))
                .setClose(LocalTime.of(13, 0))
                .setAverageOrderPreparationTime(10)
                .build();
        restaurantTom.addTimeSlot(new TimeSlot(timeSlotStartTime1, restaurantTom, 4));
        restaurantTom.addTimeSlot(new TimeSlot(timeSlotStartTime2, restaurantTom, 4));
        RestaurantRepository.add(restaurantTom);

        restaurantLeo = new Restaurant.Builder()
                .setName("Chez James")
                .setDescription("Le restaurant préféré de Leo")
                .setOpen(LocalTime.of(11, 0))
                .setClose(LocalTime.of(13, 0))
                .setAverageOrderPreparationTime(10)
                .build();
        restaurantLeo.addTimeSlot(new TimeSlot(timeSlotStartTime1, restaurantLeo, 4));
        restaurantLeo.addTimeSlot(new TimeSlot(timeSlotStartTime2, restaurantLeo, 4));
        RestaurantRepository.add(restaurantLeo);
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
        assertEquals(deliveryDateTime, groupOrder.getDeliveryDateTime());
    }

    @Then("the group order is created and the delivery location and delivery date time are initialized")
    public void theGroupOrderIsCreatedAndTheDeliveryLocationAndDeliveryDateTimeAreInitialized() {
        GroupOrder groupOrder = groupOrderService.findGroupOrderById(codeToShare);
        assertEquals(location.getId(), groupOrder.getDeliveryLocationID());
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
        assertNull(groupOrder.getDeliveryDateTime());
    }

    @Given("a group order created without a delivery datetime")
    public void aGroupOrderCreatedWithoutADeliveryDatetime() {
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocationID(location.getId())
                .build();
        codeToShare = groupOrder.getId();
        GroupOrderRepository.add(groupOrder);
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
        GroupOrder groupOrder = GroupOrderRepository.findGroupOrderById(codeToShare);
        assertEquals(deliveryDateTime,groupOrder.getDeliveryDateTime());
    }

    @Then("the group order is not modified and the delivery datetime is still null")
    public void theGroupOrderIsNotModifiedAndTheDeliveryDatetimeIsStillNull() {
        GroupOrder groupOrder = GroupOrderRepository.findGroupOrderById(codeToShare);
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
        GroupOrderRepository.add(groupOrder);
    }


    @And("a suborder of the user {string} with the status {status} added in the group order")
    public void aSuborderWithTheStatusAddedInTheGroupOrder(String name, OrderStatus status) {
        user1 = new RegisteredUser(name, Role.STUDENT);
        RegisteredUserRepository.add(user1);
        int paidSuborderID = OrderService.createSuborder(user1.getId(), restaurantTom.getId(), codeToShare);
        paidSuborder = SubOrderRepository.findById(paidSuborderID);
        paidSuborder.setStatus(status);
    }

    @And("a suborder of the user {string} not already placed with the status {status} added in the group order")
    public void aSuborderNotAlreadyPlacedWithTheStatusCREATEDAddedInTheGroupOrder(String name, OrderStatus status) {
        user2 = new RegisteredUser(name, Role.STUDENT);
        RegisteredUserRepository.add(user2);
        int unpaidSuborderID = OrderService.createSuborder(user2.getId(), restaurantLeo.getId(), codeToShare);
        unpaidSuborder = SubOrderRepository.findById(unpaidSuborderID);
        unpaidSuborder.setStatus(status);
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
        assertEquals(OrderStatus.PLACED, paidSuborder.getStatus());
        assertEquals(OrderStatus.CANCELED, unpaidSuborder.getStatus());
    }


    @Then("the suborder and the groupOrder are canceled")
    public void theSuborderAndTheGroupOrderIsCanceled() {
        assertEquals(OrderStatus.CANCELED, unpaidSuborder.getStatus());
        assertEquals(OrderStatus.CANCELED, groupOrder.getStatus());
    }

    @Then("the suborder and the groupOrder are placed")
    public void theSuborderAndTheGroupOrderIsPlaced() {
        assertEquals(OrderStatus.PLACED, groupOrder.getStatus());
        assertEquals(OrderStatus.PLACED, paidSuborder.getStatus());
    }


    @Then("the suborder is in the timeslot stating at {string} the {string} of the restaurant")
    public void theSuborderIsInTheTimeslotStatingAtTheOfTheRestaurant(String orderTime, String orderDate) {
        LocalDateTime timeSlotStartTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime)
        );
        TimeSlot ts =  restaurantTom.getCurrentTimeSlot(timeSlotStartTime);
        assertNotNull(ts);
        assertTrue(ts.getOrders().contains(paidSuborder));
    }

}

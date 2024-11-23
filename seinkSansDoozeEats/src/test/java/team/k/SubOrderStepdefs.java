package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.repository.LocationRepository;
import commonlibrary.repository.RegisteredUserRepository;
import commonlibrary.repository.RestaurantRepository;
import commonlibrary.repository.TimeSlotRepository;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.restaurantService.RestaurantService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubOrderStepdefs {
    RegisteredUser registeredUser;
    Restaurant restaurant;
    Location deliveryLocation;
    GroupOrder groupOrder;
    SubOrder subOrder;

    RestaurantService restaurantService;

    RegisteredUserRepository registeredUserRepository;
    RestaurantRepository restaurantRepository;
    TimeSlotRepository timeSlotRepository;
    LocationRepository locationRepository;

    @Before
    public void setUp() {
        registeredUserRepository = new RegisteredUserRepository();
        restaurantRepository = new RestaurantRepository();
        timeSlotRepository = new TimeSlotRepository();
        locationRepository = new LocationRepository();
        restaurantService = new RestaurantService(restaurantRepository, timeSlotRepository);
    }

    @Given("a registeredUser called {string} with the role {role}")
    public void aRegisteredUserNamedWithTheRole(String name, Role role) {
        registeredUser = new RegisteredUser(name, role);
        registeredUserRepository.add(registeredUser);
    }

    @And("a restaurant called {string} open from {int}:{int} to {int}:{int} with an average order preparation time of {int} minutes")
    public void aRestaurantNamedOpenFromTo(String name, int openHours, int openMinutes, int closeHours, int closeMinutes, int averageOrderPreparationTime) {
        LocalTime openTime = LocalTime.of(openHours, openMinutes);
        LocalTime closeTime = LocalTime.of(closeHours, closeMinutes);
        restaurant = new Restaurant.Builder()
                .setName(name)
                .setOpen(openTime)
                .setClose(closeTime)
                .setAverageOrderPreparationTime(averageOrderPreparationTime)
                .build();
        restaurantRepository.add(restaurant);
    }

    @And("with a productionCapacity of {int} on the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void withAProductionCapacityOfForTheTimeslotAtOn(int productionCapacity, int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, productionCapacity);
        timeSlotRepository.add(timeSlot);
        restaurantService.addTimeSlotToRestaurant(restaurant.getId(), timeSlot.getId());
    }

    @And("the delivery location {string}, {string} in {string}")
    public void aDeliveryLocationWithTheNumberTheStreetAndTheCity(String streetNumber, String street, String city) {
        deliveryLocation = new Location.Builder()
                .setNumber(String.valueOf(streetNumber))
                .setAddress(street)
                .setCity(city)
                .build();
        locationRepository.add(deliveryLocation);
    }

    @And("a group order created for {string} the {string}")
    public void aGroupOrderCreatedForThe(String orderTime, String orderDate) {
        LocalDateTime deliveryDateTime = LocalDateTime.of(
                LocalDate.parse(orderDate),
                LocalTime.parse(orderTime));
        groupOrder = new GroupOrder.Builder()
                .withDeliveryLocation(deliveryLocation)
                .withDate(deliveryDateTime)
                .build();
    }

    @And("a suborder created in the group order for the restaurant {string}")
    public void aSuborderCreatedInTheGroupOrderForTheRestaurantNaga(String restaurantName) {
        restaurantService.getRestaurantByName(restaurantName);
        subOrder = new OrderBuilder()
                .setGroupOrder(groupOrder)
                .setRestaurant(restaurant)
                .setUser(registeredUser)
                .setDeliveryTime(groupOrder.getDeliveryDateTime())
                .build();
        registeredUser.setCurrentOrder(subOrder);
        groupOrder.addSubOrder(subOrder);
    }

    @When("the registeredUser pays the suborder at {string} the {string}")
    public void theRegisteredUserPaysTheSuborder(String hour, String day) {
        LocalDateTime paymentTime = LocalDateTime.of(
                LocalDate.parse(day),
                LocalTime.parse(hour));
        registeredUser.getCurrentOrder().pay(paymentTime);
    }

    @Then("the subOrder has {status} status in the groupOrder")
    public void theSubOrderHasPAIDStatusInTheGroupOrder(OrderStatus orderStatus) {
        Optional<SubOrder> subOrderOptional = groupOrder.getSubOrders().stream().findFirst();
        assertTrue(subOrderOptional.isPresent());
        assertEquals(orderStatus,subOrderOptional.get().getStatus());
    }
}
package team.k;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.SubOrderJPARepository;
import commonlibrary.repository.TimeSlotJPARepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.orderservice.OrderService;
import team.k.restaurantservice.RestaurantService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndividualOrderStepdefs {

    @Autowired
    private TimeSlotJPARepository timeSlotRepository;
    @Autowired
    private RestaurantJPARepository restaurantRepository;
    @Autowired
    private LocationJPARepository locationRepository;
    @Autowired
    private SubOrderJPARepository subOrderRepository;
    @Autowired
    private RegisteredUserJPARepository registeredUserRepository;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RestaurantService restaurantService;

    int restaurantID;
    int registeredUserID;
    int deliveryLocationID;

    private IllegalArgumentException orderNotCreatedException;
    private List<LocalDateTime> effectivePossibleDeliveryTimes;

    @Given("the registeredUser named {string} with the role {role}")
    @Transactional
    public void aRegisteredUserNamedWithTheRole(String name, Role role) {
        initializeUser(name, role);
    }

    @Transactional
    protected void initializeUser(String userName, Role student) {
        RegisteredUser user = new RegisteredUser(userName, student);
        registeredUserRepository.save(user);
        registeredUserID = user.getId();
    }

    @Transactional
    @And("the restaurant named {string} open from {int}:{int} to {int}:{int} with an average order preparation time of {int} minutes")
    public void aRestaurantNamedOpenFromTo(String name, int openHours, int openMinutes, int closeHours, int closeMinutes, int averageOrderPreparationTime) {
        LocalTime openTime = LocalTime.of(openHours, openMinutes);
        LocalTime closeTime = LocalTime.of(closeHours, closeMinutes);
        restaurantID = createAndPersistRestaurant(name, averageOrderPreparationTime, openTime, closeTime);
    }

    @Transactional
    protected int createAndPersistRestaurant(String name, int averageOrderPreparationTime, LocalTime openTime, LocalTime closeTime) {
        Restaurant restaurant = new Restaurant.Builder()
                .setName(name)
                .setOpen(openTime)
                .setClose(closeTime)
                .setAverageOrderPreparationTime(averageOrderPreparationTime)
                .build();
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    @Transactional
    @And("with the productionCapacity {int} for the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void withAProductionCapacityOfForTheTimeslotAtOn(int productionCapacity, int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        Restaurant restaurant = restaurantRepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, productionCapacity);
        timeSlotRepository.save(timeSlot);
        restaurantService.addTimeSlotToRestaurant(restaurant.getId(), timeSlot.getId());
    }

    @Transactional
    @And("the delivery location with the number {string}, the street {string} and the city {string}")
    public void aDeliveryLocationWithTheNumberTheStreetAndTheCity(String streetNumber, String street, String city) {
        Location deliveryLocation = new Location.Builder()
                .setNumber(String.valueOf(streetNumber))
                .setAddress(street)
                .setCity(city)
                .build();
        locationRepository.save(deliveryLocation);
    }

    @When("a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for {int}h{int} on {int}-{int}-{int} the current date being {int}-{int}-{int} {int}:{int}")
    public void aRegisteredUserCreatesAnOrderForTheRestaurantNagaWithTheDeliveryPlaceCreatedForHOnTheCurrentDateBeing(int deliveryTimeHours, int deliveryTimeMinutes, int deliveryTimeDay, int deliveryTimeMonth, int deliveryTimeYear, int currentDay, int currentMonth, int currentYear, int currentHours, int currentMinutes) {
        LocalDateTime deliveryTime = LocalDateTime.of(deliveryTimeYear, deliveryTimeMonth, deliveryTimeDay, deliveryTimeHours, deliveryTimeMinutes);
        LocalDateTime now = LocalDateTime.of(currentYear, currentMonth, currentDay, currentHours, currentMinutes);
        try {
            orderService.createIndividualOrder(registeredUserID, restaurantID, deliveryLocationID, deliveryTime, now);
        } catch (IllegalArgumentException e) {
            orderNotCreatedException = e;
        }
    }

    @Then("the registeredUser should have his currentOrder with the status CREATED")
    public void theRegisteredUserShouldHaveACurrentOrderWithTheStatusCREATED() {
        RegisteredUser registeredUser = registeredUserRepository.findById((long) registeredUserID).orElseThrow(NoSuchElementException::new);
        assertNotNull(registeredUser.getCurrentOrder());
        assertEquals(OrderStatus.CREATED, registeredUser.getCurrentOrder().getStatus());
    }

    @And("the registeredUser should have his currentOrder with no dishes")
    public void theRegisteredUserShouldHaveHisCurrentOrderWithNoDished() {
        RegisteredUser registeredUser = registeredUserRepository.findById((long) registeredUserID).orElseThrow(NoSuchElementException::new);
        assertEquals(0, registeredUser.getCurrentOrder().getDishes().size());
    }

    @And("the restaurant should have {int} order with the status CREATED")
    public void theRestaurantShouldHaveAnOrderWithTheStatusCREATED(int numberOfOrders) {
        Restaurant restaurant = restaurantRepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        RegisteredUser registeredUser = registeredUserRepository.findById((long) registeredUserID).orElseThrow(NoSuchElementException::new);
        TimeSlot timeSlot = restaurant.getPreviousTimeSlot(registeredUser.getCurrentOrder().getDeliveryDate());
        int nbOfOrderCreated = timeSlot.getNumberOfCreatedOrders();
        assertEquals(numberOfOrders, nbOfOrderCreated);
    }

    @And("the order should have been added to the suborder repository")
    public void theOrderShouldHaveBeenAddedToTheSuborderRepository() {
        RegisteredUser registeredUser = registeredUserRepository.findById((long) registeredUserID).orElseThrow(NoSuchElementException::new);
        assertNotNull(subOrderRepository.findById((long) registeredUser.getCurrentOrder().getId()));
    }

    @When("a registeredUser creates an order for the restaurant Naga with the deliveryPlace created but without delivery date the current date being {int}-{int}-{int} {int}:{int}")
    public void aRegisteredUserCreatesAnOrderForTheRestaurantNagaWithTheDeliveryPlaceCreatedButWithoutDeliveryDateTheCurrentDateBeing(int currentDay, int currentMonth, int currentYear, int currentHours, int currentMinutes) {
        LocalDateTime now = LocalDateTime.of(currentYear, currentMonth, currentDay, currentHours, currentMinutes);
        try {
            orderService.createIndividualOrder(registeredUserID, restaurantID, deliveryLocationID, null, now);
        } catch (IllegalArgumentException e) {
            orderNotCreatedException = e;
        }
    }

    @Then("the registeredUser should not have any currentOrder")
    public void theRegisteredUserShouldNotHaveAnyCurrentOrder() {
        assertEquals(IllegalArgumentException.class, orderNotCreatedException.getClass());
        RegisteredUser registeredUser = registeredUserRepository.findById((long) registeredUserID).orElseThrow(NoSuchElementException::new);
        assertNull(registeredUser.getCurrentOrder());
    }


    @Transactional
    @Given("another timeslot at Naga beginning at {int}h{int} on {int}-{int}-{int} but to many order already created on this timeslot")
    public void anotherTimeslotAtNagaBeginningAtHOnButToManyOrderAlreadyCreatedOnThisTimeslot(int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        Restaurant restaurant = restaurantRepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, 0);
        SubOrder orderToFillTimeSlot1 = initializeSuborder(registeredUserID);
        SubOrder orderToFillTimeSlot2 = initializeSuborder(registeredUserID);
        timeSlot.addOrder(orderToFillTimeSlot1);
        timeSlot.addOrder(orderToFillTimeSlot2);
        timeSlotRepository.save(timeSlot);
        restaurantService.addTimeSlotToRestaurant(restaurant.getId(), timeSlot.getId());
    }

    private SubOrder initializeSuborder(int registeredUserID) {
        RegisteredUser registeredUser = registeredUserRepository.findById((long) registeredUserID).orElseThrow(NoSuchElementException::new);
        SubOrder order = new OrderBuilder()
                .setRestaurantID(restaurantID)
                .setUserID(registeredUserID)
                .build();
        subOrderRepository.save(order);
        registeredUser.addOrderToHistory(order);
        return order;
    }

    @Transactional
    @Given("Naga has a productionCapacity of {int} for the all the timeslots of {int}-{int}-{int} starting at")
    public void nagaHasAProductionCapacityOfForTheAllTheTimeslotsOfStartingAt(int productionCapacity, int startDay, int startMonth, int startYear, List<String> startHours) {
        for (String timeslot : startHours) {
            String[] parts = timeslot.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, hours, minutes);
            Restaurant restaurant = restaurantRepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
            TimeSlot timeSlot = new TimeSlot(startTime, restaurant, productionCapacity);
            restaurant.addTimeSlot(timeSlot);
        }
    }


    @Transactional
    @And("the timeslots of {int}-{int}-{int} starting at following hours each have {int} {status} order\\(s)")
    public void theTimeslotsStartingAtFollowingHoursEachHaveNbStatusOrders(int startDay, int startMonth, int startYear, int numberOfOrders, OrderStatus status, List<String> startHours) {
        for (String timeslot : startHours) {
            String[] parts = timeslot.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, hours, minutes);
            Restaurant restaurant = restaurantRepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
            TimeSlot timeSlot = restaurant.getCurrentTimeSlot(startTime);
            for (int i = 0; i < numberOfOrders; i++) {
                SubOrder order = new OrderBuilder()
                        .setRestaurantID(restaurantID)
                        .setUserID(registeredUserID)
                        .setStatus(status)
                        .build();
                subOrderRepository.save(order);
                timeSlot.addOrder(order);
            }
        }
    }

    @When("Jack consults the possible delivery times for the restaurant Naga for the {int}-{int}-{int}")
    public void jackConsultsThePossibleDeliveryTimesForTheRestaurantNaga(int day, int month, int year) {
        Restaurant restaurant = restaurantRepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        effectivePossibleDeliveryTimes = restaurantService.getAllAvailableDeliveryTimesOfRestaurantOnDay(restaurant.getId(), LocalDate.of(year, month, day));
    }

    @Then("he can see the timeslots starting at following hours for the {int}-{int}-{int}")
    public void heCanSeeTheTimeslotsStartingAtFollowingHours(int day, int month, int year, List<String> possibleDeliveryTimesString) {
        List<LocalDateTime> possibleDeliveryTimes = new ArrayList<>();
        for (String timeslot : possibleDeliveryTimesString) {
            String[] parts = timeslot.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            LocalDateTime startTime = LocalDateTime.of(year, month, day, hours, minutes);
            possibleDeliveryTimes.add(startTime);
        }
        assertEquals(possibleDeliveryTimes.size(), this.effectivePossibleDeliveryTimes.size());
        for (int i = 0; i < possibleDeliveryTimes.size(); i++) {
            int finalI = i;
            assertTrue(this.effectivePossibleDeliveryTimes.stream().anyMatch(time -> time.equals(possibleDeliveryTimes.get(finalI))));
        }
    }
}

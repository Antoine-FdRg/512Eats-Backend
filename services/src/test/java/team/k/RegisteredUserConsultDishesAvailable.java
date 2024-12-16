package team.k;

import commonlibrary.enumerations.Role;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.SubOrderJPARepository;
import commonlibrary.repository.TimeSlotJPARepository;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.mockito.MockitoAnnotations;
import commonlibrary.model.Dish;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import org.springframework.beans.factory.annotation.Autowired;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.orderservice.OrderService;
import team.k.restaurantservice.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RegisteredUserConsultDishesAvailable {

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
    Restaurant nagaRestaurant;

    int registeredUserID;
    int orderID;
    List<Dish> availableDishes;

    @DataTableType
    public Dish dishEntry(Map<String, String> entry) {
        return new Dish.Builder()
                .setName(entry.get("name"))
                .setPrice(Double.parseDouble(entry.get("price")))
                .setPreparationTime(Integer.parseInt(entry.get("preparationTime")))
                .build();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a registeredUser named {string} with the role {role}")
    @Transactional
    public void aRegisteredUserNamedWithTheRole(String name, Role role) {
        initializeUser(name, role);
    }

    @Transactional
    @And("a restaurant named {string} open from {int}:{int} to {int}:{int} with an average order preparation time of {int} minutes")
    public void aRestaurantNamedOpenFromTo(String name, int openHours, int openMinutes, int closeHours, int closeMinutes, int averageOrderPreparationTime) {
        LocalTime openTime = LocalTime.of(openHours, openMinutes);
        LocalTime closeTime = LocalTime.of(closeHours, closeMinutes);
        restaurantID = createAndPersist(name, averageOrderPreparationTime, openTime, closeTime);
    }

    @Transactional
    protected int createAndPersist(String name, int averageOrderPreparationTime, LocalTime openTime, LocalTime closeTime) {
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
    @And("with a productionCapacity of {int} for the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void withAProduvtionCapacityOfForTheTimeslotAtOn(int productionCapacity, int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        Restaurant restaurant = restaurantRepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, productionCapacity);
        timeSlotRepository.save(timeSlot);
        restaurantService.addTimeSlotToRestaurant(restaurant.getId(), timeSlot.getId());
    }

    @Transactional
    @And("a delivery location with the number {string}, the street {string} and the city {string}")
    public void aDeliveryLocationWithTheNumberTheStreetAndTheCity(String streetNumber, String street, String city) {
        Location deliveryLocation = new Location.Builder()
                .setNumber(String.valueOf(streetNumber))
                .setAddress(street)
                .setCity(city)
                .build();
        locationRepository.save(deliveryLocation);
    }

    @Transactional
    @Given("the restaurant {string} has the following dishes with preparation time")
    public void theRestaurantNagaHasTheFollowingDishesWithPreparationTime(String restaurantName, List<Dish> dishes) {
        nagaRestaurant = new Restaurant.Builder()
                .setId(1)
                .setName(restaurantName)
                .setAverageOrderPreparationTime(10)
                .build();
        for (Dish dish : dishes) {
            nagaRestaurant.addDish(dish);
        }
        restaurantRepository.save(nagaRestaurant);
    }

    @Transactional
    @And("User {string} has a currentOrder for the restaurant Naga in the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void userHasACurrentOrderForTheRestaurantNagaInTheTimeslotBeginningAtOn(String userName, int min, int hour, int day, int month, int year) {
        initializeUser(userName, Role.STUDENT);
        RegisteredUser registeredUser = registeredUserRepository.findById((long) orderID).orElseThrow(NoSuchElementException::new);
        initializeSuborder(min, hour, day, month, year, registeredUser);
        TimeSlot timeSlot = new TimeSlot(LocalDateTime.of(year, month, day, hour, min), nagaRestaurant, nagaRestaurant.getAverageOrderPreparationTime());
        nagaRestaurant.addTimeSlot(timeSlot);
    }

    @Transactional
    protected void initializeSuborder(int min, int hour, int day, int month, int year, RegisteredUser registeredUser) {
        SubOrder order = new OrderBuilder()
                .setRestaurantID(nagaRestaurant.getId())
                .setUserID(registeredUserID)
                .setDeliveryTime(LocalDateTime.of(year, month, day, hour, min))
                .build();
        subOrderRepository.save(order);
        registeredUser.setCurrentOrder(order);
        orderID = order.getId();
    }

    @Transactional
    protected void initializeUser(String userName, Role student) {
        RegisteredUser user = new RegisteredUser(userName, student);
        registeredUserRepository.save(user);
        registeredUserID = user.getId();
    }

    @When("Jack consults the available dishes of the restaurant Naga")
    public void jackConsultsTheAvailableDishesOfTheRestaurantNagaForTheTimeslotBeginningAtOn() {
        availableDishes = orderService.getAvailableDishes(orderID);
    }
    @Then("he can see only the following dish names")
    public void heCanSeeOnlyTheFollowingDishes(List<String> expectedDishes) {
        List<String> expectedDishNames = expectedDishes.stream().filter(Objects::nonNull).toList();
        List<String> actualDishNames = availableDishes.stream().filter(Objects::nonNull).map(Dish::getName).toList();
        actualDishNames.forEach(n->{
            assertTrue(expectedDishNames.contains(n));
        });
        expectedDishNames.forEach(n->{
            assertTrue(actualDishNames.contains(n));
        });
    }

    @And("Jack adds the dish {string} to his basket")
    @Transactional
    public void jackAddsTheDishToHisBasket(String dishName) {
        Dish dish = nagaRestaurant.getDishes().stream().filter(d -> d.getName().equals(dishName)).findFirst().orElse(null);
        SubOrder order = subOrderRepository.findById((long) orderID).orElseThrow(NoSuchElementException::new);
        assertNotNull(dish);
        order.addDish(dish);
    }

}

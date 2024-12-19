package team.k;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.repository.GroupOrderJPARepository;
import commonlibrary.repository.IndividualOrderJPARepository;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.SubOrderJPARepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import commonlibrary.model.Dish;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.orderservice.OrderService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

public class ManagingRestaurantPreparationCapacityStepDefs {

    @Mock
    private RestaurantJPARepository restaurantRepository;
    @Mock
    private SubOrderJPARepository subOrderRepository;
    @Mock
    private IndividualOrderJPARepository individualOrderRepository;
    @Mock
    private RegisteredUserJPARepository registeredUserRepository;
    @Mock
    private LocationJPARepository locationRepository;
    @Mock
    private GroupOrderJPARepository groupOrderRepository;
    private OrderService orderService;
    Restaurant restaurant;

    int freeProductionCapacity;
    TimeSlot timeSlot;

    SubOrder order;
    Dish dish;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService =new OrderService(
                locationRepository,
                restaurantRepository,
                subOrderRepository,
                registeredUserRepository,
                groupOrderRepository,
                individualOrderRepository);
    }

    @Given("an order with the status {string} in the restaurant {string} with a chosen dish {string} with a production capacity of {int} and an average preparation time of {int} min with a delivery time at {int}:{int}")
    public void anOrderWithTheStatusInTheRestaurantWithAChosenDish(String statusCreated, String restaurantName, String dishName, int productionCapacity, int averagePreparationTime, int hours, int minutes) {
        restaurant = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).setAverageOrderPreparationTime(averagePreparationTime).build();
        dish = new Dish.Builder().setName(dishName).setDescription("Cheeseburger").setPrice(5).setPreparationTime(productionCapacity).build();
        restaurant.addDish(dish);
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(restaurantRepository.findById((long) restaurant.getId())).thenReturn(Optional.ofNullable(restaurant));
        order = new OrderBuilder()
                .setRestaurantID(restaurant.getId())
                .setDeliveryTime(LocalDateTime.of(2024, 10, 12, hours, minutes, 0))
                .build();
        when(subOrderRepository.findAll()).thenReturn(List.of(order));
        when(subOrderRepository.findById((long) order.getId())).thenReturn(Optional.ofNullable(order));
        order.setStatus(OrderStatus.valueOf(statusCreated));
    }

    @And("the restaurant {string} has a time slot available at {int}:{int} with a capacity of {int}")
    public void theRestaurantHasATimeSlotAvailableAtWithACapacityOf(String restaurantName, int hours, int min, int preparationCapacity) {
        timeSlot = new TimeSlot(LocalDateTime.of(2024, 10, 12, hours, min, 0), restaurant, preparationCapacity);
        restaurant.addTimeSlot(timeSlot);
        freeProductionCapacity = timeSlot.getFreeProductionCapacity();
    }


    @When("a registered user places the command")
    public void aRegisteredUserPlacesTheCommand() {
        timeSlot.addOrder(order);
        orderService.addDishToOrder(order.getId(), dish.getId());
        order.setStatus(OrderStatus.PLACED);
    }


    @Then("the preparation capacity of the timeslot is not at {int}")
    public void thePreparationCapacityOfTheOrderHasIncreased(int capcaity) {
        assertNotEquals(freeProductionCapacity, timeSlot.getFreeProductionCapacity());
        assertNotEquals(timeSlot.getFreeProductionCapacity(), capcaity);
    }


}

package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import commonlibrary.model.Dish;
import commonlibrary.enumerations.FoodType;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.repository.GroupOrderRepository;
import commonlibrary.repository.LocationRepository;
import commonlibrary.repository.RegisteredUserRepository;
import commonlibrary.repository.RestaurantRepository;
import commonlibrary.repository.SubOrderRepository;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import team.k.orderService.OrderService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class ManagingRestaurantPreparationCapacityStepDefs {


    OrderService orderService;
    SubOrderRepository subOrderRepository;

    Restaurant restaurant;

    int freeProductionCapacity;
    TimeSlot timeSlot;

    SubOrder order;
    Dish dish;

    RestaurantRepository restaurantRepository;
    @Mock
    LocationRepository locationRepository;
    @Mock
    GroupOrderRepository groupOrderRepository;
    @Mock
    RegisteredUserRepository registeredUserRepository;

    @Before
    public void setUp() {
        subOrderRepository = new SubOrderRepository();
        restaurantRepository = new RestaurantRepository();
        orderService = new OrderService(
                groupOrderRepository,
                locationRepository,
                subOrderRepository,
                restaurantRepository,
                registeredUserRepository);
    }

    @Given("an order with the status {string} in the restaurant {string} with a chosen dish {string} with a production capacity of {int} and an average preparation time of {int} min with a delivery time at {int}:{int}")
    public void anOrderWithTheStatusInTheRestaurantWithAChosenDish(String statusCreated, String restaurantName, String dishName, int productionCapacity, int averagePreparationTime, int hours, int minutes) {
        restaurant = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).setAverageOrderPreparationTime(averagePreparationTime).build();
        dish = new Dish.Builder().setName(dishName).setDescription("Cheeseburger").setPrice(5).setPreparationTime(productionCapacity).build();
        restaurant.addDish(dish);
        order = new OrderBuilder().setRestaurantID(restaurant.getId()).setDeliveryTime(LocalDateTime.of(2024, 10, 12, hours, minutes, 0)).build();
        subOrderRepository.add(order);
        order.setStatus(OrderStatus.valueOf(statusCreated));
        restaurantRepository.add(restaurant);
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

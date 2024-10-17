package team.k;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.enumerations.OrderStatus;
import team.k.order.IndividualOrder;
import team.k.order.OrderBuilder;
import team.k.order.SubOrder;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;
import team.k.restaurant.Restaurant;
import team.k.restaurant.TimeSlot;
import team.k.service.OrderService;
import team.k.service.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ManagingRestaurantPreparationCapacityStepDefs {


    OrderService orderService;
    SubOrderRepository subOrderRepository;

    Restaurant restaurant;

    int freeProductionCapacity;
    TimeSlot timeSlot;

    SubOrder order;
    Dish dish;


    @Before
    public void setUp() {
        subOrderRepository = new SubOrderRepository();
        orderService = new OrderService(subOrderRepository);
    }

    @Given("an order with the status {string} in the restaurant {string} with a chosen dish {string} with a production capacity of {int} with a delivery time at {int}:{int}")
    public void anOrderWithTheStatusInTheRestaurantWithAChosenDish(String statusCreated, String restaurantName, String dishName, int productionCapacity, int hours, int minutes) {
        restaurant = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).build();
        dish = new Dish.Builder().setName(dishName).setDescription("Cheeseburger").setPrice(5).setPreparationTime(productionCapacity).build();
        restaurant.addDish(dish);
        order = new OrderBuilder().setRestaurant(restaurant).setDeliveryTime(LocalDateTime.of(2024, 10, 12, hours, minutes, 0)).build();
        subOrderRepository.add(order);
        order.setStatus(OrderStatus.valueOf(statusCreated));
    }

    @And("the restaurant {string} has a time slot available at {int}:{int} with a capacity of {int}")
    public void theRestaurantHasATimeSlotAvailableAtWithACapacityOf(String restaurantName, int hours, int min, int preparationCapacity) {
        timeSlot = new TimeSlot(LocalDateTime.of(2024, 10, 12, hours, min, 0), restaurant, preparationCapacity, 3);
        restaurant.addTimeSlot(timeSlot);
        freeProductionCapacity = timeSlot.getFreeProductionCapacity();
    }


    @When("a registered user places the command")
    public void aRegisteredUserPlacesTheCommand() {

        timeSlot.addOrder(order);
        orderService.addDishToOrder(0, dish);
        order.setStatus(OrderStatus.PLACED);

    }


    @Then("the preparation capacity of the timeslot is not at {int}")
    public void thePreparationCapacityOfTheOrderHasIncreased(int capcaity) {
        assertNotEquals(freeProductionCapacity, timeSlot.getFreeProductionCapacity());
        assertNotEquals(timeSlot.getFreeProductionCapacity(), capcaity);
    }


}

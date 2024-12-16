package team.k;

import commonlibrary.enumerations.Role;
import commonlibrary.external.PaymentProcessor;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.model.restaurant.TimeSlot;
import commonlibrary.model.restaurant.discount.FreeDishAfterXOrders;
import commonlibrary.model.restaurant.discount.RoleDiscount;
import commonlibrary.model.restaurant.discount.UnconditionalDiscount;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.SubOrderJPARepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import team.k.orderservice.OrderService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class RegisteredUserUsesDiscountStepDefs {

    int registeredUserID;
    int orderID;
    FreeDishAfterXOrders freeDiscount;
    UnconditionalDiscount unconditionalDiscount;
    RoleDiscount roleDiscount;

    @Mock
    PaymentProcessor paymentProcessor;

    int restaurantID;

    @Autowired
    private RestaurantJPARepository restaurantJPARepository;
    @Autowired
    private RegisteredUserJPARepository registeredUserJPARepository;
    @Autowired
    private SubOrderJPARepository subOrderJPARepository;
    @Autowired
    private OrderService orderService;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Transactional
    @Given("an order containing {int} dishes is created by a registered user whose name is {string} and his role is {role}")
    public void anOrderContainingDishesIsCreatedByARegisteredUserWhoseNameIsAndHisRoleIsSTUDENT(int number, String name, Role role) {
        Restaurant internalRestaurant = initializeRestaurant();
        internalRestaurant = restaurantJPARepository.findById((long) internalRestaurant.getId()).orElseThrow(NoSuchElementException::new);
        restaurantID = internalRestaurant.getId();
        internalRestaurant.addTimeSlot(
                new TimeSlot(
                        LocalDateTime.of(2025, 1, 1, 10, 0),
                        internalRestaurant,
                        3
                )
        );

        registeredUserID = createAndPersistUser(name, role);
        initializeUserOrderHistory(registeredUserID);
        initializeUserOrder(number,registeredUserID);
        RegisteredUser registeredUser = registeredUserJPARepository.findById((long) registeredUserID).orElseThrow(NoSuchElementException::new);
        orderID = registeredUser.getCurrentOrder().getId();
    }

    @Transactional
    protected Restaurant initializeRestaurant() {
        Restaurant restaurant = new Restaurant.Builder()
                .setName("Le restaurant")
                .setDescription("Ce restaurant a plein de discount différente")
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setAverageOrderPreparationTime(10)
                .build();
        restaurantJPARepository.save(restaurant);
        return restaurant;
    }

    @Transactional
    protected int createAndPersistUser(String name, Role role) {
        RegisteredUser registeredUser = new RegisteredUser(name, role);
        registeredUserJPARepository.save(registeredUser);
        return registeredUser.getId();
    }

    @Transactional
    protected void initializeUserOrderHistory(int userID) {
        RegisteredUser registeredUser = registeredUserJPARepository.findById((long) userID).orElseThrow(NoSuchElementException::new);
        // Initialisation des commandes de l'historique de l'utilisateur
        SubOrder previousOrder = new OrderBuilder()
                .setUserID(registeredUser.getId())
                .setRestaurantID(restaurantID)
                .setDeliveryLocation(new Location.Builder()
                        .setNumber("1")
                        .setAddress("adresse")
                        .setCity("city")
                        .build())
                .build();
        for (int i = 0; i < 10; i++) {
            registeredUser.addOrderToHistory(previousOrder);
        }
    }

    @Transactional
    protected void initializeUserOrder(int numberOfDishes, int userID) {
        RegisteredUser registeredUser = registeredUserJPARepository.findById((long) userID).orElseThrow(NoSuchElementException::new);
        // Initialisation de la commande actuelle
        SubOrder order = new OrderBuilder()
                .setUserID(registeredUser.getId())
                .setRestaurantID(restaurantID)
                .setDeliveryTime(LocalDateTime.of(2025, 1, 1, 10, 50))
                .build();

        // Ajout des plats dans la commande actuelle
        for (int i = 0; i < numberOfDishes - 1; i++) {
            Dish dish = new Dish.Builder()
                    .setName("dish " + i)
                    .setPrice(10.0)
                    .build();
            order.addDish(dish);
        }
        Dish cheapestDish = new Dish.Builder()
                .setName("cheapest dish")
                .setPrice(5.0)
                .build();
        subOrderJPARepository.save(order);
        order.addDish(cheapestDish);
        registeredUser.setCurrentOrder(order);
    }

    @And("the restaurant have freeDishAfterXOrders discount")
    @Transactional
    public void theRestaurantHaveAUnconditionalDiscount() {
        Restaurant restaurantToApplyDiscount = restaurantJPARepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        freeDiscount = new FreeDishAfterXOrders(restaurantToApplyDiscount.getId(), 10);
        restaurantToApplyDiscount.setDiscountStrategy(freeDiscount);
    }

    @When("The user pays the order with the discount")
    public void theUserPaysTheOrderWithTheDiscount() {
        when(paymentProcessor.processPayment(anyDouble())).thenReturn(true);
        orderService.paySubOrder(registeredUserID, orderID, LocalDateTime.of(2025, 1, 1, 10, 0), paymentProcessor);
    }

    @Then("the cheapest dish is free in the order")
    public void theCheapestDishIsFreeInTheOrder() {
        SubOrder order = subOrderJPARepository.findById((long) orderID).orElseThrow(NoSuchElementException::new);
        assertEquals(90.0, order.getPrice(), 0);
        assertEquals(90.0, order.getPayment().getAmount(), 0);
    }


    @And("the restaurant have unconditional discount")
    @Transactional
    public void theRestaurantHaveUnconditionalDiscount() {
        Restaurant restaurantToApplyDiscount = restaurantJPARepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        unconditionalDiscount = new UnconditionalDiscount(restaurantToApplyDiscount.getId(), 0.25);
        restaurantToApplyDiscount.setDiscountStrategy(unconditionalDiscount);
    }

    @Then("the price is lower than before")
    public void thePriceIsLowerThanBefore() {
        SubOrder order = subOrderJPARepository.findById((long) orderID).orElseThrow(NoSuchElementException::new);
        assertEquals(71.25, order.getPrice(), 0);
        assertEquals(71.25, order.getPayment().getAmount(), 0);
    }

    @And("the restaurant have Role discount")
    @Transactional
    public void theRestaurantHaveRoleDiscount() {
        Restaurant restaurantToApplyDiscount = restaurantJPARepository.findById((long) restaurantID).orElseThrow(NoSuchElementException::new);
        roleDiscount = new RoleDiscount(restaurantToApplyDiscount.getId(), 0.2, Role.STUDENT);
        restaurantToApplyDiscount.setDiscountStrategy(roleDiscount);
    }

    @Then("the price is lower than the previous one")
    public void thePriceIsLowerThanThePreviousOne() {
        SubOrder order = subOrderJPARepository.findById((long) orderID).orElseThrow(NoSuchElementException::new);
        assertEquals(76, order.getPrice(), 0);
        assertEquals(76, order.getPayment().getAmount(), 0);
    }


    @Then("the price does not change")
    public void thePriceDoesNotChange() {
        SubOrder order = subOrderJPARepository.findById((long) orderID).orElseThrow(NoSuchElementException::new);
        assertEquals(95.0, order.getPrice(), 0);
    }
}

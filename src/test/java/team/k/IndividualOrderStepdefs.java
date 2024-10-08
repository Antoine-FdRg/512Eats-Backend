package team.k;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import team.k.common.Dish;
import team.k.common.DishBuilder;
import team.k.enumerations.OrderStatus;
import team.k.enumerations.Role;
import team.k.restaurant.Restaurant;
import team.k.restaurant.RestaurantBuilder;
import team.k.restaurant.TimeSlot;
import team.k.services.OrderService;
import team.k.services.RestaurantService;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class IndividualOrderStepdefs {
    RegisteredUser registeredUser;
    Restaurant restaurant;
    RestaurantService restaurantService = new RestaurantService();
    OrderService orderService = new OrderService();

    @ParameterType("STUDENT|CAMPUS_EMPLOYEE")
    public Role role(String role) {
        return Role.valueOf(role);
    }

    @ParameterType("CREATED|PAID|PLACED|DELIVERING|COMPLETED|DISCOUNT_USED|CANCELED")
    public OrderStatus status(String status) {
        return OrderStatus.valueOf(status);}

    @Given("a registeredUser named {string} with the role {role}")
    public void unRegisteredUserNommeDeRole(String name, Role role) {
        registeredUser = new RegisteredUser(name, role);
    }

    @And("a restaurant named {string} open from {int}:{int} to {int}:{int}")
    public void unRestaurantNomméOuvertDeÀ(String name, int openHours, int openMinutes, int closeHours, int closeMinutes) {
        LocalTime openTime = LocalTime.of(openHours, openMinutes);
        LocalTime closeTime = LocalTime.of(closeHours, closeMinutes);
        restaurant = new RestaurantBuilder().setName(name).setOpen(openTime).setClose(closeTime).build();
    }

    @And("with a productionCapacity of {int} for the timeslot beginning at {int}:{int} on {int}-{int}-{int}")
    public void avecUneProductionCapacityDeSurLeCréneauDe(int productionCapacity, int startHours, int startMinutes, int startDay, int startMonth, int startYear) {
        LocalDateTime startTime = LocalDateTime.of(startYear, startMonth, startDay, startHours, startMinutes);
        TimeSlot timeSlot = new TimeSlot(startTime, restaurant, productionCapacity, 10);
        restaurantService.addTimeSlotToRestaurant(restaurant, timeSlot);
    }
    
    @And("a dish named {string} that costs {double} and takes {int} minutes to be prepared")
    public void unPlatNomméAvecUnPrixDeEtUnPreparationTimeDeMinutes(String dishName, double price, int preparationTime) {
        Dish dish = new DishBuilder().setName(dishName).setPrice(price).setPreparationTime(preparationTime).build();
        restaurantService.addDishToRestaurant(restaurant, dish);
    }

    @When("a registeredUser adds {string} to his basket")
    public void unRegisteredUserAjouteÀSonPanier(String dishName) {
        Dish dish = restaurant.getDishes().stream().filter(d -> d.getName().equals(dishName)).findFirst().orElseThrow();
        orderService.addDishToRegisteredUserBasket(registeredUser, dish, restaurant);
    }

    @Then("his current order contains {int} dish")
    public void ilAUneCommandeActuelleContenantPlat(int nbOfDishes) {
        assertEquals(nbOfDishes,registeredUser.getCurrentOrder().getDishes().size());
    }

    @And("his current order has the status {status}")
    public void saCommandeEstAuStatus(OrderStatus status) {
        assertEquals(status, registeredUser.getCurrentOrder().getStatus());
    }
}

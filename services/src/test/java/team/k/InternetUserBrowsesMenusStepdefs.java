package team.k;

import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.repository.DishJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import team.k.managementservice.ManageRestaurantService;
import team.k.restaurantservice.RestaurantService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;

public class InternetUserBrowsesMenusStepdefs {

    @Autowired
    private ManageRestaurantService manageRestaurantService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantJPARepository restaurantJPARepository;
    @Autowired
    private DishJPARepository dishJPARepository;

    private Restaurant restaurant;
    Restaurant restaurantB;

    List<Dish> dishes;
    List<Dish> restaurantDishes;

    private NoSuchElementException errorMessage;

    // Remove the restaurant after each scenario
    @After
    public void tearDown() {
        if (restaurant != null) {
            manageRestaurantService.deleteRestaurant(restaurant.getId());
        }
    }

    @Transactional
    @Given("A restaurant {string} with a dish {string} and a dish {string}")
    public void aRestaurantExistInTheListOfRestaurantsWithADishAndADish(String restaurantName, String dishNameA, String dishNameB) {
        dishes = new ArrayList<>();
        Dish dishA = new Dish.Builder().setName(dishNameA).setDescription("Description").setPrice(5).setPreparationTime(3).build();
        dishJPARepository.save(dishA);
        dishes.add(dishA);
        Dish dishB = new Dish.Builder().setName(dishNameB).setDescription("Description").setPrice(5).setPreparationTime(3).build();
        dishJPARepository.save(dishB);
        dishes.add(dishB);
        Restaurant restaurantA = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(22, 0, 0)).setFoodTypes(List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL)).build();
        restaurantA.addDish(dishA);
        restaurantA.addDish(dishB);
        restaurantJPARepository.save(restaurantA);
    }

    @When("The user wants to have dishes non registered of the restaurant {string}")
    public void theUserWantsToHaveDishesNonRegisteredOfTheRestaurant(String restaurantName) {
        try {
            restaurantDishes = restaurantService.getAllDishesFromRestaurant(
                    restaurantJPARepository.findById(
                            (long)restaurantB.getId()
                    ).orElseThrow(NoSuchElementException::new).getId());
        } catch (NoSuchElementException e) {
            this.errorMessage = e;
        }

    }

    @When("The user wants to have dishes of the restaurant {string}")
    public void theUserWantsToHaveDishesOfTheRestaurant(String restaurantName) {
        restaurant = restaurantService.getRestaurantByName(restaurantName);

        try {
            restaurantDishes = restaurant.getDishes();
        } catch (NoSuchElementException e) {
            this.errorMessage = e;
        }

    }

    @Then("the user gets the dishes {string} and {string} registered in the restaurant selected")
    public void theUserGetsTheDishesAndRegisteredInTheRestaurantSelected(String dishNameA, String dishNameB) {
        List<Dish> restaurantDishes = restaurant.getDishes();
        assertEquals(2, restaurantDishes.size());
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameA)).count());
        assertEquals(1, restaurantDishes.stream().filter(dish -> dish.getName().equals(dishNameB)).count());
    }

    @Given("A restaurant {string} with no dishes registered")
    public void aRestaurantWithNoDishesRegistered(String restaurantName) {
        restaurantB = new Restaurant.Builder().setName(restaurantName).setOpen(LocalTime.of(8, 0, 0)).setClose(LocalTime.of(22, 0, 0)).setFoodTypes(List.of(FoodType.ASIAN_FOOD, FoodType.POKEBOWL)).build();
        manageRestaurantService.addRestaurant(restaurantB);
    }

    @Then("The user gets no dishes registered in the restaurant selected and have this message :{string}")
    public void theUserGetsNoDishesRegisteredInTheRestaurantSelectedAndHaveThisMessage(String expectedMessage) {
        assertEquals(this.errorMessage.getMessage(), expectedMessage);
    }


}

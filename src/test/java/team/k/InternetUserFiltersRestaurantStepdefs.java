package team.k;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class InternetUserFiltersRestaurantStepdefs {


    @Given("a list of restaurants {string} and {string} with registered dishes")
    public void aListOfRestaurantsAndWithRegisteredDishes(String restaurantNameA, String restaurantNameB) {

    }


    @When("Registered user selects a food type : {string}")
    public void registeredUserSelectsAFoodType(String foodType) {

    }

    @Then("Registered user should see the restaurant that serves that food type")
    public void registeredUserShouldSeeTheRestaurantThatServesThatFoodType() {

    }

    @When("Registered user selects open restaurants")
    public void registeredUserSelectsOpenRestaurants() {

    }

    @Then("Registered user should see the restaurant that are open")
    public void registeredUserShouldSeeTheRestaurantThatAreOpen() {

    }

    @When("Registered user searches for a restaurant by name")
    public void registeredUserSearchesForARestaurantByName() {

    }

    @Then("Registered user should see the restaurant that matches the name")
    public void registeredUserShouldSeeTheRestaurantThatMatchesTheName() {
    }
}

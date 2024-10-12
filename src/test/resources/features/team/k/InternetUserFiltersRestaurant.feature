Feature: Registered user can filter  restaurant


  Scenario: Registered user can filter restaurant by food type
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 12 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user selects a food type : "Sushi"
    Then Registered user should see the restaurant that serves that food type

  Scenario: Registered user can filter restaurant by type of food but no restaurant found
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 12 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user selects a food type : "Banane"
    Then Registered user should see a message "No restaurants found with the food types: [Banane]"



  Scenario: Registered user can filter restaurant by availability
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 13 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user selects restaurants that are open at 12: 10
    Then Registered user should see the restaurant that are open

  Scenario: Registered user can filter restaurant by availability but no restaurant found
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 13 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user selects restaurants that are open at 10: 10
    Then Registered user should see a message "No restaurants found with availability at: 10:10"


  Scenario: Registered user can filter restaurant by name
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 12 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user searches for a restaurant with name "512EatRestaurant"
    Then Registered user should see the restaurant that matches the name "512EatRestaurant" for his name choice

  Scenario: Registered user can filter restaurant by name but no restaurant found
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 12 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user searches for a restaurant with name "512PizzaRestaurant"
    Then Registered user should see a message "No restaurants found with the name: 512PizzaRestaurant"


Feature: Registered user can filter  restaurant

  Scenario: Registered user can filter restaurant by food type
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 12 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user selects a food type : "Sushi"
    Then Registered user should see the restaurant that serves that food type

  Scenario: Registered user can filter restaurant by availability
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 13 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user selects restaurants that are open at 12h 10
    Then Registered user should see the restaurant that are open


  Scenario: Registered user can filter restaurant by name
    Given a list of restaurants "512EatRestaurant" a "Sushi" restaurant which is open from 12 to 15 and "512BurgerRestaurant" a "Burger" restaurant opened from 12 to 13 with registered dishes
    When Registered user searches for a restaurant with name "512EatRestaurant"
    Then Registered user should see the restaurant that matches the name "512EatRestaurant"

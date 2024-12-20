Feature: Internet User can filter  restaurant

  Background:
    Given The restaurant "512EatRestaurant" a "SUSHI" restaurant open from 12 to 15 with the dish "maki" registered
    And The restaurant "512PokeRestaurant" a "SUSHI" restaurant open from 12 to 13 with the dish "poke" registered

  Scenario: Internet User can filter restaurant by food type
    When Internet User selects a food type : "SUSHI"
    Then Internet User should see the 2 restaurants that serves that food type

  Scenario: Internet User can filter restaurant by type of food but no restaurant found
    When Internet User selects a food type : "PIZZA"
    Then Internet User should see a message "No restaurants found with the food types: [PIZZA]"

  Scenario: Internet User can filter restaurant by availability
    When Internet User selects restaurants that are open for a delivery 12:55 on 01-01-2025
    Then Internet User should see the restaurant that are open

  Scenario: Internet User can filter restaurant by availability but no restaurant found
    When Internet User selects restaurants that are open for a delivery 10:10 on 01-01-2025
    Then Internet User should see a message "No restaurants found with availability at: 2025-01-01T10:10"

  Scenario: Internet User can filter restaurant by name
    When Internet User searches for a restaurant with name "512PokeRestaurant"
    Then Internet User should see the restaurant that matches the name "512PokeRestaurant" for his name choice

  Scenario: Internet User can filter restaurant by name but no restaurant found
    When Internet User searches for a restaurant with name "512PizzaRestaurant"
    Then Internet User should see a message "No restaurants found with the name: 512PizzaRestaurant"


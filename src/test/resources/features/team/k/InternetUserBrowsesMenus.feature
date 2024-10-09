Feature: Browse available dishes of one restaurant

  Scenario:
    Given A restaurant "512EatRestaurant" exists in the list of restaurants with a dish "Strawberry salad" and a dish "Candy apple"
    When The user wants to have dishes of the restaurant "512EatRestaurant"
    Then the user gets the dishes "Strawberry salad" and "Candy apple" registered in the restaurant selected

Feature: Browse available dishes of one restaurant

  Scenario:
    Given A restaurant "512EatRestaurant" with a dish "Strawberry salad" and a dish "Candy apple"
    When The user wants to have dishes of the restaurant "512EatRestaurant"
    Then the user gets the dishes "Strawberry salad" and "Candy apple" registered in the restaurant selected

  Scenario:
    Given A restaurant "512BurgerRestaurant" with no dishes registered
    When The user wants to have dishes non registered of the restaurant "512BurgerRestaurant"
    Then The user gets no dishes registered in the restaurant selected and have this message :"No dishes available"








    
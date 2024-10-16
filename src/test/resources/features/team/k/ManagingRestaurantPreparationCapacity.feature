Feature: Managing restaurant preparationCapacity

  Scenario:
    Given an order with the status "CREATED" in the restaurant "512EatRestaurant" with a chosen dish "cheesburger"
    When a registered user places the command
    Then the preparation capacity of the order has increased
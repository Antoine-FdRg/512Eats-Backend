Feature: Managing restaurant preparationCapacity

  Scenario:
    Given an order with the status "CREATED" in the restaurant "512EatRestaurant" with a chosen dish "cheesburger" with a production capacity of 5 with a delivery time at 12:50
    And the restaurant "512EatRestaurant" has a time slot available at 12:00 with a capacity of 3
    When a registered user places the command
    Then the preparation capacity of the timeslot is not at 90
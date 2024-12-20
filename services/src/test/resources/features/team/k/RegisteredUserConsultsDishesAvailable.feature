Feature: Individual order

  Background:
    Given a registeredUser named "Jack" with the role STUDENT
    And a restaurant named "Naga" open from 10:00 to 14:00 with an average order preparation time of 30 minutes
    And with a productionCapacity of 2 for the timeslot beginning at 12:00 on 01-01-2025
    And a delivery location with the number "930", the street "Rte des Colles" and the city "Biot"

  Scenario: A RegisteredUser creates a individual order and select a delivery time should see only the available items for this time
    Given the restaurant "Naga" has the following dishes with preparation time
      | name   | price | preparationTime |
      | Sushi  | 10    | 20              |
      | Burger | 16    | 10              |
      | Pizza  | 12    | 40              |
    And User "Jack" has a currentOrder for the restaurant Naga in the timeslot beginning at 12:00 on 01-01-2025
    When Jack consults the available dishes of the restaurant Naga
    Then he can see only the following dish names
      | Sushi  |
      | Burger |

  Scenario: A RegisteredUser creates a individual order and select a delivery time and add an item in his basket should see only the available items for this time
    Given the restaurant "Naga" has the following dishes with preparation time
      | name   | price | preparationTime |
      | Sushi  | 10    | 20              |
      | Burger | 16    | 10              |
      | Pizza  | 12    | 20              |
    And User "Jack" has a currentOrder for the restaurant Naga in the timeslot beginning at 12:00 on 01-01-2025
    And Jack adds the dish "Sushi" to his basket
    When Jack consults the available dishes of the restaurant Naga
    Then he can see only the following dish names
      | Burger  |

  Scenario: A RegisteredUser creates a individual order and select a delivery time and add an item in his basket should see only the available items for this time
    Given the restaurant "Naga" has the following dishes with preparation time
      | name   | price | preparationTime |
      | Sushi  | 10    | 20              |
      | Burger | 16    | 10              |
      | Pizza  | 12    | 30              |
    And User "Jack" has a currentOrder for the restaurant Naga in the timeslot beginning at 12:00 on 01-01-2025
    And Jack adds the dish "Pizza" to his basket
    When Jack consults the available dishes of the restaurant Naga
    Then he can see only the following dish names
      | |

Feature: Restaurant Manager updates his restaurant info

  Background:
    Given the restaurant "Naga" has the following information
      | open   | 08:00 |
      | closed | 17:00 |

  Scenario: Restaurant Manager updates open time
    When the restaurant manager updates the open time to "09:00"
    Then the restaurant Naga should have the following information
      | open   | 09:00 |
      | closed | 17:00 |

  Scenario: Restaurant Manager updates closed time
    When the restaurant manager updates the closed time to "18:00"
    Then the restaurant Naga should have the following information
      | open   | 08:00 |
      | closed | 18:00 |

  Scenario: Restaurant Manager add a new dish
    When the restaurant manager adds a new dish "Beef" with price 12.00
    Then the restaurant Naga should have the new dish "Beef" with price 12.00

  Scenario: Restaurant Manager remove a dish
    When the restaurant manager adds a new dish "Beef" with price 12.00
    And the restaurant manager removes the dish recently added
    Then the restaurant Naga should not have the dish "Beef"

  Scenario: Restaurant Manager updates a dish
    When the restaurant manager adds a new dish "Beef" with price 12.00
    And the restaurant manager updates a dish with price to 15.00 with preparation time 20
    Then the restaurant Naga should have the dish updated with price 15.00 and preparation time 20
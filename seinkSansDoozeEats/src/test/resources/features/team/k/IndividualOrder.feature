Feature: Individual order

  Background:
    Given a registeredUser named "Jack" with the role STUDENT
    And a restaurant named "Naga" open from 10:00 to 14:00 with an average order preparation time of 30 minutes
    And with a productionCapacity of 2 for the timeslot beginning at 12:00 on 01-01-2025
    And a delivery location with the number "930", the street "Rte des Colles" and the city "Biot"

  Scenario: creation d'une order avec succes
    When a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for 12h55 on 01-01-2025 the current date being 01-01-2025 10:00
    Then the registeredUser should have his currentOrder with the status CREATED
    And the registeredUser should have his currentOrder with no dishes
    And the restaurant should have 1 order with the status CREATED
    And the order should have been added to the suborder repository

  Scenario: creation d'une order en erreur (date de livraison non renseignée)
    When a registeredUser creates an order for the restaurant Naga with the deliveryPlace created but without delivery date the current date being 01-01-2025 10:00
    Then the registeredUser should not have any currentOrder

  Scenario: creation d'une order en erreur (date de livraison antérieure à la date courante)
    When a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for 8h00 on 01-01-2025 the current date being 01-01-2025 10:00
    Then the registeredUser should not have any currentOrder

  Scenario: creation d'une order en erreur (date de livraison postérieure à la date de fermeture du restaurant + 20min de livraison)
    When a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for 15h00 on 01-01-2025 the current date being 01-01-2025 10:00
    Then the registeredUser should not have any currentOrder

  Scenario: creation d'une order en erreur (timeslot complet)
    Given another timeslot at Naga beginning at 12h30 on 01-01-2025 but to many order already created on this timeslot
    When a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for 13h35 on 01-01-2025 the current date being 01-01-2025 10:00
    Then the registeredUser should not have any currentOrder

  Scenario: choix de la date livraison lors de la création d'une commande
    Given Naga has a productionCapacity of 2 for the all the timeslots of 01-01-2025 starting at
      | 10:00 |
      | 10:30 |
      | 11:00 |
      | 11:30 |
      | 12:00 |
      | 12:30 |
      | 13:00 |
      | 13:30 |
    And the timeslots of 01-01-2025 starting at following hours each have 2 PLACED order(s)
      | 10:00 |
      | 10:30 |
      | 11:00 |
    And the timeslots of 01-01-2025 starting at following hours each have 1 PLACED order(s)
      | 11:30 |
    And the timeslots of 01-01-2025 starting at following hours each have 1 CREATED order(s)
      | 12:30 |
    And the timeslots of 01-01-2025 starting at following hours each have 1 CREATED order(s)
      | 13:00 |
    And the timeslots of 01-01-2025 starting at following hours each have 1 PLACED order(s)
      | 13:00 |
    And the timeslots of 01-01-2025 starting at following hours each have 2 CREATED order(s)
      | 13:30 |
    When Jack consults the possible delivery times for the restaurant Naga for the 01-01-2025
    Then he can see the timeslots starting at following hours for the 01-01-2025
      | 12:20 |
      | 12:50 |
      | 13:20 |


  Scenario: A RegisteredUser creates a individual order and select a delivery time should see only the available items for this time
    Given the restaurant "Naga" has the following dishes with preparation time
      | id | name   | price | preparationTime |
      | 1  | Sushi  | 10    | 20              |
      | 2  | Burger | 16    | 10              |
      | 3  | Pizza  | 12    | 40              |
    And User "Jack" has a currentOrder for the restaurant Naga in the timeslot beginning at 12:00 on 01-01-2025
    When Jack consults the available dishes of the restaurant Naga
    Then he can see only the following dishes
      | id | name   | price | preparationTime |
      | 1  | Sushi  | 10    | 20              |
      | 2  | Burger | 16    | 10              |

  Scenario: A RegisteredUser creates a individual order and select a delivery time and add an item in his basket should see only the available items for this time
    Given the restaurant "Naga" has the following dishes with preparation time
      | id | name   | price | preparationTime |
      | 1  | Sushi  | 10    | 20              |
      | 2  | Burger | 16    | 10              |
      | 3  | Pizza  | 12    | 20              |
    And User "Jack" has a currentOrder for the restaurant Naga in the timeslot beginning at 12:00 on 01-01-2025
    And Jack adds the dish "Sushi" to his basket
    When Jack consults the available dishes of the restaurant Naga
    Then he can see only the following dishes
      | id | name   | price | preparationTime |
      | 2  | Burger | 16    | 10              |

  Scenario: A RegisteredUser creates a individual order and select a delivery time and add an item in his basket should see only the available items for this time
    Given the restaurant "Naga" has the following dishes with preparation time
      | id | name   | price | preparationTime |
      | 1  | Sushi  | 10    | 20              |
      | 2  | Burger | 16    | 10              |
      | 3  | Pizza  | 12    | 30              |
    And User "Jack" has a currentOrder for the restaurant Naga in the timeslot beginning at 12:00 on 01-01-2025
    And Jack adds the dish "Pizza" to his basket
    When Jack consults the available dishes of the restaurant Naga
    Then he can see only the following dishes
      | id | name | price | preparationTime |

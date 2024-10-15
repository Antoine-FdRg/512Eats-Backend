Feature: Create an individual order

  Background:
    Given a registeredUser named "Jack" with the role STUDENT
    And a restaurant named "Naga" open from 10:00 to 14:00
    And with a productionCapacity of 2 for the timeslot beginning at 12:00 on 01-01-2025
    And a delivery location with the number "930", the street "Rte des Colles" and the city "Biot"

    Scenario: creation d'une order
        When a registeredUser creates an order for the restaurant Naga with the deliveryPlace created for 13h on 01-01-2025
        Then the registeredUser should have his currentOrder with the status CREATED
        Then the registeredUser should have his currentOrder with no dishes
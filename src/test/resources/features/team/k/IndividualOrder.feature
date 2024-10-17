Feature: Create an individual order

  Background:
    Given a registeredUser named "Jack" with the role STUDENT
    And a restaurant named "Naga" open from 10:00 to 14:00
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

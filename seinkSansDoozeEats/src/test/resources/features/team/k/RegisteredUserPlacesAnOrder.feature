#language : en
Feature: Place an order

  Background:
    Given an individual order is created by a registered user whose name is "Tom" and his role is STUDENT to be delivered to "930", "Rue de la Loi" in "Biot"

  Scenario: A registered user places an order
    When The user places the order at 12:00 on 01-01-2025
    Then the status of the order is placed now

  Scenario: A registered user pays for an order and his command appear in his history
    When The user pays the order at 12:00 on 01-01-2025
    Then the order appears in the user's history
    And the status of the order is placed now

  Scenario: A registered user pays for an order but the payment fails and his command doesn't appear in his history
    When The user pays the order and the payment fails at 12:00 on 01-01-2025
    Then the order does not appears in the user's history


#language : en
Feature: Place an order

  Scenario: A registered user places an order
    Given an order is created by a registered user whose name is "Tom" and his role is STUDENT
    When The user places the order
    Then the status of the order is placed now

  Scenario: A registered user pays for an order and this command appear in his history
    Given an order is created by a registered user whose name is "Tom" and his role is STUDENT
    When The user pays the order
    Then the order appears in the user's history

  Scenario: A registered user pays an order
    Given an order is created by a registered user whose name is "Tom" and his role is STUDENT
    When The user pays the order
    Then the status of the order is placed now
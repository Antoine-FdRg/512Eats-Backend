Feature: Use discount

  Background:
    Given an order containing 10 dishes is created by a registered user whose name is "Tom" and his role is STUDENT


  Scenario: A registered user creates an order with FreeDishAfterXOrders discount
    And the restaurant have freeDishAfterXOrders discount
    When The user places the order with the discount
    Then the cheapest dish is free in the order

  Scenario: A registered user creates an order with Unconditional discount
    And the restaurant have unconditional discount
    When The user places the order with the discount
    Then the price is lower than before

  Scenario: A registered user creates an order with Role discount
    And the restaurant have Role discount
    When The user places the order with the discount
    Then the price is lower than the previous one
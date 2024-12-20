Feature: Use discount


  Scenario: A registered user creates an order with FreeDishAfterXOrders discount
    Given an order containing 10 dishes is created by a registered user whose name is "Tom" and his role is STUDENT
    And the restaurant have freeDishAfterXOrders discount
    When The user pays the order with the discount
    Then the cheapest dish is free in the order

  Scenario: A registered user creates an order with Unconditional discount
    Given an order containing 10 dishes is created by a registered user whose name is "Tim" and his role is STUDENT
    And the restaurant have unconditional discount
    When The user pays the order with the discount
    Then the price is lower than before

  Scenario: A registered user creates an order with Role discount
    Given an order containing 10 dishes is created by a registered user whose name is "Tum" and his role is STUDENT
    And the restaurant have Role discount
    When The user pays the order with the discount
    Then the price is lower than the previous one

  Scenario: A registered user creates an order with Role discount
    Given an order containing 10 dishes is created by a registered user whose name is "Tym" and his role is CAMPUS_EMPLOYEE
    And the restaurant have Role discount
    When The user pays the order with the discount
    Then the price does not change
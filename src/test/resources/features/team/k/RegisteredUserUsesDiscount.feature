Feature: Use discount


  Scenario: A registered user creates an order with discount
    Given an order containing 10 dishes is created by a registered user whose name is "Tom" and his role is STUDENT
    And the restaurant have a unconditional discount
    When The user places the order with the discount
    Then the cheapest dish is free in the order
#language : en
  Feature: Place an order

    Scenario: A registered user place an order
      Given an order is created by a registered user whose name is "Tom" and his role is STUDENT
      When The user places the order
      Then the status of the order is placed now
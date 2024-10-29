#language: en
Feature: A registered user creates a group order

  Scenario: A registered user creates a group order
    Given a delivery location
    When the user creates a group order with the delivery location
    Then the group order is created and the delivery location is initialized
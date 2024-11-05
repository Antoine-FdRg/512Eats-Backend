#language: en
Feature: A registered user creates a group order

  Scenario: A registered user creates a group order
    Given a delivery location
    When the user creates a group order with the delivery location for the "2025-01-23" at "13:00" on "2025-01-23" at "10:00"
    Then the group order is created and the delivery location is initialized and the delivery date time is the "2025-01-23" at "13:00"

  Scenario: A registered user creates a group order without delivery location
    Given a delivery location
    When the user creates a group order without the delivery location for the "2025-01-23" at "13:00" on "2025-01-23" at "10:00"
    Then the group order is not created

  Scenario: A registered user creates a group order without delivery date/time
    Given a delivery location
    When the user creates a group order with the delivery location on "2025-01-23" at "10:00"
    Then the group order is created and the delivery location is initialized but the delivery date time is not
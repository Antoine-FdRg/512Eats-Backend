#language: en
Feature: A registered user creates a group order

  Background:
    Given a delivery location

  Scenario: A registered user creates a group order
    When the user creates a group order with the delivery location for the "2025-01-23" at "12:50" on "2025-01-23" at "10:00"
    Then the group order is created and the delivery location is initialized and the delivery date time is the "2025-01-23" at "12:50"

  Scenario: A registered user creates a group order without delivery location
    When the user creates a group order without the delivery location for the "2025-01-23" at "12:50" on "2025-01-23" at "10:00"
    Then the group order is not created

  Scenario: A registered user creates a group order without delivery datetime
    When the user creates a group order with the delivery location on "2025-01-23" at "10:00"
    Then the group order is created and the delivery location is initialized but the delivery date time is not

  Scenario: A registered user modifies a group order that has no a delivery datetime with a correct datetime
    Given a group order created without a delivery datetime
    When the user modifies the delivery datetime to set "2025-01-23" at "12:50" on "2025-01-23" at "10:00"
    Then the group order delivery datetime is "2025-01-23" at "12:50"

  Scenario: A registered user modifies a group order that has no delivery datetime with a too early datetime
    Given a group order created without a delivery datetime
    When the user modifies the delivery datetime to set "2025-01-23" at "10:20" on "2025-01-23" at "10:00"
    Then the group order is not modified and the delivery datetime is still null

  Scenario: A registered user modifies a group order that has a delivery datetime
    Given a group order created with "2025-01-23" at "13:20" as delivery datetime
    When the user modifies the delivery datetime to set "2025-01-23" at "12:50" on "2025-01-23" at "10:00"
    Then the group order delivery datetime is "2025-01-23" at "13:20"
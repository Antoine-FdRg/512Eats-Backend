#language: en
Feature: A registered user creates a group order

  Background:
    Given a delivery location

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
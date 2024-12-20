#language:en

Feature: A registered user validates a group order

  Background:
    Given a delivery location
    Given two restaurants available at "11:00" and at "12:00" on "2024-12-18"
    Given a group order created with "2024-12-18" at "11:50" as delivery datetime

  Scenario:
    Given a suborder of the user "Tom" with the status PAID added in the group order
    When a group is placed at "2024-12-18" at "11:00"
    Then the suborder and the groupOrder are placed
    And the suborder is in the timeslot stating at "11:00" the "2024-12-18" of the restaurant

  Scenario:
    Given a suborder of the user "Leo" not already placed with the status CREATED added in the group order
    When a group is placed at "2024-12-18" at "11:00"
    Then the suborder and the groupOrder are canceled

  Scenario:
    Given a suborder of the user "Tom" with the status PAID added in the group order
    And a suborder of the user "Leo" not already placed with the status CREATED added in the group order
    When a group is placed at "2024-12-18" at "11:00"
    Then one suborder is placed and the other one is canceled
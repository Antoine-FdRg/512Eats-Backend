#language:en

Feature: A registered user validates a group order

  Background:
    Given a delivery location
    Given a group order created with "2024-12-10" at "12:00" as delivery datetime

  Scenario:
    Given a suborder of the user "Tom" with the status PAID added in the group order
    When a group is placed at "2024-12-10" at "11:00"
    Then the suborder and the groupOrder are placed

  Scenario:
    Given a suborder of the user "Leo" not already placed with the status CREATED added in the group order
    When a group is placed at "2024-12-10" at "11:00"
    Then the suborder and the groupOrder are canceled

  Scenario:
    Given a suborder of the user "Tom" with the status PAID added in the group order
    And a suborder of the user "Leo" not already placed with the status CREATED added in the group order
    When a group is placed at "2024-12-10" at "11:00"
    Then one suborder is placed and the other one is canceled
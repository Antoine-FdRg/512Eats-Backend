Feature: Individual order

  Background:
    Given a registeredUser called "Jack" with the role STUDENT
    And a group order created for "12:20" the "2025-01-01"
    And a restaurant called "Naga" open from 10:00 to 14:00 with an average order preparation time of 30 minutes
    And with a productionCapacity of 2 on the timeslot beginning at 12:00 on 01-01-2025
    And the delivery location "930", "Rte des Colles" in "Biot"
    And a suborder created in the group order for the restaurant "Naga"

  Scenario: a registeredUser pays its suborder
    When the registeredUser pays the suborder at "12:20" the "2025-01-01"
    Then the subOrder has PAID status in the groupOrder


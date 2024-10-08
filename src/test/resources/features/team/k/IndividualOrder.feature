Feature: Créer une order

  Background:
    Given a registeredUser named "Jack" with the role STUDENT
    And a restaurant named "Naga" open from 10:00 to 14:00
    And with a productionCapacity of 2 for the timeslot beginning at 12:00 on 01-01-2025
    And a dish named "Poulet nouilles" that costs 9.90 and takes 5 minutes to be prepared

    Scenario: creation d'une order
        When a registeredUser adds "Poulet nouilles" to his basket
        Then his current order contains 1 dish
        And his current order has the status CREATED
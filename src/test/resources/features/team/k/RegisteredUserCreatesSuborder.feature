Feature: Registered user creates a suborder


  Scenario: A registered user creates a suborder in a group order
    Given a groupOrder with the id 0 and without any suborder
    And a restaurant "512Eats"  with a dish "burger" an opening time "10:00" and closing time "18:00"
    When the user order a "burger" in the restaurant "512Eats"
    Then the suborder has the status created
    And the groupe order with the id 0 has a suborder with the status created


  Scenario: A registered user creates an individual order
    Given a restaurant "512Eats"  with a dish "burger" an opening time "10:00" and closing time "18:00"
    When the user orders a "burger" in the restaurant "512Eats" for the location : "Mougins"
    Then the order has the status created
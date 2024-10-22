Feature: Restaurant Manager

  Background:
    Given the restaurant "Naga" has the following information
      | open   | 08:00 |
      | closed | 17:00 |

  Scenario: Restaurant Manager updates his restaurant info (open time)
    When the restaurant manager updates the open time to "09:00"
    Then the restaurant Naga should have the following information
      | open   | 09:00 |
      | closed | 17:00 |

  Scenario: Restaurant Manager updates his restaurant info (closed time)
    When the restaurant manager updates the closed time to "18:00"
    Then the restaurant Naga should have the following information
      | open   | 08:00 |
      | closed | 18:00 |

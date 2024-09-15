Feature: Item Management

  Scenario: Add a new item
    Given I am on the Add Item screen
    When I enter valid item details
    And I save the item
    Then the item should be added to the inventory

  Scenario: Delete an existing item
    Given I have an item in the inventory
    When I delete the item
    Then the item should be removed from the inventory

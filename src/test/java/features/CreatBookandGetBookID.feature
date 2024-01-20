Feature: Create New Books
  Scenario: i want to Create new book
    Given User is authorized with credentials
    When user sends the request to Create New book
    Then Get Booking ID details
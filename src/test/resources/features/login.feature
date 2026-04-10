Feature: Авторизация
  Scenario: Успешный вход
    Given I Am Sending A Login Request With Data
      | username | Andrey88 |
      | password | password |
    Then RESPONSE STATUS 200
    And THE RESPONSE CONTAINS THE ACCESSTOKEN FIELD
    And THE TOKEN IS SAVED FOR FURTHER REQUESTS


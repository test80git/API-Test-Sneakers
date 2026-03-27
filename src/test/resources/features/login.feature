Feature: Авторизация
  Scenario: Успешный вход
    Given I Am Sending A Login Request With Data
      | username | Andrey88 |
      | password | password |
    Then RESPONSE STATUS 200
    And THE RESPONSE CONTAINS THE ACCESSTOKEN FIELD
    And THE TOKEN IS SAVED FOR FURTHER REQUESTS

  Scenario: Неверный пароль
    Given I Am Sending A Login Request With Data
      | username | Andrey88 |
      | password | wrongpass |
    Then RESPONSE STATUS 401
    And ERROR MESSAGE "Неверные учетные данные пользователя"

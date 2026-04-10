Feature: Авторизация

  Scenario: Неверный пароль
    Given I Am Sending A Login Request With Data
      | username | Andrey88 |
      | password | wrongpass |
    Then RESPONSE STATUS 401
#    And ERROR MESSAGE "Неверные учетные данные пользователя"
    And ERROR MESSAGE "Bad credentials"

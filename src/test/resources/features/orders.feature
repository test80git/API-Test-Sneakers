Feature: Заказы
  Как пользователь
  Я хочу оформлять и оплачивать заказы
  Чтобы покупать товары

  Background:
    Given I am logged in as "Pushkin"
    And I clear my cart

  Scenario: Оформление заказа и оплата
    Given I added an item to my cart
      | productId | sizeRu | quantity |
      | 112233445 | 44     | 3        |
    Then RESPONSE STATUS 200
    When Create Order From Cart
      | promoCode   | strategyType |
      | TESTTHEBEST | QUANTITY     |
    Then RESPONSE STATUS 200
    Then RESPONSE ORDER
      | status  | subtotal | discount | total |
      | PENDING | 26700    | 4005     | 22695 |
#    Оплата Карта заблокирована
    And PAY ORDER
      | cardNumber       | expiryDate | cardHolder  |
      | 0000000000000000 | 12/27      | IVAN PETROV |
    Then RESPONSE STATUS 200
    And REQUEST PROCESSED PaymentResponse
      | success | message             |
      | false   | Карта заблокирована |
# Недостаточно средств
    And PAY ORDER
      | cardNumber       | expiryDate | cardHolder  |
      | 1234567890123456 | 12/27      | IVAN PETROV |
    Then RESPONSE STATUS 200
    And REQUEST PROCESSED PaymentResponse
      | success | message              |
      | false   | Недостаточно средств |
    #    Срок действия карты истёк
    And PAY ORDER
      | cardNumber       | expiryDate | cardHolder  |
      | 4000000000000000 | 12/17      | IVAN PETROV |
    Then RESPONSE STATUS 200
    And ERROR MESSAGE "Срок действия карты истёк"
#   успешная оплата
    And PAY ORDER
      | cardNumber       | expiryDate | cardHolder  |
      | 4111111111111111 | 12/27      | IVAN PETROV |
    Then RESPONSE STATUS 200
    And REQUEST PROCESSED PaymentResponse
      | success | message            |
      | true    | Payment successful |


  Scenario: Ошибка оплаты
    Given I added an item to my cart
      | productId | sizeRu | quantity |
      | 112233990 | 37     | 1        |
    Then RESPONSE STATUS 200
    When Create Order From Cart
      | promoCode | strategyType |
      |           | NONE         |
    Then RESPONSE STATUS 200
    Then RESPONSE ORDER
      | status  | subtotal | discount | total |
      | PENDING | 5800     | 0        | 5800  |
#    Неверный формат срока действия. Используйте MM/YY
    And PAY ORDER
      | cardNumber       | expiryDate | cardHolder  |
      | 4000000000000000 | 13/27      | IVAN PETROV |
    Then RESPONSE STATUS 400
    And ERROR MESSAGE "Неверный формат срока действия. Используйте MM/YY"
# Номер карты должен содержать 16 цифр
    And PAY ORDER
      | cardNumber | expiryDate | cardHolder  |
      | 400        | 12/27      | IVAN PETROV |
    Then RESPONSE STATUS 400
    And ERROR MESSAGE "Номер карты должен содержать 16 цифр"
    Then I cancel the order


  Scenario: Получение всех заказов
    And I clear all my orders
    When I am requesting a list of my orders
    Then RESPONSE STATUS 200
    And Each Order Contains Fields id, orderNumber, status


  Scenario: Отмена заказа
    Given I added an item to my cart
      | productId | sizeRu | quantity |
      | 223344556 | 44     | 1        |
    Then RESPONSE STATUS 200
    When Create Order From Cart
      | promoCode | strategyType |
      |           | NONE         |
    Then RESPONSE STATUS 200
    Then RESPONSE ORDER
      | status  | subtotal | discount | total |
      | PENDING | 14500    | 0        | 14500 |
    Then The Number Of Orders Is Equal 1
    Then I cancel the order
    Then RESPONSE STATUS 200
    And Receive Order By ID
    Then RESPONSE STATUS 200



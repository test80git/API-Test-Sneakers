Feature: Корзина
  Как пользователь
  Я хочу управлять корзиной
  Чтобы покупать товары

  Background:
    Given I am logged in as "AdaLovelace"
    And I clear my cart

  Scenario: Успешное добавление товара
    Given I added an item to my cart
      | productId | sizeRu | quantity |
      | 112233445 | 42     | 2        |
    Then RESPONSE STATUS 200
    And cart contains product 112233445 size 42 with quantity 2
    And cart contains product 112233445 size 42 with name "Кроссовки Nike Air Max 90"

  Scenario: Успешное добавление товара
    Given I added an item to my cart
      | productId | sizeRu | quantity |
      | 112233445 | 43     | 3        |
    Then RESPONSE STATUS 200
    And cart contains product 112233445 size 43 with quantity 3
    And cart contains product 112233445 size 43 with name "Кроссовки Nike Air Max 90"

  Scenario: Добавление несуществующего товара
    Given I added an item to my cart
      | productId | sizeRu | quantity |
      | 999999999 | 42     | 2        |
    Then RESPONSE STATUS 404
    And ERROR MESSAGE "Товар не найден"


  Scenario: Добавление больше чем в наличии
    Given I added an item to my cart
      | productId | sizeRu | quantity |
      | 112233445 | 42     | 8        |
    Then RESPONSE STATUS 400
    And ERROR MESSAGE "Недостаточно товара"
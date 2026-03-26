Feature: Корзина
  Как пользователь
  Я хочу управлять корзиной
  Чтобы покупать товары

  Background:
    Given I am logged in as "Andrey88"
    And I clear my cart

  Scenario: Успешное добавление товара
    When I add product 112233445 size 42 quantity 2
    Then RESPONSE STATUS 200
    And cart contains product 112233445 size 42 with quantity 2
    And cart contains product 112233445 size 42 with name "Кроссовки Nike Air Max 90"

  Scenario: Успешное добавление товара с Ошибкой специально
    When I add product 112233445 size 43 quantity 3
    Then RESPONSE STATUS 200
    And cart contains product 112233445 size 43 with quantity 3
    And cart contains product 112233445 size 43 with name "Кроссовки Nike Air Max"

  Scenario: Добавление несуществующего товара
    When I add product 999999 size 42 quantity 1
    Then RESPONSE STATUS 404
    And ERROR MESSAGE "Товар не найден"


  Scenario: Добавление больше чем в наличии
    When I add product 112233445 size 42 quantity 8
    Then RESPONSE STATUS 400
    And ERROR MESSAGE "Недостаточно товара"
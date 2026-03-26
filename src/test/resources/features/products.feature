Feature: Получение товаров
  Как пользователь
  Я хочу просматривать каталог товаров
  Чтобы выбирать кроссовки

  Background:
    Given I am logged in as "Andrey88"

  Scenario: Получение всех товаров
    When I M Sending A GET Request To "/api/v1/products"
    Then RESPONSE STATUS 200
    And The Response Returns A List Of Products
    And Each Product Contains Fields id, name, price

  Scenario: Получение товара по ID
    And There Is A Product With ID 112233445
    When I M Sending A GET Request To "/api/v1/products/{id}", Where id = 112233445
    Then RESPONSE STATUS 200
    And The Response Contains A Field "name"
    And Field "id" Equally 112233445

  Scenario: Поиск товаров по названию
    When I M Sending A GET Request To "/api/v1/products/search?name=Nike"
    Then RESPONSE STATUS 200
    And The Response Returns A List Of Products
    And each product contains 'Nike' in the title

  Scenario: Товар не найден
    When I M Sending A GET Request To "/api/v1/products/999999"
    Then RESPONSE STATUS 404
    And ERROR MESSAGE "Товар с ID 999999 не найден"
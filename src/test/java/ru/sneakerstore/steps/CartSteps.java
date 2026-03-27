package ru.sneakerstore.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import ru.sneakerstore.api.utils.ApiClient;
import ru.sneakerstore.dto.request.AddToCartRequest;
import ru.sneakerstore.dto.response.CartItemResponse;
import ru.sneakerstore.dto.response.CartResponse;
import ru.sneakerstore.dto.response.OrderResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CartSteps {

    @Given("I added an item to my cart")
    public void addToCart(io.cucumber.datatable.DataTable data) {
        List<Map<String, String>> rows = data.asMaps();
        for (Map<String, String> row : rows) {
            AddToCartRequest request = AddToCartRequest.builder()
                    .productId(Long.parseLong(row.get("productId")))
                    .sizeRu(Integer.parseInt(row.get("sizeRu")))
                    .quantity(Integer.parseInt(row.get("quantity")))
                    .build();

            System.out.println("REQUEST: " + request);
            var response = ApiClient.post("/api/v1/cart/add", request);
            System.out.println("RESPONSE status: " + response.statusCode());
            System.out.println("RESPONSE body: " + response.getBody().asString());
            ResponseContext.setResponse(response);
        }
    }

    @Then("cart contains product {int} size {int} with quantity {int}")
    public void cartContainsProduct(int productId, int sizeRu, int expectedQuantity) {
        CartResponse cart = getCart(); // отдельный метод получения корзины
        CartItemResponse item = cart.getItems().stream()
                .filter(i -> i.getProductId() == productId && i.getSizeRu() == sizeRu)
                .findFirst()
                .orElse(null);

        assertThat(item).isNotNull();
        assertThat(item.getQuantity()).isEqualTo(expectedQuantity);
    }

    @Then("cart contains product {int} size {int} with name {string}")
    public void cartContainsProductName(int productId, int sizeRu, String expectedName) {
        CartResponse cart = getCart(); // отдельный метод получения корзины
        CartItemResponse item = cart.getItems().stream()
                .filter(i -> i.getProductId() == productId && i.getSizeRu() == sizeRu)
                .findFirst()
                .orElse(null);

        assertThat(item).isNotNull();
        assertThat(item.getProductName()).isEqualTo(expectedName);
    }

    @Given("I clear my cart")
    public void clearCart() {
        Response response = ApiClient.delete("/api/v1/cart/clear");
        System.out.println("Response Clear cart status: " + response.statusCode());
        ResponseContext.setResponse(response);
        System.out.println(getCart());
    }

    @And("I clear all my orders")
    public void clearAllOrders() {
        // Получаем список заказов
        Response response = ApiClient.get("/api/v1/orders");
        if (response.statusCode() == 200) {
            List<OrderResponse> orders = response.jsonPath().getList("", OrderResponse.class);
            for (OrderResponse order : orders) {
                // Отменяем только PENDING заказы
                if ("PENDING".equals(order.getStatus())) {
                    ApiClient.post("/api/v1/orders/" + order.getId() + "/cancel", null);
                }
            }
        }
    }

    // Метод для получения корзины
    public CartResponse getCart() {
        var cartResponse = ApiClient.get("/api/v1/cart");
        assertThat(cartResponse.statusCode()).isEqualTo(200);
        return cartResponse.as(CartResponse.class);
    }

}

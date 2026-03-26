package ru.sneakerstore.steps;

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

import static org.assertj.core.api.Assertions.assertThat;

public class CartSteps {

    @When("I add product {int} size {int} quantity {int}")
    public void addProduct(int productId, int sizeRu, int quantity) {
        AddToCartRequest  request = AddToCartRequest.builder()
                .productId((long) productId)
                .sizeRu(sizeRu)
                .quantity(quantity)
                .build();
        System.out.println("REQUEST: " + request);
      var  response = ApiClient.post("/api/v1/cart/add", request);
        System.out.println("RESPONSE status: " + response.statusCode());
        System.out.println("RESPONSE body: " + response.getBody().asString());
        ResponseContext.setResponse(response);
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
        ResponseContext.setResponse(response);
    }


    // Метод для получения корзины
    public CartResponse getCart() {
        var cartResponse = ApiClient.get("/api/v1/cart");
        assertThat(cartResponse.statusCode()).isEqualTo(200);
        return cartResponse.as(CartResponse.class);
    }

}

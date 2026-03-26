package ru.sneakerstore.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.ru.*;
import ru.sneakerstore.api.utils.ApiClient;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductSteps {

    @Given("There Is A Product With ID {int}")
    public void productExists(int productId) {
        var response = ApiClient.get("/api/v1/products/" + productId);
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @And("The Response Returns A List Of Products")
    public void responseIsListOfProducts() {
        var response = ResponseContext.getResponse();
        assertThat(response.jsonPath().getList("$")).isNotEmpty();
    }

    @And("Each Product Contains Fields id, name, price")
    public void eachProductHasRequiredFields() {
        var response = ResponseContext.getResponse();
        var products = response.jsonPath().getList("$");
        for (int i = 0; i < products.size(); i++) {
            assertThat(response.jsonPath().getString("[" + i + "].id")).isNotEmpty();
            assertThat(response.jsonPath().getString("[" + i + "].name")).isNotEmpty();
            assertThat(response.jsonPath().getString("[" + i + "].price")).isNotEmpty();
        }
    }

    @And("Field {string} Equally {int}")
    public void fieldEquals(String field, int expectedValue) {
        var response = ResponseContext.getResponse();
        assertThat(response.jsonPath().getInt(field)).isEqualTo(expectedValue);
    }

    @And("each product contains {string} in the title")
    public void eachProductContainsInName(String searchTerm) {
        var response = ResponseContext.getResponse();
        var names = response.jsonPath().getList("name");
        for (Object name : names) {
            assertThat(name.toString().toLowerCase()).contains(searchTerm.toLowerCase());
        }
    }
}
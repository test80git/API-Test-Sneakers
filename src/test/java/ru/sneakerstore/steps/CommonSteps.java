package ru.sneakerstore.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.ru.*;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import ru.sneakerstore.api.utils.ApiClient;
import ru.sneakerstore.dto.response.CartResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    @BeforeAll
    public static void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }


    @Given("I M Sending A GET Request To {string}")
    public void sendGetRequest(String path) {
       var response = ApiClient.get(path);
        ResponseContext.setResponse(response);
    }

    @When("I M Sending A GET Request To {string}, Where id = {int}")
    public void sendGetRequestWithId(String pathTemplate, int id) {
        String path = pathTemplate.replace("{id}", String.valueOf(id));
      var  response = ApiClient.get(path);
        ResponseContext.setResponse(response);
    }

    @Then("RESPONSE STATUS {int}")
    public void statusCode(int statusCode) {
        var response = ResponseContext.getResponse();
        assertThat(response).isNotNull();
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @And("The Response Contains A Field {string}")
    public void responseHasField(String field) {
        var response = ResponseContext.getResponse();
        assertThat(response).isNotNull();
        assertThat(response.jsonPath().getString(field)).isNotEmpty();
    }


    @And("ERROR MESSAGE {string}")
    public void errorMessage(String expectedMessage) {
        var response = ResponseContext.getResponse();
        assertThat(response).isNotNull();
        assertThat(response.jsonPath().getString("message")).contains(expectedMessage);
    }


}
package ru.sneakerstore.steps;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.ru.*;
import io.restassured.response.Response;
import ru.sneakerstore.api.utils.ApiClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {

    private final Hooks hooks = new Hooks();

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String username) {
        hooks.loginAs(username);
    }

    @Given("I Am Sending A Login Request With Data")
    public void loginWithCredentials(io.cucumber.datatable.DataTable data) {
        String username = data.cell(0, 1);
        String password = data.cell(1, 1);

        Response   response = ApiClient.post("/api/v1/auth/login", Map.of(
                "username", username,
                "password", password
        ));
        // Всегда сохраняем ответ, даже если статус 401
        ResponseContext.setResponse(response);
    }


    @And("THE RESPONSE CONTAINS THE ACCESSTOKEN FIELD")
    public void hasAccessToken() {
        var response = ResponseContext.getResponse();
        assertThat(response).isNotNull();
        assertThat(response.jsonPath().getString("token")).isNotEmpty();
    }

    @And("THE TOKEN IS SAVED FOR FURTHER REQUESTS")
    public void saveToken() {
        var response = ResponseContext.getResponse();
        assertThat(response).isNotNull();
        String token = response.jsonPath().getString("token");
        ApiClient.setAuthToken(token);
    }

}
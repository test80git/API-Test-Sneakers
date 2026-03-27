package ru.sneakerstore.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.sneakerstore.api.utils.ApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Hooks {

    @Before
    public void setup() {
        try {
            Properties props = new Properties();
            try (InputStream input = ApiClient.class.getClassLoader()
                    .getResourceAsStream("application-test.properties")) {
                props.load(input);
            }
            RestAssured.baseURI = props.getProperty("api.base.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Этот метод будет вызываться из шага с логином
    public void loginAs(String username) {
        ApiClient.setAuthToken(null);
        String password = "password"; // у всех пользователей одинаковый пароль

        System.out.println("LOGIN: Trying to login as " + username);

        Response response = given()
                .contentType("application/json")
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/api/v1/auth/login");

        System.out.println("LOGIN Response status: " + response.statusCode());
        System.out.println("LOGIN Response body: " + response.getBody().asString());

        if (response.statusCode() == 200) {
            String token = response.jsonPath().getString("token");
            ScenarioContext.setCurrentUser(username);
            ScenarioContext.setCurrentToken(token);
            ApiClient.setAuthToken(token);
            System.out.println("TOKEN SET for: " + username);
        } else {
            throw new RuntimeException("Login failed for user: " + username);
        }
    }

    @After
    public void cleanup(Scenario scenario) {
        if (scenario.isFailed()) {
            var response = ResponseContext.getResponse();
            Allure.addAttachment("Response",
                    response != null ? response.getBody().asString() : "No response");
        }
        ResponseContext.clear();
        ScenarioContext.clear();
    }

}

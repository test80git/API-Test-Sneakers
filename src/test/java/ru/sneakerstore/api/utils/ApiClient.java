package ru.sneakerstore.api.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ApiClient {
    @Setter
    @Getter
    private static String authToken;
    private static String baseUrl;

    static {
        try {
            Properties props = new Properties();
            try (InputStream input = ApiClient.class.getClassLoader()
                    .getResourceAsStream("application-test.properties")) {
                props.load(input);
            }
            baseUrl = props.getProperty("api.base.url");
            RestAssured.baseURI = baseUrl;
            System.out.println("Base URL: " + baseUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Response post(String path, Object body) {
        var request = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", authToken != null ? "Bearer " + authToken : "");

        if (body != null) {
            request.body(body);
        }

        return request.when().post(path);
    }

    public static Response get(String path) {
        return RestAssured.given()
                .header("Authorization", authToken != null ? "Bearer " + authToken : "")
                .when()
                .get(path);
    }

    public static Response delete(String path) {
        return RestAssured.given()
                .header("Authorization", authToken != null ? "Bearer " + authToken : "")
                .when()
                .delete(path);
    }

}

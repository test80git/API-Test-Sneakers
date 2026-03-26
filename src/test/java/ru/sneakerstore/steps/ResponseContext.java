package ru.sneakerstore.steps;

import io.restassured.response.Response;

public class ResponseContext {
    private static final ThreadLocal<Response> currentResponse = new ThreadLocal<>();

    public static void setResponse(Response response) {
        currentResponse.set(response);
    }

    public static Response getResponse() {
        return currentResponse.get();
    }

    public static void clear() {
        currentResponse.remove();
    }
}
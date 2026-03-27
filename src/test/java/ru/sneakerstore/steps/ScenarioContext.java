package ru.sneakerstore.steps;

public class ScenarioContext {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<String> currentToken = new ThreadLocal<>();
    private static final ThreadLocal<Long> lastOrderId = new ThreadLocal<>();


    public static void setCurrentUser(String username) {
        currentUser.set(username);
    }

    public static String getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentToken(String token) {
        currentToken.set(token);
    }

    public static String getCurrentToken() {
        return currentToken.get();
    }

    public static void setLastOrderId(Long id) {
        lastOrderId.set(id);
    }

    public static Long getLastOrderId() {
        return lastOrderId.get();
    }

    public static void clear() {
        currentUser.remove();
        currentToken.remove();
        lastOrderId.remove();
    }
}

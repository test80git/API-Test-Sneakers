package ru.sneakerstore.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import ru.sneakerstore.api.utils.ApiClient;
import ru.sneakerstore.dto.request.CreateOrderRequest;
import ru.sneakerstore.dto.request.PaymentRequest;
import ru.sneakerstore.dto.response.CartResponse;
import ru.sneakerstore.dto.response.OrderResponse;
import ru.sneakerstore.dto.response.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderSteps {

    private OrderResponse lastOrder;
    private Long lastOrderId;


    @When("Create Order From Cart")
    public void createOrder(DataTable data) {
        CreateOrderRequest request = null;
        List<Map<String, String>> rows = data.asMaps();
        for (Map<String, String> row : rows) {
            request = CreateOrderRequest.builder()
                    .promoCode(row.get("promoCode"))
                    .strategyType(row.get("strategyType"))
                    .build();
        }
        Response response = ApiClient.post("/api/v1/orders", request);
        ResponseContext.setResponse(response);

        if (response.statusCode() == 200) {
            OrderResponse order = response.as(OrderResponse.class);
            ScenarioContext.setLastOrderId(order.getId());
            System.out.println("Order created with ID: " + order.getId());
        } else {
            System.out.println("Order creation failed with status: " + response.statusCode());
            System.out.println("Response body: " + response.asString());
        }
        OrderResponse order = response.jsonPath().getObject("", OrderResponse.class);
        OrderContext.setCurrentOrder(order);
        ResponseContext.setResponse(response);
    }

    @And("PAY ORDER")
    public void payOrder(DataTable data) {
        Long orderId = ScenarioContext.getLastOrderId();
        if (orderId == null) {
            throw new IllegalStateException("No order ID available. Please create an order first.");
        }

        PaymentRequest request = null;
        List<Map<String, String>> rows = data.asMaps();
        for (Map<String, String> row : rows) {
            request = PaymentRequest.builder()
                    .cardNumber(row.get("cardNumber"))
                    .expiryDate(row.get("expiryDate"))
                    .cardHolder(row.get("cardHolder"))
                    .build();
        }
        Response response = ApiClient.post("/api/v1/orders/" + orderId + "/pay", request);
        System.out.println("Response PayOrder: " + response);
        ResponseContext.setResponse(response);
    }

    @When("I am requesting a list of my orders")
    public void getUserOrders() {
        Response response = ApiClient.get("/api/v1/orders");
        System.out.println("Response List: " + response.jsonPath().getList("id"));
        ResponseContext.setResponse(response);
    }

    @When("I cancel the order")
    public void cancelOrder() {
        Long orderId = (long) ScenarioContext.getLastOrderId();
        System.out.println("Cancelling order with ID: " + orderId);
        if (orderId == null) {
            throw new IllegalStateException("No order ID available. Please create an order first.");
        }

        Response response = ApiClient.post("/api/v1/orders/" + orderId + "/cancel", null);
        System.out.println("Cancelling order ID: " + orderId);
        ResponseContext.setResponse(response);
    }

    @And("Receive Order By ID")
    public void orderStatus() {
        Long orderId = ScenarioContext.getLastOrderId();
        if (orderId == null) {
            throw new IllegalStateException("No order ID available. Please create an order first.");
        }
        Response response = ApiClient.get("/api/v1/orders/" + orderId);
        ResponseContext.setResponse(response);
    }

    @And("cart is empty")
    public void cartIsEmpty() {
        Response response = ApiClient.get("/api/v1/cart");
        CartResponse cart = response.as(CartResponse.class);
        assertThat(cart.getItems()).isEmpty();
    }


    @Then("RESPONSE ORDER")
    public void statusCode(DataTable data) {
        var response = ResponseContext.getResponse();
        System.out.println("Response ORDER: " + response.asString());
        OrderResponse actualOrder = response.as(OrderResponse.class);

        List<Map<String, String>> rows = data.asMaps();

        if (rows.isEmpty()) {
            // Если таблица пустая, проверяем только что orderNumber существует
            assertThat(actualOrder.getOrderNumber()).isNotEmpty();
            return;
        }

        Map<String, String> expected = rows.get(0);

        // Проверяем только те поля, которые указаны в таблице
        if (expected.containsKey("id") && !expected.get("id").isEmpty()) {
            assertThat(actualOrder.getId()).isEqualTo(Long.parseLong(expected.get("id")));
        }

        if (expected.containsKey("orderNumber") && !expected.get("orderNumber").isEmpty()) {
            assertThat(actualOrder.getOrderNumber()).isEqualTo(expected.get("orderNumber"));
        } else {
            assertThat(actualOrder.getOrderNumber()).isNotEmpty();
        }

        if (expected.containsKey("status") && !expected.get("status").isEmpty()) {
            assertThat(actualOrder.getStatus().name()).isEqualTo(expected.get("status"));
        }

        if (expected.containsKey("subtotal") && !expected.get("subtotal").isEmpty()) {
            assertThat(actualOrder.getSubtotal().stripTrailingZeros())
                    .isEqualTo(new BigDecimal(expected.get("subtotal")).stripTrailingZeros());
        }

        if (expected.containsKey("discount") && !expected.get("discount").isEmpty()) {
            assertThat(actualOrder.getDiscount().stripTrailingZeros())
                    .isEqualTo(new BigDecimal(expected.get("discount")).stripTrailingZeros());
        }

        if (expected.containsKey("total") && !expected.get("total").isEmpty()) {
            assertThat(actualOrder.getTotal().stripTrailingZeros())
                    .isEqualTo(new BigDecimal(expected.get("total")).stripTrailingZeros());
        }
    }

    @And("REQUEST PROCESSED PaymentResponse")
    public void processedRequest(DataTable data) {
        PaymentRequest request = null;
        List<Map<String, String>> rows = data.asMaps();
        for (Map<String, String> row : rows) {
            request = PaymentRequest.builder()
                    .cardNumber(row.get("cardNumber"))
                    .expiryDate(row.get("expiryDate"))
                    .cardHolder(row.get("cardHolder"))
                    .build();
        }
        Response response = ApiClient.post("/api/v1/orders/" + lastOrderId + "/pay", request);
        ResponseContext.setResponse(response);
    }


    @And("Each Order Contains Fields id, orderNumber, status")
    public void eachOrderHasRequiredFields() {
        var response = ResponseContext.getResponse();
        assertThat(response).isNotNull();

        List<OrderResponse> orders = response.jsonPath().getList("", OrderResponse.class);

        for (int i = 0; i < orders.size(); i++) {
            assertThat(response.jsonPath().getString("[" + i + "].id")).isNotEmpty();
            assertThat(response.jsonPath().getString("[" + i + "].orderNumber")).isNotEmpty();
            assertThat(response.jsonPath().getString("[" + i + "].status")).isNotEmpty();
        }
    }


    @And("The Number Of Orders Is Equal {int}")
    public void ordersCount(int expectedCount) {
        // Получаем список всех заказов
        Response response = ApiClient.get("/api/v1/orders");
        List<OrderResponse> orders = response.jsonPath().getList("", OrderResponse.class);
        OrderContext.setUserOrders(orders);

        List<OrderResponse> pendingOrders = orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.PENDING))
                .toList();

        AssertionsForClassTypes.assertThat(pendingOrders.size()).isEqualTo(expectedCount);
    }


}

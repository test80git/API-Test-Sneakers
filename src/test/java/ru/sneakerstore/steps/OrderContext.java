package ru.sneakerstore.steps;

import lombok.Getter;
import lombok.Setter;
import ru.sneakerstore.dto.response.OrderResponse;

import java.util.List;

public class OrderContext {
    @Getter
    @Setter
    private static OrderResponse currentOrder;
    @Getter
    @Setter
    private static List<OrderResponse> userOrders;

}

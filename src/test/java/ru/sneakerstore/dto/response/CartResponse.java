package ru.sneakerstore.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.platform.suite.api.SuiteDisplayName;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuiteDisplayName("Ответ с содержимым корзины")
public class CartResponse {
    private Long cartId;

    private List<CartItemResponse> items;

    private Integer totalItems;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal total;

}
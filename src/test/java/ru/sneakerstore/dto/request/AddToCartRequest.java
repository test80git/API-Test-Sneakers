package ru.sneakerstore.dto.request;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Name("Запрос на добавление товара в корзину")
public class AddToCartRequest {

    private Long productId;
    private Integer sizeRu;
    private Integer quantity;
}
package ru.sneakerstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sneakerstore.dto.model.Brand;
import ru.sneakerstore.dto.model.Model;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private Brand brand;
    private Model model;
    private Integer sizeRu;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private String imageUrl;
}
package ru.sneakerstore.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProductVariant {
    private Long id;
    @JsonIgnore
    private Product product;
    private Integer sizeRu;
    private Integer sizeEur;
    private Integer stockQuantity;

    private String status; // IN_STOCK, OUT_OF_STOCK, COMING_SOON
}
package ru.sneakerstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.platform.suite.api.SuiteDisplayName;
import ru.sneakerstore.dto.model.Brand;
import ru.sneakerstore.dto.model.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuiteDisplayName("Позиция в корзине")
public class CartItemResponse {
    private Long productId;
    private String productName;
    private Brand brand;
    private Model model;
    private Integer sizeRu;
    private Integer sizeEur;
    private String color;
    private String sex;
    @JsonIgnore
    private String countryOfManufacture;
    private String weight;
    private String soleHeight;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
    private String imageUrl;
    private LocalDateTime addedAt;
}

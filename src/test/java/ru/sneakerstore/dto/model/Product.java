package ru.sneakerstore.dto.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;

    @ToString.Exclude
    private Brand brand;

    @ToString.Exclude
    private Model model;
    private String sex;
    private String color;

    private String countryOfManufacture;

    private String weight;

    private String soleHeight;

    private BigDecimal price;
    private String imageUrl;
    private String description;

    @ToString.Exclude
    private List<ProductVariant> variants = new ArrayList<>();

    // Хелпер для добавления варианта
    public void addVariant(ProductVariant variant) {
        variants.add(variant);
        variant.setProduct(this);
    }
}
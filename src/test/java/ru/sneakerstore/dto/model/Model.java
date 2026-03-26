package ru.sneakerstore.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.platform.suite.api.SuiteDisplayName;

import java.util.List;


@Data
@NoArgsConstructor
@SuiteDisplayName("Модель обуви (конкретная модель в рамках бренда)")
public class Model {
    private Long id;
    private String name;
    @JsonIgnore
    @ToString.Exclude
    private Brand brand;
    private String description;
    @JsonIgnore
    private List<Product> products;
}
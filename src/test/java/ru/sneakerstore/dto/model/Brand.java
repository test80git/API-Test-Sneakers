package ru.sneakerstore.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.platform.suite.api.SuiteDisplayName;

import java.util.List;


@Data
@NoArgsConstructor
@SuiteDisplayName("Бренд производитель обуви")
public class Brand {
    private Long id;
    private String name;
    private String country;
    private String description;

    @JsonIgnore
    @ToString.Exclude
    private List<Product> products;
}
package ru.romashkaco.myproducts.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class Product {
    Long id;
    @NotNull(message = "Product name is required!")
    @Size(max = 255, message = "Product name should not exceed 255 characters")
    String name;
    @Size(max = 4096, message = "Product description should not exceed 4096 characters")
    String description;
    @Min(value = 0, message = "Price cannot be negative")
    long price;
    boolean inStock;
}

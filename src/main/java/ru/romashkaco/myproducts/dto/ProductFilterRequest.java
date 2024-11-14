package ru.romashkaco.myproducts.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class ProductFilterRequest {

    @Size(max = 255, message = "Name should not exceed 255 characters")
    String name;

    @Min(value = 0, message = "Price cannot be negative")
    Long minPrice;

    @Min(value = 0, message = "Price cannot be negative")
    Long maxPrice;

    @NotNull(message = "Sort field cannot be null")
    Boolean inStock;

    @Min(value = 0, message = "Page number cannot be negative")
    int page = 1;

    @Min(value = 1, message = "Size should be at least 1")
    int size = 10;

    @NotNull(message = "Sort field cannot be null")
    @Size(max = 50, message = "Sort field name should not exceed 50 characters")
    String sortBy = "name";

    @NotNull(message = "Ascending flag cannot be null")
    private boolean ascending = true;
}

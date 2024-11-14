package ru.romashkaco.myproducts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Size(max = 255, message = "Document name cannot exceed 255 characters")
    @Column(nullable = false)
    String documentName;

    @ManyToOne
    Product product;

    @Min(value = 0, message = "Quantity sold cannot be negative")
    @Column(nullable = false)
    Integer quantitySold;

    @Min(value = 0, message = "Purchase price cannot be negative")
    Long purchasePrice;
}

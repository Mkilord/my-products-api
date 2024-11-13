package ru.romashkaco.myproducts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.romashkaco.myproducts.dto.OperationResponse;
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.service.ProductServiceI;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ProductController {
    ProductServiceI productService;

    @GetMapping
    public List<Product> getAllProduct() {
        log.debug("Controller is called: getAllProduct");
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        log.debug("Controller is called: getProductById");
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        log.debug("Controller is called: createProduct");
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        log.debug("Controller is called: updateProduct");
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public OperationResponse deleteProduct(@PathVariable Long id) {
        log.debug("Controller is called: deleteProduct");
        productService.delete(id);
        return new OperationResponse("Operation successful");
    }
}

package ru.romashkaco.myproducts.service;

import ru.romashkaco.myproducts.model.Product;

import java.util.List;

public interface ProductServiceI {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product create(Product product);

    Product updateProduct(Long id, Product updatedProduct);

    void delete(Long id);
}

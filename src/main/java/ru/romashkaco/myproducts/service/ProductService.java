package ru.romashkaco.myproducts.service;

import ru.romashkaco.myproducts.dto.ProductFilterRequest;
import ru.romashkaco.myproducts.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product create(Product product);

    Product updateProduct(Long id, Product updatedProduct);

    List<Product> getFilteredProducts(ProductFilterRequest filterRequest);

    void delete(Long id);
}

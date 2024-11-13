package ru.romashkaco.myproducts.repository;

import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.romashkaco.myproducts.model.Product;

import java.util.*;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Repository

public class ProductRepositoryImpl implements ProductRepository {
    final Map<Long, Product> productMap = new HashMap<>();
    Long currentId = 1L;

    @Override
    public List<Product> findAll() {
        return List.copyOf(productMap.values());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public Optional<Product> update(Product product) {
        var id = product.getId();
        if (!existsById(id)) return Optional.empty();
        productMap.put(id, product);
        return Optional.of(product);
    }

    @Override
    public Product create(Product product) {
        product.setId(currentId++);
        productMap.put(product.getId(), product);
        return product;
    }

    @Override
    public boolean deleteById(Long id) {
        var removedProduct = productMap.remove(id);
        return Objects.nonNull(removedProduct);
    }

    @Override
    public boolean existsById(Long id) {
        return productMap.containsKey(id);
    }
}

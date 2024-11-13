package ru.romashkaco.myproducts.repository;

import ru.romashkaco.myproducts.model.Product;

import java.util.List;
import java.util.Optional;


public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Optional<Product> update(Product product);

    Product create(Product product);

    boolean deleteById(Long id);

    boolean existsById(Long id);


}

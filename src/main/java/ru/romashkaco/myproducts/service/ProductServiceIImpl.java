package ru.romashkaco.myproducts.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.romashkaco.myproducts.exception.ResourceNotFoundException;
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.repository.ProductRepository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
@AllArgsConstructor
public class ProductServiceIImpl implements ProductServiceI {

    ProductRepository repo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public Product create(Product product) {
        return repo.create(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        updatedProduct.setId(id);
        return repo.update(updatedProduct).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        var notDeleted = !repo.deleteById(id);
        if (notDeleted) throw new ResourceNotFoundException(id);
    }
}

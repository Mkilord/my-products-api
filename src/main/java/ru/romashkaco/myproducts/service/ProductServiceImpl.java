package ru.romashkaco.myproducts.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romashkaco.myproducts.dto.ProductFilterRequest;
import ru.romashkaco.myproducts.exception.ResourceNotFoundException;
import ru.romashkaco.myproducts.model.Product;
import ru.romashkaco.myproducts.repository.ProductRepository;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static ru.romashkaco.myproducts.repository.specification.ProductSpecification.*;

@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    ProductRepository repo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    @Override
    public Product create(Product product) {
        return repo.save(product);
    }

    @Transactional
    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        if (id == null || !repo.existsById(id)) throw new ResourceNotFoundException(id);
        updatedProduct.setId(id);
        return repo.save(updatedProduct);

    }

    @Override
    public List<Product> getFilteredProducts(ProductFilterRequest filReq) {
        var spec = nameContains(filReq.getName())
                .and(priceGraterThanOrEqual(filReq.getMinPrice()))
                .and(priceLessThanOrEqual(filReq.getMaxPrice()))
                .and(inStock(filReq.getInStock()));
        var sortBy = filReq.getSortBy();
        var sort = filReq.isAscending() ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(filReq.getPage(), filReq.getSize(), sort);
        return repo.findAll(spec, pageable).getContent();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        var deletedCount = repo.deleteByIdAndReturnCount(id);
        if (deletedCount == 0) throw new ResourceNotFoundException(id);
    }
}

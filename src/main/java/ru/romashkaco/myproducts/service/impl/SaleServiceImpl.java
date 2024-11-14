package ru.romashkaco.myproducts.service.impl;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romashkaco.myproducts.exception.ResourceNotFoundException;
import ru.romashkaco.myproducts.model.Sale;
import ru.romashkaco.myproducts.repository.ProductRepository;
import ru.romashkaco.myproducts.repository.SaleRepository;
import ru.romashkaco.myproducts.service.SaleService;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SaleServiceImpl implements SaleService {
    SaleRepository saleRepository;
    ProductRepository productRepository;

    @Transactional
    @Override
    public Sale create(Sale sale) {
        var productId = sale.getProduct().getId();
        var product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(productId));
        sale.setPurchasePrice(product.getPrice() * sale.getQuantitySold());
        productRepository.save(product);
        return saleRepository.save(sale);
    }

    @Transactional
    @Override
    public Sale update(Long id, Sale updatedSale) {
        saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        updatedSale.setId(id);
        return saleRepository.save(updatedSale);
    }

    @Transactional
    @Override
    public void deleteSale(Long id) {
        var updatedCount = saleRepository.deleteByIdAndReturnCount(id);
        if (updatedCount == 0) throw new ResourceNotFoundException(id);
    }

    @Override
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }
}

package ru.romashkaco.myproducts.service.impl;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romashkaco.myproducts.exception.ResourceNotFoundException;
import ru.romashkaco.myproducts.model.Supply;
import ru.romashkaco.myproducts.repository.ProductRepository;
import ru.romashkaco.myproducts.repository.SupplyRepository;
import ru.romashkaco.myproducts.service.SupplyService;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SupplyServiceImpl implements SupplyService {

    SupplyRepository supplyRepository;

    ProductRepository productRepository;

    @Transactional
    @Override
    public Supply create(Supply supply) {
        var product = supply.getProduct();
        var productId = product.getId();
        productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(productId));
        product.setInStock(true);
        return supplyRepository.save(supply);
    }

    @Transactional
    @Override
    public Supply update(Long id, Supply updatedSupply) {
        supplyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        updatedSupply.setId(id);
        return supplyRepository.save(updatedSupply);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        var deletedCount = supplyRepository.deleteByIdAndReturnCount(id);
        if (deletedCount == 0) throw new ResourceNotFoundException(id);
    }

    @Override
    public Supply getSupplyById(Long id) {
        return supplyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public List<Supply> getAllSupply() {
        return supplyRepository.findAll();
    }
}

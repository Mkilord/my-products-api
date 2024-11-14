package ru.romashkaco.myproducts.service;

import ru.romashkaco.myproducts.model.Sale;

import java.util.List;

public interface SaleService {
    Sale create(Sale sale);

    Sale update(Long id, Sale sale);

    void deleteSale(Long id);

    Sale getSaleById(Long id);

    List<Sale> getAllSales();
}

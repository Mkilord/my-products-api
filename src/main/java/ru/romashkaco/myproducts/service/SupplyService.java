package ru.romashkaco.myproducts.service;

import ru.romashkaco.myproducts.model.Sale;
import ru.romashkaco.myproducts.model.Supply;

import java.util.List;

public interface SupplyService {
    Supply create(Supply supply);

    Supply update(Long id, Supply supply);

    void delete(Long id);

    Supply getSupplyById(Long id);
    List<Supply> getAllSupply();


}

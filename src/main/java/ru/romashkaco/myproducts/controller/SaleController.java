package ru.romashkaco.myproducts.controller;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.romashkaco.myproducts.model.Sale;
import ru.romashkaco.myproducts.service.SaleService;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/sales")
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SaleController {

    SaleService saleService;

    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleService.create(sale);
    }

    @PutMapping("/{id}")
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        return saleService.update(id, sale);
    }

    @DeleteMapping("/{id}")
    public void deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
    }

    @GetMapping("/{id}")
    public Sale getSale(@PathVariable Long id) {
        return saleService.getSaleById(id);
    }
}

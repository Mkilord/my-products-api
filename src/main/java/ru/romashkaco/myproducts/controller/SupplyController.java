package ru.romashkaco.myproducts.controller;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.romashkaco.myproducts.model.Supply;
import ru.romashkaco.myproducts.service.SupplyService;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/supplies")
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SupplyController {
    SupplyService supplyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Supply createSupply(@RequestBody Supply supply) {
        return supplyService.create(supply);
    }

    @PutMapping("/{id}")
    public Supply updateSupply(@PathVariable Long id, @RequestBody Supply supply) {
        return supplyService.update(id, supply);
    }

    @DeleteMapping("/{id}")
    public void deleteSupply(@PathVariable Long id) {
        supplyService.delete(id);
    }

    @GetMapping("/{id}")
    public Supply getSupply(@PathVariable Long id) {
        return supplyService.getSupplyById(id);
    }
}

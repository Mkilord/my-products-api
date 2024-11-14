package ru.romashkaco.myproducts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.romashkaco.myproducts.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Modifying
    @Query("DELETE FROM Sale s WHERE s.id = :id")
    int deleteByIdAndReturnCount(@Param("id") Long id);
}

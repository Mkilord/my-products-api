package ru.romashkaco.myproducts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.romashkaco.myproducts.model.Supply;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
    @Modifying
    @Query("DELETE FROM Supply s WHERE s.id = :id")
    int deleteByIdAndReturnCount(@Param("id") Long id);
}

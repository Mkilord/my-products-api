package ru.romashkaco.myproducts.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.romashkaco.myproducts.model.Product;

import java.util.Objects;

public class ProductSpecification {
    public static Specification<Product> nameContains(String name) {
        return (root, query, criteria) -> {
            if (Objects.isNull(name) || name.isEmpty()) {
                return criteria.conjunction();
            }
            return criteria.like(criteria.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> priceGraterThanOrEqual(Long price) {
        return (root, query, criteria) -> {
            if (Objects.isNull(price)) {
                return criteria.conjunction();
            }
            return criteria.greaterThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Product> priceLessThanOrEqual(Long price) {
        return (root, query, criteria) -> {
            if (Objects.isNull(price)) {
                return criteria.conjunction();
            }
            return criteria.lessThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Product> inStock(Boolean inStock) {
        return (root, query, criteria) -> {
            if (Objects.isNull(inStock)) {
                return criteria.conjunction();
            }
            return criteria.equal(root.get("inStock"), inStock);
        };
    }
}

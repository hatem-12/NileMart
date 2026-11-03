package org.route;

import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findBySku(String sku);
    Map<String,Product> findAll();
    void save(Product product);

}

package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository repository;
    private final PricingService pricingService;

    public BigDecimal getPrice(String sku, int qty) {
        if (qty<=0){
            throw new StoreException("Invalid quantity.");
        }
        Product product = repository.findBySku(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new StoreException("Product isn't available."));
        return pricingService.getPrice(product, qty);
    }
    public void reduceStock(String sku, int qty) {
        if (qty<=0){
            throw new StoreException("Invalid quantity.");
        }

            Optional<Product> product = repository.findBySku(sku);
            if (product.isEmpty()){
                throw new StoreException("This product not exist");
            }

            if ((product.get().getStockQuantity() - qty) >= 0) {
                product.get().setStockQuantity(product.get().getStockQuantity() - qty);
                repository.save(product.get());
            }
            else {
                throw new StoreException("Quantity larger than stock");
            }

    }
    public Optional<Product> findBySku(String sku){
        return repository.findBySku(sku);
    }
public Map<String,Product> findAll(){
        return repository.findAll();
}
}

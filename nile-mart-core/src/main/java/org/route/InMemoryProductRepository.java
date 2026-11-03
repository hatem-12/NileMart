package org.route;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Primary
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> store = new HashMap<>();

    @PostConstruct
    void saveProducts() {

        Product p1 = Product.builder().name("Smartphone X5 128GB").sku("PH-X5-128").category("Electronics").price(BigDecimal.valueOf(16000)).stockQuantity(12).isActive(true).build();
        Product p2 = Product.builder().name("Fast Charger 20W").sku("ACC-CHG-20W").category("Electronics").price(BigDecimal.valueOf(650)).stockQuantity(40).isActive(true).build();
        Product p3 = Product.builder().name("Wireless Earbuds").sku("ACC-BUDS-6").category("Electronics").price(BigDecimal.valueOf(1100)).stockQuantity(8).isActive(true).build();
        Product p4 = Product.builder().name("Cotton Galabeya").sku("FSH-GAL-001").category("Fashion").price(BigDecimal.valueOf(900)).stockQuantity(25).isActive(true).build();
        Product p5 = Product.builder().name("Non-stick Frying Pan 28cm").sku("HOM-PAN-28").category("Home & Kitchen").price(BigDecimal.valueOf(1200)).stockQuantity(3).isActive(true).build();
        Product p6 = Product.builder().name("Wooden Spice Rack").sku("HOM-SPC-RCK").category("Home & Kitchen").price(BigDecimal.valueOf(750)).stockQuantity(10).isActive(true).build();
        Product p7 = Product.builder().name("Sun Cream SPF50").sku("BTY-SUN-50").category("Beauty").price(BigDecimal.valueOf(350)).stockQuantity(0).isActive(true).build();
        Product p8 = Product.builder().name("The Cairo Trilogy").sku("BK-CAI-TRI").category("Books").price(BigDecimal.valueOf(450)).stockQuantity(15).isActive(true).build();
        store.put(p1.getSku(), p1);
        store.put(p2.getSku(), p2);
        store.put(p3.getSku(), p3);
        store.put(p4.getSku(), p4);
        store.put(p5.getSku(), p5);
        store.put(p6.getSku(), p6);
        store.put(p7.getSku(), p7);
        store.put(p8.getSku(), p8);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(store.get(sku));

    }

    @Override
    public Map<String, Product> findAll() {
        return new HashMap<>(store);
    }


    @Override
    public void save(Product product) {

            store.put(product.getSku(), product);

    }


    @PreDestroy
    void printCatalog() {
        System.out.println("Catalog closed");
    }


}

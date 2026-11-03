package org.route;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Component
@Scope("prototype")
public class ShoppingCart {
    private  String cartNumber;
    private Customer customer;
    @Getter(AccessLevel.NONE)
    private final Map<String, Integer> cartProducts = new HashMap<>();
    private final ProductService productService;
    private final int maxQuantityPerProduct;
private  boolean checkedOut = false;
    public ShoppingCart(
            ProductService productService,
            @Value("${store.max-quantity-per-product}") int maxQuantityPerProduct) {

        this.productService = productService;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }

    public void initialize(String cartNumber, Customer customer) {
        this.cartNumber = cartNumber;
        this.customer = customer;
    }

    public void addItem(String sku, int qty) {
        if (checkedOut) {
            throw new StoreException("This cart has already been checked out");
        }
        if (qty <= 0) {
            throw new StoreException("Invalid qty");
        }
        Optional<Product> product = productService.findBySku(sku);
        if (product.isEmpty()) {
            throw new StoreException("Product not found in stock");
        }
        if (product.get().getStockQuantity() <= 0) {
            throw new StoreException("Product finished from stock");
        }
        if (!product.get().isActive()){
            throw new StoreException("Product not active");
        }
        int lastAmount = cartProducts.getOrDefault(sku,0);
        int newAmount = lastAmount + qty;

            if (newAmount > maxQuantityPerProduct) {
                throw new StoreException("Amount can't be larger than "+maxQuantityPerProduct);
            }
            if (newAmount <= product.get().getStockQuantity()) {
                cartProducts.put(sku, newAmount);

            } else {
                throw new StoreException("New amount larger than stock ");
            }

    }

    public BigDecimal subtotal() {
        BigDecimal subtotal = BigDecimal.valueOf(0);
        for (Map.Entry<String, Integer> p : cartProducts.entrySet()) {
            subtotal = subtotal.add(productService.getPrice(p.getKey(), p.getValue()));
        }
        return subtotal;
    }
    public void closeCart() {
        this.checkedOut = true;
    }
    public boolean isEmpty() {
        return cartProducts.isEmpty();
    }

    public void clear() {
        cartProducts.clear();
    }

    public Map<String, Integer> getLines() {
        return new HashMap<>(cartProducts);
    }
}


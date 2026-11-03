package org.route;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
    private final String sku;
    private final String name;
    private final String category;
    private BigDecimal price;
    private int stockQuantity;
    private boolean isActive;
   }

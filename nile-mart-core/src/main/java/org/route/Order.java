package org.route;

import java.math.BigDecimal;
import java.util.Map;

public record Order(String orderNumber, Customer customer, Governorates governorate,Map<String ,Integer> lines ,
                    BigDecimal subtotal,BigDecimal vat,BigDecimal shippingFee,BigDecimal total) {
    public Order {
        lines = Map.copyOf(lines);
    }
}

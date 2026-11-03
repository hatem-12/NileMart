package org.route;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FreeShipping implements ShippingCalculator{

    @Override
    public BigDecimal calculate(Governorates g) {
        return BigDecimal.valueOf(0);
    }
}

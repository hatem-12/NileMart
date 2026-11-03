package org.route;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@Primary

public class ZoneShipping implements ShippingCalculator {
    @Override
    public BigDecimal calculate(Governorates g) {
        return g.getShippingZone().getFee();
    }
}

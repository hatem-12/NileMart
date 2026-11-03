package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingQuoteService {
    private final ShippingCalculator zoneShipping;
    private final ShippingCalculator freeShipping;
    private final BigDecimal threshold;

    public ShippingQuoteService(ShippingCalculator zoneShipping,@Qualifier("freeShipping")ShippingCalculator freeShipping,@Value("${store.free-shipping-threshold}") BigDecimal threshold) {
        this.zoneShipping = zoneShipping;
        this.freeShipping = freeShipping;
        this.threshold = threshold;
    }
    public BigDecimal quote(Governorates g,BigDecimal subtotal){
       if (subtotal.compareTo(threshold) >= 0 && g.getShippingZone().getFreeShippingFlag()){
return freeShipping.calculate(g);
       }
       else {
           return zoneShipping.calculate(g);
       }
    }
}

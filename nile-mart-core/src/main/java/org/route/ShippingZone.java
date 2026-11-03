package org.route;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Getter
public enum ShippingZone {
    GREATER_CAIRO(BigDecimal.valueOf(50),true),
    ALEX_DELTA(BigDecimal.valueOf(65),true),
    CANAL(BigDecimal.valueOf(70),true),
    UPPER_EGYPT(BigDecimal.valueOf(85),true),
    REMOTE(BigDecimal.valueOf(120),false);

    private final BigDecimal fee;
    private final Boolean freeShippingFlag;

    ShippingZone(BigDecimal fee, Boolean freeShippingFlag) {
        this.fee = fee;
        this.freeShippingFlag = freeShippingFlag;
    }

}

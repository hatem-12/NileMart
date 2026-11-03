package org.route;

public enum Governorates {

        CAIRO(ShippingZone.GREATER_CAIRO),
        GIZA(ShippingZone.GREATER_CAIRO),
        QALYUBIA(ShippingZone.GREATER_CAIRO),

        ALEXANDRIA(ShippingZone.ALEX_DELTA),
        BEHEIRA(ShippingZone.ALEX_DELTA),
        KAFR_EL_SHEIKH(ShippingZone.ALEX_DELTA),
        DAKAHLIA(ShippingZone.ALEX_DELTA),
        DAMIETTA(ShippingZone.ALEX_DELTA),
        GHARBIA(ShippingZone.ALEX_DELTA),
        MONUFIA(ShippingZone.ALEX_DELTA),
        SHARQIA(ShippingZone.ALEX_DELTA),

        PORT_SAID(ShippingZone.CANAL),
        ISMAILIA(ShippingZone.CANAL),
        SUEZ(ShippingZone.CANAL),

        FAIYUM(ShippingZone.UPPER_EGYPT),
        BENI_SUEF(ShippingZone.UPPER_EGYPT),
        MINYA(ShippingZone.UPPER_EGYPT),
        ASYUT(ShippingZone.UPPER_EGYPT),
        SOHAG(ShippingZone.UPPER_EGYPT),
        QENA(ShippingZone.UPPER_EGYPT),
        LUXOR(ShippingZone.UPPER_EGYPT),
        ASWAN(ShippingZone.UPPER_EGYPT),

        MATROUH(ShippingZone.REMOTE),
        NORTH_SINAI(ShippingZone.REMOTE),
        SOUTH_SINAI(ShippingZone.REMOTE),
        RED_SEA(ShippingZone.REMOTE),
        NEW_VALLEY(ShippingZone.REMOTE);
        private final ShippingZone shippingZone;

    Governorates(ShippingZone shippingZone) {
        this.shippingZone = shippingZone;
    }
    public ShippingZone getShippingZone() {
        return shippingZone;
    }
}

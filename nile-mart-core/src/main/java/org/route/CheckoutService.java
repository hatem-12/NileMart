package org.route;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> notificationSenders;

    private void rechekStock(ShoppingCart cart) {
        if (cart.isEmpty())
            throw new StoreException("Cart is empty");

        for (Map.Entry<String, Integer> line : cart.getLines().entrySet()) {
          Product product=  productService.findBySku(line.getKey()).orElseThrow(() -> new StoreException("Not found this product"));
            if (product.getStockQuantity() < line.getValue()) {
                throw new StoreException("Product quantity in cart larger than stock");
            }
            if (!product.isActive())
throw new StoreException("Product not active");
        }
    }

    private BigDecimal calculateTotalPrice(BigDecimal subtotal, BigDecimal tax, BigDecimal shippingFee) {
        return subtotal.add(tax).add(shippingFee).setScale(2,RoundingMode.HALF_UP);
    }

    private void reducesStock(ShoppingCart cart) {
        for (Map.Entry<String, Integer> r : cart.getLines().entrySet()) {
            productService.reduceStock(r.getKey(), r.getValue());
        }
    }

    private Order saveOrder(ShoppingCart cart, Governorates g, Map<String, Integer> cartMap,BigDecimal subtotal,BigDecimal tax,BigDecimal shippingFee, BigDecimal total) {
        String orderNumber = orderRegistry.createOrderNumber();
        Order order = new Order(orderNumber, cart.getCustomer(), g, cartMap, subtotal,tax, shippingFee, total);
        orderRegistry.addOrder(order);
        return order;
    }

    private void notifiesSender( Order order) {
        DecimalFormatSymbols symbols =
                DecimalFormatSymbols.getInstance(Locale.US);

        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);

        String message = "Order " + order.orderNumber()
                + " confirmed. Total "
                + df.format(order.total())
                + " EGP";

        for (NotificationSender sender : notificationSenders) {
            sender.send(order.customer(), message);
        }
    }

    private void clearsTheCart(ShoppingCart cart) {
        cart.clear();
    }
    private void printOrderSummary(Order order) {

        DecimalFormatSymbols symbols =
                DecimalFormatSymbols.getInstance(Locale.US);

        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);

        System.out.println("\n========== ORDER SUMMARY ==========");
        System.out.println("Order Number: " + order.orderNumber());
        System.out.println("-----------------------------------");

        System.out.printf("%-20s %12s EGP%n", "Subtotal:", df.format(order.subtotal()));
        System.out.printf("%-20s %12s EGP%n", "VAT (14%):", df.format(order.vat()));
        System.out.printf("%-20s %12s EGP%n", "Shipping:", df.format(order.shippingFee()));

        System.out.println("-----------------------------------");
        System.out.printf("%-20s %12s EGP%n", "TOTAL:", df.format(order.total()));
        System.out.println("===================================\n");
    }
    @Synchronized
    public void placeOrder(ShoppingCart cart, Governorates governorates) {

        Map<String, Integer> cartMap = new HashMap<>(cart.getLines());
        if(cart.isCheckedOut()){
            throw new StoreException("This cart has already been checked out");
        }
        rechekStock(cart);
        BigDecimal subtotal = cart.subtotal();
        BigDecimal tax = taxCalculator.calculateVat(subtotal);
        BigDecimal shippingFee = shippingQuoteService.quote(governorates, subtotal);
        BigDecimal total = calculateTotalPrice(subtotal, tax, shippingFee);
        reducesStock(cart);
        Order order = saveOrder(cart, governorates, cartMap,subtotal,tax,shippingFee, total);
        printOrderSummary(order);
        notifiesSender(order);
        System.out.println("Checkout completed successfully!");
        clearsTheCart(cart);
        cart.closeCart();
    }
}

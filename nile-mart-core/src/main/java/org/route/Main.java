package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public void printHomeScreen() {
        System.out.println("========== NILEMART ========== \n" +
                "1. Show products \n" +
                "2. Create a cart \n" +
                "3.Add a product to a cart \n" +
                "4. Show a cart \n" +
                "5. Check out a cart \n" +
                "0. Exit\n" +
                "======================\n" +
                "Choice operation,please");
    }

    public void printProducts(ProductService productService) {
        Map<String, Product> products = productService.findAll();
        if (products.isEmpty()) {
            System.out.println("no product found.");
            return;
        }
        System.out.println("============================================================================================");
        System.out.printf("%-18s %-25s %-15s %-12s %-8s %-10s%n",
                "SKU", "PRODUCT NAME", "CATEGORY", "PRICE (EGP)", "STOCK","Activate");
        System.out.println("============================================================================================");

        for (Product product : products.values()) {
            System.out.printf("%-18s %-25s %-15s %-12.2f %-8d %-10s%n",
                    product.getSku(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.isActive());
            System.out.println("--------------------------------------------------------------------------------------------");
        }
        System.out.println("============================================================================================");

    }

    public ShoppingCart createCart(ShoppingCart cart, String cartNumber, Scanner scanner) {

        String customerName;
        String customerPhone;
        String email = null;
        System.out.println("Enter customer name:");
        customerName = scanner.nextLine().trim();
        if ( customerName.isEmpty()|| !customerName.trim().matches("[\\p{L} ]+")) {
            throw new StoreException("Invalid name");
        }
        System.out.println("Enter customer phone:");
        customerPhone = scanner.nextLine().trim();
        if (customerPhone.isEmpty()||
                !customerPhone.matches("01\\d{9}")) {
            throw new StoreException("Invalid phone");
        }
        System.out.println("Do you have an email? (true/false)");
        String answer = scanner.nextLine().trim();
        if (!answer.equalsIgnoreCase("true") &&
                !answer.equalsIgnoreCase("false")) {
            throw new StoreException("Please enter true or false");
        }
        boolean emailOrNot = Boolean.parseBoolean(answer);
        if (emailOrNot) {
            System.out.println("Enter email:");
            email = scanner.nextLine().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new StoreException("Invalid email");
            }
        }

        Customer customer = Customer.builder()
                .fullName(customerName.trim())
                .phone(customerPhone.trim())
                .email(email)
                .build();

        cart.initialize(cartNumber, customer);

        System.out.println("Cart created successfully with id = " + cart.getCartNumber());
        return cart;

    }

    public void addProduct(Map<Integer, ShoppingCart> carts, Scanner scanner) {

        System.out.println("Please enter Cart ID:");
        int cartId;

        try {
            cartId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new StoreException("Invalid cart ID");
        }

        ShoppingCart cart = carts.get(cartId);

        if (cart == null) {
            throw new StoreException("Cart not found");
        }
        if (cart.isCheckedOut()) {
            throw new StoreException("This cart has already been checked out");
        }
        System.out.println("Please enter SKU:");
        String sku = scanner.nextLine().trim();

        System.out.println("Please enter quantity:");

        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new StoreException("Invalid quantity");
        }

        if (qty <= 0) {
            throw new StoreException("Quantity must be greater than zero");
        }

        cart.addItem(sku, qty);

        System.out.println("Product added successfully");
    }

    public void showCart(Map<Integer, ShoppingCart> carts, Scanner scanner,ProductService productService) {
        int cartId;
        System.out.println("Enter cart ID:");
        try {
            cartId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new StoreException("Invalid cart ID");
        }

        ShoppingCart cart = carts.get(cartId);
        if (cart == null) {
            throw new StoreException("Cart not found");
        }
        if (cart.isCheckedOut()) {
            throw new StoreException("This cart has already been checked out");
        }
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        System.out.println("\n================ NILEMART CART ================");
        System.out.println("Cart ID: " + cart.getCartNumber());
        System.out.println("---------------------------------------------------------------");

        System.out.printf("%-18s %-25s %-8s %-15s%n",
                "SKU", "PRODUCT NAME", "QTY", "LINE TOTAL");

        System.out.println("---------------------------------------------------------------");

        for (Map.Entry<String, Integer> item : cart.getLines().entrySet()) {
            Product product = productService
                    .findBySku(item.getKey().trim())
                    .orElseThrow(() -> new StoreException("Product not found"));

            BigDecimal lineTotal = productService
                    .getPrice(item.getKey().trim(), item.getValue());

            System.out.printf("%-18s %-25s %-8d %-15.2f%n",
                    product.getSku(),
                    product.getName(),
                    item.getValue(),

                    lineTotal);
        }

        System.out.println("---------------------------------------------------------------");

        System.out.printf("%-51s %,.2f EGP%n",
                "CART SUBTOTAL:", cart.subtotal());

        System.out.println("===============================================================");
    }

    public void checkOutCart(Map<Integer, ShoppingCart> allCarts, Scanner scanner, CheckoutService checkoutService) {
        int cartId;
        System.out.println("Enter cart ID:");
        try {
            cartId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new StoreException("Invalid cart ID");
        }

        ShoppingCart cart = allCarts.get(cartId);
        if (cart == null) {
            throw new StoreException("Cart not found");
        }
        if (cart.isCheckedOut()) {
            throw new StoreException("This cart has already been checked out");
        }
        if (cart.isEmpty()) {
            System.out.println("Can't check out for empty cart!");
            return;
        }
        System.out.print("Choose governorate: ");
        Governorates[] governorates = Governorates.values();

        for (int i = 0; i < governorates.length; i++) {
            System.out.println((i + 1) + ". " + governorates[i]);
        }
        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new StoreException("Invalid governorate choice");
        }
        if (choice < 1 || choice > governorates.length) {
            throw new StoreException("Invalid governorate");
        }
        Governorates selectedGovernorate = governorates[choice - 1];
        checkoutService.placeOrder(cart, selectedGovernorate);
    }

    void main() {
        Map<Integer, ShoppingCart> allCarts = new HashMap<>();
        int cartNumber = 1;
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)){
        ProductService productService = context.getBean(ProductService.class);
        CheckoutService checkoutService = context.getBean(CheckoutService.class);
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            printHomeScreen();
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            try {

                switch (choice) {
                    case 0:
                        return;
                    case 1:
                        printProducts(productService);
                        break;
                    case 2:
                        allCarts.put(cartNumber, createCart(
                                context.getBean(ShoppingCart.class),
                                String.valueOf(cartNumber),
                                scanner
                        ));
                        ++cartNumber;
                        break;
                    case 3:
                        addProduct(allCarts, scanner);
                        break;
                    case 4:
                        showCart(allCarts, scanner,productService);
                        break;
                    case 5:
                        checkOutCart(allCarts, scanner, checkoutService);
                        break;
                    default:
                        System.out.println("Invalid operation choice.");
                }

            } catch (StoreException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (true);
        }
    }

}
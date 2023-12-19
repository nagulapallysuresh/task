package com.marolix.supermarket.service;

import com.marolix.supermarket.model.Product;
import com.marolix.supermarket.repository.ProductRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void displayProducts() {
        try {
            List<Product> products = ProductRepository.fetchProducts();
            System.out.println("\nProducts:");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (Exception e) {
            handleSQLException(e);
        }
    }

    public static void findProductById() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to find: ");
        int productId = scanner.nextInt();

        try {
            Product product = ProductRepository.findProductById(productId);

            if (product != null) {
                System.out.println("Product found:");
                System.out.println(product);
            } else {
                System.out.println("Product not found.");
            }
        } catch (Exception e) {
            handleSQLException(e);
        }
    }

    public static void calculateTotalRevenue() {
        try {
            List<Product> products = ProductRepository.fetchProducts();
            double totalRevenue = products.stream().mapToDouble(Product::getPrice).sum();

            System.out.println("Total Revenue: $" + totalRevenue);
        } catch (Exception e) {
            handleSQLException(e);
        }
    }

    public static void displayCheapestProduct() {
        try {
            Product cheapestProduct = ProductRepository.findCheapestProduct();
            if (cheapestProduct != null) {
                System.out.println("Cheapest Product:");
                System.out.println(cheapestProduct);
            } else {
                System.out.println("No products available.");
            }
        } catch (Exception e) {
            handleSQLException(e);
        }
    }

    public static void updateProductPrice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to update price: ");
        int productId = scanner.nextInt();

        System.out.print("Enter new product price: $");
        double newPrice = scanner.nextDouble();

        try {
            boolean success = ProductRepository.updateProductPrice(productId, newPrice);

            if (success) {
                System.out.println("Product price updated successfully.");
            } else {
                System.out.println("Failed to update product price. (Check if the ID exists)");
            }
        } catch (Exception e) {
            handleSQLException(e);
        }
    }

    public static void addProduct(String productName, double productPrice) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(productPrice);

        try {
            boolean success = ProductRepository.addProduct(product);

            if (success) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (Exception e) {
            handleSQLException(e);
        }
    }

    public static boolean updateProduct(int productId, double newPrice) {
        String updateQuery = "UPDATE products SET price = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, productId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public static boolean deleteProduct(int productId) {
        String deleteQuery = "DELETE FROM products WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }
    public static void generateBill() {
        Scanner scanner = new Scanner(System.in);
        List<Product> products = ProductRepository.fetchProducts();

        if (products.isEmpty()) {
            System.out.println("No products available. Cannot generate a bill.");
            return;
        }

        System.out.println("Select products for the bill (enter product IDs separated by commas):");
        displayProducts();

        System.out.print("Enter product IDs (comma-separated): ");
        String productIdsInput = scanner.nextLine();
        String[] productIdsArray = productIdsInput.split(",");

        double totalCost = 0;

        try {
            for (String productIdStr : productIdsArray) {
                int productId = Integer.parseInt(productIdStr.trim());
                Product product = ProductRepository.findProductById(productId);

                if (product != null) {
                    System.out.println("Product added to the bill: " + product);
                    totalCost += product.getPrice();
                } else {
                    System.out.println("Product with ID " + productId + " not found.");
                }
            }

            System.out.println("\nTotal Cost: $" + totalCost);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid product IDs.");
        } catch (Exception e) {
            handleSQLException(e);
        }
    }
    private static void handleSQLException(Exception e) {
        e.printStackTrace();  
        System.out.println("An error occurred with the database: " + e.getMessage());
    }
}

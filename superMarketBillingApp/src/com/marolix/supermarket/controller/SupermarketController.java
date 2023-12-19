package com.marolix.supermarket.controller;

import com.marolix.supermarket.repository.ProductRepository;
import com.marolix.supermarket.service.ProductService;

import java.util.Scanner;

public class SupermarketController {
    public static void main(String[] args) {
        ProductRepository.createProductTable();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Supermarket Products:");
            System.out.println("1. Display Products");
            System.out.println("2. Add Product");
            System.out.println("3. Update Product Price");
            System.out.println("4. Delete Product");
            System.out.println("5. Generate Bill");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ProductService.displayProducts();
                    break;
                case 2:
                    System.out.println("Adding a new product...");
                    System.out.print("Enter product name: ");
                    scanner.nextLine();  // Consume the newline character
                    String productName = scanner.nextLine();
                    System.out.print("Enter product price: $");
                    double productPrice = scanner.nextDouble();
                    ProductService.addProduct(productName, productPrice);
                    break;
                case 3:
                    ProductService.updateProductPrice();
                    break;
                case 4:
                    System.out.println("Deleting a product...");
                    System.out.print("Enter product ID to delete: ");
                    int productIdToDelete = scanner.nextInt();
                    ProductService.deleteProduct(productIdToDelete);
                    break;
                case 5:
                    ProductService.generateBill();
                    break;
                case 0:
                    System.out.println("Exiting... Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }
}

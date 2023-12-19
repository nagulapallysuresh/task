package com.marolix.supermarket.repository;

import com.marolix.supermarket.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void createProductTable() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "price DOUBLE NOT NULL)";
            statement.executeUpdate(createTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Product> fetchProducts() {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM products";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                products.add(new Product(id, name, price));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void addProduct(String name, double price) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO products (name, price) VALUES (?, ?)")) {

            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public static boolean addProduct(Product product) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO products (name, price) VALUES (?, ?)")) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void updateProduct(Product product) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE products SET name = ?, price = ? WHERE id = ?")) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int productId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM products WHERE id = ?")) {

            preparedStatement.setInt(1, productId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateProductPrice(int productId, double newPrice) {
        String updateQuery = "UPDATE products SET price = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, productId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Product findCheapestProduct() {
        String selectQuery = "SELECT * FROM products ORDER BY price LIMIT 1";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                return new Product(id, name, price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Product findProductById(int productId) {
        String selectQuery = "SELECT * FROM products WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                return new Product(id, name, price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


  
}

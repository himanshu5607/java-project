package com.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Ap {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/trail"; // Database name is user_registration
        String username = "root"; // Replace with your DB username
        String password = ""; // Replace with your DB password

        Scanner scanner = new Scanner(System.in);

        // Establish a connection
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

            // Display options to the user
            System.out.println("Choose an option: ");
            System.out.println("1: Insert a new record");
            System.out.println("2: Update an existing record");
            System.out.println("3: Delete a record");
            System.out.println("4: Display all records");

            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline character after number

            switch (option) {
                case 1:
                    // Insert a new record
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String passwordInput = scanner.nextLine();
                    System.out.print("Confirm password: ");
                    String confirmPassword = scanner.nextLine();

                    // Check if password and confirm_password match
                    if (!passwordInput.equals(confirmPassword)) {
                        System.out.println("Passwords do not match. Try again.");
                        break;
                    }

                    String insertQuery = "INSERT INTO users (email, password, confirm_password) VALUES ('" 
                                        + email + "', '" + passwordInput + "', '" + confirmPassword + "')";
                    try (Statement statement = connection.createStatement()) {
                        int rowsInserted = statement.executeUpdate(insertQuery);
                        if (rowsInserted > 0) {
                            System.out.println("A new record was inserted successfully!");
                        }
                    }
                    break;

                case 2:
                    // Update an existing record
                    System.out.print("Enter ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();  // Consume newline character
                    System.out.print("Enter new email: ");
                    String updateEmail = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String updatePassword = scanner.nextLine();
                    System.out.print("Confirm new password: ");
                    String updateConfirmPassword = scanner.nextLine();

                    // Ensure passwords match
                    if (!updatePassword.equals(updateConfirmPassword)) {
                        System.out.println("Passwords do not match. Try again.");
                        break;
                    }

                    String updateQuery = "UPDATE users SET email = '" + updateEmail + "', password = '" 
                                        + updatePassword + "', confirm_password = '" 
                                        + updateConfirmPassword + "' WHERE id = " + updateId;
                    try (Statement statement = connection.createStatement()) {
                        int rowsUpdated = statement.executeUpdate(updateQuery);
                        if (rowsUpdated > 0) {
                            System.out.println("The record was updated successfully!");
                        }
                    }
                    break;

                case 3:
                    // Delete a record
                    System.out.print("Enter ID to delete: ");
                    int deleteId = scanner.nextInt();
                    String deleteQuery = "DELETE FROM users WHERE id = " + deleteId;
                    try (Statement statement = connection.createStatement()) {
                        int rowsDeleted = statement.executeUpdate(deleteQuery);
                        if (rowsDeleted > 0) {
                            System.out.println("The record was deleted successfully!");
                        }
                    }
                    break;

                case 4:
                    // Select and display all records
                    String selectQuery = "SELECT * FROM users";
                    try (Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery(selectQuery)) {

                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String userEmail = resultSet.getString("email");
                            String userPassword = resultSet.getString("password");
                            String userConfirmPassword = resultSet.getString("confirm_password");
                            String createdAt = resultSet.getString("created_at");
                            System.out.println("ID: " + id + ", Email: " + userEmail 
                                               + ", Password: " + userPassword 
                                               + ", Confirm Password: " + userConfirmPassword 
                                               + ", Created At: " + createdAt);
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        scanner.close();
    }
}

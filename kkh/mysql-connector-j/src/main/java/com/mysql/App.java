package com.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class App {

    public static void main(String[] args) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/trail";

        String username = "root";

        String password = "";



        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

            String query = "SELECT * FROM users";

            try (Statement statement = connection.createStatement();

                 ResultSet resultSet = statement.executeQuery(query)) {



                while (resultSet.next()) {

                    int id = resultSet.getInt("id");

                    String name = resultSet.getString("email");
                    String nameq = resultSet.getString("password");
                    //String name3 = resultSet.getString("confirm_password");
                    String name4 = resultSet.getString("created_at");




                    System.out.println("ID: " + id + ", Name: " + name  + ", password: " + nameq +", time:" + name4);


                }

            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
    


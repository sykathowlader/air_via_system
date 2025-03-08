package com.example.login;

import javafx.scene.control.Alert;

import java.sql.*;

/**
 * This class represents a connection to a MySQL database and provides methods to establish and close the connection.
 * @author [Ali Miftah, adapted by Sykat Howlader]
 */
public class DatabaseConnectivity {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseUser = "root"; // Use "airvia_user" 
        String databasePassword = ""; // "airvia_pass"
        String url = "jdbc:mysql://localhost:3306/air_via_db"; // Local MySQL URL

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Connected to MySQL database successfully.");
        } catch (Exception e) {
            e.printStackTrace(); // Look at the console output for the specific error
            System.out.println("Error details: " + e.getMessage()); // Print the error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not connect to the database: " + e.getMessage());
            alert.showAndWait();
        }

        return databaseLink;
    }

    public void closeConnection() {
        try {
            if (databaseLink != null && !databaseLink.isClosed()) {
                databaseLink.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
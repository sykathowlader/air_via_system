package com.example.login;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.SQLException;

/**
 * The AdminDatabaseController class controls the functionality of the database in the Admin application.
 * @author [Sykat Howlader]
 */

public class AdminDatabaseController {
    AdminHomeController controller = new AdminHomeController();

    public void goToAdminBlank(ActionEvent event) throws IOException {
        controller.goToAdminBlank(event);
    }

    public void goToAdminHome(ActionEvent event) throws IOException {
        controller.goToAdminHome(event);
    }
    public void goToAdminAdvisor(ActionEvent event) throws IOException {
        controller.goToAdminAdvisor(event);
    }
    public void goToAdminDatabase(ActionEvent event) throws IOException {
        controller.goToAdminDatabase(event);
    }
    public void goToAdminStock(ActionEvent event) throws IOException, SQLException {
        controller.goToAdminStock(event);
    }
    public void goToAdminLogout(ActionEvent event) throws IOException {
        AdvisorLogoutController logoutController = new AdvisorLogoutController();
        logoutController.logout(event);
    }


    private static final String DB_USER = "in2018g17_a";
    private static final String DB_PASSWORD = "ac33CKRy";
    private static final String DB_NAME = "in2018g17";

    public void handleBackupButton(ActionEvent event) {
        // Construct the command to backup the database
        String[] command = {"mysqldump", "-u" + DB_USER, "-p" + DB_PASSWORD, DB_NAME, "-r", "backup.sql"};

        try {
            // Execute the command to backup the database
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            Process process = pb.start();

            // Wait for the command to complete
            int exitCode = process.waitFor();

            // Check the exit code of the command
            if (exitCode == 0) {
                // Backup was successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Database Backup");
                alert.setHeaderText(null);
                alert.setContentText("Database backup was successful.");
                alert.showAndWait();
            } else {
                // Backup failed
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Backup");
                alert.setHeaderText(null);
                alert.setContentText("Database backup failed.");
                alert.showAndWait();

                // Print error stream
                InputStream errorStream = process.getErrorStream();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println(errorLine);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Error backing up database: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Backup Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while backing up the database.");
            alert.showAndWait();
        }
    }






}

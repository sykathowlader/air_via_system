package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The AdminBlankController class controls the functionality of blanks in the Admin application.
 * @author [Sykat Howlader]
 */
public class AdminBlankController {

    @FXML
    TextField adFromBlankField;
    @FXML
    TextField adToBlankField;
    AdminHomeController controller = new AdminHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    public void addBlanksToDatabase(ActionEvent event) {
        // Get the values entered by the user
        String fromBlank = adFromBlankField.getText().trim();
        String toBlank = adToBlankField.getText().trim();

        // Check that the values entered are 11-digit numbers
        if (!fromBlank.matches("\\d{11}") || !toBlank.matches("\\d{11}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter 11-digit numbers");
            alert.showAndWait();
            return;
        }

        // Check that the range is not already in the database
        try (PreparedStatement stmt = connDB.prepareStatement("SELECT COUNT(*) FROM Blank WHERE BlankNumber BETWEEN ? AND ?")) {
            stmt.setString(1, fromBlank);
            stmt.setString(2, toBlank);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Some blanks already exist in stock");
                alert.showAndWait();
                return;
            }
        } catch (SQLException ex) {
            // Handle exceptions appropriately
        }

        // Range is not in database, proceed to add blanks
        try {
            long from = Long.parseLong(fromBlank);
            long to = Long.parseLong(toBlank);

            PreparedStatement stmt = connDB.prepareStatement("INSERT INTO Blank (BlankNumber) VALUES (?)");
            int count = 0; // Counter variable for blanks added
            for (long i = from; i <= to; i++) {
                stmt.setLong(1, i);
                stmt.executeUpdate();
                count++; // Increment the counter for each blank added
            }
            stmt.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, count + " blanks added successfully");
            alert.showAndWait();
            goToAdminBlank(event);
        } catch (NumberFormatException ex) {
            // Handle exceptions appropriately
        } catch (SQLException ex) {
            // Handle exceptions appropriately
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }







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
}

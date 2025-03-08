package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**

 This class is the controller for the Manager Discount view. It handles user interactions with the view,
 and communicates with the database to add or delete discounts.
 @author [Sykat Howlader]
 */

public class ManagerDiscountController implements Initializable {

    @FXML
    ChoiceBox disTypeChoice;
    @FXML
    TextField disPercentageField;
    @FXML
    ChoiceBox disDelType;
    @FXML
    ChoiceBox disDelPercentage;

    ManagerHomeController controller = new ManagerHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();


    public void addDiscount (ActionEvent event) throws SQLException, IOException {
        if (disPercentageField.getText().isBlank() || disTypeChoice.getValue() == null) {
            // Display an error message if any of the required fields are blank
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter values for all fields.");
            alert.showAndWait();
            return;
        }

        int percentage = 0;
        try {
            percentage = Integer.parseInt(disPercentageField.getText());
        } catch (NumberFormatException e) {
            // Display an error message if the text in the percentage field is not a valid integer
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for percentage.");
            alert.showAndWait();
            return;
        }

        String type = disTypeChoice.getValue().toString();

        // Insert the commission into the database
        PreparedStatement pstmt = connDB.prepareStatement("INSERT INTO Discount (Type, Percentage) VALUES (?, ?)");
        pstmt.setString(1, type);
        pstmt.setInt(2, percentage);
        int insertedRows = pstmt.executeUpdate();

        // Display a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, insertedRows + " Discount added successfully.");
        alert.showAndWait();
        goToManagerDiscount(event);
    }


    public void deleteDiscount (ActionEvent event) throws SQLException, IOException {
        String selectedType = (String) disDelType.getValue();
        int selectedPercentage = (int) disDelPercentage.getValue();

        // Delete the commission from the database
        PreparedStatement pstmt = connDB.prepareStatement("DELETE FROM Discount WHERE Type = ? AND Percentage = ?");
        pstmt.setString(1, selectedType);
        pstmt.setInt(2, selectedPercentage);
        int deletedRows = pstmt.executeUpdate();

        // Display a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, deletedRows + " discount plan deleted successfully.");
        alert.showAndWait();
        goToManagerDiscount(event);
    }


        public void goToManagerBlank (ActionEvent event) throws IOException {
            controller.goToManagerBlank(event);
        }
        public void goToManagerReport (ActionEvent event) throws IOException {
            controller.goToManagerReport(event);
        }
        public void goToManagerDiscount (ActionEvent event) throws IOException {
            controller.goToManagerDiscount(event);
        }
        public void goToManagerStock (ActionEvent event) throws IOException, SQLException {
            controller.goToManagerStock(event);
        }
        public void goToManagerLogout (ActionEvent event) throws IOException {
            AdvisorLogoutController logoutController = new AdvisorLogoutController();
            logoutController.logout(event);
        }

        public void goToManagerCommission (ActionEvent event) throws IOException {
            controller.goToManagerCommission(event);
        }
        public void goToManagerHome (ActionEvent event) throws IOException {
            controller.goToManagerHome(event);
        }
    public void goToManagerAlert(ActionEvent event) throws IOException {
        controller.goToManagerAlert(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disTypeChoice.getItems().addAll("Fixed", "Flexible");
        disDelType.getItems().addAll("Fixed", "Flexible");

        // Add listener to commDelType
        disDelType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    // Execute query to retrieve matching percentages from database
                    PreparedStatement pstmt = connDB.prepareStatement("SELECT Percentage FROM Discount WHERE Type = ?");
                    pstmt.setString(1, (String) newValue);
                    ResultSet rs = pstmt.executeQuery();

                    // Clear and repopulate commDelPercentage with the retrieved percentages
                    disDelPercentage.getItems().clear();
                    while (rs.next()) {
                        disDelPercentage.getItems().add(rs.getInt("Percentage"));
                    }
                } catch (SQLException e) {
                    // Handle exception
                    e.printStackTrace();
                }
            }
        });

    }
}




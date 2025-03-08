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

 This class is responsible for managing commission information for the manager. It allows the manager to add
 and delete commission percentages for domestic and international sales. It also provides navigation functionality
 to other pages in the manager interface.
 @author [Sykat Howlader]
 */
public class ManagerCommissionController implements Initializable {
    @FXML
    TextField commPercentageField;
    @FXML
    ChoiceBox commTypeChoice;
    @FXML
    ChoiceBox commDelType;
    @FXML
    ChoiceBox commDelPercentage;

    ManagerHomeController controller = new ManagerHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    public void addCommission(ActionEvent event) throws SQLException, IOException {
        if (commPercentageField.getText().isBlank() || commTypeChoice.getValue() == null) {
            // Display an error message if any of the required fields are blank
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter values for all fields.");
            alert.showAndWait();
            return;
        }

        int percentage = 0;
        try {
            percentage = Integer.parseInt(commPercentageField.getText());
        } catch (NumberFormatException e) {
            // Display an error message if the text in the percentage field is not a valid integer
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for percentage.");
            alert.showAndWait();
            return;
        }

        String type = commTypeChoice.getValue().toString();

        // Insert the commission into the database
        PreparedStatement pstmt = connDB.prepareStatement("INSERT INTO Commission (Type, Percentage) VALUES (?, ?)");
        pstmt.setString(1, type);
        pstmt.setInt(2, percentage);
        int insertedRows = pstmt.executeUpdate();

        // Display a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, insertedRows + " commission added successfully.");
        alert.showAndWait();
        goToManagerCommission(event);
    }
    public void deleteCommission(ActionEvent event) throws SQLException, IOException {
        String selectedType = (String) commDelType.getValue();
        int selectedPercentage = (int) commDelPercentage.getValue();

        // Delete the commission from the database
        PreparedStatement pstmt = connDB.prepareStatement("DELETE FROM Commission WHERE Type = ? AND Percentage = ?");
        pstmt.setString(1, selectedType);
        pstmt.setInt(2, selectedPercentage);
        int deletedRows = pstmt.executeUpdate();

        // Display a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, deletedRows + " commission deleted successfully.");
        alert.showAndWait();
        goToManagerCommission(event);
    }





    public void goToManagerBlank(ActionEvent event) throws IOException {
        controller.goToManagerBlank(event);
    }
    public void goToManagerReport(ActionEvent event) throws IOException {
        controller.goToManagerReport(event);
    }
    public void goToManagerDiscount(ActionEvent event) throws IOException {
        controller.goToManagerDiscount(event);
    }
    public void goToManagerStock(ActionEvent event) throws IOException, SQLException {
        controller.goToManagerStock(event);
    }
    public void goToManagerLogout(ActionEvent event) throws IOException {
        AdvisorLogoutController logoutController = new AdvisorLogoutController();
        logoutController.logout(event);
    }

    public void goToManagerCommission(ActionEvent event) throws IOException {
        controller.goToManagerCommission(event);
    }
    public void goToManagerHome(ActionEvent event) throws IOException {
        controller.goToManagerHome(event);
    }
    public void goToManagerAlert(ActionEvent event) throws IOException {
        controller.goToManagerAlert(event);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commTypeChoice.getItems().addAll("Domestic", "International");
        commDelType.getItems().addAll("Domestic", "International");

        // Add listener to commDelType
        commDelType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    // Execute query to retrieve matching percentages from database
                    PreparedStatement pstmt = connDB.prepareStatement("SELECT Percentage FROM Commission WHERE Type = ?");
                    pstmt.setString(1, (String) newValue);
                    ResultSet rs = pstmt.executeQuery();

                    // Clear and repopulate commDelPercentage with the retrieved percentages
                    commDelPercentage.getItems().clear();
                    while (rs.next()) {
                        commDelPercentage.getItems().add(rs.getInt("Percentage"));
                    }
                } catch (SQLException e) {
                    // Handle exception
                    e.printStackTrace();
                }
            }
        });

    }
}

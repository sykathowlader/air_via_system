package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * The AdvisorCustomerController class allows the advisor to manage customer in the system.
 *  @author [Sykat Howlader]
 */

public class AdvisorCustomerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
            private TextField customerNameField;
    @FXML
            private TextField customerEmailField;
    @FXML
            private TextField customerPhoneField;
    @FXML
            private ChoiceBox customerTypeChoice;



    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();
    private AdvisorHomeController controller = new AdvisorHomeController();


    public void goToAdvisorSellBlank(ActionEvent event) throws IOException {
        controller.goToAdvisorSellBlank(event);

    }
    public void goToAdvisorRefund(ActionEvent event) throws IOException {
        controller.goToAdvisorRefund(event);
    }
    public void goToAdvisorReport(ActionEvent event) throws IOException {
        controller.goToAdvisorReport(event);
    }
    public void goToAdvisorStock(ActionEvent event) throws IOException, SQLException {
        controller.goToAdvisorStock(event);
    }
    public void goToAdvisorLogout(ActionEvent event) throws IOException {
        AdvisorLogoutController logoutController = new AdvisorLogoutController();
        logoutController.logout(event);
    }

    public void goToAdvisorCustomer(ActionEvent event) throws IOException {
        controller.goToAdvisorCustomer(event);
    }
    public void goToAdvisorHome(ActionEvent event) throws IOException {
        controller.goToAdvisorHome(event);
    }

    public void cancel_click(ActionEvent event) throws IOException {
        controller.goToAdvisorCustomer(event);
    }


    public void create_customer(ActionEvent event) throws IOException, SQLException {
        if (customerNameField.getText().isBlank() || customerEmailField.getText().isBlank() || customerPhoneField.getText().isBlank() || customerTypeChoice.getValue() == null){
            // Display an error message if any of the required fields are blank
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter values for all fields.");
            alert.showAndWait();
            return;
        }

        // Check if a customer with the same email exists in the database
        PreparedStatement pstmt = connDB.prepareStatement("SELECT * FROM CustomerAccount WHERE Email = ?");
        pstmt.setString(1, customerEmailField.getText());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            // A customer with the same email already exists in the database, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "A customer with the same email already exists");
            alert.showAndWait();
        } else {
            // No customer with the same email exists in the database, insert the new customer
            PreparedStatement pstmtInsert = connDB.prepareStatement("INSERT INTO CustomerAccount (Name, Email, TelNumber, CustomerType) VALUES (?, ?, ?, ?);");
            pstmtInsert.setString(1, customerNameField.getText());
            pstmtInsert.setString(2, customerEmailField.getText());
            pstmtInsert.setString(3, customerPhoneField.getText());
            pstmtInsert.setString(4, (String) customerTypeChoice.getValue());
            pstmtInsert.executeUpdate();
            goToAdvisorCustomer(event);

            // Display a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer created successfully.");
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTypeChoice.getItems().addAll("Regular", "Valued");
    }
}

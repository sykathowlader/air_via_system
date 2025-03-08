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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

/**

 The ManagerReportController class is responsible for generating a report of sales within a specified date range and type
 for the logged in manager. The class includes methods for handling button clicks to navigate to other views,
 initializing the ChoiceBox with available types, and generating the report.
 @author [Sykat Howlader]
 */

public class ManagerReportController implements Initializable {
    ManagerHomeController controller = new ManagerHomeController();

    public static Date manStartDate;
    public static Date manEndDate;
    public static String manSelectedType;


    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    public ChoiceBox manType;
    @FXML
    public DatePicker manStart;
    @FXML
    public DatePicker manEnd;

    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();










    public void handleGenerateButton(ActionEvent event) throws IOException {
        Date startDate = java.sql.Date.valueOf(manStart.getValue());
        this.manStartDate = startDate;
        Date endDate = java.sql.Date.valueOf(manEnd.getValue());
        this.manEndDate = endDate;
        String selectedType = (String) manType.getValue();
        this.manSelectedType = selectedType;

        try {
            // Check if there are any matching SellDate values in the table within the specified date range
            String sql = "SELECT COUNT(*) AS count FROM Sell WHERE Type = ? AND SellDate BETWEEN ? AND ? AND AdvisorId = ?";
            PreparedStatement stmt = connDB.prepareStatement(sql);
            stmt.setString(1, selectedType);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            stmt.setInt(4, LoginController.loggedInAdvisorId);
            ResultSet rs = stmt.executeQuery();

            int count = rs.getInt("count");
            if (count == 0) {
                // Show an error message and return without loading the fxml file
                Alert alert = new Alert(Alert.AlertType.ERROR, "No records found within the specified date range.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Load the fxml file and display it in a new stage
        root = FXMLLoader.load(getClass().getResource("manager_report_table.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(LoginController.loggedInAdvisorId);
        stage.show();
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
        manType.getItems().addAll("Domestic", "International");
    }
}

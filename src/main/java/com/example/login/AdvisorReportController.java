package com.example.login;

import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * The AdvisorReportController class controls the generation of the individual report of the advisor.
 @author [Sykat Howlader]
 */

public class AdvisorReportController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

      public static Date startDate;
      public static Date endDate;
      public static String selectedType;




    @FXML
    public TableView<Sale> adTable;

    @FXML
    public ChoiceBox adType;
    @FXML
    public DatePicker adStart;
    @FXML
    public DatePicker adEnd;




    private AdvisorHomeController controller = new AdvisorHomeController();

    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    public void handleGenerateButton(ActionEvent event) throws IOException {
        Date startDate = java.sql.Date.valueOf(adStart.getValue());
        this.startDate = startDate;
        Date endDate = java.sql.Date.valueOf(adEnd.getValue());
        this.endDate = endDate;
        String selectedType = (String) adType.getValue();
        this.selectedType = selectedType;

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
        root = FXMLLoader.load(getClass().getResource("advisor_report_table.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(LoginController.loggedInAdvisorId);
        stage.show();
    }











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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adType.getItems().addAll("Domestic", "International");
    }


    public Date getStartDate() {
        return startDate;
    }

    public  Date getEndDate() {
        return endDate;
    }

    public  String getSelectedType() {
        return selectedType;
    }




}

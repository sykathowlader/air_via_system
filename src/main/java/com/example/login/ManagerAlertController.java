package com.example.login;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;


/**

 This class is the controller for the manager alerts page.
 It initializes the TableView and TableColumns for displaying the list of sales that have not been paid within 30 days.
 It also contains methods for navigating to other pages and populating the table with data from the Sell table in the database.
 @author [Sykat Howlader]
 */

public class ManagerAlertController implements Initializable {
    @FXML
    TableView<Sale> alTable;
    @FXML
    TableColumn<Sale, Integer> alSale;
    @FXML
    TableColumn<Sale, String> alEmail;
    @FXML
    TableColumn<Sale, Date> alDate;


    ManagerHomeController controller = new ManagerHomeController();


    public void goToManagerBlank(ActionEvent event) throws IOException {
        controller.goToManagerBlank(event);
    }
    public void goToManagerAlert(ActionEvent event) throws IOException {
        controller.goToManagerAlert(event);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void populateTable() throws SQLException {
        DatabaseConnectivity connect = new DatabaseConnectivity();
        Connection conn = connect.getConnection();

        // Define the SQL query to retrieve data from the Sale table
        String sql = "SELECT SaleID, CustomerEmail, SellDate FROM Sell WHERE SellDate < ? AND PaymentDate IS NULL";

        // Prepare the statement with the SQL query
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Set the parameter for the query to the current date minus 30 days
        LocalDate thirtyDaysAgo = LocalDate.now().minus(30, ChronoUnit.DAYS);
        pstmt.setDate(1, java.sql.Date.valueOf(thirtyDaysAgo));

        // Execute the query and get the result set
        ResultSet rs = pstmt.executeQuery();

        // Create an ObservableList to hold the data retrieved from the Sale table
        ObservableList<Sale> data = FXCollections.observableArrayList();

        // Loop through the result set and add the data to the ObservableList
        while (rs.next()) {
            Sale sale = new Sale(
                    rs.getInt("SaleID"),
                    rs.getString("CustomerEmail"),
                    rs.getDate("SellDate")
            );
            data.add(sale);
        }

        // Populate the table view with the data
        alTable.setItems(data);
        alSale.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSale_id()).asObject());
        alEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer_email()));
        alDate.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getSell_date()));

        // Close the result set, statement, and connection
        rs.close();
        pstmt.close();
        conn.close();
    }
}


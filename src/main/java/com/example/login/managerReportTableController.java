package com.example.login;

import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * This class contains the controller logic for the manager report table view.
 * It allows the user to view and download a report of sales data for a given date range and ticket type.
 * @author [Sykat Howlader]
 */
public class managerReportTableController implements Initializable {
    @FXML public TableView<Sale> adTable;
    @FXML public ChoiceBox manType;
    @FXML public DatePicker manStart;
    @FXML public DatePicker manEnd;
    @FXML TableColumn<Sale, Long> manTickNum;
    @FXML TableColumn<Sale, Float> manTickFare;
    @FXML TableColumn<Sale, String> manTickCurr;
    @FXML TableColumn<Sale, Float> manTickExc;
    @FXML TableColumn<Sale, Float> manTickTax;
    @FXML TableColumn<Sale, String> manTickPay;
    @FXML TableColumn<Sale, String> manTickComm;
    @FXML TableColumn<Sale, Integer> manTickTotNum;
    @FXML TableColumn<Sale, Float> manTickTotFare;
    @FXML TableColumn<Sale, Float> manTickTotTax;
    @FXML TableColumn<Sale, Float> manTotComm;
    @FXML TableColumn<Sale, Integer> manAdId;
    @FXML TableView<Sale> manTable;

    ManagerHomeController controller = new ManagerHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date startDate = ManagerReportController.manStartDate;
        Date endDate = ManagerReportController.manEndDate;
        String selectedType = ManagerReportController.manSelectedType;
        populateManTable(startDate, endDate, selectedType);
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

    public void populateManTable(Date fromDate, Date toDate, String type) {
        try {
            String sql = "SELECT AdvisorID, BlankNumber, TotalAmount, Currency, ExchangeRate, PaymentType, CommissionRate FROM Sell WHERE Type = ? AND SellDate BETWEEN ? AND ?";
            PreparedStatement stmt = connDB.prepareStatement(sql);
            stmt.setString(1, type);
            stmt.setDate(2, new java.sql.Date(fromDate.getTime()));
            stmt.setDate(3, new java.sql.Date(toDate.getTime()));

            ResultSet rs = stmt.executeQuery();
            ObservableList<Sale> data = FXCollections.observableArrayList();
            while (rs.next()) {
                Sale sale = new Sale(rs.getInt("AdvisorID"), rs.getLong("BlankNumber"), rs.getFloat("TotalAmount"), rs.getString("Currency"), rs.getFloat("ExchangeRate"), rs.getString("PaymentType"), rs.getString("CommissionRate"));
                data.add(sale);
            }
            manTable.setItems(data);

            manAdId.setCellValueFactory(new PropertyValueFactory<>("advisorID"));
            manTickNum.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
            manTickFare.setCellValueFactory(new PropertyValueFactory<>("totAmount"));
            manTickCurr.setCellValueFactory(new PropertyValueFactory<>("currency"));
            manTickExc.setCellValueFactory(new PropertyValueFactory<>("exchangeRate"));
            manTickTax.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotAmount() * 0.2f).asObject());
            manTickPay.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
            manTickComm.setCellValueFactory(new PropertyValueFactory<>("commissionRate"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void handleDownloadPdf(ActionEvent event) {
        ObservableList<Sale> tableData = manTable.getItems();

        if (tableData.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No data to export");
            alert.showAndWait();
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setInitialFileName("ManagerReport.txt");
            File selectedFile = fileChooser.showSaveDialog(manTable.getScene().getWindow());
            if (selectedFile != null) {
                StringBuilder content = new StringBuilder();
                content.append("AirVia LTD report\n\n");
                content.append("The report type is ").append(ManagerReportController.manSelectedType).append("\n");
                content.append("From: ").append(ManagerReportController.manStartDate).append(" To: ").append(ManagerReportController.manEndDate).append("\n\n");

                content.append(String.format("%-10s %-15s %-15s %-10s %-15s %-15s %-15s %-15s%n",
                    "Advisor ID", "Ticket Number", "Fare", "Currency", "Exchange Rate", "Tax", "Payment Type", "Commission"));
                content.append("-".repeat(110)).append("\n");

                for (Sale row : tableData) {
                    content.append(String.format("%-10d %-15d %-15.2f %-10s %-15.2f %-15.2f %-15s %-15s%n",
                        row.getAdvisorID(), row.getTicketNumber(), row.getTotAmount(), row.getCurrency(),
                        row.getExchangeRate(), row.getTax(), row.getPaymentType(), row.getCommissionRate()));
                }

                Path path = selectedFile.toPath();
                Files.write(path, content.toString().getBytes());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Text file saved");
                alert.setContentText("The text file has been saved to " + selectedFile.getAbsolutePath());
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Text export failed");
            alert.setContentText("An error occurred while exporting the text file: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
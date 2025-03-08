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
 * The AdvisorReportTableController class controls the view of the report generated by the advisor.
 * @author [Sykat Howlader]
 */
public class AdvisorReportTableController implements Initializable {
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    @FXML public TableView<Sale> adTable;
    @FXML public ChoiceBox adType;
    @FXML public DatePicker adStart;
    @FXML public DatePicker adEnd;
    @FXML TableColumn<Sale, Long> adTickNum;
    @FXML TableColumn<Sale, Float> adTickFare;
    @FXML TableColumn<Sale, String> adTickCurr;
    @FXML TableColumn<Sale, Float> adTickExc;
    @FXML TableColumn<Sale, Float> adTickTax;
    @FXML TableColumn<Sale, String> adTickPay;
    @FXML TableColumn<Sale, String> adTickComm;
    @FXML TableColumn<Sale, Integer> adTickTotNum;
    @FXML TableColumn<Sale, Float> adTickTotFare;
    @FXML TableColumn<Sale, Float> adTickTotTax;
    @FXML TableColumn<Sale, Float> adTotComm;

    AdvisorHomeController controller = new AdvisorHomeController();

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
        Date startDate = AdvisorReportController.startDate;
        Date endDate = AdvisorReportController.endDate;
        String selectedType = AdvisorReportController.selectedType;
        populateAdTable(startDate, endDate, selectedType);
    }

    public void populateAdTable(Date fromDate, Date toDate, String type) {
        try {
            String sql = "SELECT BlankNumber, TotalAmount, Currency, ExchangeRate, PaymentType, CommissionRate FROM Sell WHERE Type = ? AND SellDate BETWEEN ? AND ? AND AdvisorId = ?";
            PreparedStatement stmt = connDB.prepareStatement(sql);
            stmt.setString(1, type);
            stmt.setDate(2, new java.sql.Date(fromDate.getTime()));
            stmt.setDate(3, new java.sql.Date(toDate.getTime()));
            stmt.setInt(4, LoginController.loggedInAdvisorId);

            ResultSet rs = stmt.executeQuery();
            ObservableList<Sale> data = FXCollections.observableArrayList();
            while (rs.next()) {
                Sale sale = new Sale(rs.getLong("BlankNumber"), rs.getFloat("TotalAmount"), rs.getString("Currency"), rs.getFloat("ExchangeRate"), rs.getString("PaymentType"), rs.getString("CommissionRate"));
                data.add(sale);
            }
            adTable.setItems(data);

            adTickNum.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
            adTickFare.setCellValueFactory(new PropertyValueFactory<>("totAmount"));
            adTickCurr.setCellValueFactory(new PropertyValueFactory<>("currency"));
            adTickExc.setCellValueFactory(new PropertyValueFactory<>("exchangeRate"));
            adTickTax.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotAmount() * 0.2f).asObject());
            adTickPay.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
            adTickComm.setCellValueFactory(new PropertyValueFactory<>("commissionRate"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void handleDownloadPdf(ActionEvent event) {
        ObservableList<Sale> tableData = adTable.getItems();

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
            fileChooser.setInitialFileName("AdvisorReport.txt");
            File selectedFile = fileChooser.showSaveDialog(adTable.getScene().getWindow());
            if (selectedFile != null) {
                StringBuilder content = new StringBuilder();
                content.append("AirVia LTD report\n\n");
                content.append("Prepared by Advisor with ID ").append(LoginController.loggedInAdvisorId).append("\n");
                content.append("The report type is ").append(AdvisorReportController.selectedType).append("\n");
                content.append("From: ").append(AdvisorReportController.startDate).append(" To: ").append(AdvisorReportController.endDate).append("\n\n");

                content.append(String.format("%-15s %-15s %-10s %-15s %-15s %-15s %-15s%n", 
                    "Ticket Number", "Fare", "Currency", "Exchange Rate", "Tax", "Payment Type", "Commission"));
                content.append("-".repeat(100)).append("\n");

                for (Sale row : tableData) {
                    content.append(String.format("%-15d %-15.2f %-10s %-15.2f %-15.2f %-15s %-15s%n",
                        row.getTicketNumber(), row.getTotAmount(), row.getCurrency(), row.getExchangeRate(), 
                        row.getTax(), row.getPaymentType(), row.getCommissionRate()));
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
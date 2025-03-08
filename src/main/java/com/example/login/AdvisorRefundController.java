package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


/**
 * The AdvisorRefundController class controls the refund of the system.
 *  @author [Sykat Howlader]
 */

public class AdvisorRefundController {

    private AdvisorHomeController controller = new AdvisorHomeController();
    @FXML
    TextField refundAmountField;
    @FXML
    TextField refundSaleField;
    @FXML
    DatePicker paymentDatePicker;
    @FXML
    TextField paySale;
    @FXML
    TextField payAm;


    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    public void makePayment(ActionEvent event) {
        // Get the values of the text fields and date picker
        String saleID = paySale.getText();
        String amount = payAm.getText();
        LocalDate paymentDate = paymentDatePicker.getValue();

        try {
            // Prepare the SQL statement
            PreparedStatement stmt = connDB.prepareStatement("SELECT * FROM Sell WHERE SaleID = ?");
            stmt.setInt(1, Integer.parseInt(saleID));

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Check if the SaleID exists in the Sell table
            if (!rs.next()) {
                // If SaleID does not exist, display an alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sale ID Error");
                alert.setContentText("The Sale ID entered does not exist.");
                alert.showAndWait();
            } else {
                // If SaleID exists, check if PaymentDate is null and if TotalAmount matches
                if (rs.getDate("PaymentDate") == null && rs.getFloat("TotalAmount") == Float.parseFloat(amount)) {
                    // If PaymentDate is null and TotalAmount matches, update PaymentDate with DatePicker input
                    PreparedStatement updateStmt = connDB.prepareStatement("UPDATE Sell SET PaymentDate = ? WHERE SaleID = ?");
                    updateStmt.setDate(1, java.sql.Date.valueOf(paymentDate));
                    updateStmt.setInt(2, Integer.parseInt(saleID));
                    updateStmt.executeUpdate();
                    // Display a success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Payment Success");
                    alert.setContentText("Payment was successful.");
                    alert.showAndWait();
                    goToAdvisorRefund(event);
                } else {
                    // Display an error message if PaymentDate is not null or TotalAmount does not match
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Payment Error");
                    alert.setContentText("Payment was not successful.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException | IOException e) {
            // Handle any SQL errors
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            // Close the database connection
            try {
                if (connDB != null) {
                    connDB.close();
                }
            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
            }
        }
    }


    public void refund(ActionEvent event) {
        String saleNumber = refundSaleField.getText().trim();
        String refundAmount = refundAmountField.getText().trim();

        // Check if saleNumber exists in Sale table
        if(!checkSaleNumber(saleNumber)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sale Number Error");
            alert.setHeaderText(null);
            alert.setContentText("Sale Number is invalid");
            alert.showAndWait();
            return;
        }

        try {
            // Check if sale has already been refunded
            String checkRefundedQuery = "SELECT Refunded FROM Sell WHERE SaleID = ?";
            PreparedStatement checkRefundedPst = connDB.prepareStatement(checkRefundedQuery);
            checkRefundedPst.setString(1, saleNumber);
            ResultSet checkRefundedRs = checkRefundedPst.executeQuery();
            if (checkRefundedRs.next()) {
                int refunded = checkRefundedRs.getInt("Refunded");
                if (refunded == 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Refund Error");
                    alert.setHeaderText(null);
                    alert.setContentText("This sale has already been refunded!");
                    alert.showAndWait();
                    return;
                }
            }

            // Get the total amount of the sale from Sell table
            String getTotalAmountQuery = "SELECT TotalAmount FROM Sell WHERE SaleID = ?";
            PreparedStatement getTotalAmountPst = connDB.prepareStatement(getTotalAmountQuery);
            getTotalAmountPst.setString(1, saleNumber);
            ResultSet getTotalAmountRs = getTotalAmountPst.executeQuery();

            if (getTotalAmountRs.next()) {
                double totAmount = getTotalAmountRs.getDouble("TotalAmount");
                double refundAmt = Double.parseDouble(refundAmount);

                // Check if refund amount is less than or equal to total amount
                if (refundAmt > totAmount) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Refund Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Refund amount is greater than total amount!");
                    alert.showAndWait();
                } else {
                    String insertQuery = "INSERT INTO Refund(RefundAmount, SaleID, AdvisorID) VALUES (?, ?, ?)";
                    PreparedStatement insertPst = connDB.prepareStatement(insertQuery);
                    insertPst.setDouble(1, refundAmt);
                    insertPst.setString(2, saleNumber);
                    insertPst.setInt(3, LoginController.loggedInAdvisorId);
                    insertPst.executeUpdate();

                    // Update the "Refunded" column in the Sell table
                    String updateQuery = "UPDATE Sell SET Refunded = ? WHERE SaleID = ?";
                    PreparedStatement updatePst = connDB.prepareStatement(updateQuery);
                    updatePst.setInt(1, 1);
                    updatePst.setString(2, saleNumber);
                    updatePst.executeUpdate();

                    goToAdvisorRefund(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Refund Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Refund processed successfully!");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkSaleNumber(String saleNumber) {
        try {
            String query = "SELECT SaleID FROM Sell WHERE SaleID = ?";
            PreparedStatement statement = connDB.prepareStatement(query);
            statement.setString(1, saleNumber);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error while checking sale number: " + e.getMessage());
            return false;
        }
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
}

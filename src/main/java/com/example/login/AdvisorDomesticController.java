package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * The AdvisorDomesticController class manages the domestic blanks of the system
 * @author [Sykat Howlader]
 */
public class AdvisorDomesticController implements Initializable {

    @FXML ChoiceBox<Long> domBlankChoice;
    @FXML ChoiceBox<String> domDiscountChoice;
    @FXML ChoiceBox<String> domPaymentType;
    @FXML ChoiceBox<String> domCommRate;
    @FXML DatePicker domPaymentDate;
    @FXML TextField domDeparture;
    @FXML TextField domFirstLeg;
    @FXML TextField domDestination;
    @FXML TextField domCurrency;
    @FXML TextField domExchange;
    @FXML TextField domTotAmount;
    @FXML DatePicker domDepDate;
    @FXML CheckBox domCheckBox;

    private AdvisorHomeController controller = new AdvisorHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int currentUser = LoginController.loggedInAdvisorId;
        String query = "SELECT BlankNumber FROM Blank " +
                "WHERE AdvisorAssigned = ? AND SellDate IS NULL AND " +
                "(BlankNumber LIKE '1%' OR BlankNumber LIKE '2%');";
        try {
            PreparedStatement statement = connDB.prepareStatement(query);
            statement.setInt(1, currentUser);
            ResultSet resultSet = statement.executeQuery();
            domBlankChoice.getItems().clear();

            while (resultSet.next()) {
                domBlankChoice.getItems().add(resultSet.getLong("BlankNumber"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        domPaymentType.getItems().addAll("Cash", "Card");
        displayCommissionRate();
        displayDiscount();

        DatabaseConnectivity connect = new DatabaseConnectivity();
        connDB = connect.getConnection();
    }

    public void make_sell(ActionEvent event) throws IOException {
        String departure = domDeparture.getText();
        String firstLeg = domFirstLeg.getText();
        String destination = domDestination.getText();
        Long blankNumber = domBlankChoice.getValue() != null ? Long.parseLong(domBlankChoice.getValue().toString()) : null;
        Float exchangeRate = !domExchange.getText().isBlank() ? Float.parseFloat(domExchange.getText()) : null;
        Float total = !domTotAmount.getText().isBlank() ? Float.parseFloat(domTotAmount.getText()) : null;
        String currency = domCurrency.getText();
        String paymentType = domPaymentType.getValue();
        String commissionRate = domCommRate.getValue();
        String discountAmount = domDiscountChoice.getValue();
        LocalDate paymentDate = domPaymentDate.getValue();
        LocalDate departureDate = domDepDate.getValue();
        LocalDate sellDate = LocalDate.now();
        boolean payLater = domCheckBox.isSelected();
        String type = "Domestic";
        int currentAdvisorId = LoginController.loggedInAdvisorId;
        String current_email = AdvisorBlankController.currentCustomerEmail;

        if (!departure.isBlank() && !destination.isBlank() && blankNumber != null && total != null && !currency.isBlank() && !paymentType.isBlank() && !commissionRate.isBlank()) {
            try (PreparedStatement stmt = connDB.prepareStatement(
                    "INSERT INTO Sell (Type, SellDate, AdvisorID, Departure, FirstLeg, Destination, BlankNumber, ExchangeRate, TotalAmount, Currency, PaymentType, CommissionRate, Discount, PaymentDate, DepartureDate, PayLater, CustomerEmail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, type);
                stmt.setDate(2, Date.valueOf(sellDate));
                stmt.setInt(3, currentAdvisorId);
                stmt.setString(4, departure);

                if (firstLeg.isBlank()) {
                    stmt.setNull(5, Types.VARCHAR);
                } else {
                    stmt.setString(5, firstLeg);
                }

                stmt.setString(6, destination);
                stmt.setLong(7, blankNumber);

                if (exchangeRate == null) {
                    stmt.setNull(8, Types.FLOAT);
                } else {
                    stmt.setFloat(8, exchangeRate);
                }
                if (total == null) {
                    stmt.setNull(9, Types.FLOAT);
                } else {
                    stmt.setFloat(9, total);
                }
                stmt.setString(10, currency);
                stmt.setString(11, paymentType);
                stmt.setString(12, commissionRate);
                stmt.setString(13, discountAmount);
                if (paymentDate == null) {
                    stmt.setNull(14, Types.FLOAT);
                } else {
                    stmt.setDate(14, Date.valueOf(paymentDate));
                }
                stmt.setDate(15, Date.valueOf(departureDate));
                stmt.setBoolean(16, payLater);
                if (current_email == null) {
                    stmt.setNull(17, Types.VARCHAR);
                } else {
                    stmt.setString(17, current_email);
                }

                stmt.executeUpdate();
                int saleId = 0;
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getInt(1);
                    System.out.println("Generated SaleID: " + saleId);
                } else {
                    System.err.println("Error retrieving SaleID.");
                }

                if (blankNumber != null) {
                    try (PreparedStatement stmt2 = connDB.prepareStatement(
                            "UPDATE Blank SET SellDate = ? WHERE BlankNumber = ?")) {
                        stmt2.setDate(1, Date.valueOf(sellDate));
                        stmt2.setLong(2, blankNumber);
                        stmt2.executeUpdate();
                        System.out.println("Blank record updated with sell date.");
                    } catch (SQLException e) {
                        System.err.println("Error updating blank record in database: " + e.getMessage());
                    }
                }
                System.out.println("Sell record added to database.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Well done!");
                alert.setHeaderText(null);
                alert.setContentText("The sale is made successfully");
                alert.showAndWait();
                downloadPDF(saleId);
                goToAdvisorSellBlank(event);

            } catch (SQLException e) {
                System.err.println("Error adding sell record to database: " + e.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the required fields.");
            alert.showAndWait();
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

    public void displayDiscount() {
        try {
            String query = "SELECT * FROM CustomerAccount WHERE Email = ?";
            PreparedStatement pstmt = connDB.prepareStatement(query);
            pstmt.setString(1, AdvisorBlankController.currentCustomerEmail);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                query = "SELECT Type, Percentage FROM Discount";
                pstmt = connDB.prepareStatement(query);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String type = rs.getString("Type");
                    Integer percentage = rs.getInt("Percentage");
                    String displayString = type + " (" + percentage.toString() + "%)";
                    domDiscountChoice.getItems().add(displayString);
                }
            } else {
                System.out.println("Current email not found in CustomerAccount table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving discounts: " + e.getMessage());
        }
    }

    public void displayCommissionRate() {
        try {
            String query = "SELECT Percentage FROM Commission WHERE Type = ?";
            PreparedStatement pstmt = connDB.prepareStatement(query);
            pstmt.setString(1, "Domestic");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer rate = rs.getInt("Percentage");
                domCommRate.getItems().add(rate.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving commission rates: " + e.getMessage());
        }
    }

    public void disableCheckBox() {
        try {
            String query = "SELECT * FROM CustomerAccount WHERE Email = ?";
            PreparedStatement pstmt = connDB.prepareStatement(query);
            pstmt.setString(1, AdvisorBlankController.currentCustomerEmail);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                domCheckBox.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking current email: " + e.getMessage());
        }
    }

    public void downloadPDF(int sale_id) throws IOException {
        String departure = domDeparture.getText();
        String firstLeg = domFirstLeg.getText().isBlank() ? "" : domFirstLeg.getText();
        String destination = domDestination.getText();
        Long blankNumber = domBlankChoice.getValue() != null ? Long.parseLong(domBlankChoice.getValue().toString()) : null;
        Float exchangeRate = !domExchange.getText().isBlank() ? Float.parseFloat(domExchange.getText()) : null;
        Float total = !domTotAmount.getText().isBlank() ? Float.parseFloat(domTotAmount.getText()) : null;
        String currency = domCurrency.getText();
        String paymentType = domPaymentType.getValue();
        String commissionRate = domCommRate.getValue();
        String discountAmount = domDiscountChoice.getValue();
        LocalDate paymentDate = domPaymentDate.getValue();
        LocalDate departureDate = domDepDate.getValue();
        LocalDate sellDate = LocalDate.now();
        boolean payLater = domCheckBox.isSelected();

        StringBuilder content = new StringBuilder();
        content.append("Sale Information\n");
        content.append("Sale ID: ").append(sale_id).append("\n");
        content.append("Departure: ").append(departure).append("\n");
        content.append("First Leg: ").append(firstLeg).append("\n");
        content.append("Destination: ").append(destination).append("\n");
        content.append("Blank Number: ").append(blankNumber).append("\n");
        content.append("Exchange Rate: ").append(exchangeRate).append("\n");
        content.append("Total: ").append(total).append("\n");
        content.append("Currency: ").append(currency).append("\n");
        content.append("Payment Type: ").append(paymentType).append("\n");
        content.append("Commission Rate: ").append(commissionRate).append("\n");
        content.append("Discount Amount: ").append(discountAmount).append("\n");
        content.append("Payment Date: ").append(paymentDate).append("\n");
        content.append("Departure Date: ").append(departureDate).append("\n");
        content.append("Sell Date: ").append(sellDate).append("\n");
        content.append("Pay Later: ").append(payLater).append("\n");

        String fileName = "SaleInfo_" + sale_id + ".txt";
        String downloadFolder = System.getProperty("user.home") + "/Downloads/";
        Path path = Paths.get(downloadFolder + fileName);
        Files.write(path, content.toString().getBytes());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Download Completed");
        alert.setHeaderText(null);
        alert.setContentText("The file " + fileName + " has been downloaded to your download folder.");
        alert.showAndWait();
    }
}











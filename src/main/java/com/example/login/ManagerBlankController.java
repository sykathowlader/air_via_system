package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
This class allows the manager to manage blanks in the system, assigning and reassign blaks to a specific adviso
 and, it also allows to remove blanks.
 @author [Sykat Howlader]
*/

public class ManagerBlankController {
    @FXML
    TextField fromBlankField;
    @FXML
    TextField toBlankField;
    @FXML
    TextField advisorBlankField;

    ManagerHomeController controller = new ManagerHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();

    public void assignBlankToAdvisor(ActionEvent event) throws SQLException, IOException {
        if (fromBlankField.getText().isBlank() || toBlankField.getText().isBlank() || advisorBlankField.getText().isBlank()) {
            // Display an error message if any of the required fields are blank
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter values for all fields.");
            alert.showAndWait();
            return;
        }

        long from = 0;
        long to = 0;
        int advisorID = 0;
        try {
            from = Long.parseLong(fromBlankField.getText());
            to = Long.parseLong(toBlankField.getText());
            advisorID = Integer.parseInt(advisorBlankField.getText());

        } catch (NumberFormatException e) {
            // Display an error message if the text in the text fields is not a valid long integer
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers.");
            alert.showAndWait();
            return;
        }

        // Check if the specified advisor exists in the database
        PreparedStatement pstmtAdvisor = connDB.prepareStatement("SELECT * FROM TravelAdvisor WHERE AdvisorID = ?");
        pstmtAdvisor.setInt(1, advisorID);
        ResultSet rsAdvisor = pstmtAdvisor.executeQuery();

        if (!rsAdvisor.next()) {
            // The specified advisor does not exist in the database, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "The specified advisor does not exist.");
            alert.showAndWait();
            return;
        }

        // Check if there are matching blanks in the database
        PreparedStatement pstmtBlank = connDB.prepareStatement("SELECT * FROM Blank WHERE BlankNumber BETWEEN ? AND ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        pstmtBlank.setLong(1, from);
        pstmtBlank.setLong(2, to);
        ResultSet rsBlank = pstmtBlank.executeQuery();

        boolean hasAssignedBlanks = false;

        while (rsBlank.next()) {
            if (rsBlank.getDate("SellDate") != null) {
                // The blank has already been sold, display an error message
                Alert alert = new Alert(Alert.AlertType.ERROR, "Blank number " + rsBlank.getLong("BlankNumber") + " has already been sold.");
                alert.showAndWait();
                return;
            } else if (rsBlank.getInt("AdvisorAssigned") != 0) {
                // The blank has already been assigned, ask if the user wants to overwrite the assignment
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,  + rsBlank.getLong("BlankNumber") + " is already assigned. Do you want to reassign?");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setPrefWidth(500); // set a larger width
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.CANCEL) {
                    return;
                }
                hasAssignedBlanks = true;
            }
        }

        // Update the AdvisorAssigned column in the database for the matching blanks
        PreparedStatement pstmtUpdate = connDB.prepareStatement("UPDATE Blank SET AdvisorAssigned = ? WHERE BlankNumber BETWEEN ? AND ?");
        pstmtUpdate.setInt(1, advisorID);
        pstmtUpdate.setLong(2, from);
        pstmtUpdate.setLong(3, to);
        int updatedRows = pstmtUpdate.executeUpdate();

        if (updatedRows == 0 && hasAssignedBlanks) {
            // No new assignments were made, display a message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No new assignments were made.");
            alert.showAndWait();
        } else {
            goToManagerBlank(event);
            // Display a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, updatedRows + " blanks assigned to advisor successfully.");
            alert.showAndWait();
        }
    }


    public void removeBlank(ActionEvent event) throws SQLException, IOException {
        if (fromBlankField.getText().isBlank() || toBlankField.getText().isBlank()) {
            // Display an error message if any of the required fields are blank
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter values for all fields.");
            alert.showAndWait();
            return;
        }

        long from = 0;
        long to = 0;
        try {
            from = Long.parseLong(fromBlankField.getText());
            to = Long.parseLong(toBlankField.getText());
        } catch (NumberFormatException e) {
            // Display an error message if the text in the text fields is not a valid long integer
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers in the range fields.");
            alert.showAndWait();
            return;
        }

        // Check if any of the blanks are already sold
        PreparedStatement pstmtSold = connDB.prepareStatement("SELECT * FROM Blank WHERE BlankNumber BETWEEN ? AND ? AND SellDate IS NOT NULL");
        pstmtSold.setLong(1, from);
        pstmtSold.setLong(2, to);
        ResultSet rsSold = pstmtSold.executeQuery();

        if (rsSold.next()) {
            // Some of the blanks are already sold, display a message
            Alert alert = new Alert(Alert.AlertType.ERROR, "The following blanks are already sold and cannot be deleted:\n" + getBlankNumbers(rsSold));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setPrefWidth(500);
            alert.showAndWait();
            return;
        }

        // Ask for confirmation before deleting the blanks from the database
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the blanks from the database
            PreparedStatement pstmtDelete = connDB.prepareStatement("DELETE FROM Blank WHERE BlankNumber BETWEEN ? AND ? AND SellDate IS NULL");
            pstmtDelete.setLong(1, from);
            pstmtDelete.setLong(2, to);
            int deletedRows = pstmtDelete.executeUpdate();
            goToManagerBlank(event);
            // Display a success message
            alert = new Alert(Alert.AlertType.INFORMATION, deletedRows + " blanks deleted successfully.");
            alert.showAndWait();
        }
    }

    private String getBlankNumbers(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(rs.getLong("BlankNumber")).append("\n");
        } while (rs.next());
        return sb.toString();
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





}

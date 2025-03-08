package com.example.login;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**

 The ManagerStockController class manages the display of the current stock of blanks in the inventory.
 @author [Sykat Howlader]
*/
public class ManagerStockController implements Initializable {
    @FXML
    TableView<Blank> manStockTable;
    @FXML
    TableColumn<Blank, Integer> manAdCol;
    @FXML
    TableColumn<Blank, Long> manNumCol;
    @FXML
    TableColumn<Blank, String> manTypeCol;
    @FXML
    VBox root;
    ManagerHomeController controller = new ManagerHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the table columns
        manAdCol.setCellValueFactory(new PropertyValueFactory<>("AdvisorAssigned"));
        manNumCol.setCellValueFactory(new PropertyValueFactory<>("BlankNumber"));
        manTypeCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Blank, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Blank, String> cellData) {
                String blankNumber = String.valueOf(cellData.getValue().getBlankNumber());
                if (blankNumber.startsWith("201") || blankNumber.startsWith("101")) {
                    return new SimpleStringProperty("Domestic");
                } else if (blankNumber.startsWith("444") || blankNumber.startsWith("440") || blankNumber.startsWith("420")) {
                    return new SimpleStringProperty("International");
                } else {
                    return new SimpleStringProperty("");
                }
            }
        });

        // Load the data from the database into the table
        try (Statement statement = connDB.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Blank WHERE SellDate IS NULL")) {
            ObservableList<Blank> blanks = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int advisorAssigned = resultSet.getInt("AdvisorAssigned");
                long blankNumber = resultSet.getLong("BlankNumber");
                //LocalDate sellDate = resultSet.getDate("SellDate").toLocalDate();
                Blank blank = new Blank(blankNumber, advisorAssigned);
                blanks.add(blank);
            }
            manStockTable.setItems(blanks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

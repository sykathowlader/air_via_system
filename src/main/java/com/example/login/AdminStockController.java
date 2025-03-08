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
import java.util.ResourceBundle;
/**
 * The AdminStockController class keeps track of the stock of blanks in Admin application.
 * @author [Sykat Howlader].
 */

public class AdminStockController implements Initializable {

    @FXML
    TableView<Blank> admStockTable;
    @FXML
    TableColumn<Blank, Integer> admAdCol;
    @FXML
    TableColumn<Blank, Long> admNumCol;
    @FXML
    TableColumn<Blank, String> admTypeCol;


    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();
    AdminHomeController controller = new AdminHomeController();

    public void goToAdminBlank(ActionEvent event) throws IOException {
        controller.goToAdminBlank(event);
    }

    public void goToAdminHome(ActionEvent event) throws IOException {
        controller.goToAdminHome(event);
    }
    public void goToAdminAdvisor(ActionEvent event) throws IOException {
        controller.goToAdminAdvisor(event);
    }
    public void goToAdminDatabase(ActionEvent event) throws IOException {
        controller.goToAdminDatabase(event);
    }
    public void goToAdminStock(ActionEvent event) throws IOException, SQLException {
        controller.goToAdminStock(event);
    }
    public void goToAdminLogout(ActionEvent event) throws IOException {
        AdvisorLogoutController logoutController = new AdvisorLogoutController();
        logoutController.logout(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        // Set up the table columns
        admAdCol.setCellValueFactory(new PropertyValueFactory<>("AdvisorAssigned"));
        admNumCol.setCellValueFactory(new PropertyValueFactory<>("BlankNumber"));
        admTypeCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Blank, String>, ObservableValue<String>>() {
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
            admStockTable.setItems(blanks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The AdvisorStockController class controls the stock view if the blanks.
 *  @author [Sykat Howlader]
 */

public class AdvisorStockController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Blank> advisorStockTable;

    @FXML
    private TableColumn<Blank, String> advisorStockBlankType;
    @FXML
    private TableColumn<Blank, Long> advisorStockBlankNumber;



    // This method is called when the controller is initialized

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseConnectivity connect = new DatabaseConnectivity();
        Connection connDB = connect.getConnection();
        int currentAdvisor = LoginController.loggedInAdvisorId;

        advisorStockBlankType.setCellValueFactory(new PropertyValueFactory<>("blankType"));
        advisorStockBlankNumber.setCellValueFactory(new PropertyValueFactory<>("blankNumber"));

        ObservableList<Blank> blankList = FXCollections.observableArrayList();
        try {
            Statement stmt = connDB.createStatement();
            String query = "SELECT * FROM Blank WHERE AdvisorAssigned = " + currentAdvisor + " AND SellDate IS NULL";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Long blankNumber = rs.getLong("BlankNumber");
                String blankType = getBlankType(blankNumber);
                Blank blank = new Blank(blankType, blankNumber);
                blankList.add(blank);
            }
            advisorStockTable.setItems(blankList);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }







    private String getBlankType(long blankNumber) {
        String blankNumberStr = String.valueOf(blankNumber);
        if (blankNumberStr.startsWith("101") || blankNumberStr.startsWith("201")) {
            return "Domestic";
        } else if (blankNumberStr.startsWith("444") || blankNumberStr.startsWith("440") || blankNumberStr.startsWith("420")) {
            return "International";
        } else {
            return "Unknown";
        }
    }







    public void goToAdvisorSellBlank(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("advisor_blank.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToAdvisorRefund(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("advisor_refund.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToAdvisorReport(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("advisor_report.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToAdvisorLogout(ActionEvent event) throws IOException {
        AdvisorLogoutController logoutController = new AdvisorLogoutController();
        logoutController.logout(event);
    }

    public void goToAdvisorCustomer(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("advisor_customer.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToAdvisorHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("advisor_home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




    /*public void displayBlanks(ActionEvent event) throws SQLException {
        // Retrieve the logged in advisor ID from the currentUser field
        int loggedInAdvisorId = LoginController.loggedInAdvisorId;

        // Create a list to hold the Blank objects
        ObservableList<Blank> blanks = FXCollections.observableArrayList();

        // Create a SQL query to retrieve the relevant data from the Blank table
        String query = "SELECT * FROM Blank WHERE AdvisorAssigned = '" + loggedInAdvisorId + "'";

        // Execute the query and iterate over the results

        try (Statement statement = connDB.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String blankNumber = resultSet.getString("BlankNumber");
                String advisorAssigned = resultSet.getString("AdvisorAssigned");
                String blankType = getBlankType(blankNumber);

                // Create a new Blank object with the retrieved data
                Blank blank = new Blank(blankType, blankNumber);

                // Add the Blank object to the list
                blanks.add(blank);
            }
        }

        TableView<Blank> tableView = advisorStockTable;
        tableView.setItems(blanks);

        // Set up the table columns to display the appropriate data
        TableColumn<Blank, String> typeColumn = advisorStockBlankType;
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("blankType"));

        TableColumn<Blank, Integer> numberColumn = advisorStockBlankNumber;
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("blankNumber"));

        // Open a new window to display the table view
        Stage stage = new Stage();
        stage.setScene(new Scene(tableView));
        stage.show();
    }*/




}


package com.example.login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


/**
 * The AdminAdvisorController class controls the functionality of the Admin Advisor page in the Travel Advisor application.
 * @author [Sykat Howlader]
 */

public class AdminAdvisorController implements Initializable {

    @FXML
    private TableView<Advisor> advisorTable;
    @FXML
    private TableColumn<Advisor, Integer> advisorIdColumn;
    @FXML
    private TableColumn<Advisor, String> usernameColumn;
    @FXML
    private TableColumn<Advisor, String> nameColumn;
    @FXML
    private TableColumn<Advisor, String> emailColumn;
    @FXML
    private TableColumn<Advisor, Long> telNumberColumn;
    @FXML
    private TableColumn<Advisor, String> passwordColumn;

    AdminHomeController controller = new AdminHomeController();
    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();






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

        advisorIdColumn.setCellValueFactory(new PropertyValueFactory<>("advisorID"));

        // Set up the username column
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> {
            Advisor advisor = event.getRowValue();
            advisor.setUsername(event.getNewValue());
            updateAdvisor(advisor);
        });

        // Set up the name column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Advisor advisor = event.getRowValue();
            advisor.setName(event.getNewValue());
            updateAdvisor(advisor);
        });

        // Set up the email column
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            Advisor advisor = event.getRowValue();
            advisor.setEmail(event.getNewValue());
            updateAdvisor(advisor);
        });

        // Set up the telNumber column
        telNumberColumn.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
        telNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        telNumberColumn.setOnEditCommit(event -> {
            Advisor advisor = event.getRowValue();
            advisor.setTelNumber(Long.parseLong(String.valueOf(event.getNewValue())));
            updateAdvisor(advisor);
        });

        // Set up the password column
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit(event -> {
            Advisor advisor = event.getRowValue();
            advisor.setPassword(event.getNewValue());
            updateAdvisor(advisor);
        });

        // Set up the table
        advisorTable.setEditable(true);
        advisorTable.getColumns().addAll(advisorIdColumn, usernameColumn, nameColumn, emailColumn, telNumberColumn, passwordColumn);
        advisorTable.setItems(getAdvisorList());
        }

    private ObservableList<Advisor> getAdvisorList() {
        ObservableList<Advisor> advisors = FXCollections.observableArrayList();

        try {
            DatabaseConnectivity connect = new DatabaseConnectivity();
            Connection connDB = connect.getConnection();
            // Execute SELECT query
            String query = "SELECT AdvisorID, Username, Name, Email, TelNumber, Password FROM TravelAdvisor";
            Statement stmt = connDB.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Add result set to the ObservableList
            while (rs.next()) {
                Integer advisorID = rs.getInt("AdvisorID");
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                Long telNumber = rs.getLong("TelNumber");
                String password = rs.getString("Password");
                Advisor advisor = new Advisor(advisorID, username, name, email, telNumber, password);
                advisors.add(advisor);
            }

            // Close database connection
            rs.close();
            stmt.close();
            connDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return advisors;
    }

    private void updateAdvisor(Advisor advisor) {
        try {

            // Establish database connection

            // Execute UPDATE query
            String query = "UPDATE TravelAdvisor SET Username=?, Name=?, Email=?, TelNumber=?, Password=? WHERE AdvisorID=?";
            PreparedStatement stmt = connDB.prepareStatement(query);
            stmt.setString(1, advisor.getUsername());
            stmt.setString(2, advisor.getName());
            stmt.setString(3, advisor.getEmail());
            stmt.setLong(4, advisor.getTelNumber());
            stmt.setString(5, advisor.getPassword());
            stmt.setInt(6, advisor.getAdvisorID());
            stmt.executeUpdate();

            // Close database connection
            stmt.close();
            connDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


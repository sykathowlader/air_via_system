package com.example.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**

 The LoginController class is responsible for controlling the login window of the application.
 It handles user login authentication and redirects to the appropriate home screen based on the user's role.
 @author [Sykat Howlader]
 */

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField passwordId;
    @FXML
    private TextField usernameId;
    @FXML
    private Button loginButtonId;
    @FXML
    private Button cancelButtonId;
    @FXML
    private Label loginMessage;
    public String currentUser;
    public static int loggedInAdvisorId;



    DatabaseConnectivity connect = new DatabaseConnectivity();
    Connection connDB = connect.getConnection();
    private AdvisorHomeController advisorControl = new AdvisorHomeController();
    private ManagerHomeController managerControl = new ManagerHomeController();
    private AdminHomeController adminControl = new AdminHomeController();



    public void log_in(ActionEvent event) throws IOException {
        if (usernameId.getText().isBlank() == false && passwordId.getText().isBlank() == false) {
            try {
                Statement stm = connDB.createStatement();
                String advisorLogin = "SELECT * FROM TravelAdvisor WHERE Username = '" + usernameId.getText() + "' AND Password = '" + passwordId.getText() + "'";
                ResultSet rs = stm.executeQuery(advisorLogin);
                if (rs.next()) {
                    loginMessage.setText("WELCOME");
                    currentUser = usernameId.getText();
                    System.out.println(currentUser);
                    loggedInAdvisorId = getLoggedInAdvisorId();
                    System.out.println(loggedInAdvisorId);
                    advisorControl.goToAdvisorHome(event);
                }
                Statement stm2 = connDB.createStatement();
                String advisorLogin2 = "SELECT * FROM OfficeManager WHERE Username = '" + usernameId.getText() + "' AND Password = '" + passwordId.getText() + "'";
                ResultSet rs2 = stm2.executeQuery(advisorLogin2);
                if (rs2.next()) {
                    loginMessage.setText("WELCOME");
                    managerControl.goToManagerHome(event);
                }
                Statement stm3 = connDB.createStatement();
                String advisorLogin3 = "SELECT * FROM SystemAdministrator WHERE Username = '" + usernameId.getText() + "' AND Password = '" + passwordId.getText() + "'";
                ResultSet rs3 = stm3.executeQuery(advisorLogin3);
                if (rs3.next()) {
                    loginMessage.setText("WELCOME");
                    adminControl.goToAdminHome(event);
                } else {
                    loginMessage.setText("Invalid login details");
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error connecting to the database. Please try again later.");
                alert.showAndWait();
            }
        } else {
            loginMessage.setText("Please enter Username or Password");
        }
    }
    public int getLoggedInAdvisorId() throws SQLException {
        int advisorId = -1;
        if (!usernameId.getText().isBlank() && !passwordId.getText().isBlank()) {
            Statement stm = connDB.createStatement();
            String advisorLogin = "SELECT AdvisorID FROM TravelAdvisor WHERE Username = '"+usernameId.getText()+"' AND Password = '"+passwordId.getText()+"'";
            ResultSet rs = stm.executeQuery(advisorLogin);
            if (rs.next()) {
                advisorId = rs.getInt("AdvisorID");
            }
        }
        return advisorId;
    }

    public void handleCloseApplication(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the application?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }
}
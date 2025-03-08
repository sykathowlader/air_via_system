package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The AdminHomeController class is the home of the Admin application.
 * @author [Sykat Howlader]
 */

public class AdminHomeController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label welcomeLabel;



    public void goToAdminBlank(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admin_blank.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(LoginController.loggedInAdvisorId);
        stage.show();
    }

    public void goToAdminHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admin_home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(LoginController.loggedInAdvisorId);
        stage.show();
    }
    public void goToAdminAdvisor(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admin_advisor.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToAdminDatabase(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admin_database.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToAdminStock(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("admin_stock.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToAdminLogout(ActionEvent event) throws IOException {
        AdvisorLogoutController logoutController = new AdvisorLogoutController();
        logoutController.logout(event);
    }


}

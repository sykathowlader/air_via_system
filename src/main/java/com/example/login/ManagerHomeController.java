package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**

 This class is responsible controlling the home page of the manager application.
 @author [Sykat Howlader]
 */

public class ManagerHomeController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML



    public void goToManagerBlank(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manager_blank.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(LoginController.loggedInAdvisorId);
        stage.show();
    }

    public void goToManagerAlert(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manager_alert.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(LoginController.loggedInAdvisorId);
        stage.show();
    }
    public void goToManagerReport(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manager_report.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToManagerDiscount(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manager_discount.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToManagerStock(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("manager_stock.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToManagerLogout(ActionEvent event) throws IOException {
        AdvisorLogoutController logoutController = new AdvisorLogoutController();
        logoutController.logout(event);
    }

    public void goToManagerCommission(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manager_commission.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToManagerHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manager_home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

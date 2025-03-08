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
 * The AdvisorHomeController class controls the home page of the Advisor application.
 *  @author [Sykat Howlader]
 */

public class AdvisorHomeController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label welcomeLabel;


   // private AdvisorStockController stockController = new AdvisorStockController();

    public void goToAdvisorSellBlank(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("advisor_blank.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(LoginController.loggedInAdvisorId);
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
    public void goToAdvisorStock(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("advisor_stock.fxml"));
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
}

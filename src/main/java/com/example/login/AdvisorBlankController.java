package com.example.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * The AdvisorBlankController class controls the blak functionalities of the Advisor application
 *  @author [Sykat Howlader]
 */

public class AdvisorBlankController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ChoiceBox <String> blankChoiceBox;
    private String[] blankType = {"Domestic", "International"};

     @FXML
     private Button nextBlankButton;
     @FXML
     private TextField customerEmailField;
     @FXML
     CheckBox guestCheck;

     public static String currentCustomerEmail;


    private AdvisorHomeController controller = new AdvisorHomeController();

   // private DatabaseConnectivity connect = new DatabaseConnectivity();
  //  private Connection connDB = connect.getConnection();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blankChoiceBox.getItems().addAll(blankType);
        }
        
    public void goNextAdvisorBlank(ActionEvent event) throws IOException, SQLException {
        String selectedChoice = blankChoiceBox.getValue();
        String fxmlFileName = null;

        if (selectedChoice.equals("Domestic")) {
            fxmlFileName = "domestic_blank.fxml";
        } else if (selectedChoice.equals("International")) {
            fxmlFileName = "international_blank.fxml";
        }
        String email = customerEmailField.getText();
        System.out.println(email);
        boolean emailExists = checkIfEmailExists(email);

        if (guestCheck.isSelected()) {
            root = FXMLLoader.load(getClass().getResource(fxmlFileName));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if (emailExists) {
            this.currentCustomerEmail=email;
            root = FXMLLoader.load(getClass().getResource(fxmlFileName));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            // Email does not exist, show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email not found");
            alert.setContentText("Please create a customer first");
            alert.showAndWait();
        }
    }

    private boolean checkIfEmailExists(String email) throws SQLException {
        boolean emailExists = false;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
             DatabaseConnectivity connect = new DatabaseConnectivity();
             Connection connDB = connect.getConnection();
            statement = connDB.createStatement();
            String query = "SELECT * FROM CustomerAccount WHERE Email = '"+email+"'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                emailExists = true;
            }
        } finally {

            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
           // connect.closeConnection();
        }
        return emailExists;
    }

        }





/*root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

 */
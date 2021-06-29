package com.mycompany.libengine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXButton;
import database.DB;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class NameLogoController implements Initializable {

    DB db = new DB();
    Connection con;
    String sql;

    @FXML
    private TextField nameSchool;
    @FXML
    private Pane choseLogo;
    @FXML
    private ImageView logo;
    @FXML
    private JFXButton next;

    Alert a = new Alert(Alert.AlertType.WARNING);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = db.getConnection();

        //setting the logo of the alertBox
        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
    }

    @FXML
    private void swithToItemRegistration(ActionEvent event) throws IOException {

        if (nameSchool.getText().length() >= 1 && logo.getImage() != null) {
            //saving the name to the databse

            //redirecting to the next screen
            App.setRoot("itemRegistrationStartUp");
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Please provide the logo and the name of your school");
            a.show();

        }

    }

    @FXML
    private void loadLogo(MouseEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image representing the logo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images Files", "*.jpg", "*.png", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile);
            try {
                CopyFile.copyFileUsingStream(selectedFile, new File("C:/Users/X M G/Documents/NetBeansProjects/LibEngine/src/main/resources/images/4x/Logo UIECC.png"));

                //load the logo
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(1000),
                        ae -> {
                            logo.setImage(new Image("images/4x/Logo UIECC.png"));
                        }));
                timeline.play();
            } catch (IOException ex) {
                Logger.getLogger(NameLogoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

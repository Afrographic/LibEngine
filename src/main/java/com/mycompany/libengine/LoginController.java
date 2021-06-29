/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import database.DB;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author X M G
 */
public class LoginController {

    @FXML
    private TextField usernametxt;
    @FXML
    private TextField passwordtxt;
    DB db = new DB();
    Connection con;
    String sql;

    @FXML
    private void login(ActionEvent event) throws IOException {
        try {
            con = db.getConnection();
            String username = usernametxt.getText();
            String password = passwordtxt.getText();
            sql = "select * from admin where username = ? and password = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                usernametxt.getScene().getWindow().setWidth(1366);
                Scene scene = usernametxt.getScene();

                scene.setRoot(loadFXML("homepage"));
                scene.getWindow().centerOnScreen();

            } else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
                a.setAlertType(AlertType.ERROR);
                a.setHeaderText("Incorrect credential! please try again!");
                a.setContentText("");
                a.show();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}

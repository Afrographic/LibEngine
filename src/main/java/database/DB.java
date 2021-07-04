/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author X M G
 */
public class DB {

    // create a alert
    Alert a = new Alert(Alert.AlertType.WARNING);

    String url = "jdbc:mysql://localhost:3306/libengine";
    Connection con;

    public DB() {
        try {
            con = DriverManager.getConnection(url, "root", "");
        } catch (SQLException ex) {
            //System.out.println("Unable to connect");
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Database connexion error, please turn your mysql server on!");
            a.show();
        }

    }

    public Connection getConnection() {

        return con;
    }

}

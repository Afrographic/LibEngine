/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import database.DB;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class StudentsRegistrationController implements Initializable {

    DB db = new DB();
    Connection con;
    String sql;
    Alert a = new Alert(Alert.AlertType.WARNING);

    @FXML
    private TextField fullName;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;

    @FXML
    private ImageView backButton;
    @FXML
    private ComboBox studentDepart;

    int[] departIndexes = new int[30];

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initializing the database connection
        con = db.getConnection();

        //Disable an input field
        //fullName.setDisable(true);
        // TODO
        //        Getting the data from the database
        try {

            sql = "select * from department";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int i = 0;
            while (rs.next()) {
                String departItem = rs.getString("department");

                studentDepart.getItems().add(departItem);
                departIndexes[i] = rs.getInt("idDepart");
                i++;

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void resetField() {
        fullName.setText("");
        email.setText("");
        phone.setText("");

    }

    @FXML
    private void saveUser(MouseEvent event) {
        try {
            String fullNameVal = fullName.getText(),
                    emailVal = email.getText(),
                    department = studentDepart.getSelectionModel().getSelectedItem().toString(),
                    phoneVal = phone.getText();

            int departIndex = studentDepart.getSelectionModel().getSelectedIndex();

            System.out.println(" Selected index " + departIndex);

            if (fullNameVal.length() < 2) {
                // set alert type
                a.setAlertType(AlertType.ERROR);
                a.setContentText("The fullname must be at least 3 characters long");

                // show the dialog
                a.show();
            } else if (emailVal.length() < 2) {
                System.out.println("Invalid Email");
            } else if (phoneVal.length() < 2) {
                System.out.println("Invalid Phone number");
            } else {
                try {
                    sql = "insert into student  (fullName,email,tel,idDepart) values (?,?,?,?)";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setString(1, fullNameVal);
                    st.setString(2, emailVal);
                    st.setString(3, phoneVal);
                    st.setInt(4, departIndexes[departIndex]);

                    st.execute();

                    // Confirmation message
                    // set alert type
                    a.setAlertType(AlertType.INFORMATION);
                    a.setContentText("User registered successuflly!");
                    a.show();

                    // reset the field
                    this.resetField();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    a.setAlertType(AlertType.ERROR);
                    a.setContentText("Database error, please try again ");
                    a.show();
                }
            }

        } catch (Exception e) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Please fill all the field");
            a.show();
        }
    }

    @FXML
    private void importParseCsvFile(MouseEvent event) {
        System.out.println("Import csv file");
    }

    @FXML
    private void popScreen(MouseEvent event) throws IOException {
        App.setRoot("homePage");
    }

}

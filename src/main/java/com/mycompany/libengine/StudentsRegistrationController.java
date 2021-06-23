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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    @FXML
    private TextField txtmatricule;
    @FXML
    private ComboBox txtgender;

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
        //        Getting the departments  from the database
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

        // Setting the gender
        txtgender.getItems().add("Male");
        txtgender.getItems().add("Feminine");

        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        // Add a custom icon.
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
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
                    phoneVal = phone.getText(),
                    gender = txtgender.getSelectionModel().getSelectedItem().toString(), 
                    matricule = txtmatricule.getText();

            int departIndex = studentDepart.getSelectionModel().getSelectedIndex();

            System.out.println(matricule);
            System.out.println(gender);

            System.out.println(" Selected index " + departIndex);

            if (fullNameVal.length() < 2) {
                // set alert type
                a.setAlertType(AlertType.ERROR);
                a.setContentText("Name too short or empty!");

                // show the dialog
                a.show();
            } else if(matricule.length() < 2){
              // set alert type
              a.setAlertType(AlertType.ERROR);
              a.setContentText("Matricule too short or empty!");

              // show the dialog
              a.show();
            }else if (!emailVal.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
             
                     // set alert type
              a.setAlertType(AlertType.ERROR);
              a.setContentText("Invalid Email!");

              // show the dialog
              a.show();
            } else if (!phoneVal.matches("6[0-9]{8}")) {
                  // set alert type
              a.setAlertType(AlertType.ERROR);
              a.setContentText("Invalid Phone number, must 9 numbers!");

              // show the dialog
              a.show();
            } else {
                try {
                    sql = "insert into student  (fullName,email,tel,idDepart,matricule,gender) values (?,?,?,?,?,?)";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setString(1, fullNameVal);
                    st.setString(2, emailVal);
                    st.setString(3, phoneVal);
                    st.setInt(4, departIndexes[departIndex]);
                    st.setString(5, matricule);
                    st.setString(6, gender);

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

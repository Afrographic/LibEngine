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
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class ItemRegistrationController implements Initializable {

    @FXML
    private TextField itemName;
    @FXML
    private TextField Author;
    @FXML
    private ComboBox itemType;
    @FXML
    private TextField stock;
    @FXML
    private ImageView backButton;
    @FXML
    private TextField position;

    DB db = new DB();
    Connection con;
    String sql;
    // create a alert
    Alert a = new Alert(AlertType.WARNING);
    String[] itemTyepArray = {"Book", "CDs", "Research document"};
    @FXML
    private TextField txtEdition;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // loading the items of the combo box
        itemType.getItems().addAll("Book", "CD", "Research document");

        //initializing the database connection
        con = db.getConnection();

        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        // Add a custom icon.
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
    }

    public void resetField() {
        itemName.setText("");
        Author.setText("");
        position.setText("");
        stock.setText("");
    }

    @FXML
    private void saveItem(MouseEvent event) {

        try {

           

            String itemNameVal = itemName.getText(),
                    AuthorVal = Author.getText(),
                    ItemTypeVal = itemType.getSelectionModel().getSelectedItem().toString(),
                    edition = txtEdition.getText(),
                    positionVal = position.getText();
            int stockVal = Integer.parseInt(stock.getText());
            // String itemTypeVal = itemTyepArray[itemType.getVisibleRowCount()];

            if (itemNameVal.length() < 2) {
              
                // set alert type
                a.setAlertType(AlertType.ERROR);
                a.setContentText("The item name is too short!");

                // show the dialog
                a.show();
            } else if (AuthorVal.length() < 2) {
               
                // set alert type
                a.setAlertType(AlertType.ERROR);
                a.setContentText("The Author name is too short!");

                // show the dialog
                a.show();
            } else if(!edition.matches("[0-9]{4}")){
                a.setAlertType(AlertType.ERROR);
                a.setContentText("The edition year must be 04 digits!");
                a.show();
            }else if (positionVal.length() < 2) {
               
                // set alert type
                a.setAlertType(AlertType.ERROR);
                a.setContentText("The Position need to be set");

                // show the dialog
                a.show();
            } else if ((stockVal < 0)) {
                
                // set alert type
                a.setAlertType(AlertType.ERROR);
                a.setContentText("iNVALID  stock value!");

                // show the dialog
                a.show();
            } else if (itemType.getSelectionModel().getSelectedItem().equals("")) {
              
                // set alert type
                a.setAlertType(AlertType.ERROR);
                a.setContentText("Please chose an item type!");

                // show the dialog
                a.show();
            } else {
                try {
                    sql = "insert into libraryitem  (itemName,author,position,stock,ItemType,editionYear) values (?,?,?,?,?,?)";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setString(1, itemNameVal);
                    st.setString(2, AuthorVal);
                    st.setString(3, positionVal);
                    st.setInt(4, stockVal);
                    st.setString(5, ItemTypeVal);
                    st.setInt(6, Integer.parseInt(edition));
                    st.execute();

                    // Confirmation message
                    // set alert type
                    Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                    // Add a custom icon.
                    stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
                    a.setAlertType(AlertType.INFORMATION);
                    a.setContentText("Book registered successuflly!");
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
          
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Please fill all the field");
            a.show();
        }
    }

    @FXML
    private void importParseCsvFile(MouseEvent event) {
    }

    @FXML
    private void popScreen(MouseEvent event) throws IOException {
        App.setRoot("studentRegistrationStartUp");
    }

}

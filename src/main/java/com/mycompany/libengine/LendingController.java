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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class LendingController implements Initializable {

    DB db = new DB();
    Connection con;
    String sql;
    int totalBorrow;
    @FXML
    private Label totalBorrowUI;
    @FXML
    private VBox borrowesItemContainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initializing the database connection...
        con = db.getConnection();

        //Get total lending...
        getTotalBorrow();
        getTemplate();
    }

    public void getTotalBorrow() {
        sql = "select COUNT(idBorrow) as totalBorrow from itemstostudent";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                System.out.println("print the shit" + rs.getInt("totalBorrow"));

                totalBorrow = rs.getInt("totalBorrow");
                //updating the UI
                totalBorrowUI.setText(totalBorrow + " Lendings");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getTemplate() {
        sql = "select * from student ,libraryitem,itemstostudent where itemstostudent.idStudent = student.idStudent and itemstostudent.idLibItem = libraryitem.idLibItem";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                //get the items
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void popScreen(MouseEvent event) throws IOException {
        App.setRoot("homePage");
    }

}

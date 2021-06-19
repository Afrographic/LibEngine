package com.mycompany.libengine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import database.DB;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.sql.*;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = db.getConnection();
        // TODO
    }

    @FXML
    private void swithToItemRegistration(ActionEvent event) throws IOException {

        App.setRoot("itemRegistrationStartUp");

    }

}

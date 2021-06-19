/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class ItemRegistrationStartUp implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void swithToStudentsRegistrationStartUp(ActionEvent event) throws IOException {

        App.setRoot("studentRegistrationStartUp");

    }

    @FXML

    private void swithToItemRegistration(ActionEvent event) throws IOException {

        App.setRoot("itemRegistration");

    }

}

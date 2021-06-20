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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Lending;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class PenaltyController implements Initializable {

//    DB variables
    DB db = new DB();
    Connection con;
    String sql;

//    End

    public static ArrayList<Lending> lendings = new ArrayList<Lending>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // calculate the penalties

        String date = "15/06/2021 21:49:33";
        String lentDate = date.split(" ")[0];

        // get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentDated = dtf.format(now);

        String currentDate = currentDated.split(" ")[0];

        //get duration
        elapsedDays(currentDate, lentDate);
    }

    public static void launchPenaltyScreen(ArrayList<Lending> lendingsArg){
        lendings = lendingsArg;

        System.out.println("Length of the lending "+lendings.size());
        try {
            App.setRoot("penalties");
        } catch (IOException e) {
           
            e.printStackTrace();
        }
    }

    long elapsedDays(String lentDate, String currentDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final LocalDate firstDate = LocalDate.parse(currentDate, formatter);
        final LocalDate secondDate = LocalDate.parse(lentDate, formatter);
        final long days = ChronoUnit.DAYS.between(firstDate, secondDate);

        System.out.println("Days between: " + days);

        return days;
    }

    void getPenalizeStud() {
        sql = "select * from student ,libraryitem,itemstostudent,department where itemstostudent.idStudent = student.idStudent and itemstostudent.idLibItem = libraryitem.idLibItem and department.idDepart = student.idDepart order by idBorrow ASC";
    }

    @FXML
    private void showLending(ActionEvent event) {
        try {
            App.setRoot("lending");
        } catch (IOException ex) {
            Logger.getLogger(LendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

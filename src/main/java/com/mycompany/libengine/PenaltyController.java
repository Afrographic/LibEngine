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
import Model.LibItem;
import Model.Student;
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

    public static void launchPenaltyScreen(ArrayList<Lending> lendingsArg) {
        lendings = lendingsArg;

        System.out.println("Length of the lending " + lendings.size());
        System.out.println("Length of the items per users "+sortingLendingByUser());
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
        //
    }

    public static ArrayList<LendingByUser> sortingLendingByUser() {
        ArrayList<LendingByUser> lendingByUser = new ArrayList<LendingByUser>();

        for (int i = 0; i < lendings.size(); i++) {
            int idStudent = lendings.get(i).student.idStud;
            ArrayList<LibItem> items = new ArrayList<LibItem>();

            // Searching the lending of the particular and classify them
            for (int j = 0; j < lendings.size(); j++) {
                if (lendings.get(j).student.idStud == idStudent) {
                    items.add(lendings.get(j).libItem);
                    //remove the item
                    lendings.remove(j);
                }
            }

            //Appending the user with its corresponding items
            Student student = lendings.get(i).student;
            lendingByUser.add(new LendingByUser(student,items));

        }

        return lendingByUser;
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

class LendingByUser {

    public Student student;
    public ArrayList<LibItem> items;

    public LendingByUser(Student student, ArrayList<LibItem> items) {
        this.student = student;
        this.items = items;
    }
}

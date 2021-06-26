/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import java.io.File;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import database.DB;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author X M G
 */
public class LoadCSV {

    static String sql;
    static DB db = new DB();
    static Connection con;
    static Alert a = new Alert(Alert.AlertType.WARNING);

    public static void loadLibItemsCSV() throws IOException {
        con = db.getConnection();
        int alreadyExist = 0;

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open items csv File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("CSV Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile);

            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                List<String[]> r = reader.readAll();
                r.forEach(x -> System.out.println(Arrays.toString(x)));

                if (r.get(0).length == 6 && r.get(0)[3].equals("stock")) {

                    //load excel data
                    sql = "insert into libraryitem  (itemName,author,position,stock,ItemType,editionYear) values (?,?,?,?,?,?)";
                    try {
                        PreparedStatement st = con.prepareStatement(sql);

                        for (int i = 1; i < r.size(); i++) {

                            if (!itemalreadyExist(r.get(i)[0], Integer.parseInt(r.get(i)[5]))) {
                                st.setString(1, r.get(i)[0]);
                                st.setString(2, r.get(i)[1]);
                                st.setString(3, r.get(i)[2]);
                                st.setInt(4, Integer.parseInt(r.get(i)[3]));
                                st.setString(5, r.get(i)[4]);
                                st.setInt(6, Integer.parseInt(r.get(i)[5]));
                                st.addBatch();
                            } else {
                                alreadyExist++;
                            }

                        }
                        int[] result = st.executeBatch();
                        System.out.println("The number of rows inserted: " + result.length);

                        // set alert type
                        // Add a custom icon.
                        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
                        a.setAlertType(Alert.AlertType.INFORMATION);

                        if (result.length != 0) {
                            if (alreadyExist > 0) {
                                a.setContentText(result.length + "Items was successfully inserted and " + alreadyExist + "  already exist!");
                            } else {
                                a.setContentText("Items was successfully inserted inside the database!");
                            }
                        } else {
                            a.setContentText("All the items already exist!, nothing inserted!");
                        }
                        a.show();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                } else {
                    // set alert type
                    a.setAlertType(AlertType.ERROR);
                    a.setContentText("Invalid items CSV format! please read the doc!");
                    a.show();
                }

            } catch (CsvException e) {
                System.out.println("Error File!");
            }
        }
    }

    static boolean itemalreadyExist(String itemName, int editionYear) {
        boolean alreadyExist = false;
        sql = "select * from libraryitem where itemName  = ? and editionYear = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, itemName);
            st.setInt(2, editionYear);
            ResultSet rs = st.executeQuery();

            int i = 0;
            while (rs.next()) {
                i++;
            }

            System.out.println("Value of i " + i);

            if (i != 0) {
                alreadyExist = true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return alreadyExist;
    }

    public static void loadStudentsCSV() throws FileNotFoundException, IOException {
        con = db.getConnection();
        int alreadyExist = 0;

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open students csv File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("CSV Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile);

            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                List<String[]> r = reader.readAll();
                r.forEach(x -> System.out.println(Arrays.toString(x)));

                if (r.get(0).length == 6 && r.get(0)[4].equals("matricule")) {

                    //load excel data
                    sql = "insert into student  (fullName,email,tel,idDepart,matricule,gender) values (?,?,?,?,?,?)";
                    try {
                        PreparedStatement st = con.prepareStatement(sql);

                        for (int i = 1; i < r.size(); i++) {

                            if (!studentAlreadyExist(r.get(i)[0], r.get(i)[4])) {
                                st.setString(1, r.get(i)[0]);
                                st.setString(2, r.get(i)[1]);
                                st.setString(3, r.get(i)[2]);
                                st.setString(4, r.get(i)[3]);
                                st.setString(5, r.get(i)[4]);
                                st.setString(6, r.get(i)[5]);
                                st.addBatch();
                            } else {
                                alreadyExist++;
                            }

                        }
                        int[] result = st.executeBatch();
                        System.out.println("The number of rows inserted: " + result.length);

                        // set alert type
                        // Add a custom icon.
                        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
                        a.setAlertType(Alert.AlertType.INFORMATION);

                        if (result.length != 0) {
                            if (alreadyExist > 0) {
                                a.setContentText(result.length + "Students was successfully inserted and " + alreadyExist + "  already exist!");
                            } else {
                                a.setContentText("Students was successfully inserted inside the database!");
                            }
                        } else {
                            a.setContentText("All the students already exist!, nothing inserted!");
                        }
                        a.show();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                } else {
                    // set alert type
                    a.setAlertType(AlertType.ERROR);
                    a.setContentText("Invalid Students CSV format! please read the doc!");
                    a.show();
                }

            } catch (CsvException e) {
                System.out.println("Error File!");
            }
        }
    }

    static boolean studentAlreadyExist(String nameStudent, String matricule) {
        boolean alreadyExist = false;
        sql = "select * from student  where fullname  = ? and matricule = ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, nameStudent);
            st.setString(2, matricule);
            ResultSet rs = st.executeQuery();

            int i = 0;
            while (rs.next()) {
                i++;
            }

            System.out.println("Value of i " + i);

            if (i != 0) {
                alreadyExist = true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return alreadyExist;
    }
}

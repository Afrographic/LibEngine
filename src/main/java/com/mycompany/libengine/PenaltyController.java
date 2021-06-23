/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import database.DB;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Lending;
import Model.LendingByUser;
import Model.LibItem;
import Model.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    static String currentDate;
    static int totalitems;
    static int totalusers;
    public static ArrayList<Lending> lendings = new ArrayList<Lending>();
    public static ArrayList<LendingByUser> lendingByUser = new ArrayList<LendingByUser>();
    @FXML
    private Label titlePenalties;
    @FXML
    private VBox penaltiesContainer;
    @FXML
    private ImageView warningSign;
    @FXML
    private VBox safeLib;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize this shit");
        totalitems = 0;
        totalusers = 0;

        // Clear all the items
        penaltiesContainer.getChildren().clear();

        System.out.println("Shit " + lendings.size());
        enginerenderStudentPenalized(lendingByUser);
        //update penalties title
        titlePenalties.setText(totalusers + " student" + (totalusers > 1 ? "s" : "") + " exceeded the time to return " + totalitems + " item" + (totalitems > 1 ? "s" : ""));

        if (totalitems == 0) {
            String librarySafeTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?import com.jfoenix.controls.JFXButton?> <?import javafx.geometry.Insets?><?import javafx.scene.Cursor?><?import javafx.scene.control.Label?> <?import javafx.scene.control.Separator?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.Pane?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?> <VBox alignment=\"CENTER\" prefHeight=\"576.0\" prefWidth=\"1366.0\" style=\"-fx-background-color: white;\"> <VBox.margin> <Insets top=\"25.0\" /> </VBox.margin> <children> <ImageView fitHeight=\"393.0\" fitWidth=\"500.0\" pickOnBounds=\"true\" preserveRatio=\"true\" scaleX=\"-1.0\"> <image> <Image url=\"images/4x/NoPenalties.png\" /> </image> </ImageView> <Label text=\"The library is Safe!\" textFill=\"#737373\"> <font> <Font name=\"Century Gothic\" size=\"20.0\" /> </font> <VBox.margin> <Insets top=\"20.0\" /> </VBox.margin> </Label> </children> </VBox>";

            FXMLLoader loader = new FXMLLoader();
            try {
                VBox layout = (VBox) loader.load(
                        new ByteArrayInputStream(librarySafeTemplate.getBytes()
                        ));

                safeLib.setStyle("-fx-background-color:#fff;");
                safeLib.getChildren().clear();
                safeLib.getChildren().add(layout);
            } catch (IOException e) {

                e.printStackTrace();
            }
            warningSign.setVisible(false);
        }
    }

    public static void launchPenaltyScreen(ArrayList<Lending> lendingsArg) {
        lendings = lendingsArg;

        // get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentDated = dtf.format(now);
        currentDate = currentDated.split(" ")[0];

        // System.out.println("Length of the lending " + lendings.size());
        lendingByUser = sortingLendingByUser();
        try {
            App.setRoot("penalties");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static long elapsedDays(String lentDate, String currentDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final LocalDate firstDate = LocalDate.parse(currentDate, formatter);
        final LocalDate secondDate = LocalDate.parse(lentDate, formatter);
        final long days = ChronoUnit.DAYS.between(firstDate, secondDate);

        LocalDate tomorrow = firstDate.plusDays(1);

        //System.out.println("Days between: " + days);
        return days;
    }

    private static ArrayList<LendingByUser> sortingLendingByUser() {
        ArrayList<LendingByUser> lendingByUser = new ArrayList<LendingByUser>();
        for (int i = 0; i < lendings.size(); i++) {
            int idStudent = lendings.get(i).student.idStud;
            ArrayList<Lending> lendingsForUser = new ArrayList<Lending>();

            // Searching the lending of the particular and classify them
            for (int j = 0; j < lendings.size(); j++) {
                if (lendings.get(j).student.idStud == idStudent) {
                    //computing the duration
                    String dateLent = lendings.get(j).lendDate;
                    int duration = lendings.get(j).duration;
                    String lentDate = dateLent.split(" ")[0];

                    long elapseddays = elapsedDays(currentDate, lentDate);

                    if (elapseddays > duration) {
                        System.out.println("Duration holded " + (elapseddays - duration));
                        lendingsForUser.add(lendings.get(j));
                        //increase the number of items in late return

                        //remove the item
                        // lendings.remove(j);
                    }
                }
            }
            //Appending the user with its corresponding items
            if (lendingsForUser.size() > 0) {
                Student student = lendings.get(i).student;
                lendingByUser.add(new LendingByUser(student, lendingsForUser));
            }
        }
        return lendingByUser;
    }

    void enginerenderStudentPenalized(ArrayList<LendingByUser> penalizedStuds) {
        ArrayList<String> studentNames = new ArrayList<String>();

        for (int i = 0; i < penalizedStuds.size(); i++) {

            if (!studentNames.contains(penalizedStuds.get(i).student.fullname)) {
                studentNames.add(penalizedStuds.get(i).student.fullname);
                renderStudentPenalized(penalizedStuds.get(i));
                totalitems = totalitems + penalizedStuds.get(i).lendings.size();
                totalusers++;
            }

        }
    }

    private void renderStudentPenalized(LendingByUser penalizedStud) {
        String template_fxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?import com.jfoenix.controls.JFXButton?> <?import javafx.geometry.Insets?> <?import javafx.scene.Cursor?> <?import javafx.scene.control.Label?> <?import javafx.scene.control.ScrollPane?> <?import javafx.scene.control.Separator?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.Pane?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?> <HBox alignment=\"CENTER_LEFT\" prefHeight=\"65.0\" prefWidth=\"1274.0\"> <children> <ImageView fitHeight=\"49.0\" fitWidth=\"46.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\"images/4x/penalties.png\" /> </image> </ImageView> <VBox alignment=\"CENTER_LEFT\" prefHeight=\"66.0\" prefWidth=\"364.0\"> <children> <Label text=\"" + penalizedStud.student.fullname + "\"> <font> <Font name=\"Century Gothic Bold\" size=\"15.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" text=\"" + penalizedStud.student.depart + "\" textFill=\"#5e5e5e\"> <font> <Font name=\"Century Gothic\" size=\"13.0\" /> </font> </Label> </children> <HBox.margin> <Insets left=\"25.0\" /> </HBox.margin> </VBox> <VBox alignment=\"CENTER_LEFT\" layoutX=\"95.0\" layoutY=\"10.0\" prefHeight=\"66.0\" prefWidth=\"583.0\"> <children> <Label text=\"Number of items\"> <font> <Font name=\"Century Gothic Bold\" size=\"15.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" text=\"" + penalizedStud.lendings.size() + " item" + (penalizedStud.lendings.size() > 1 ? "s" : "") + "\" textFill=\"#5e5e5e\"> <font> <Font name=\"Century Gothic\" size=\"13.0\" /> </font> </Label> </children> </VBox> <JFXButton prefHeight=\"50.0\" prefWidth=\"220.0\" style=\"-fx-background-color: #6534AC;\" text=\"More details\" textFill=\"WHITE\" translateX=\"38.0\"> <font> <Font size=\"19.0\" /> </font> <graphic> <ImageView fitHeight=\"24.0\" fitWidth=\"21.0\" pickOnBounds=\"true\" preserveRatio=\"true\" translateX=\"-10.0\" translateY=\"2.0\"> <image> <Image url=\"images/4x/moreDetail.png\" /> </image> </ImageView> </graphic> </JFXButton> </children> </HBox>";

        try {

            FXMLLoader loader = new FXMLLoader();
            HBox layout = (HBox) loader.load(
                    new ByteArrayInputStream(template_fxml.getBytes()
                    ));

            //getting the button
            Node givebackButton = layout.getChildren().get(layout.getChildren().size() - 1);
            givebackButton.setCursor(Cursor.HAND);

            //Creating the mouse event handler
            EventHandler<MouseEvent> giveBackEvent = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    //show student penalized details with its items
                    showStuddetailsWithItems(penalizedStud);
                }
            };
            //Registering the event filter
            givebackButton.addEventFilter(MouseEvent.MOUSE_CLICKED, giveBackEvent);

//                        Add seperator
            Separator separator = new Separator();
            penaltiesContainer.getChildren().addAll(layout, separator);

        } catch (IOException ex) {
            System.out.println("PArsing error");
            Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void showStuddetailsWithItems(LendingByUser penalizedStud) {

        StudentProfileController.loadStudProfileSreen(penalizedStud);
    }

    @FXML
    private void showLending(ActionEvent event) {
        //reset all the variables
        totalitems = 0;
        totalusers = 0;
        lendings = new ArrayList<Lending>();
        penaltiesContainer.getChildren().clear();
        try {
            App.setRoot("lending");
        } catch (IOException ex) {
            Logger.getLogger(LendingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showHomePage(MouseEvent event) {
        try {
            App.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import Model.Lending;
import Model.LendingByUser;
import Model.LibItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class StudentProfileController implements Initializable {

    @FXML
    private Label totalItems;
    @FXML
    private Label fullName;
    @FXML
    private Label sex;
    @FXML
    private Label matricule;
    @FXML
    private Label department;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label titleItemsHold;
    @FXML
    private VBox itemsContainer;

    public static LendingByUser userDetailsVal;
    @FXML
    private Label nameTitle;
    @FXML
    private Label departTitle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // clearing the items container
        itemsContainer.getChildren().clear();
        //initialising all the variable
        totalItems.setText("Hold " + userDetailsVal.lendings.size() + " item" + (userDetailsVal.lendings.size() > 1 ? "s" : ""));

        // title
        nameTitle.setText(userDetailsVal.student.fullname);
        departTitle.setText(userDetailsVal.student.depart);

        System.out.println("Let's check this shit");
        System.out.println(userDetailsVal.student.email);
        System.out.println(userDetailsVal.student.tel);

        // Student informations
        fullName.setText(userDetailsVal.student.fullname);
        sex.setText(userDetailsVal.student.sex);
        matricule.setText(userDetailsVal.student.matricule);
        department.setText(userDetailsVal.student.depart);
        email.setText(userDetailsVal.student.email);
        phone.setText(userDetailsVal.student.tel);

        // title of the screen
        titleItemsHold.setText(userDetailsVal.student.fullname + " hold " + userDetailsVal.lendings.size() + " items");

        // rendering the template
        engineRenderHoldedItem(userDetailsVal.lendings);
    }

    public static void loadStudProfileSreen(LendingByUser userDetails) {
        userDetailsVal = userDetails;

        try {
            App.setRoot("StudentProfile");
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    void engineRenderHoldedItem(ArrayList<Lending> lendindsByUser){
        for(int i = 0 ; i < lendindsByUser.size(); i++){
            renderHoldedItem(lendindsByUser.get(i));
        }
    }

    void renderHoldedItem(Lending lending) {
        String template_fxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?import javafx.geometry.Insets?> <?import javafx.scene.Cursor?> <?import javafx.scene.control.Label?> <?import javafx.scene.control.ScrollPane?> <?import javafx.scene.control.Separator?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?> <HBox alignment=\"CENTER_LEFT\" prefHeight=\"65.0\" prefWidth=\"1274.0\"> <children> <ImageView fitHeight=\"49.0\" fitWidth=\"46.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\""+lending.libItem.itemIcon+"\" /> </image> </ImageView> <VBox alignment=\"CENTER_LEFT\" prefHeight=\"66.0\" prefWidth=\"364.0\"> <children> <Label text=\""+lending.libItem.itemName+"\"> <font> <Font name=\"Century Gothic Bold\" size=\"15.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" text=\"Lent on "+lending.lendDate+"\" textFill=\"#5e5e5e\"> <font> <Font name=\"Century Gothic\" size=\"13.0\" /> </font> </Label> </children> <HBox.margin> <Insets left=\"25.0\" /> </HBox.margin> </VBox> <VBox alignment=\"CENTER_LEFT\" layoutX=\"95.0\" layoutY=\"10.0\" prefHeight=\"66.0\" prefWidth=\"289.0\"> <children> <Label text=\"Was supposed to return on\"> <font> <Font name=\"Century Gothic Bold\" size=\"15.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" text=\""+lending.returnDay+"\" textFill=\"#5e5e5e\"> <font> <Font name=\"Century Gothic\" size=\"13.0\" /> </font> </Label> </children> </VBox> <VBox alignment=\"CENTER_LEFT\" layoutX=\"445.0\" layoutY=\"10.0\" prefHeight=\"66.0\" prefWidth=\"505.0\"> <children> <Label text=\"Delay day"+ (lending.delayDay > 1 ? "s" : "")+"\"> <font> <Font name=\"Century Gothic Bold\" size=\"15.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" text=\""+lending.delayDay+" day"+ (lending.delayDay > 1 ? "s" : "")+"\" textFill=\"#F70000\"> <font> <Font name=\"Century Gothic\" size=\"13.0\" /> </font> </Label> </children> </VBox> <ImageView fitHeight=\"30.0\" fitWidth=\"27.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\"images/4x/penalties.png\" /> </image> </ImageView> </children> </HBox>";

        try {

            FXMLLoader loader = new FXMLLoader();
            HBox layout = (HBox) loader.load(
                    new ByteArrayInputStream(template_fxml.getBytes()
                    ));

//                        Add seperator
            Separator separator = new Separator();
            itemsContainer.getChildren().addAll(layout, separator);

        } catch (IOException ex) {
            System.out.println("PArsing error");
            Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String computeReturnDay(String lentDay,int duration){
        return "";
    }

    @FXML
    private void showPenaltyScreen(MouseEvent event) {
        try {
            App.setRoot("penalties");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

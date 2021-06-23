/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import Model.LibItem;
import Model.Student;
import database.DB;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author X M G
 */
public class SearchEngine {

    public String searchItem;
    Alert a = new Alert(Alert.AlertType.WARNING);
    DB db = new DB();
    Connection con;
    String sql;
    String currentDate;

    String[] months = {"January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public SearchEngine(String searchItem) {
        this.searchItem = searchItem;

         // get the current date
         DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
         LocalDateTime now = LocalDateTime.now();
         String currentDated = dtf.format(now);
         currentDate = currentDated.split(" ")[0];

        con = db.getConnection();
    }

    public void borrowItem(int idStud, int idLibItem, int duration, String borrowDate) throws IOException {
        System.out.println("" + idLibItem);
        System.out.println("" + idStud);
        if(getHoldeditems(idStud)<= 3){
            if(!checkPenalty(idStud)){
                try {
                    sql = "insert into itemstostudent (idStudent,idLibItem,dateBorrow,duration) values(?,?,?,?)";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setInt(1, idStud);
                    st.setInt(2, idLibItem);
                    st.setString(3, borrowDate);
                    st.setInt(4, duration);
                    st.execute();

                // HomePageController homePageController = new HomePageController();

                    // Confirmation message
                    // set alert type
                    Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                    // Add a custom icon.
                    stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Book Borrowed successuflly!");
                    a.show();

                    //decreasing the stock value
                    sql = "update  libraryitem set stock = stock - 1 where idLibItem = " + idLibItem + " and stock > 0";
                    try {
                        Statement sta = con.createStatement()  ;
                        sta.executeUpdate(sql);

                        System.out.println("Stock decremented");
                    } catch (SQLException e) {
                        System.out.println("Update stock error");
                        System.out.println(e.getMessage());
                    }

                    //Closing the borrow screen
                   App.setRoot("homePage");
                  
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }else{
                Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                // Add a custom icon.
                stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
                a.setAlertType(AlertType.ERROR);
                a.setContentText("This student is penalized! cannot lend any item!");
                a.show();
            }
        }else{
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
            a.setAlertType(AlertType.ERROR);
            a.setContentText("This student already have 04 library items! cannot lend more.");
            a.show();
        }
    }

    public int getHoldeditems(int idStudent){
        int holdedItems = 0;
        sql = "select COUNT(idBorrow) as totalBorrow from itemstostudent where idStudent = "+idStudent;
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                holdedItems =  rs.getInt("totalBorrow");
            }
            return holdedItems;
        }catch(SQLException e){
            System.out.println("Database error");

            return holdedItems;
        }
    }

    public boolean checkPenalty(int idStudent){
        boolean penalized = false;
        sql = "select * from itemstostudent where idStudent = "+idStudent;
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int duration =  rs.getInt("duration");
                String lendDate = rs.getString("dateBorrow");

                 //computing elapsedDays
                 String lentDate = lendDate.split(" ")[0];
                 long elapseddays = PenaltyController.elapsedDays(currentDate, lentDate);
                
                 //determining the lateness
                 if(elapseddays > duration){
                    penalized = true;
                 }
            }
            return penalized;
        }catch(SQLException e){
            return penalized;
        }
        
    }

    public Student[] searchStudent(VBox searchContainer, int idLibItem, int duration) {
        //Clear the result zone
        searchContainer.getChildren().clear();

        System.out.println("Children suppossed to cleared");
        System.out.println(searchContainer.getChildren().toString());

        Student[] studentsFound = new Student[3];
        sql = "select * from student,department where fullname like '%" + this.searchItem + "%' and student.idDepart = department.idDepart LIMIT 3";

        try {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int i = 0;
            while (rs.next()) {

                int idStud = rs.getInt("idStudent");
                String fullName = rs.getString("fullName");
                String tel = rs.getString("tel");
                String email = rs.getString("email");
                String depart = rs.getString("department");
                String sex = "Masculin";
                String matricule = "BGNOSSJJS";

                if (idStud > 0) {

                    //Render the template
                    String FXML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?import com.jfoenix.controls.JFXButton?> <?import javafx.geometry.Insets?> <?import javafx.scene.Cursor?> <?import javafx.scene.control.Label?> <?import javafx.scene.control.TextField?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.ColumnConstraints?> <?import javafx.scene.layout.GridPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.Pane?> <?import javafx.scene.layout.RowConstraints?> <?import javafx.scene.layout.StackPane?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?> <HBox alignment=\"CENTER_LEFT\" maxHeight=\"1.7976931348623157E308\" maxWidth=\"1.7976931348623157E308\" prefHeight=\"50.0\" prefWidth=\"199.0\" style=\"-fx-background-color: #fff; -fx-background-radius: 45px;\"> <children> <ImageView fitHeight=\"58.0\" fitWidth=\"70.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\"images/4x/userIcon.png\" /> </image> </ImageView> <VBox prefHeight=\"50.0\" prefWidth=\"582.0\"> <HBox.margin> <Insets bottom=\"5.0\" left=\"25.0\" top=\"5.0\" /> </HBox.margin> <children> <Label prefHeight=\"31.0\" prefWidth=\"581.0\" text=\"" + fullName + "\" textFill=\"#1e1e1e\"> <font> <Font name=\"Century Gothic Bold\" size=\"24.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" prefHeight=\"19.0\" prefWidth=\"578.0\" text=\"" + depart + "\" textFill=\"#595959\"> <font> <Font name=\"Century Gothic\" size=\"15.0\" /> </font> </Label> </children> </VBox> <JFXButton  contentDisplay=\"RIGHT\" prefHeight=\"42.0\" prefWidth=\"212.0\" style=\"-fx-background-color: #6534AC; -fx-background-radius: 45px;\" text=\"Lend\" textFill=\"WHITE\"> <graphic> <ImageView fitHeight=\"21.0\" fitWidth=\"21.0\" pickOnBounds=\"true\" preserveRatio=\"true\" translateX=\"10.0\" translateY=\"2.0\"> <image> <Image url=\"images/4x/borrow_sign.png\" /> </image> </ImageView> </graphic> <font> <Font name=\"Century Gothic Bold\" size=\"21.0\" /> </font></JFXButton> </children> <VBox.margin> <Insets bottom=\"10.0\" /> </VBox.margin></HBox>";
                    try {
                        if (i <= 2) {
                            FXMLLoader loader = new FXMLLoader();
                            HBox layout = (HBox) loader.load(
                                    new ByteArrayInputStream(FXML_STRING.getBytes()
                                    ));

                            layout.setCursor(Cursor.HAND);

                            //Creating the mouse event handler
                            EventHandler<MouseEvent> borrowItem = new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent e) {
                                    // get the current date
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                                    LocalDateTime now = LocalDateTime.now();
                                    System.out.println(dtf.format(now));
                                    try {
                                        borrowItem(idStud, idLibItem, duration, dtf.format(now));
                                    } catch (IOException ex) {
                                        Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            };
                            //Registering the event filter
                            layout.addEventFilter(MouseEvent.MOUSE_CLICKED, borrowItem);
                            searchContainer.getChildren().add(layout);
                            i++;
                        }

                    } catch (IOException ex) {
                        System.out.println("PArsing error");
                        Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    studentsFound[i] = new Student(idStud, fullName, tel, email, depart,sex,matricule);
                    i++;
                }

                System.out.println("Final result " + i);
            }

            //Checking if there is no result
            System.out.println("Student Array");
            System.out.println("" + Arrays.toString(studentsFound).length());

            if (Arrays.toString(studentsFound).length() == 18) {

                ImageView emptySearchResult = new ImageView();
                emptySearchResult.setImage(new Image("images/4x/notFound.jpg"));
                searchContainer.getChildren().add(emptySearchResult);
                System.out.println("No result");
            }

            return studentsFound;

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            return studentsFound;
        }

    }

    public LibItem[] searchLibItems() {

        LibItem[] libItemsFound = new LibItem[3];
        sql = "select * from  libraryitem where fullname like %'" + this.searchItem + "'% LIMIT 3";
        return libItemsFound;

    }
}

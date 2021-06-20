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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
        borrowesItemContainer.getChildren().clear();
        sql = "select * from student ,libraryitem,itemstostudent,department where itemstostudent.idStudent = student.idStudent and itemstostudent.idLibItem = libraryitem.idLibItem and department.idDepart = student.idDepart order by idBorrow ASC";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                int idBorrow = rs.getInt("idBorrow");
                String lender = rs.getString("fullName");
                String departmentLender = rs.getString("department");
                String lendDate = rs.getString("dateBorrow");
                String itemType = rs.getString("itemType");
                String itemName = rs.getString("itemName");
                String itemIcon = "images/4x/book_active.png";

                if (null != itemType) {
                    switch (itemType) {
                        case "Book":
                            itemIcon = "images/4x/book_active.png";
                            break;
                        case "CD":
                            itemIcon = "images/4x/cd_active.png";
                            break;
                        case "Research document":
                            itemIcon = "images/4x/docs_active.png";
                            break;
                        default:
                            break;
                    }
                }

                System.out.println(itemType);
                System.out.println(itemIcon);

                //template rendering
                if (idBorrow > 0) {
                    String fxmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                            + "\n"
                            + "<?import com.jfoenix.controls.JFXButton?> <?import javafx.geometry.Insets?> <?import javafx.scene.Cursor?> <?import javafx.scene.control.Label?> <?import javafx.scene.control.ScrollPane?> <?import javafx.scene.control.Separator?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.Pane?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?><HBox alignment=\"CENTER_LEFT\" prefHeight=\"65.0\" prefWidth=\"1274.0\"> <children> <ImageView fitHeight=\"49.0\" fitWidth=\"46.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\"" + itemIcon + "\" /> </image> </ImageView> <VBox alignment=\"CENTER_LEFT\" prefHeight=\"66.0\" prefWidth=\"364.0\"> <children> <Label text=\"" + itemName + "\"> <font> <Font name=\"Century Gothic Bold\" size=\"15.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" text=\"Lent on " + lendDate + "\" textFill=\"#5e5e5e\"> <font> <Font name=\"Century Gothic\" size=\"13.0\" /> </font> </Label> </children> <HBox.margin> <Insets left=\"25.0\" right=\"45.0\" /> </HBox.margin> </VBox> <VBox alignment=\"CENTER_LEFT\" layoutX=\"95.0\" layoutY=\"10.0\" prefHeight=\"66.0\" prefWidth=\"583.0\"> <children> <Label text=\"Lender\"> <font> <Font name=\"Century Gothic Bold\" size=\"15.0\" /> </font> </Label> <Label layoutX=\"10.0\" layoutY=\"10.0\" text=\"" + lender + " -" + departmentLender + "\" textFill=\"#5e5e5e\"> <font> <Font name=\"Century Gothic\" size=\"13.0\" /> </font> </Label> </children> </VBox> <JFXButton prefHeight=\"50.0\" prefWidth=\"220.0\" style=\"-fx-background-color: #6534AC;\" text=\"Give Back\" textFill=\"WHITE\" translateX=\"38.0\"> <font> <Font size=\"19.0\" /> </font> <graphic> <ImageView fitHeight=\"24.0\" fitWidth=\"21.0\" pickOnBounds=\"true\" preserveRatio=\"true\" translateX=\"-10.0\" translateY=\"2.0\"> <image> <Image url=\"images/4x/putBack.png\" /> </image> </ImageView> </graphic></JFXButton> </children> </HBox>";

                    try {

                        FXMLLoader loader = new FXMLLoader();
                        HBox layout = (HBox) loader.load(
                                new ByteArrayInputStream(fxmlStr.getBytes()
                                ));

                        layout.setCursor(Cursor.HAND);

                        //Creating the mouse event handler
//                        EventHandler<MouseEvent> borrowItem = new EventHandler<MouseEvent>() {
//                            @Override
//                            public void handle(MouseEvent e) {
//                                // get the current date
//                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//                                LocalDateTime now = LocalDateTime.now();
//                                System.out.println(dtf.format(now));
//                                try {
//                                    borrowItem(idStud, idLibItem, duration, dtf.format(now));
//                                } catch (IOException ex) {
//                                    Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//                        };
                        //Registering the event filter
                        //  layout.addEventFilter(MouseEvent.MOUSE_CLICKED, borrowItem);
//                        Add seperator
                        Separator separator = new Separator();
                        borrowesItemContainer.getChildren().addAll(layout, separator);

                    } catch (IOException ex) {
                        System.out.println("PArsing error");
                        Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

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

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Lending;
import Model.LibItem;
import Model.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
    @FXML
    private TextField searchLendUI;

    String currentDate;

    ArrayList<Lending> lendings = new ArrayList<Lending>();

    // create a alert
    Alert a = new Alert(Alert.AlertType.WARNING);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         // get the current date
         DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
         LocalDateTime now = LocalDateTime.now();
         String currentDated = dtf.format(now);
         currentDate = currentDated.split(" ")[0];
        //initializing the database connection...
        con = db.getConnection();

        //Get total lending...
        getTotalBorrow();

        //generateTemplate
        sql = "select * from student ,libraryitem,itemstostudent,department where itemstostudent.idStudent = student.idStudent and itemstostudent.idLibItem = libraryitem.idLibItem and department.idDepart = student.idDepart order by idBorrow ASC";
        getTemplate(sql);

        

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

    public void getTemplate(String sqlParam) {
        borrowesItemContainer.getChildren().clear();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlParam);
            int i = 0;
            while (rs.next()) {
                // Information about lending
                int idBorrow = rs.getInt("idBorrow");
                String lendDate = rs.getString("dateBorrow");
                int duration = rs.getInt("duration");
                
                // Information about the library
                int idLibItem = rs.getInt("idLibItem");         
                String itemType = rs.getString("itemType");
                String itemName = rs.getString("itemName");
                String itemAuthor = rs.getString("author");
                int stock = rs.getInt("stock");       
                String itemIcon = "images/4x/book_active.png";
                String position =  rs.getString("position");
                
                // information about the borrower
                String lender = rs.getString("fullName");
                int idStud = rs.getInt("idStudent");
                String departmentLender = rs.getString("department");
                String tel = rs.getString("tel");
                String email = rs.getString("email");
                String sexe = "M";
                String matricule = "MG144714";

                //computing elapsedDays
                String lentDate = lendDate.split(" ")[0];
                long elapseddays = PenaltyController.elapsedDays(currentDate, lentDate);

                // computing return day
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                final LocalDate firstDate = LocalDate.parse(lentDate, formatter);
                LocalDate returnDate = firstDate.plusDays(duration);

                // Storing data inside the object
                if( idBorrow>0){
                    Student student = new Student(idStud,lender,tel,email,departmentLender,sexe,matricule);
                    LibItem libItem = new LibItem(idLibItem,itemIcon,itemName,itemAuthor,stock,position);
                    Lending lending = new Lending(idBorrow, lendDate, libItem, student,duration,returnDate.format(formatter),(elapseddays-duration));
                    lendings.add(lending);

                    // incrementing for the next insertion
                    i++;
                }
                

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

                        //getting the button
                        Node givebackButton = layout.getChildren().get(layout.getChildren().size() - 1);

                        //Creating the mouse event handler
                        EventHandler<MouseEvent> giveBackEvent = new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                System.out.println("Remove this shit from here");
                                giveItemBack(idLibItem, idBorrow);

                                // Confirmation message
                                // set alert type
                                a.setAlertType(Alert.AlertType.INFORMATION);
                                a.setContentText("Item was successfully returned back!");
                                a.show();
                                try {
                                    App.setRoot("lending");
                                } catch (IOException ex) {
                                    System.out.println("Unable to reload page");
                                    Logger.getLogger(LendingController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        };
                        //Registering the event filter
                        givebackButton.addEventFilter(MouseEvent.MOUSE_CLICKED, giveBackEvent);

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

    public void giveItemBack(int idLibItem, int idBorrow) {
        //delete the book from borrow
        sql = "delete from itemstostudent where idBorrow = ?";
        try {
            PreparedStatement preparedStmt = con.prepareStatement(sql);
            preparedStmt.setInt(1, idBorrow);
            preparedStmt.execute();
        } catch (SQLException e) {
            System.out.println("Database error");
        }

        //increasing the stock of the book
        sql = "update  libraryitem set stock = stock + 1 where idLibItem = " + idLibItem;
        try {
            Statement sta = con.createStatement();
            int rs = sta.executeUpdate(sql);

            System.out.println("Stock incremented");
        } catch (SQLException e) {
            System.out.println("Update stock error");
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void popScreen(MouseEvent event) throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    private void searchBorrowItem(KeyEvent event) {
        if (searchLendUI.getText().length() >= 1) {
            sql = "select * from student ,libraryitem,itemstostudent,department where itemstostudent.idStudent = student.idStudent and itemstostudent.idLibItem = libraryitem.idLibItem and department.idDepart = student.idDepart and libraryitem.itemName like '%" + searchLendUI.getText() + "%' order by idBorrow ASC";
            getTemplate(sql);
        }
    }

    @FXML
    private void showPenalties(ActionEvent event) {
        PenaltyController.launchPenaltyScreen(lendings);
    }

}

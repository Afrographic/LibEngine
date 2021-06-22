package com.mycompany.libengine;

import Model.LibItem;
import Model.Student;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import database.DB;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.application.Application;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.css.*;

import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class HomePageController implements Initializable {

    @FXML
    private JFXButton registerStudBtn;

    @FXML
    private JFXButton registerItemsBtn;

    @FXML
    private JFXButton showBooksBtn;
    @FXML
    private JFXButton showRDBtn;
    @FXML
    private JFXButton showCDBtn;
    @FXML
    private GridPane templateRenderer;

    LibItem[] libitems = new LibItem[9];

    DB db = new DB();
    Connection con;
    String sql;
    // create a alert
    Alert a = new Alert(Alert.AlertType.WARNING);
    @FXML
    private ImageView bookIcon;
    @FXML
    private ImageView docIcon;
    @FXML
    private ImageView cdIcon;
    @FXML
    private ImageView registerStudIcon;
    @FXML
    private ImageView registerItemIcon;
    @FXML
    private ImageView manageStudIcon;
    @FXML
    private HBox overflowPane;
    @FXML
    private VBox borrowItemView;
    @FXML
    private ImageView closeBorrowViewBtn;
    @FXML
    private ImageView bItemIcon;
    @FXML
    private Label bItemTitle;
    @FXML
    private Label bItemAuthor;
    @FXML
    private Label bitemStock;
    @FXML
    private Label bItemLocation;
    @FXML
    private TextField searchUserInput;
    @FXML
    private ImageView searchTriggerButton;
    @FXML
    private VBox resultSearchContainer;
    @FXML
    private StackPane parent;
    @FXML
    private VBox parentSnackBar;
    @FXML
    private TextField duration;
    @FXML
    private Label libItemID;
    @FXML
    private JFXButton lendBtn;

    Coords[] coords = new Coords[24];

    int durationVal = 14;
    int offsetQuery = 0;

    int totalBook;
    int totalRD;
    int totalCD;

    String currentItemType;
    @FXML
    private Label totalItems;
    @FXML
    private TextField searchItemHome;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // get the coordinates
        int k = 0;
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 3; j++) {
                coords[k] = new Coords(i, j);
                k++;
            }
        }

        //initializing the database connection
        con = db.getConnection();

        //show books at startUp
        sql = "select  * from libraryitem where itemType = 'Book' LIMIT 24 offset 0";
        generateItems("Book", "images/4x/book_active.png", 0,sql);

//        Hide the borrow book screen
        this.closeBorrowScreen();

        //set the default duration
        duration.setText("" + durationVal);

        //compute the total amount of each items per categories
        computeTotalItemsPerCategories();

        //init total items to books and search promptText
        currentItemType = "Book";
        totalItems.setText(totalBook + " Books");
        searchItemHome.setPromptText("Search a book...");

    }

    void computeTotalItemsPerCategories() {
        //retrieve total books
        sql = "select COUNT(idLibItem) as totalBooks from libraryitem where itemType = 'Book'";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                totalBook = rs.getInt("totalBooks");
            }
        } catch (SQLException e) {
            System.out.println("Database error");
        }
        //retrieve total Cds
        sql = "select COUNT(idLibItem) as totalCDs from libraryitem where itemType = 'CD'";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                totalCD = rs.getInt("totalCDs");
            }
        } catch (SQLException e) {
            System.out.println("Database error");
        }

        //retrieve total research material
        sql = "select COUNT(idLibItem) as totalRD from libraryitem where itemType = 'Research document'";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                totalRD = rs.getInt("totalRD");
            }
        } catch (SQLException e) {
            System.out.println("Database error");
        }
    }

    public void closeBorrowScreen() {

        overflowPane.setStyle("-fx-translate-y:-800px");
        borrowItemView.setStyle("-fx-scale-x:0;-fx-scale-y:0;-fx-scale-z:0");
        closeBorrowViewBtn.setStyle("-fx-scale-x:0;-fx-scale-y:0;-fx-scale-z:0");

    }

    @FXML
    private void closeBorrowView(MouseEvent event) {
        closeBorrowScreen();
    }

    void UncolorButtons() {
        bookIcon.setImage(new Image("images/4x/books.png"));
        docIcon.setImage(new Image("images/4x/docs.png"));
        cdIcon.setImage(new Image("images/4x/cd.png"));

        showBooksBtn.setTextFill(Color.web("#4D4D4D"));

        showRDBtn.setTextFill(Color.web("#4D4D4D"));

        showCDBtn.setTextFill(Color.web("#4D4D4D"));

        showRDBtn.setStyle("-fx-background-color:#fff;-fx-background-radius:45px;");
        showBooksBtn.setStyle("-fx-background-color:#fff;-fx-background-radius:45px;");
        showCDBtn.setStyle("-fx-background-color:#fff;-fx-background-radius:45px;");

    }

    void borrowItem(LibItem libItem) {

        if(libItem.stock > 0){

            borrowItemView.setStyle("-fx-scale-x:1;-fx-scale-y:1;-fx-scale-z:1");
            closeBorrowViewBtn.setStyle("-fx-scale-x:1;-fx-scale-y:1;-fx-scale-z:1");
            overflowPane.setStyle("-fx-translate-y:0px");

            //        update the borrow screen
            bItemIcon.setImage(new Image(libItem.itemIcon));
            bItemAuthor.setText(libItem.itemAuthor);
            bitemStock.setText("" + libItem.stock);
            bItemLocation.setText(libItem.position);
            bItemTitle.setText(libItem.itemName);
            libItemID.setText("" + libItem.libItemId);
         }else{
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Sorry this item is out of stock!");
            a.show();
         }
    }

    public void generateItems(String itemType, String itemIcon, int offset, String sql) {
        //Clear GridPane
        templateRenderer.getChildren().clear();

        try {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int i = 0;
            while (rs.next()) {
                String itemName = rs.getString("itemName");
                String author = rs.getString("author");
                String position = rs.getString("position");
                int stock = rs.getInt("stock");
                int idLibItem = rs.getInt("idLibItem");
                int edition = 2021;

                String FXML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<?import com.jfoenix.controls.JFXButton?><?import javafx.scene.control.Separator?> <?import javafx.geometry.Insets?> <?import javafx.scene.paint.*?><?import javafx.scene.Cursor?> <?import javafx.scene.control.Label?> <?import javafx.scene.control.TextField?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.ColumnConstraints?> <?import javafx.scene.layout.GridPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.Pane?> <?import javafx.scene.layout.RowConstraints?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?>  <VBox prefHeight=\"110.0\" prefWidth=\"239.0\" style=\"-fx-padding: 10px; -fx-border-color: #aaa; -fx-border-width: 0.5px;\"> <children> <HBox alignment=\"CENTER_LEFT\" prefHeight=\"35.0\" prefWidth=\"239.0\"> <children> <ImageView fitHeight=\"26.0\" fitWidth=\"41.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\"" + itemIcon + "\" /> </image> </ImageView> <Label prefHeight=\"32.0\" prefWidth=\"146.0\" text=\"" + itemName + "\" wrapText=\"true\"> <font> <Font name=\"Century Gothic Bold\" size=\"11.0\" /> </font> <HBox.margin> <Insets left=\"10.0\" /> </HBox.margin> </Label> <HBox alignment=\"CENTER\" prefHeight=\"21.0\" prefWidth=\"58.0\" style=\"-fx-background-color: "+(stock == 0 ? "#F70000":"#6534AC")+"; -fx-background-radius: 45px;\"> <HBox.margin> <Insets left=\"17.0\" /> </HBox.margin> <children> <Label text=\"" + stock + "\" textFill=\"WHITE\" /> </children> </HBox> </children> </HBox> <Separator opacity=\"0.38\" prefWidth=\"200.0\"> <VBox.margin> <Insets top=\"8.0\" /> </VBox.margin> </Separator> <HBox prefHeight=\"36.0\" prefWidth=\"230.0\"> <VBox.margin> <Insets top=\"5.0\" /> </VBox.margin> <children> <Label prefHeight=\"35.0\" prefWidth=\"103.0\" text=\"By " + author + "\" wrapText=\"true\"> <font> <Font name=\"Century Gothic\" size=\"10.0\" /> </font> </Label> <Label prefHeight=\"36.0\" prefWidth=\"51.0\" text=\"Edition " + edition + "\" textFill=\"#6534ac\" wrapText=\"true\"> <font> <Font name=\"Century Gothic Bold\" size=\"10.0\" /> </font> </Label> <JFXButton prefHeight=\"36.0\" prefWidth=\"75.0\" style=\"-fx-background-color: "+(stock == 0 ? "#C6C6C6":"#6534AC")+";\" text=\"Lend\" textFill=\""+(stock == 0 ? "#828282":"white")+"\"> <font> <Font name=\"Century Gothic Bold\" size=\"12.0\" /> </font> </JFXButton> </children> </HBox> </children> </VBox>";
                try {
                    if (i <= 24) {
                        FXMLLoader loader = new FXMLLoader();
                        VBox layout = (VBox) loader.load(
                                new ByteArrayInputStream(FXML_STRING.getBytes()
                                ));

                        layout.setCursor(Cursor.HAND);

                        if(stock == 0){
                            layout.setStyle("-fx-border-color:#F70000;-fx-padding:10px;-fx-border-width:0.5px");
                        }

                        //Creating the mouse event handler
                        EventHandler<MouseEvent> borrowItem = new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                borrowItem(new LibItem(idLibItem, itemIcon, itemName, author, stock, position));

                            }
                        };
                        //Registering the event filter
                        layout.addEventFilter(MouseEvent.MOUSE_CLICKED, borrowItem);

                        GridPane.setConstraints(layout, coords[i].y, coords[i].x);

                        templateRenderer.getChildren().add(layout);
                        i++;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void showBooks(ActionEvent event) {
        //
        UncolorButtons();
        bookIcon.setImage(new Image("images/4x/book_active.png"));
        showBooksBtn.setTextFill(Color.web("#6534AC"));
        showBooksBtn.setStyle("-fx-background-color:#EBE4F4;-fx-background-radius:45px;");

        sql = "select  * from libraryitem where itemType = 'Book' LIMIT 24 offset 0";
        generateItems("Book", "images/4x/book_active.png", offsetQuery,sql);

        //init total items to books
        currentItemType = "Book";
        totalItems.setText(totalBook + " Books");
        searchItemHome.setPromptText("Search a book...");
    }

    @FXML
    private void showRD(ActionEvent event) {
        UncolorButtons();
        docIcon.setImage(new Image("images/4x/docs_active.png"));
        showRDBtn.setTextFill(Color.web("#6534AC"));
        showRDBtn.setStyle("-fx-background-color:#EBE4F4;-fx-background-radius:45px;");

        sql = "select  * from libraryitem where itemType = 'Research document' LIMIT 24 offset 0";
        generateItems("Research document", "images/4x/docs_active.png", offsetQuery,sql);

        //init total items to books
        currentItemType = "Research document";
        totalItems.setText(totalRD + " RDs");
        searchItemHome.setPromptText("Search a research document...");
    }

    @FXML
    private void showCD(ActionEvent event) {
        UncolorButtons();
        cdIcon.setImage(new Image("images/4x/cd_active.png"));
        showCDBtn.setTextFill(Color.web("#6534AC"));
        showCDBtn.setStyle("-fx-background-color:#EBE4F4;-fx-background-radius:45px;");
        
        sql = "select  * from libraryitem where itemType = 'CD' LIMIT 24 offset 0";
        generateItems("CD", "images/4x/cd_active.png", offsetQuery,sql);

        //init total items to books
        currentItemType = "CD";
        totalItems.setText(totalCD + " CDs");
        searchItemHome.setPromptText("Search a CD...");
    }

    @FXML
    private void showRegisterItemScreen(ActionEvent event) throws IOException {
        App.setRoot("itemRegistration");
    }

    @FXML
    private void showRegisterStudScreen(ActionEvent event) throws IOException {
        App.setRoot("studentsRegistration");
    }

    @FXML
    private void searchStudent(MouseEvent event) {
        searchStudentTemplate();
    }

    public void searchStudentTemplate() {
        String searchitem = searchUserInput.getText();
        Student[] studentsFound = new Student[3];

        if (searchitem.length() >= 1 && duration.getText().length() >= 1) {
            SearchEngine searchEngine = new SearchEngine(searchitem);
            int LidItemId = Integer.parseInt(libItemID.getText());
            durationVal = Integer.parseInt(duration.getText());
            studentsFound = searchEngine.searchStudent(resultSearchContainer, LidItemId, durationVal);

        } else {

            JFXSnackbar snackbar = new JFXSnackbar(parent);
            snackbar.setStyle("-jfx-background-color: #f44336; -fx-text-fill: WHITE;");

//            Customizing the snackBar
            String css = this.getClass().getClassLoader().getResource("style/snackbar.css").toExternalForm();
            snackbar.getStylesheets().add(css);

            snackbar.fireEvent(new SnackbarEvent(
                    new JFXSnackbarLayout("Please make sured the search area is not  empty!"),
                    Duration.seconds(4), null));
        }
    }

    @FXML
    private void showLendingScreen(ActionEvent event) throws IOException {
        App.setRoot("lending");
    }

    @FXML
    private void searchStudentLive(KeyEvent event) {
        searchStudentTemplate();
    }

    @FXML
    private void searchItemsHome(KeyEvent event) {

        if(searchItemHome.getText().length() >= 1){
            sql = "select  * from libraryitem where itemType = '"+currentItemType+"' and itemName like '%"+searchItemHome.getText()+"%' LIMIT 24 offset 0";
            generateItems("CD", "images/4x/cd_active.png", offsetQuery,sql);
         }

    }

}

class Coords {

    public int x;
    public int y;

    Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

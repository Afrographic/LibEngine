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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //initializing the database connection
        con = db.getConnection();

        //show books at startUp
        generateItems("Book", "images/4x/book_active.png");

//        Hide the borrow book screen
        this.closeBorrowScreen();
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
    }

    public void generateItems(String itemType, String itemIcon) {
        //Clear GridPane
        templateRenderer.getChildren().clear();
        sql = "select  * from libraryitem where itemType = '" + itemType + "' LIMIT 9";

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

                String FXML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<?import com.jfoenix.controls.JFXButton?> <?import javafx.geometry.Insets?> <?import javafx.scene.paint.*?><?import javafx.scene.Cursor?> <?import javafx.scene.control.Label?> <?import javafx.scene.control.TextField?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.ColumnConstraints?> <?import javafx.scene.layout.GridPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.Pane?> <?import javafx.scene.layout.RowConstraints?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?> <Pane prefHeight=\"244.0\" prefWidth=\"326.0\" style=\"-fx-border-color: #EEEEEE; -fx-border-width: 1px; -fx-border-radius: 12px;\"> <children> <VBox alignment=\"CENTER\" prefHeight=\"214.0\" prefWidth=\"326.0\"> <children> <HBox prefHeight=\"55.0\" prefWidth=\"326.0\"> <children> <ImageView fitHeight=\"41.0\" fitWidth=\"41.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\"" + itemIcon + "\" /> </image> </ImageView> <Label prefHeight=\"46.0\" prefWidth=\"173.0\" text=\"" + itemName + " \" textFill=\"#353535\" wrapText=\"true\"> <HBox.margin> <Insets left=\"15.0\" /> </HBox.margin> <font> <Font name=\"Century Gothic Bold\" size=\"16.0\" /> </font> </Label> </children> <padding> <Insets left=\"15.0\" right=\"15.0\" /> </padding> </HBox> <HBox layoutX=\"10.0\" layoutY=\"10.0\" prefHeight=\"57.0\" prefWidth=\"326.0\"> <children> <VBox prefHeight=\"59.0\" prefWidth=\"140.0\"> <children> <Label text=\"Author\" textFill=\"#8c8c8c\"> <font> <Font name=\"Century Gothic Bold\" size=\"12.0\" /> </font> </Label> <Label prefHeight=\"42.0\" prefWidth=\"145.0\" text=\"" + author + "\" wrapText=\"true\"> <font> <Font name=\"Century Gothic\" size=\"12.0\" /> </font> </Label> </children> </VBox> <HBox alignment=\"CENTER_LEFT\" prefHeight=\"0.0\" prefWidth=\"156.0\" style=\"-fx-background-color: #828484; -fx-background-radius: 45px;\"> <HBox.margin> <Insets left=\"5.0\" /> </HBox.margin> <children> <Label text=\"Stock\" textFill=\"WHITE\"> <font> <Font name=\"Century Gothic Bold\" size=\"18.0\" /> </font> </Label> <HBox alignment=\"CENTER\" maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" prefHeight=\"38.0\" prefWidth=\"70.0\" style=\"-fx-background-color: #fff; -fx-background-radius: 45px;\"> <HBox.margin> <Insets left=\"10.0\" /> </HBox.margin> <children> <Label text=\"" + stock + "\"> <font> <Font name=\"Century Gothic\" size=\"12.0\" /> </font> </Label> </children> </HBox> </children> <opaqueInsets> <Insets /> </opaqueInsets> <padding> <Insets left=\"15.0\" /> </padding> </HBox> </children> <padding> <Insets bottom=\"5.0\" left=\"15.0\" right=\"15.0\" top=\"5.0\" /> </padding> </HBox> <JFXButton prefHeight=\"44.0\" prefWidth=\"206.0\" scaleX=\"0.8\" scaleY=\"0.8\" scaleZ=\"0.8\" style=\"-fx-background-color: #6534AC; -fx-background-radius: 45px;\" text=\"Lend\" textFill=\"WHITE\"> <font> <Font name=\"Century Gothic\" size=\"12.0\" /> </font> <graphic> <ImageView fitHeight=\"28.0\" fitWidth=\"37.0\" pickOnBounds=\"true\" preserveRatio=\"true\" translateX=\"90.0\" translateY=\"3.0\"> <image> <Image url=\"images/4x/borrow_sign.png\" /> </image> </ImageView> </graphic> <padding> <Insets right=\"80.0\" /> </padding> <VBox.margin> <Insets top=\"15.0\" /> </VBox.margin>  </JFXButton> </children> </VBox> </children> </Pane>";
                try {
                    if (i <= 8) {
                        FXMLLoader loader = new FXMLLoader();
                        Pane layout = (Pane) loader.load(
                                new ByteArrayInputStream(FXML_STRING.getBytes()
                                ));

                        layout.setCursor(Cursor.HAND);

                        //Creating the mouse event handler
                        EventHandler<MouseEvent> borrowItem = new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                borrowItem(new LibItem(idLibItem, itemIcon, itemName, author, stock, position));

                            }
                        };
                        //Registering the event filter
                        layout.addEventFilter(MouseEvent.MOUSE_CLICKED, borrowItem);

                        if (i == 0) {
                            GridPane.setConstraints(layout, 0, 0);
                        }
                        if (i == 1) {
                            GridPane.setConstraints(layout, 1, 0);
                        }
                        if (i == 2) {
                            GridPane.setConstraints(layout, 2, 0);
                        }
                        if (i == 3) {
                            GridPane.setConstraints(layout, 0, 1);
                        }
                        if (i == 4) {
                            GridPane.setConstraints(layout, 1, 1);
                        }
                        if (i == 5) {
                            GridPane.setConstraints(layout, 2, 1);
                        }
                        if (i == 6) {
                            GridPane.setConstraints(layout, 0, 2);
                        }
                        if (i == 7) {
                            GridPane.setConstraints(layout, 1, 2);
                        }
                        if (i == 8) {
                            GridPane.setConstraints(layout, 2, 2);
                        }

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
        generateItems("Book", "images/4x/book_active.png");
    }

    @FXML
    private void showRD(ActionEvent event) {
        UncolorButtons();
        docIcon.setImage(new Image("images/4x/docs_active.png"));
        showRDBtn.setTextFill(Color.web("#6534AC"));
        showRDBtn.setStyle("-fx-background-color:#EBE4F4;-fx-background-radius:45px;");
        generateItems("Research document", "images/4x/docs_active.png");
    }

    @FXML
    private void showCD(ActionEvent event) {
        UncolorButtons();
        cdIcon.setImage(new Image("images/4x/cd_active.png"));
        showCDBtn.setTextFill(Color.web("#6534AC"));
        showCDBtn.setStyle("-fx-background-color:#EBE4F4;-fx-background-radius:45px;");
        generateItems("CD", "images/4x/cd_active.png");
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

    @FXML
    private void searchStudent(KeyEvent event) {
        searchStudentTemplate();
    }

    public void searchStudentTemplate() {
        String searchitem = searchUserInput.getText();
        Student[] studentsFound = new Student[3];

        if (searchitem.length() >= 1 && duration.getText().length() >= 1) {
            SearchEngine searchEngine = new SearchEngine(searchitem);
            int LidItemId = Integer.parseInt(libItemID.getText());
            int durationVal = Integer.parseInt(duration.getText());
            studentsFound = searchEngine.searchStudent(resultSearchContainer, LidItemId, durationVal);

        } else {

            JFXSnackbar snackbar = new JFXSnackbar(parent);
            snackbar.setStyle("-jfx-background-color: #f44336; -fx-text-fill: WHITE;");

//            Customizing the snackBar
            String css = this.getClass().getClassLoader().getResource("style/snackbar.css").toExternalForm();
            snackbar.getStylesheets().add(css);

            snackbar.fireEvent(new SnackbarEvent(
                    new JFXSnackbarLayout("Please make sure you input the duration and the search area must not be empty!"),
                    Duration.seconds(4), null));
        }
    }

    @FXML
    private void showLendingScreen(ActionEvent event) throws IOException {
        App.setRoot("lending");
    }

}

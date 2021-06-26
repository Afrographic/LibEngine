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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
    String currentItemIcon;
    @FXML
    private Label totalItems;
    @FXML
    private TextField searchItemHome;
    @FXML
    private ImageView resetSearchIcon;
    @FXML
    private ImageView registerStudIcon1;

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
        generateItems("Book", "images/4x/book_active.png", 0, sql);

//        Hide the borrow book screen
        this.closeBorrowScreen();

        //set the default duration
        duration.setText("" + durationVal);

        //compute the total amount of each items per categories
        computeTotalItemsPerCategories();

        //init total items to books and search promptText
        currentItemType = "Book";
        currentItemIcon = "images/4x/book_active.png";
        totalItems.setText(totalBook + " Books");
        searchItemHome.setPromptText("Search a book...");

        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        // Add a custom icon.
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));

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
        sql = "select  * from libraryitem where itemType = '" + currentItemType + "' LIMIT 24 offset 0";
        generateItems(currentItemType, currentItemIcon, offsetQuery, sql);
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

        if (libItem.stock > 0) {

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
        } else {

            a.setAlertType(AlertType.ERROR);
            a.setHeaderText("Cannot lend this item");
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

                int edition = rs.getInt("editionYear");

                LibItem libitem = new LibItem(idLibItem, itemIcon, Utils.formatString(itemName), Utils.formatString(author), stock, position, itemType);

                String FXML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<?import com.jfoenix.controls.JFXButton?><?import javafx.scene.control.Separator?> <?import javafx.geometry.Insets?> <?import javafx.scene.paint.*?><?import javafx.scene.Cursor?> <?import javafx.scene.control.Label?> <?import javafx.scene.control.TextField?> <?import javafx.scene.image.Image?> <?import javafx.scene.image.ImageView?> <?import javafx.scene.layout.AnchorPane?> <?import javafx.scene.layout.ColumnConstraints?> <?import javafx.scene.layout.GridPane?> <?import javafx.scene.layout.HBox?> <?import javafx.scene.layout.Pane?> <?import javafx.scene.layout.RowConstraints?> <?import javafx.scene.layout.VBox?> <?import javafx.scene.text.Font?>  <VBox prefHeight=\"110.0\" prefWidth=\"239.0\" style=\"-fx-padding: 10px; -fx-border-color: #aaa; -fx-border-width: 0.5px;\"> <children> <HBox alignment=\"CENTER_LEFT\" prefHeight=\"35.0\" prefWidth=\"239.0\"> <children> <ImageView fitHeight=\"26.0\" fitWidth=\"41.0\" pickOnBounds=\"true\" preserveRatio=\"true\"> <image> <Image url=\"" + itemIcon + "\" /> </image> </ImageView> <Label prefHeight=\"32.0\" prefWidth=\"146.0\" text=\"" + libitem.itemName + "\" wrapText=\"true\"> <font> <Font name=\"Century Gothic Bold\" size=\"11.0\" /> </font> <HBox.margin> <Insets left=\"10.0\" /> </HBox.margin> </Label> <HBox alignment=\"CENTER\" prefHeight=\"21.0\" prefWidth=\"58.0\" style=\"-fx-background-color: #fff; -fx-background-radius: 45px;\"> <HBox.margin> <Insets left=\"17.0\" /> </HBox.margin> <children> <Label text=\"S: " + stock + "\" textFill=\"" + (stock == 0 ? "#F70000" : "#4d4d4d") + "\" /> </children> </HBox> </children> </HBox> <Separator opacity=\"0.38\" prefWidth=\"200.0\"> <VBox.margin> <Insets top=\"8.0\" /> </VBox.margin> </Separator> <HBox prefHeight=\"36.0\" prefWidth=\"230.0\"> <VBox.margin> <Insets top=\"5.0\" /> </VBox.margin> <children> <Label prefHeight=\"35.0\" prefWidth=\"103.0\" text=\"By " + libitem.itemAuthor + "\" wrapText=\"true\"> <font> <Font name=\"Century Gothic\" size=\"10.0\" /> </font> </Label> <Label prefHeight=\"36.0\" prefWidth=\"51.0\" text=\"Edition " + (edition == 0 ? "2021" : edition) + "\" textFill=\"#6534ac\" wrapText=\"true\"> <font> <Font name=\"Century Gothic Bold\" size=\"10.0\" /> </font> </Label> <JFXButton prefHeight=\"36.0\" prefWidth=\"75.0\" style=\"-fx-background-color: " + (stock == 0 ? "#C6C6C6" : "#6534AC") + ";\" text=\"Lend\" textFill=\"" + (stock == 0 ? "#828282" : "white") + "\"> <font> <Font name=\"Century Gothic Bold\" size=\"12.0\" /> </font> </JFXButton> </children> </HBox> </children> </VBox>";
                try {
                    if (i <= 24) {
                        FXMLLoader loader = new FXMLLoader();
                        VBox layout = (VBox) loader.load(new ByteArrayInputStream(FXML_STRING.getBytes()));

                        if (stock == 0) {
                            layout.setStyle("-fx-border-color:#F70000;-fx-padding:10px;-fx-border-width:0.5px");
                        }

                        //Creating the mouse event handler
                        EventHandler<MouseEvent> borrowItem = new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                borrowItem(libitem);

                            }
                        };

                        // Create ContextMenu
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem item1 = new MenuItem("Update the stock");
                        item1.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                updateItemStock(libitem);
                            }
                        });
                        MenuItem item2 = new MenuItem("Delete the item");
                        item2.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                deleteItem(libitem);
                            }
                        });

                        MenuItem item3 = new MenuItem("Change position");
                        item3.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                changePosition(libitem);
                            }
                        });

                        // Add MenuItem to ContextMenu
                        contextMenu.getItems().addAll(item1, item2, item3);

                        // When user right-click on Circle
                        layout.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent event) {

                                contextMenu.show(layout, event.getScreenX(), event.getScreenY());
                            }
                        });

                        //Add borrow event to the borrow button
                        HBox lenParent = (HBox) layout.getChildren().get(layout.getChildren().size() - 1);
                        Node lenButton = lenParent.getChildren().get(layout.getChildren().size() - 1);
                        lenButton.setCursor(Cursor.HAND);
                        lenButton.addEventFilter(MouseEvent.MOUSE_CLICKED, borrowItem);

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

    void updateItemStock(LibItem libItem) {
        TextInputDialog dialog = new TextInputDialog("Current stock " + libItem.stock);
        // Get the Stage.
        dialog.setTitle("Update the stock");
        dialog.setHeaderText("You are about to update the stock of the " + libItem.itemType + " " + libItem.itemName);
        dialog.setContentText("Please enter the new stock :");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            int stock = Integer.parseInt(result.get());

            //increasing the stock of the book
            sql = "update  libraryitem set stock = " + stock + " where idLibItem = " + libItem.libItemId;
            try {
                Statement sta = con.createStatement();
                int rs = sta.executeUpdate(sql);

                sql = "select  * from libraryitem where itemType = '" + currentItemType + "' LIMIT 24 offset 0";
                generateItems(currentItemType, currentItemIcon, offsetQuery, sql);

                a.setAlertType(AlertType.INFORMATION);
                a.setHeaderText("Stock updated successfully!");

                a.show();
            } catch (SQLException e) {
                System.out.println("Update stock error");
                System.out.println(e.getMessage());
            }

        }
    }

    void changePosition(LibItem libItem) {
        System.out.println("We are changing the position");
    }

    void deleteItem(LibItem libItem) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete the " + libItem.itemType + " " + libItem.itemName);
        // alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //make sure that the book is not lended to anybody
            try {
                sql = "select * from itemstostudent where idLibItem = " + libItem.libItemId;
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                if (!rs.next()) {

                    //delete the book from borrow
                    sql = "delete from libraryitem where idLibItem = ?";
                    try {
                        PreparedStatement preparedStmt = con.prepareStatement(sql);
                        preparedStmt.setInt(1, libItem.libItemId);
                        preparedStmt.execute();

                        //compute the total items again...
                        computeTotalItemsPerCategories();
                        if (currentItemType.equals("Book")) {
                            totalItems.setText(totalBook + " Books");
                        } else if (currentItemType.equals("CD")) {
                            totalItems.setText(totalCD + " CDs");
                        } else {
                            totalItems.setText(totalRD + " RDs");
                        }

                        sql = "select  * from libraryitem where itemType = '" + currentItemType + "' LIMIT 24 offset 0";
                        generateItems(currentItemType, currentItemIcon, offsetQuery, sql);

                        a.setAlertType(AlertType.INFORMATION);
                        a.setContentText(libItem.itemType + " deleted !");
                        a.show();
                    } catch (SQLException e) {
                        System.out.println("Database error");
                    }
                } else {
                    a.setAlertType(AlertType.INFORMATION);
                    a.setContentText("Cannot delete this item, it is lended to a student!");
                    a.show();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            // ... user chose CANCEL or closed the dialog
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
        generateItems("Book", "images/4x/book_active.png", offsetQuery, sql);

        //init total items to books
        currentItemType = "Book";
        currentItemIcon = "images/4x/book_active.png";
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
        generateItems("Research document", "images/4x/docs_active.png", offsetQuery, sql);

        //init total items to books
        currentItemType = "Research document";
        currentItemIcon = "images/4x/docs_active.png";
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
        generateItems("CD", "images/4x/cd_active.png", offsetQuery, sql);

        //init total items to books
        currentItemType = "CD";
        currentItemIcon = "images/4x/cd_active.png";
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

        if (searchItemHome.getText().length() >= 1) {
            resetSearchIcon.setImage(new Image("images/4x/close_grey.png"));
            sql = "select  * from libraryitem where itemType = '" + currentItemType + "' and itemName like '%" + searchItemHome.getText() + "%' LIMIT 24 offset 0";
            generateItems(currentItemType, currentItemIcon, offsetQuery, sql);
        }

    }

    @FXML
    private void resetSearch(MouseEvent event) {
        searchItemHome.clear();
        resetSearchIcon.setImage(new Image("images/4x/search.png"));
        sql = "select  * from libraryitem where itemType = '" + currentItemType + "' LIMIT 24 offset 0";
        generateItems(currentItemType, currentItemIcon, offsetQuery, sql);
    }

    @FXML
    private void showStatsScreen(ActionEvent event) {
        try {
            App.setRoot("statistics");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

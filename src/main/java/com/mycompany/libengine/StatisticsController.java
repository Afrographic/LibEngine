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
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class StatisticsController implements Initializable {

    @FXML
    private ComboBox<String> months;
    @FXML
    private VBox chartParent;
    @FXML
    private Label totalLending;
    @FXML
    private Label totalReturns;
    @FXML
    private Label itemsOutside;

    public ArrayList<LendingPerDay> lendingsPerDay = new ArrayList<LendingPerDay>();

    DB db = new DB();
    Connection con;
    String sql;
    int currentYear;
    int currentMonth;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lendingsPerDay = initTable(lendingsPerDay);
        //loading the months
        months.getItems().addAll("January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

        //Getting the current date value
        LocalDate currentdate = LocalDate.now();
        //Getting the current month and year
        currentMonth = currentdate.getMonthValue();
        currentYear = currentdate.getYear();
        String month = currentdate.getMonth().toString();
        month = Utils.formatString(month);
        months.getSelectionModel().select(month);

        //render the chart of the current month
        System.out.println("Current mont id " + months.getSelectionModel().getSelectedIndex() + 1);

        System.out.println("Current month: " + currentMonth + " current year " + currentYear);

        //initializing the database connection
        con = db.getConnection();

        computeTotal(currentMonth, currentYear);

        //get total transactions
        renderEngineChart(currentMonth);

    }

    void computeTotal(int currentMonth, int currentYear) {
        getTotalLending(currentMonth, currentYear);
        getTotalReturn(currentMonth, currentYear);
        getTotalItemsOut(currentMonth, currentYear);
    }

    @FXML
    private void popScreen(MouseEvent event) throws IOException {
        App.setRoot("HomePage");
    }

    @FXML
    private void resetSearch(MouseEvent event) {
    }

    void getTotalLending(int currentMonth, int currentYear) {

        sql = "select COUNT(idHLending) as totalLending from historylending where dateBorrow like '%/" + (currentMonth < 10 ? "0" + currentMonth : currentMonth) + "/" + currentYear + "%'";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int totallending = rs.getInt("totalLending");
                totalLending.setText("" + totallending);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    void getTotalReturn(int currentMonth, int currentYear) {
        sql = "select COUNT(idReturn) as totalReturn from historyreturn where dateReturn like '%/" + (currentMonth < 10 ? "0" + currentMonth : currentMonth) + "/" + currentYear + "%'";;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int totalReturn = rs.getInt("totalReturn");
                totalReturns.setText("" + totalReturn);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void getTotalItemsOut(int currentMonth, int currentYear) {
        sql = "select COUNT(idBorrow) as itemsOut from itemstostudent ";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int itemsOut = rs.getInt("itemsOut");
                itemsOutside.setText("" + itemsOut);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void loadChart(ActionEvent event) {
        int idMonth = months.getSelectionModel().getSelectedIndex();
        renderEngineChart(idMonth + 1);
        computeTotal(idMonth + 1, currentYear);

    }

    void renderEngineChart(int idMonth) {
        ArrayList<LendingPerDay> lendings = sortingData(idMonth, "historylending");
        ArrayList<LendingPerDay> returns = sortingData(idMonth, "historyreturn");
        System.out.println("index of the items selected " + idMonth);
        renderChart(lendings, returns);
    }

    void renderChart(ArrayList<LendingPerDay> lendings, ArrayList<LendingPerDay> returns) {
        final NumberAxis xAxis = new NumberAxis(1, 31, 1);
        final NumberAxis yAxis = new NumberAxis(0, 20, 1);
        xAxis.setLabel("Days of the month");
        yAxis.setLabel("Number of items");

        LineChart lendingEvolution = new LineChart<>(xAxis, yAxis);
        lendingEvolution.setMinWidth(1200);
        lendingEvolution.setMinHeight(520);
        lendingEvolution.createSymbolsProperty();

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Evolution of lendings");
        for (int i = 1; i <= 31; i++) {
            series1.getData().add(new XYChart.Data<>(lendings.get(i - 1).idDay, lendings.get(i - 1).totalLending));
        }

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Evolution  of returns");
        for (int i = 1; i <= 31; i++) {
            series2.getData().add(new XYChart.Data<>(returns.get(i - 1).idDay, returns.get(i - 1).totalLending));
        }

        lendingEvolution.getData().addAll(series1, series2);
        chartParent.getChildren().clear();
        chartParent.getChildren().add(lendingEvolution);
    }

    ArrayList<LendingPerDay> sortingData(int monthId, String tableName) {
        ArrayList<LendingPerDay> itemsPerDay = new ArrayList<>();
        itemsPerDay = initTable(itemsPerDay);
        sql = "select * from " + tableName + " where " + (tableName.equals("historylending") ? "dateBorrow" : "dateReturn") + " like '%/" + (monthId < 10 ? "0" + monthId : monthId) + "/" + currentYear + "%'";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String lendDate = tableName.equals("historylending") ? rs.getString("dateBorrow") : rs.getString("dateReturn");

                lendDate = lendDate.split(" ")[0].split("/")[0];
                int idDay = Integer.parseInt(lendDate);

                System.out.println("id of the day " + idDay);

                for (int i = 1; i <= 31; i++) {
                    if (itemsPerDay.get(i - 1).idDay == idDay) {
                        itemsPerDay.get(i - 1).totalLending++;

                        System.out.println("total lending " + itemsPerDay.get(i - 1).totalLending);
                    }
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return itemsPerDay;
    }

    ArrayList<LendingPerDay> initTable(ArrayList<LendingPerDay> itemsPerDay) {

        for (int i = 1; i <= 31; i++) {
            itemsPerDay.add(new LendingPerDay(i, 0));
        }

        return itemsPerDay;
    }

}

class LendingPerDay {

    public int idDay;
    public int totalLending;

    public LendingPerDay(int idDay, int totalLending) {
        this.idDay = idDay;
        this.totalLending = totalLending;
    }
}

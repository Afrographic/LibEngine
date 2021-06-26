/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

import java.net.URL;
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

/**
 * FXML Controller class
 *
 * @author X M G
 */
public class StatisticsController implements Initializable {

    @FXML
    private ImageView closeSearch;

    @FXML
    private ComboBox months;
    @FXML
    private VBox chartParent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final NumberAxis xAxis = new NumberAxis(1, 31, 1);
        final NumberAxis yAxis = new NumberAxis(0, 20, 2);
        xAxis.setLabel("Days of the month");
        yAxis.setLabel("Number of items");

        LineChart lendingEvolution = new LineChart<>(xAxis, yAxis);
        lendingEvolution.setMinWidth(1200);
        lendingEvolution.setMinHeight(520);
        lendingEvolution.createSymbolsProperty();

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Evolution of lendings");
        // series1.getData().add(new XYChart.Data(1, 11));
        //  series1.getData().add(new XYChart.Data(2, 7));
        series1.getData().add(new XYChart.Data(3, 4));
        series1.getData().add(new XYChart.Data(4, 8));
        series1.getData().add(new XYChart.Data(5, 7));
        series1.getData().add(new XYChart.Data(6, 11));
        series1.getData().add(new XYChart.Data(7, 2));
        series1.getData().add(new XYChart.Data(8, 3));
        series1.getData().add(new XYChart.Data(9, 5));
        series1.getData().add(new XYChart.Data(10, 6));
        series1.getData().add(new XYChart.Data(11, 2));
        series1.getData().add(new XYChart.Data(12, 3));
        series1.getData().add(new XYChart.Data(13, 7));
        series1.getData().add(new XYChart.Data(14, 8));
        series1.getData().add(new XYChart.Data(15, 11));
        series1.getData().add(new XYChart.Data(16, 8));
        series1.getData().add(new XYChart.Data(17, 4));
        series1.getData().add(new XYChart.Data(18, 5));
        series1.getData().add(new XYChart.Data(19, 4));
        series1.getData().add(new XYChart.Data(20, 9));
        series1.getData().add(new XYChart.Data(21, 10));
        series1.getData().add(new XYChart.Data(22, 2));
        series1.getData().add(new XYChart.Data(27, 3));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Evolution of returns");
        //  series2.getData().add(new XYChart.Data(1, 10));
        //  series2.getData().add(new XYChart.Data(2, 5));
        series2.getData().add(new XYChart.Data(3, 3));
        series2.getData().add(new XYChart.Data(4, 1));
        series2.getData().add(new XYChart.Data(5, 0));
        series2.getData().add(new XYChart.Data(6, 7));
        series2.getData().add(new XYChart.Data(7, 1));
        series2.getData().add(new XYChart.Data(8, 2));
        series2.getData().add(new XYChart.Data(9, 2));
        series2.getData().add(new XYChart.Data(10, 5));
        series2.getData().add(new XYChart.Data(11, 4));
        series2.getData().add(new XYChart.Data(12, 1));
        series2.getData().add(new XYChart.Data(13, 4));
        series2.getData().add(new XYChart.Data(14, 1));
        series2.getData().add(new XYChart.Data(15, 2));
        series2.getData().add(new XYChart.Data(16, 1));
        series2.getData().add(new XYChart.Data(17, 7));
        series2.getData().add(new XYChart.Data(18, 6));
        series2.getData().add(new XYChart.Data(19, 3));
        series2.getData().add(new XYChart.Data(20, 1));
        series2.getData().add(new XYChart.Data(21, 7));
        series2.getData().add(new XYChart.Data(22, 8));
        series2.getData().add(new XYChart.Data(27, 9));

        lendingEvolution.getData().addAll(series1, series2);

        chartParent.getChildren().add(lendingEvolution);

        System.out.println(lendingEvolution);

    }

    @FXML
    private void popScreen(MouseEvent event) {
    }

    @FXML
    private void resetSearch(MouseEvent event) {
    }

}

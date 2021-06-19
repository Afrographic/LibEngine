package com.mycompany.libengine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("loading"), 612, 400);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
        stage.show();

        //Moving to the app
        switchToApp(stage);
    }

    public void switchToApp(Stage stage) {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1),
                ae -> {
                    try {
                        //  Moving to another screen
                        //  stage.setOnCloseRequest(e -> Platform.exit());
                        stage.close();
                        Stage stage2 = new Stage();
                        //scene = new Scene(loadFXML("nameLogoStartUp"), 1366, 768);
                        scene = new Scene(loadFXML("lending"), 1366, 768);
                        stage2.setScene(scene);
                        stage2.setTitle("Lib Engine");
                        stage2.setResizable(false);
                        stage2.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
                        // stage.initStyle(StageStyle.DECORATED);
                        stage2.show();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }));
        timeline.play();

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}

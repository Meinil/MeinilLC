package com.meinil.mc.serverlc;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Meinil
 */
public class MainApplication extends Application {
    private static HostServices services;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
        services = getHostServices();
        Scene scene = new Scene(parent);
        scene.setFill(Paint.valueOf("#FFF0"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    public static HostServices getHostService() {
        return services;
    }
}
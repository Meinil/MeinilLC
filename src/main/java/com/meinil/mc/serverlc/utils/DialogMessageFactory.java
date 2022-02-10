package com.meinil.mc.serverlc.utils;

import com.meinil.mc.serverlc.controller.DialogMessageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class DialogMessageFactory {
    private Parent parent;
    private DialogMessageController controller;
    private DialogMessageFactory() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/DialogMessage.fxml"));
        try {
             parent = loader.load();
             controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DialogMessageFactory build() {
        return new DialogMessageFactory();
    }

    public DialogMessageFactory confirm(Consumer<MouseEvent> confirm) {
        controller.setConfirm(confirm);
        return this;
    }
    public DialogMessageFactory cancel(Consumer<MouseEvent> cancel) {
        controller.setConfirm(cancel);
        return this;
    }
    public DialogMessageFactory message(String message) {
        controller.setMessage(message);
        return this;
    }
    public void show() {
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
    }
}

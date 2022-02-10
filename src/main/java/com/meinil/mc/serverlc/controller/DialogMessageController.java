package com.meinil.mc.serverlc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class DialogMessageController {
    @FXML
    private Button closeBtn;
    @FXML
    private Text messageText;

    /**
     * 确定的回调
     */
    private Consumer<MouseEvent> confirm;
    /**
     * 取消的回调
     */
    private Consumer<MouseEvent> cancel;

    @FXML
    public void onConfirmAction(MouseEvent event) {
        if(confirm != null) {
            confirm.accept(event);
        }
        closeWindow();
    }
    public void setConfirm(Consumer<MouseEvent> confirm) {
        this.confirm = confirm;
    }

    @FXML
    public void onCancelAction(MouseEvent event) {
        if (cancel != null) {
            cancel.accept(event);
        }
        closeWindow();
    }
    public void setCancel(Consumer<MouseEvent> cancel) {
        this.cancel = cancel;
    }

    public void setMessage(String message) {
        messageText.setText(message);
    }

    private void closeWindow() {
        Stage stage = (Stage)closeBtn.getScene().getWindow();
        stage.close();
    }
}

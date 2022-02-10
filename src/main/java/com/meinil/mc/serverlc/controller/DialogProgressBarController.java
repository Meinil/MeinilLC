package com.meinil.mc.serverlc.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class DialogProgressBarController {
    @FXML
    private Button closeBtn;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label percentageLabel;

    private Task<Void> task;

    @FXML
    public void initialize() {
    }

    @FXML
    public void onStageCloseAction(MouseEvent event) {
        task.cancel();
        Stage dialogStage = (Stage)closeBtn.getScene().getWindow();
        dialogStage.close();
    }

    public void setData(Task<Void> task) {
        this.task = task;
        progressBar.progressProperty().bind(task.progressProperty());
        percentageLabel.textProperty().bind(task.messageProperty());
    }
}

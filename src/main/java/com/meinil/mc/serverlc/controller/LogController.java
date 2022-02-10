package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.utils.DataBus;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class LogController {
    @FXML
    private TextArea logTextArea;

    @FXML
    public void initialize() {
        logTextArea.cancelEdit();
        DataBus.setData("logTextArea", logTextArea);
    }
}

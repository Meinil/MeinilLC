package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.MainApplication;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class AboutController {
    @FXML
    public void onOpenGithubAction(MouseEvent event) {
        HostServices services = MainApplication.getHostService();
        services.showDocument("https://github.com/Meinil/MeinilCL");
    }
}

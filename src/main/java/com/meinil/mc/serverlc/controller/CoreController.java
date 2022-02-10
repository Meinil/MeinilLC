package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.task.CoreInfoTask;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class CoreController {
    @FXML
    private ScrollPane coreInfoPane;
    @FXML
    private ToggleGroup coreGroup;
    @FXML
    private ToggleButton spigotRadio;
    private final Map<String, VBox> coreInfos = new HashMap<>();

    @FXML
    public void initialize() {
        initCoreGroup();
        new Thread(new CoreInfoTask(spigotRadio, coreInfos, coreInfoPane)).start();
    }

    private void initCoreGroup() {
        coreGroup.getToggles().forEach(item -> {
            ToggleButton btn = (ToggleButton)item;
            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (btn.isSelected()) {
                        new Thread(new CoreInfoTask(btn, coreInfos, coreInfoPane)).start();
                    }
                    btn.setSelected(true);
                }
            });
        });
    }
}

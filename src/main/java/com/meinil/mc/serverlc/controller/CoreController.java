package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.model.Core;
import com.meinil.mc.serverlc.task.CoreInfoTask;
import com.meinil.mc.serverlc.utils.Cache;
import com.meinil.mc.serverlc.utils.Shapes;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
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
    private HBox coreTypes;
    /**
     * 已缓存的页面
     */
    private final Map<String, VBox> coreInfos = new HashMap<>();

    @FXML
    public void initialize() {
        initCoreGroup();
    }

    private void initCoreGroup() {
        coreGroup = new ToggleGroup();
        Map<String, List<Core>> coreInfo = Cache.getCoreInfo();
        for (Map.Entry<String, List<Core>> core : coreInfo.entrySet()) {
            ToggleButton btn = new ToggleButton(core.getKey());
            Region region = new Region();
            btn.setGraphic(region);
            btn.setAccessibleText(core.getKey());
            btn.getStyleClass().addAll("icon-btn", "core-btn");
            btn.setToggleGroup(coreGroup);
            coreTypes.getChildren().add(btn);
        }
        coreGroup.selectToggle(coreGroup.getToggles().get(0));
        new Thread(new CoreInfoTask((ToggleButton) coreGroup.getToggles().get(0), coreInfos, coreInfoPane)).start();

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

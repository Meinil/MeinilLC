package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.model.Server;
import com.meinil.mc.serverlc.utils.Cache;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class MainController {
    @FXML
    private Button closeBtn;
    @FXML
    private ToggleGroup menuGroup;
    @FXML
    private Button minBtn;
    @FXML
    private AnchorPane mainContent;
    @FXML
    private ToggleButton logBtn;

    private Stage primaryStage;
    private double offsetX, offsetY;
    private final Map<String, Parent> parents = new HashMap<>();
    @FXML
    public void initialize() {
        initMenuGroup();
        switchContent(logBtn);
    }

    private void initMenuGroup() {
        menuGroup.getToggles().forEach(toggle -> {
            ToggleButton btn = (ToggleButton)toggle;
            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (btn.isSelected()) {
                        switchContent(btn);
                    }
                    btn.setSelected(true);
                }
            });
        });
    }

    /**
     * 关闭窗口
     * @param event 鼠标释放时触发
     */
    @FXML
    public void onCloseWindowAction(MouseEvent event) {
        Cache.syncFile();
        ObservableList<Server> servers = Cache.getServers();
        for (Server server : servers) {
            if (server.isStatus()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/DialogMessage.fxml"));
                    Parent parent = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.show();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Platform.exit();
    }

    /**
     * 最小化窗口
     * @param event 鼠标释放时触发
     */
    @FXML
    public void onMinWindowAction(MouseEvent event) {
        findStage();
        primaryStage.setIconified(true);
    }

    /**
     * 顶栏拖动
     * @param event 拖动事件
     */
    @FXML
    public void onSetOffsetAction(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    }
    @FXML
    public void onSetWindowsAction(MouseEvent event) {
        findStage();
        primaryStage.setX(event.getScreenX() - offsetX);
        primaryStage.setY(event.getScreenY() - offsetY);
    }

    private void switchContent(ToggleButton btn) {
        String text = btn.getAccessibleText();
        Parent parent = null;
        if (parents.containsKey(text)) {
            parent = parents.get(text);
        } else {
            try {
                parent = FXMLLoader.load(getClass().getResource("/fxml/menu/" + text + ".fxml"));
                parents.put(text, parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mainContent.getChildren().clear();
        mainContent.getChildren().add(parent);
    }

    /**
     * 获取主窗口
     */
    private void findStage() {
        if (primaryStage == null) {
            primaryStage = (Stage) minBtn.getScene().getWindow();
        }
    }
}

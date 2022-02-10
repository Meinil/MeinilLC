package com.meinil.mc.serverlc.cell;

import com.meinil.mc.serverlc.controller.ListInfoController;
import com.meinil.mc.serverlc.model.Server;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class LogListCell extends ListCell<Server> {
    private Parent parent;
    private ListInfoController controller;
    public LogListCell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/ListInfo.fxml"));
        try {
            parent = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void updateItem(Server item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            this.setGraphic(null);
        } else {
            controller.setNameLabel(item.getName());
            controller.setCoreLabel(item.getCore());
            controller.setServer(item);
            this.setGraphic(parent);
        }
    }
}

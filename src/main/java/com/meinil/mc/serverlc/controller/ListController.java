package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.cell.LogListCell;
import com.meinil.mc.serverlc.utils.Cache;
import com.meinil.mc.serverlc.model.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class ListController {
    @FXML
    private ListView<Server> contentListView;

    @FXML
    public void initialize() {
        contentListView.setPlaceholder(new Label("空空如也, 请前往《核心》页面开设服务器"));
        initListContent();
    }

    private void initListContent() {
        contentListView.setItems(Cache.getServers());
        contentListView.setCellFactory(new Callback<ListView<Server>, ListCell<Server>>() {
            @Override
            public ListCell<Server> call(ListView<Server> param) {
                return new LogListCell();
            }
        });
    }
}

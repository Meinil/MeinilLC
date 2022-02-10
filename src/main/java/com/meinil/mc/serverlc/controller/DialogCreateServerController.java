package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.utils.Cache;
import com.meinil.mc.serverlc.model.Server;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class DialogCreateServerController {
    @FXML
    private Button cancelBtn;
    @FXML
    private Text tipsText;
    @FXML
    private Button startBtn;
    @FXML
    private TextField serverName;

    private String coreName;

    /**
     * 已开设的服务器
     */
    private ObservableList<Server> servers;

    @FXML
    public void initialize() {
        servers = Cache.getServers();
        initServerName();
    }

    private void initServerName() {
        serverName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    String name = serverName.getText().trim();
                    if (name.length() == 0) {
                        startBtn.setDisable(true);
                        return;
                    }
                    for (Server server : servers) {
                        if (server.getName().equals(name)) {
                            tipsText.setText("不能使用此名称,此名称已被占用");
                            startBtn.setDisable(true);
                            return;
                        } else {
                            tipsText.setText("");
                        }
                    }
                    startBtn.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void onStartAction(MouseEvent event) {
        Server server = new Server();
        server.setCore(coreName);
        String name = serverName.getText().trim();
        File file = new File("cache/servers/" + name);
        file.mkdir();
        server.setName(name);
        server.startServer();
        servers.add(server);
        closeDialog();
    }

    public void setData(String coreName) {
        this.coreName = coreName;
    }

    /**
     * 关闭对话框
     * @param event 鼠标释放时触发
     */
    @FXML
    public void oncCancelAction(MouseEvent event) {
        closeDialog();
    }
    private void closeDialog() {
        Stage dialog = (Stage)cancelBtn.getScene().getWindow();
        dialog.close();
    }

    /**
     * 设置焦点
     */
    public void setFocus() {
        cancelBtn.requestFocus();
    }
}

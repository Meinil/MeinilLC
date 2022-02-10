package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.model.Server;
import com.meinil.mc.serverlc.utils.Cache;
import com.meinil.mc.serverlc.utils.Shapes;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class ListInfoController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coreLabel;
    @FXML
    private Label settingLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label removeLabel;

    private Server server;
    /**
     * 是否正在停止
     */
    private SimpleBooleanProperty isStopping;
    @FXML
    public void initialize() {
        initIcon();
    }

    public void setServer(Server server) {
        this.server = server;
        server.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    if (newValue && !server.stopStatus()) {
                        setIcon(statusLabel, Shapes.STATUS_RUNNING);
                        statusLabel.setText(" 停止");
                    }  else{
                        setIcon(statusLabel, Shapes.STATUS_STOP);
                        statusLabel.setText(" 运行");
                    }
                });
            }
        });
        statusLabel.disableProperty().bind(Bindings.and(server.stopProperty(), server.statusProperty()));
        removeLabel.disableProperty().bind(server.statusProperty());
        settingLabel.disableProperty().bind(server.statusProperty());
    }

    /**
     * 打开服务器设置
     * @param event 鼠标释放时触发
     */
    @FXML
    public void onOpenSettingAction(MouseEvent event) {

    }

    @FXML
    public void onRemoveAction(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/DialogMessage.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));

            DialogMessageController controller = loader.getController();
            controller.setMessage("你确定要移除此服务器吗?");
            controller.setConfirm(confirm -> {
                File file = new File("cache/servers/" + server.getName());
                boolean delete = rm(file);
                if (delete) {
                    Cache.getServers().remove(server);          // 移除服务器列表
                }
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除文件夹
     * @param file 要删除的文件夹
     * @return 是否删除成功
     */
    public boolean rm(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File file2 : files) {
                rm(file2);
            }
        }
        return file.delete();
    }

    /**
     * 启动服务器按钮
     * @param event 鼠标释放时触发
     */
    @FXML
    public void onSetStatusAction(MouseEvent event) {
        if (server.statusProperty().get()) {
            server.shutDown();
        } else {
            server.startServer();
        }
    }

    private void initIcon() {
        setIcon(nameLabel, Shapes.NAME);
        setIcon(coreLabel, Shapes.CORE);
        setIcon(settingLabel, Shapes.SETTING);
        setIcon(statusLabel, Shapes.STATUS_STOP);
        setIcon(removeLabel, Shapes.REMOVE);
    }

    private void setIcon(Label label, SVGPath svg) {
        Region graphic = (Region)label.getGraphic();
        graphic.setShape(svg);
    }

    public void setNameLabel(String name) {
        this.nameLabel.setText(name);
    }

    public void setCoreLabel(String core) {
        this.coreLabel.setText(core);
    }
}

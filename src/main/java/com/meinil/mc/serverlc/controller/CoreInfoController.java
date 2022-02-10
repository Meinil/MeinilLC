package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.model.Server;
import com.meinil.mc.serverlc.utils.Cache;
import com.meinil.mc.serverlc.model.Core;
import com.meinil.mc.serverlc.task.DownloadCoreTask;
import com.meinil.mc.serverlc.utils.DialogMessageFactory;
import com.meinil.mc.serverlc.utils.Shapes;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class CoreInfoController {
    private final static Image IMAGE_SPIGOT = new Image(CoreInfoController.class.getResource("/img/spigot.png").toExternalForm(), 50, 50, true, true, false);
    private final static Image IMAGE_BUKKIT = new Image(CoreInfoController.class.getResource("/img/bukkit.png").toExternalForm(), 0, 30, true, true, false);
    private final static Image IMAGE_VANILLA = new Image(CoreInfoController.class.getResource("/img/vanilla.png").toExternalForm(), 50, 50, true, true, false);
    private final static Image IMAGE_PURPUR = new Image(CoreInfoController.class.getResource("/img/purpur.png").toExternalForm(), 50, 50, true, true, false);

    @FXML
    private Button downloadBtn;
    @FXML
    private ImageView iconImageView;
    @FXML
    private Button removeBtn;
    @FXML
    private Button startBtn;
    @FXML
    private Label sizeText;
    @FXML
    private Label versionText;
    private String downloadUrl;
    private String coreName;
    private ObservableList<String> cores;

    @FXML
    public void initialize() {
        cores = Cache.getCores();

        Region remove = (Region)removeBtn.getGraphic();
        remove.setShape(Shapes.REMOVE);

        Region start = (Region)startBtn.getGraphic();
        start.setShape(Shapes.STATUS_STOP);

        removeBtn.disableProperty().bind(downloadBtn.disableProperty().not());
        startBtn.disableProperty().bind(downloadBtn.disableProperty().not());
    }


    @FXML
    public void onStartAction(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/DialogCreateServer.fxml"));
            Parent parent = loader.load();
            DialogCreateServerController controller = loader.getController();
            controller.setData(coreName);
            Stage stage = new Stage();
            stage.setOnShown(e -> controller.setFocus());
            stage.setTitle(coreName);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除核心按钮
     * @param event 鼠标释放时触发
     */
    @FXML
    public void onRemoveCoreAction(MouseEvent event) {
        ObservableList<Server> servers = Cache.getServers();
        for (Server server : servers) {
            if (server.getCore().equals(coreName)) {
                // 弹出窗口 禁止删除
                DialogMessageFactory.build().message("此核心仍有服务器依赖,请先前往《列表》移除服务器").show();
                return;
            }
        }
        cores.remove(coreName);
        File file = new File("cache/cores/" + coreName);
        if (file.delete()) {
            downloadBtn.setDisable(false);
            Region graphic = (Region)downloadBtn.getGraphic();
            graphic.setShape(Shapes.DOWNLOAD);
        }
    }

    /**
     * 下载核心按钮
     * @param event 鼠标释放时触发
     */
    @FXML
    private void onDownloadAction(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/DialogProgressBar.fxml"));
            Parent parent = loader.load();
            DialogProgressBarController controller = loader.getController();

            Stage downloadStage = new Stage();
            downloadStage.setScene(new Scene(parent));

            DownloadCoreTask task = new DownloadCoreTask(
                    downloadUrl,
                    coreName
            );
            task.setOnSucceeded(e -> {
                downloadStage.close();
                initDownloadBtn();
            });
            controller.setData(task);

            downloadStage.show();
            new Thread(task).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 注入数据
     * @param core 核心信息
     */
    public void setData(Core core) {
        sizeText.setText(core.getSize());
        versionText.setText(core.getVersion());
        downloadUrl = core.getUrl();
    }

    /**
     * 设置核心的名称以及图标
     * @param coreName 核心名称(不包括版本号)
     */
    public void setCoreName(String coreName) {
        this.coreName = coreName + "-" + versionText.getText();
        switch (coreName) {
            case "spigot" -> {
                iconImageView.setImage(IMAGE_SPIGOT);
            }
            case "craftbukkit" -> {
                iconImageView.setImage(IMAGE_BUKKIT);
            }
            case "purpur" -> {
                iconImageView.setImage(IMAGE_PURPUR);
            }
            default -> {
                iconImageView.setImage(IMAGE_VANILLA);
            }
        }
        initDownloadBtn();
    }

    private void initDownloadBtn() {
        Region graphic = (Region)downloadBtn.getGraphic();
        if (cores.contains(this.coreName)) {
            graphic.setShape(Shapes.RIGHT);
            downloadBtn.setText("拥有");
            downloadBtn.setDisable(true);
        } else {
            graphic.setShape(Shapes.DOWNLOAD);
        }
    }
}

package com.meinil.mc.serverlc.controller;

import com.meinil.mc.serverlc.utils.Cache;
import com.sun.management.OperatingSystemMXBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class SettingController {
    @FXML
    private ToggleButton autoSelect;
    @FXML
    private TextField javaHome;
    @FXML
    private ToggleButton manualSelect;
    @FXML
    private ToggleGroup memoryGroup;
    @FXML
    private Slider memorySlider;
    @FXML
    private Label totalMemory;
    @FXML
    private Label useMemory;
    @FXML
    private TextField vmOptions;

    @FXML
    public void initialize() {
        initJavaHome();
        initVmOptions();
        initMemory();
        initGroup();
    }

    /**
     * 初始化java
     */
    public void initJavaHome() {
        if(Cache.getJavaHome().length() != 0) {
            javaHome.setText(Cache.getJavaHome());
        } else {
            javaHome.setText(System.getProperty("java.home"));
        }
        Cache.javaHomeProperty().bind(javaHome.textProperty());
    }

    /**
     * 初始化虚拟机参数
     */
    public void initVmOptions() {
        if(Cache.getVmOptions().length() != 0) {
            vmOptions.setText(Cache.getVmOptions());
        }
        Cache.vmOptionsProperty().bind(vmOptions.textProperty());
    }

    /**
     * 初始化内存
     */
    private void initMemory() {
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double memory = osmb.getTotalMemorySize() / 1024.0 / 1024 / 1024;
        totalMemory.setText(String.format("%.2fG", memory));

        // 拖动条内存
        memorySlider.setMax(memory);
        memorySlider.disableProperty().bind(autoSelect.selectedProperty());
        if (Cache.getMemory().length() != 0) {
            String substring = Cache.getMemory().substring(0, Cache.getMemory().length() - 1);
            memorySlider.setValue(Double.valueOf(substring));
        } else {
            memorySlider.setValue(memory / 4);
        }
        useMemory.textProperty().bind(memorySlider.valueProperty().asString("%.2fG"));
        Cache.memoryProperty().bind(memorySlider.valueProperty().asString("%.0fG"));
    }

    /**
     * 初始化单选调整
     */
    private void initGroup() {
        memoryGroup.getToggles().forEach(item -> {
            ToggleButton btn = (ToggleButton)item;
            btn.setOnMouseClicked(event -> {
                btn.setSelected(true);
            });
        });
    }

    /**
     * 选择javaHome
     * @param event 鼠标释放时触发
     */
    @FXML
    public void onOpenJavaHomeAction(MouseEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(Cache.getJavaHome()));
        File file = chooser.showDialog((Stage) javaHome.getScene().getWindow());
        if (file != null) {
            File son = new File(file, "bin/java.exe");
            if (!son.exists()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/DialogMessage.fxml"));
                    Parent parent = loader.load();
                    DialogMessageController controller = loader.getController();
                    controller.setMessage("这不是一个正确的JavaHome,它子目录下应该有/bin/java.exe");
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                javaHome.setText(file.getAbsolutePath());
            }
        }
    }
}

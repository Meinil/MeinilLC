package com.meinil.mc.serverlc.task;

import com.meinil.mc.serverlc.MainApplication;
import com.meinil.mc.serverlc.controller.DialogMessageController;
import com.meinil.mc.serverlc.utils.DataBus;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class CreateServerService extends Service<Integer> {
    /**
     * 启动服务器命令
     */
    private String cmd;
    /**
     * 服务器名称
     */
    private final String name;
    private final SimpleBooleanProperty stop;
    private final SimpleBooleanProperty status;
    private Process process;
    private final TextArea logTextArea;
    public CreateServerService(String name) {
        logTextArea = (TextArea) DataBus.getData("logTextArea");
        this.name = name;
        this.stop = new SimpleBooleanProperty(false);
        this.status = new SimpleBooleanProperty(false);
    }

    /**
     * 是否按下了停止服务器按钮
     * false 未按下
     * true 已按下
     */
    public void stop() {
        stop.set(true);
    }
    public boolean isStop() {
        return stop.get();
    }
    public SimpleBooleanProperty stopProperty() {
        return stop;
    }

    /**
     * 服务器状态
     * false 停止
     * true 开启
     */
    public boolean isStatus() {
        return status.get();
    }
    public SimpleBooleanProperty statusProperty() {
        return status;
    }


    public void restart(String cmd) {
        this.cmd = cmd;
        restart();
    }

    /**
     * 0 服务器正常退出
     * 1 服务器未同意eula
     */
    @Override
    protected Task<Integer> createTask() {
        return new Task<>() {
            private boolean flag = false;
            @Override
            protected Integer call() throws Exception {
                BufferedReader reader = null;
                BufferedWriter writer = null;
                stop.set(false);
                Platform.runLater(() -> {
                    status.set(true);
                });
                try {
                    process = Runtime.getRuntime().exec(cmd);
                    reader = process.inputReader();
                    writer = process.outputWriter();
                    String line = null;
                    int sum = 0;
                    while (true) {
                        if (sum >= 500) {
                            Thread.sleep(1000);
                        }
                        if (reader.ready()) {
                            sum++;
                            line = reader.readLine();
                            if (line.contains("You need to agree to the EULA in order to run the server")) {
                                return 1;
                            }
                            String finalLine = line;
                            Platform.runLater(() -> {
                                logTextArea.appendText(finalLine + "\n");
                                if (finalLine.contains("For help, type \"help\"")) {
                                    flag = true;
                                }
                            });
                        } else if (stop.get() && flag) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                    if (writer != null) {
                        writer.write("stop");
                        writer.close();
                    }
                    if (process != null && process.isAlive()) {
                        process.destroy();
                    }
                    status.set(false);
                }
                return 0;
            }

            @Override
            protected void updateValue(Integer value) {
                super.updateValue(value);
                if (value == 0) {
                    logTextArea.appendText("服务器已关闭\n");
                } else if(value == 1) {
                    logTextArea.appendText("请先同意eula\n");
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/DialogMessage.fxml"));
                        Parent parent = loader.load();
                        DialogMessageController controller = loader.getController();
                        controller.setMessage("未同意eula(开服协议),是否前往阅读开服协议并《同意其条款》?");
                        controller.setConfirm(confirm -> {
                            MainApplication.getHostService().showDocument("https://account.mojang.com/documents/minecraft_eula");
                            try(FileOutputStream file = new FileOutputStream("cache/servers/" + name + "/eula.txt")) {
                                file.write("eula=true".getBytes(StandardCharsets.UTF_8));
                                logTextArea.appendText("已同意eula,请前往《列表》再次开启服务器\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void writer(String str) {
        try {
            BufferedWriter bufferedWriter = process.outputWriter();
            bufferedWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

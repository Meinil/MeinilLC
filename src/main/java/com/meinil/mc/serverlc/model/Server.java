package com.meinil.mc.serverlc.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.meinil.mc.serverlc.task.CreateServerService;
import com.meinil.mc.serverlc.utils.Cache;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.util.Properties;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class Server {
    /**
     * 服务器名称
     */
    private String name;
    /**
     * 服务器核心
     */
    private String core;
    /**
     * 服务器配置文件信息
     */
    @JSONField(serialize = false)
    private Properties serverProperties;
    /**
     * 状态
     * false 未开启
     * true 开启
     */
    @JSONField(serialize = false)
    private final SimpleBooleanProperty status;
    @JSONField(serialize = false)
    private final SimpleBooleanProperty stop;
    /**
     * 开启服务
     */
    @JSONField(serialize = false)
    private CreateServerService service;

    public Server() {
        status = new SimpleBooleanProperty(false);
        stop = new SimpleBooleanProperty(false);
    }

    public Server(String name, String core) {
        this();
        this.name = name;
        this.core = core;
    }

    public Properties getServerProperties() {
        if (serverProperties != null) {
            return serverProperties;
        }
        serverProperties = new Properties();
        try(FileInputStream in = new FileInputStream("cache/servers/" + name + "/server.properties")) {
            serverProperties.load(in);
            return serverProperties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SimpleBooleanProperty statusProperty() {
        return status;
    }
    public SimpleBooleanProperty stopProperty() {
        return stop;
    }

    public boolean stopStatus() {
        return service.isStop();
    }


    public boolean isStatus() {
        return status.get();
    }

    public String getName() {
        return name;
    }

    public String getCore() {
        return core;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCore(String core) {
        this.core = core;
    }

    /**
     * 关闭服务
     */
    public void shutDown() {
        if (status.get()) {
            service.stop();
        }
    }

    /**
     * 启动服务
     */
    public void startServer() {
        if (status.get()) {
            return;
        }
        if (service == null) {
            service = new CreateServerService(name);
            status.bind(service.statusProperty());
            stop.bind(service.stopProperty());
        }
        service.restart(getCmd());
    }

    @JSONField(serialize = false)
    public String getCmd() {
        return String.format("cmd /c cd %s && %s/bin/java.exe -Xms%s -Xmx%s %s -jar %s nogui",
                new File("cache/servers/" + name).getAbsolutePath(),
                Cache.getJavaHome(),
                Cache.getMemory(),
                Cache.getMemory(),
                Cache.getVmOptions(),
                new File("cache/cores/" + core).getAbsolutePath());
    }
}

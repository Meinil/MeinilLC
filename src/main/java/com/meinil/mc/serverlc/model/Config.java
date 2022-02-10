package com.meinil.mc.serverlc.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class Config {
    /**
     * java路径
     */
    private SimpleStringProperty javaHome = new SimpleStringProperty();
    /**
     * 虚拟机参数
     */
    private SimpleStringProperty vmOptions = new SimpleStringProperty();
    /**
     * 服务器内存数
     */
    private SimpleStringProperty defaultMemory = new SimpleStringProperty();

    public String getJavaHome() {
        return javaHome.get();
    }

    public SimpleStringProperty javaHomeProperty() {
        return javaHome;
    }

    public String getVmOptions() {
        return vmOptions.get();
    }

    public SimpleStringProperty vmOptionsProperty() {
        return vmOptions;
    }

    public String getDefaultMemory() {
        return defaultMemory.get();
    }

    public SimpleStringProperty defaultMemoryProperty() {
        return defaultMemory;
    }

    public void setJavaHome(String javaHome) {
        if (javaHome == null || javaHome.length() == 0) {
            javaHome = System.getProperty("java.home");
        }
        this.javaHome.set(javaHome);
    }

    public void setVmOptions(String vmOptions) {
        this.vmOptions.set(vmOptions);
    }

    public void setDefaultMemory(String defaultMemory) {
        this.defaultMemory.set(defaultMemory);
    }
}

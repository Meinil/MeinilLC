package com.meinil.mc.serverlc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.meinil.mc.serverlc.model.Config;
import com.meinil.mc.serverlc.model.Core;
import com.meinil.mc.serverlc.model.Server;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class Cache {
    /**
     * Meinil配置参数
     */
    private static Config CONFIG;
    /**
     * 核心的信息
     */
    private static Map<String, List<Core>> CORES_INFO;
    /**
     * 已下载的核心
     */
    private final static ObservableList<String> CORES = FXCollections.observableArrayList();
    /**
     * 服务器列表
     */
    private final static ObservableList<Server> SERVERS = FXCollections.observableArrayList();

    private final static String[] PATHS = new String[] {
            "cache",
            "cache/config.json",
            "cache/cores",
            "cache/cores/cores.json",
            "cache/servers",
            "cache/servers/servers.json"
    };

    static {
        createFile();
        try(FileInputStream in1 = new FileInputStream(PATHS[1]);
            FileInputStream in2 = new FileInputStream(PATHS[3]);
            FileInputStream in3 = new FileInputStream(PATHS[5])) {
            // 初始化配置文件信息
            CONFIG = JSON.parseObject(in1, Config.class);
            if (CONFIG == null) {
                CONFIG = new Config();
            }

            // 初始化 CORES_INFO
            Map<String, JSONArray> map = JSON.parseObject(in2, Map.class);
            if (map != null) {
                CORES_INFO = convert(map);
            }
            if (CORES_INFO == null) {
                String text = Request.get("https://gitee.com/dingwanli/mine-craft-server-core-info/raw/master/cache/cores.json");
                try (OutputStream out = new FileOutputStream(PATHS[3])){
                    out.write(text.getBytes(StandardCharsets.UTF_8));
                    CORES_INFO = convert(JSON.parseObject(text, Map.class));
                }
            }

            // 初始化 CORES
            File file1 = new File(PATHS[2]);
            CORES.addAll(file1.list());

            // 初始化服务器列表
            String serversJson = new String(in3.readAllBytes());
            if (!"".equals(serversJson)) {
                SERVERS.addAll(JSON.parseArray(serversJson, Server.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * jsonArray转换
     * @param map 待转换的map
     * @return 转换后的map
     */
    private static Map<String, List<Core>> convert(Map<String, JSONArray> map) {
        Map<String, List<Core>> listMap = new HashMap<>();
        map.forEach(new BiConsumer<String, JSONArray>() {
            @Override
            public void accept(String key, JSONArray objects) {
                List<Core> cores = objects.toJavaList(Core.class);
                listMap.put(key, cores);
            }
        });
        return listMap;
    }

    public static Map<String, List<Core>> getCoreInfo() {
        return CORES_INFO;
    }

    public static ObservableList<String> getCores() {
        return CORES;
    }

    public static ObservableList<Server> getServers() {
        return SERVERS;
    }



    /**
     * 获取内存
     */
    public static SimpleStringProperty memoryProperty() {
        return CONFIG.defaultMemoryProperty();
    }
    public static String getMemory() {
        return memoryProperty().get();
    }

    /**
     * 获取虚拟机参数
     */
    public static SimpleStringProperty vmOptionsProperty() {
        return CONFIG.vmOptionsProperty();
    }
    public static String getVmOptions() {
        return vmOptionsProperty().get();
    }

    /**
     * 获取java路径
     */
    public static SimpleStringProperty javaHomeProperty() {
        return CONFIG.javaHomeProperty();
    }
    public static String getJavaHome() {
        return javaHomeProperty().get();
    }

    /**
     * 同步到文件
     */
    public static void syncFile() {
        try (FileOutputStream out1 = new FileOutputStream(PATHS[1]);
             FileOutputStream out2 = new FileOutputStream(PATHS[5])){
            out1.write(JSON.toJSONString(CONFIG).getBytes(StandardCharsets.UTF_8));
            out2.write(JSON.toJSONString(SERVERS).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建必要文件
     */
    private static void createFile() {
        for (String path : PATHS) {
            File file = new File(path);
            if (!file.exists()) {
                String name = file.getName();
                if (name.contains(".")) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    file.mkdirs();
                }
            }
        }
    }
}

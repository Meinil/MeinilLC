package com.meinil.mc.serverlc.model;

import java.util.List;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class Core {
    private List<CoreInfo> spigot;

    public List<CoreInfo> getSpigot() {
        return spigot;
    }

    public void setSpigot(List<CoreInfo> spigot) {
        this.spigot = spigot;
    }

    public static class CoreInfo {
        private String version;
        private String url;
        private String size;

        public String getVersion() {
            return version;
        }

        public String getUrl() {
            return url;
        }

        public String getSize() {
            return size;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}

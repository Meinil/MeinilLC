package com.meinil.mc.serverlc;

import com.alibaba.fastjson.JSON;
import com.meinil.mc.serverlc.model.Core;
import com.meinil.mc.serverlc.utils.Request;
import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class MainTest {
    public static void main(String[] args) throws IOException {
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println(osmb.getTotalMemorySize());
    }

    public static void test() {
        String text = Request.get("https://gitee.com/dingwanli/minecraft-server-core-info/raw/master/cache/cores.json");
        Core core2 = JSON.parseObject(text, Core.class);
        System.out.println(text);
    }
}

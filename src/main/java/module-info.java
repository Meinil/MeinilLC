module com.meinil.lc {
    requires javafx.controls;
    requires javafx.fxml;
    requires fastjson;
    requires java.net.http;
    requires java.management;
    requires jdk.management;

    opens com.meinil.mc.serverlc to javafx.fxml;
    opens com.meinil.mc.serverlc.controller to javafx.fxml;
    opens com.meinil.mc.serverlc.model to fastjson;

    exports com.meinil.mc.serverlc;
    exports com.meinil.mc.serverlc.model;
    exports com.meinil.mc.serverlc.controller;
    exports com.meinil.mc.serverlc.task;
    opens com.meinil.mc.serverlc.task to javafx.fxml;
}
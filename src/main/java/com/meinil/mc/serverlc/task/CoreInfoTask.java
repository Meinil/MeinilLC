package com.meinil.mc.serverlc.task;

import com.meinil.mc.serverlc.utils.Cache;
import com.meinil.mc.serverlc.controller.CoreInfoController;
import com.meinil.mc.serverlc.model.Core;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class CoreInfoTask extends Task<VBox> {
    private ToggleButton btn;
    private ScrollPane coreInfoPane;
    private Map<String, VBox> coreInfos;
    public CoreInfoTask(ToggleButton btn,Map<String, VBox> coreInfos, ScrollPane coreInfoPane) {
        this.btn = btn;
        this.coreInfoPane = coreInfoPane;
        this.coreInfos = coreInfos;
    }

    @Override
    protected VBox call() throws Exception {
        String text = btn.getAccessibleText();
        VBox parent = null;
        if (coreInfos.containsKey(text)) {
            parent = coreInfos.get(text);
        } else {
            List<Core> cores =  Cache.getCoreInfo().get(text);
            parent = generateParent(cores);
            coreInfos.put(text, parent);
        }

        return parent;
    }

    @Override
    protected void updateValue(VBox value) {
        super.updateValue(value);
        coreInfoPane.setContent(value);
    }

    private VBox generateParent(List<Core> cores) {
        VBox parent = new VBox(10);
        cores.forEach(core -> {
            try {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/fxml/component/CoreInfo.fxml"));
                parent.getChildren().add(load.load());
                CoreInfoController controller = load.getController();
                controller.setData(core);
                controller.setCoreName(btn.getAccessibleText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return parent;
    }
}

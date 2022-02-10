package com.meinil.mc.serverlc.task;

import com.meinil.mc.serverlc.utils.Cache;
import com.meinil.mc.serverlc.utils.Request;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class DownloadCoreTask extends Task<Void> {

    private String url;
    private String name;
    public DownloadCoreTask(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    protected Void call() throws Exception {
        HttpResponse<InputStream> response = Request.getResponse(url);
        Optional<String> length = response.headers().firstValue("content-length");
        double max = Double.valueOf(length.get());  // 总长度
        double sum = 0.0;                          // 已读长度
        try(InputStream body = response.body();
            FileOutputStream file = new FileOutputStream("cache/cores/" + name)) {
            byte[] bytes = new byte[1024 * 4];
            int size = 0;
            this.updateMessage("00.00%");
            while ((size = body.read(bytes)) != -1) {
                if (this.isCancelled()) {
                    file.close();
                    File f = new File("cache/cores/" + name);
                    f.delete();
                    return null;
                }
                file.write(bytes, 0, size);
                sum += size;
                this.updateProgress(sum, max);
                this.updateMessage(String.format("%.2f%%", (sum / max) * 100));
            }
        }
        Cache.getCores().add(name);
        return null;
    }
}

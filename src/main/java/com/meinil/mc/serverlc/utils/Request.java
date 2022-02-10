package com.meinil.mc.serverlc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class Request {
    private final static HttpClient CLIENT = HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static String get(String path) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(path))
                    .GET()
                    .build();
            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResponse<InputStream> getResponse(String path) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(path))
                    .GET()
                    .build();
            return CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

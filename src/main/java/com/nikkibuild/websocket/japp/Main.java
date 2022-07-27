package com.nikkibuild.websocket.japp;

import com.google.gson.Gson;
import com.nikkibuild.websocket.japp.util.Anything;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        var app = new App();
        app.startApp();
    }

    @SneakyThrows
    public Anything readJson(String filePath) {
        var input = new InputStreamReader(new FileInputStream(filePath));
        return new Gson().fromJson(input, Anything.class);
    }
}

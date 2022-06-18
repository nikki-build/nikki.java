package com.nikkibuild.websocket.japp.util;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Message {
    @SerializedName("srvID")
    private final String serviceId;
    @SerializedName("sessionId")
    private final String sessionId;
    @SerializedName("instID")
    private final String instance;
    private final String name;
    private final String msg;
    private final Data   data;
    private final String status;
    private final String desc;

    @AllArgsConstructor
    @Getter
    public static class Data {
        private final String name;
        @SerializedName("desc")
        private final String description;
        private final Object data;
    }
}



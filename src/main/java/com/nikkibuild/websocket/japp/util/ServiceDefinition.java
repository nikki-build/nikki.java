package com.nikkibuild.websocket.japp.util;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ServiceDefinition {
    @SerializedName("srvID")
    private String       serviceId;
    @SerializedName("instID")
    private String       instanceId;
    @SerializedName("proglang")
    private String       lang;
    private Df           iDf;
    private Df           oDf;
    private String       name;
    @SerializedName("dispName")
    private String       displayName;
    @SerializedName("desc")
    private String       description;
    private List<String> tags;

    @AllArgsConstructor
    @Getter
    public static class Df {
        private String name;
        @SerializedName("desc")
        private String description;
        private Object data;
    }
}


package com.nikkibuild.websocket.japp.util;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceJoinInfo {
    @SerializedName("sessionID")
    private String            session;
    @SerializedName("userID")
    private String            user;
    @SerializedName("srv")
    private ServiceDefinition source;
    @SerializedName("wsAddr")
    private String            wsAddress;
    private String            type;
}
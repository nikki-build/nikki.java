package com.nikkibuild.websocket.japp.util;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceToken {
    @SerializedName("sessionID")
    private String sessionId;
    @SerializedName("wsAddr")
    private String wsAddress;
    @SerializedName("userID")
    private String userId;
    @SerializedName("restAddr")
    private String restAddress;
}

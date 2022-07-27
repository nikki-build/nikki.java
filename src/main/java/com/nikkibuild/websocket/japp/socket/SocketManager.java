package com.nikkibuild.websocket.japp.socket;

import com.google.gson.Gson;
import com.nikkibuild.websocket.japp.config.ServiceConfig;
import com.nikkibuild.websocket.japp.util.*;
import io.reactivex.rxjava3.core.Completable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class SocketManager {
    private final Gson                transformer = new Gson();
    private final SocketEventListener eventListener;
    private final OkHttpClient        okHttp;
    private final ServiceDefinition   properties;
    private final ServiceToken        token;
    private final ThrottleManager     throttleManager;
    private       WebSocket           socket;

    public SocketManager(String definitionPath, String tokenPath, SocketDelegate eventListener, ThrottleManager throttleManager) {
        this.eventListener = new SocketEventListener();
        this.eventListener.setDelegate(eventListener);
        var serviceConfig = new ServiceConfig(definitionPath, tokenPath);
        this.okHttp          = ServiceConfig.okHttp();
        this.properties      = serviceConfig.serviceDefinition();
        this.token           = serviceConfig.serviceToken();
        this.throttleManager = throttleManager;
    }

    public Completable start() {
        return Completable.create(e -> {
            try {
                doClose();
                var request = new Request.Builder()
                        .url(token.getWsAddress() + "?wsKey=" + makeInfoParam())
                        .build();
                socket = okHttp.newWebSocket(request, eventListener);
                if (!e.isDisposed()) {
                    e.onComplete();
                }
            } catch (Exception exception) {
                e.tryOnError(exception);
            }
        });
    }

    private String makeInfoParam() {
        var info = new ServiceJoinInfo(token.getSessionId(), token.getUserId(), properties, token.getWsAddress(), "service");
        var json = transformer.toJson(info);
        return Base64.encodeBase64URLSafeString(json.getBytes(StandardCharsets.UTF_8));
    }

    public Completable stop() {
        return Completable.create(e -> {
            try {
                doClose();
                if (!e.isDisposed()) {
                    e.onComplete();
                }
            } catch (Exception ex) {
                e.tryOnError(ex);
            }
        });
    }

    private void doClose() {
        if (socket != null) {
            socket.close(1000, "Closing on user request.");
        }
        socket = null;
    }


    public Completable sendData(Object data) {
        return Completable.create(e -> {
            try {
                if (!throttleManager.canSend()) {
                    throw new IllegalArgumentException("Bandwidth limit reached. Limit [5] messages in a minute");
                }
                var d = new Message.Data("n", "d", data);
                var message = new Message(
                        properties.getServiceId(), token.getSessionId(), properties.getInstanceId(),
                        "name", "msg", d, "ok", "desc"
                );
                var json = transformer.toJson(message);
                if (socket != null) {
                    socket.send(json);
                }
                if (!e.isDisposed()) {
                    e.onComplete();
                }
            } catch (Exception ex) {
                e.tryOnError(ex);
            }
        });
    }
}
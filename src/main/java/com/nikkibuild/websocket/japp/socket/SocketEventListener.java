package com.nikkibuild.websocket.japp.socket;

import com.google.gson.Gson;
import com.nikkibuild.websocket.japp.util.Message;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SocketEventListener extends WebSocketListener {
    private final Gson                        mapper         = new Gson();
    private       SocketDelegate              delegate;

    SocketEventListener() {
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        if (delegate != null) {
            delegate.onDisconnect(webSocket, reason);
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        if (delegate != null) {
            delegate.onDisconnect(webSocket, t.getLocalizedMessage());
        }
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        try {
            var message = mapper.fromJson(text, Message.class);
            if (message != null && delegate != null) {
                delegate.onData(message);
            }
        } catch (Exception e) {
            //ignore
        }
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        if (delegate != null) {
            delegate.onConnect(webSocket);
        }
    }

    public void setDelegate(SocketDelegate delegate) {
        this.delegate = delegate;
    }
}

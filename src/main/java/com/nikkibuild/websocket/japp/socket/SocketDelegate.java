package com.nikkibuild.websocket.japp.socket;

import okhttp3.WebSocket;

public interface SocketDelegate {
    void onConnect(WebSocket socket);

    void onDisconnect(WebSocket socket, String reason);

    void onData(Object message);
}
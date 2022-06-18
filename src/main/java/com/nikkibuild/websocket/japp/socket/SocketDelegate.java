package com.nikkibuild.websocket.japp.socket;

import com.nikkibuild.websocket.japp.util.Message;
import okhttp3.WebSocket;

public interface SocketDelegate {
    void onConnect(WebSocket socket);

    void onDisconnect(WebSocket socket, String reason);

    void onMessage(Message message);
}
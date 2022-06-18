package com.nikkibuild.websocket.japp.socket;

import com.google.gson.Gson;
import com.nikkibuild.websocket.japp.util.Message;
import io.reactivex.rxjava3.subjects.PublishSubject;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SocketEventListener extends WebSocketListener {
    private final PublishSubject<SocketEvent> statusSubject  = PublishSubject.create();
    private final PublishSubject<String>      messageSubject = PublishSubject.create();
    private final Gson                        mapper         = new Gson();
    private       SocketDelegate              delegate;

    @Inject
    SocketEventListener() {
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        if (delegate != null) {
            delegate.onDisconnect(webSocket, reason);
        }
        statusSubject.onNext(new SocketEvent(webSocket, SocketEvent.SocketStatus.CLOSED));
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        if (delegate != null) {
            delegate.onDisconnect(webSocket, t.getLocalizedMessage());
        }
        statusSubject.onNext(new SocketEvent(webSocket, SocketEvent.SocketStatus.ERROR));
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        try {
            var message = mapper.fromJson(text, Message.class);
            if (message != null && delegate != null) {
                delegate.onMessage(message);
            }
        } catch (Exception e) {
            //ignore
        }
        messageSubject.onNext(text);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        if (delegate != null) {
            delegate.onConnect(webSocket);
        }
        statusSubject.onNext(new SocketEvent(webSocket, SocketEvent.SocketStatus.OPENED));
    }

    public void setDelegate(SocketDelegate delegate) {
        this.delegate = delegate;
    }
}

class SocketEvent {
    private final WebSocket socket;
    private final SocketStatus status;

    SocketEvent(WebSocket socket, SocketStatus status) {
        this.socket = socket;
        this.status = status;
    }

    public SocketStatus status() {
        return status;
    }

    public WebSocket socket() {
        return socket;
    }

    enum SocketStatus {
        CLOSED, OPENED, ERROR
    }
}

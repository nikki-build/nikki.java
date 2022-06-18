package com.nikkibuild.websocket.japp;

import com.nikkibuild.websocket.japp.socket.SocketDelegate;
import com.nikkibuild.websocket.japp.socket.SocketEventListener;
import com.nikkibuild.websocket.japp.socket.SocketManager;
import com.nikkibuild.websocket.japp.util.Anything;
import com.nikkibuild.websocket.japp.util.Message;
import com.nikkibuild.websocket.japp.util.ServiceDefinition;
import com.nikkibuild.websocket.japp.util.ServiceToken;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.WebSocket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Scanner;
import java.util.UUID;

@Singleton
public class App implements SocketDelegate {
    private final SocketEventListener eventListener;
    private final SocketManager       socketManager;
    private final Scanner             scanner;
    private final ServiceToken        token;
    private final ServiceDefinition   definition;
    private       boolean             connected;

    @Inject
    public App(SocketEventListener eventListener, SocketManager socketManager,
               ServiceToken token,
               ServiceDefinition definition) {
        this.eventListener = eventListener;
        this.socketManager = socketManager;
        this.token         = token;
        this.definition    = definition;
        scanner            = new Scanner(System.in);
    }

    public void startApp() {
        println("---------- W E B  S O C K E T ----------");
        eventListener.setDelegate(this);
        showMenu();
    }

    private void showMenu() {
        while (true) {
            if (connected) {
                println("[1] Send Message");
                println("[2] Disconnect");
            } else {
                println("[1] Start Socket");
            }
            println("[0] Exit");
            System.out.print("CMD ~> ");
            int command = -1;
            var input   = scanner.nextLine();
            if (input == null) {
                println("Invalid command.");
            } else {
                try {
                    command = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    //ignore
                }
                switch (command) {
                    case -1 -> println("Invalid Command");
                    case 1 -> {
                        if (connected) {
                            send();
                        } else {
                            start();
                        }
                    }
                    case 2 -> stop();
                    case 0 -> exit();
                }
            }
        }
    }

    private void start() {
        println("Connecting...");
        connected = true;
        socketManager.start()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, it -> {
                    println("Error in connecting. Try connecting again.");
                });
    }

    private void stop() {
        println("Disconnecting...");
        connected = false;
        socketManager.stop()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, it -> {
                    println("Oha! failed to disconnect. " + it.getLocalizedMessage());
                });
    }

    private void exit() {
        println("Bye!");
        System.exit(0);
    }

    private void send() {
        var text = UUID.randomUUID().toString();
        println("Sending=> " + text);
        var message = temporaryMessage(text);
        socketManager.send(message)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    println();
                    println("Message sent!");
                }, (it) -> {
                    println();
                    println(it.getLocalizedMessage());
                });
    }

    private Message temporaryMessage(String text) {
        var message = new Anything(text);
        var d       = new Message.Data("n", "d", message);
        return new Message(
                definition.getServiceId(), token.getSessionId(), definition.getInstanceId(),
                "name", "msg", d, "ok", "desc"
        );
    }


    @Override
    public void onConnect(WebSocket socket) {
        connected = true;
        println();
        println("Connected");
    }

    @Override
    public void onDisconnect(WebSocket socket, String reason) {
        connected = false;
        println();
        println("Disconnected " + reason);
    }

    @Override
    public void onMessage(Message message) {
        println();
        println("New Message received! " + message.getMsg());
    }

    private void println() {
        System.out.println();
    }

    private void println(String message) {
        System.out.println(message);
    }
}

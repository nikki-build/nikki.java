package com.nikkibuild.websocket.japp;

import com.nikkibuild.websocket.japp.socket.SocketDelegate;
import com.nikkibuild.websocket.japp.socket.SocketEventListener;
import com.nikkibuild.websocket.japp.socket.SocketManager;
import com.nikkibuild.websocket.japp.socket.ThrottleManager;
import com.nikkibuild.websocket.japp.util.Anything;
import com.nikkibuild.websocket.japp.util.Message;
import com.nikkibuild.websocket.japp.util.ServiceDefinition;
import com.nikkibuild.websocket.japp.util.ServiceToken;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.WebSocket;

import java.util.Scanner;
import java.util.UUID;

public class App implements SocketDelegate {
    private final SocketManager socketManager;
    private final Scanner       scanner;
    private       boolean       connected;

    public App() {
        this.socketManager = new SocketManager("./serviceDef.json", "./serviceToken.json", this, new ThrottleManager(5, 1));

        scanner = new Scanner(System.in);
    }

    public void startApp() {
        println("---------- W E B  S O C K E T ----------");
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
                    case -1: {
                        println("Invalid Command");
                        break;
                    }
                    case 1: {
                        if (connected) {
                            send();
                        } else {
                            start();
                        }
                        break;
                    }
                    case 2: {
                        stop();
                        break;
                    }
                    case 0: {
                        exit();
                        break;
                    }
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
        socketManager.sendData(message)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    println();
                    println("Message sent!");
                }, (it) -> {
                    println();
                    println(it.getLocalizedMessage());
                });
    }

    private Object temporaryMessage(String text) {
        return text;
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
    public void onData(Object message) {
        println();
        println("New Message received! " + message);
    }

    private void println() {
        System.out.println();
    }

    private void println(String message) {
        System.out.println(message);
    }
}

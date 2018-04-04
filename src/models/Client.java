package models;

import java.util.Stack;
import java.util.function.Consumer;
import controllers.*;

public class Client extends Server {
    private static Client client;
    private String ip;
    private int port;
    private static Stack<String> errors = new Stack<>();
    private static Stack<String> info = new Stack<>();
    private static Stack<String> match = new Stack<>();


    public static Client getInstance() {
        if (client == null) {
            client = new Client("127.0.0.1", 7789, (data -> {
                if (data.contains("ERR")) errors.push(data);
                if (data.contains("SVR")) info.push(data);
                if (data.contains("SVR GAME MATCH")) match.push(data);
            }));
        }
        return client;
    }

    private Client(String ip, int port, Consumer<String> onReceiveCallback) {
        super(onReceiveCallback);
        this.ip = ip;
        this.port = port;
    }

    public Stack getErorrs() {
        return errors;
    }

    public Stack<String> getInfo() {
        return info;
    }

    public Stack<String> getMatch() {
        return match;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }

}


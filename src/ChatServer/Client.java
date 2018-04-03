package ChatServer;

import java.util.function.Consumer;

public class Client extends NetworkConnection{

    private String ip;
    private int port;

    public Client(String ip, int port, Consumer<String> onReceiveCallback) {
        super(onReceiveCallback);
        this.ip = ip;
        this.port = port;
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

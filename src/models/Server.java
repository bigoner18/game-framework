package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class Server {
    private Server.ConnectionThread connThread = new Server.ConnectionThread();
    private Consumer<String> onReceiveCallback;

    public Server(Consumer<String> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        connThread.setDaemon(true);
    }

    public void startConnection() throws Exception {
        connThread.start();
    }

    public void send(String data) {
        try {
            // System.out.println("C: " + data);
            connThread.out.println(data);
        } catch (Exception e) {
            System.out.println("Zenden wil niet..");
            e.printStackTrace();
        }
    }

    public void closeConnection() throws Exception {
        connThread.socket.close();
    }

    private class ConnectionThread extends Thread {
        private Socket socket;
        private PrintWriter out;

        @Override
        public void run() {
            try(Socket socket = new Socket("localhost", 7789);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream())))
            {
                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while(true) {
                    String data = in.readLine();
                    onReceiveCallback.accept(data);
                }

            }

            catch (IOException e) {
                System.out.println("Server start niet");
                e.printStackTrace();
            }
        }
    }
}

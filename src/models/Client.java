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
    private static Stack<String> moves = new Stack<>();
    private static Stack<String> score = new Stack<>();
    private static Stack<String> challenges = new Stack<>();


    public static Client getInstance() {
        if (client == null) {
            client = new Client("127.0.0.1", 7789, (data -> {
                if (data.contains("ERR")) errors.push(data);
                if (data.contains("SVR")) info.push(data);
                if (data.contains("SVR GAME MATCH")) match.push("{" + data.split("\\{")[1]);
                if (data.contains("SVR GAME MOVE")) moves.push("{" + data.split("\\{")[1]);
                if (data.contains("WIN") | data.contains("DRAW") | data.contains("LOSS")) score.push(data);
                if (data.contains("SVR GAME CHALLENGE")) challenges.push("{" + data.split("\\{")[1]);
                // System.out.println(data); // test
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

    public Stack<String> getMoves() { return moves; }

    public Stack<String> getScore() { return score; }

    public Stack<String> getChallenges() { return challenges; }
}


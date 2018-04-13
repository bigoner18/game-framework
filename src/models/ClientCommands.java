package models;

public abstract class ClientCommands {
    public static String login(String name) {
        Client.getInstance().send("login " + name);
        return getReturnMessage();
    }

    public static String subscribeGame(String game) {
        Client.getInstance().send("subscribe " + game);
        return getReturnMessage();
    }

    public static String getPlayers() {
        Client.getInstance().send("get playerlist");
        return getInfo();
    }
    
    public static String acceptChallenge(String number) {
	Client.getInstance().send("challenge accept " + number);
	return getInfo();
    }
    
    public static String challengePlayer(String player, String game) {
        //System.out.println("challenge \"" + player.trim() + "\" \"Tic-tac-toe\"");
        Client.getInstance().send("challenge \"" + player.trim() + "\" \"" + game + "\"");
        return getReturnMessage();
    }

    public static String sendMove(int move) {
        Client.getInstance().send("move " + move);
        return getInfo();
    }

    public static String getReturnMessage() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Client.getInstance().getErorrs().isEmpty()) {
            return "true";
        }
        else return Client.getInstance().getErorrs().pop();
    }

    public static String getInfo() {
        while (Client.getInstance().getInfo().size() == 0) {}
        String message = Client.getInstance().getInfo().pop();
        System.out.println("S: " + message);
        return message;

    }
}

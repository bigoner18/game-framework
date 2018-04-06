package controllers;

import controllers.simpleGame.TicTacToeController;
import controllers.simpleGame.ReversiController;

import javafx.application.Platform;
import models.Player;
import views.ReversiView;
import javafx.stage.Stage;
import lib.Parser;
import models.Client;
import models.ClientCommands;
import models.Game;
import views.GameLobbyView;
import views.TicTacToeView;

import java.util.*;

public class GameStartController {
    private GameLobbyView view;
    private Stage stage;
    private Thread lobbyListener;

    public GameStartController(Stage primaryStage){
        view = new GameLobbyView();
        updateListView();

        lobbyListener = new Thread(new LobbyListener());
        lobbyListener.start();
        stage = primaryStage;

        view.getStartButton().setOnMouseClicked(e -> {
           // Start een game..
        });

        view.getChallengeButton().setOnMouseClicked(e -> {
            ClientCommands.challengePlayer(view.getPlayer());
            System.out.println(ClientCommands.challengePlayer(view.getPlayer()));
        });

        view.getRefreshButton().setOnMouseClicked(e -> {
            updateListView();
        });

        primaryStage.setTitle("lobby");
        primaryStage.setScene(view);
    }

    public HashMap getGameInfo() {
        int stackSize = Client.getInstance().getMatch().size();
        if (stackSize != 1) {
            Client.getInstance().getMatch().pop().replace("\"", "");
        }

        HashMap info = Parser.parse(Client.getInstance().getMatch());
        return info;
    }

    public void createTicTacToe() {
        HashMap gameInfo = getGameInfo();
        new TicTacToeController(new Game(3, 3), stage, new TicTacToeView(), gameInfo);
    }

    public void createReversi() {
	    HashMap gameInfo = getGameInfo();
	    new ReversiController(new Game(8, 8), stage, new ReversiView(), gameInfo);
    }

    private void updateListView() {
        view.getList().clear();
        view.getListView().getItems().removeAll();

        String result = ClientCommands.getPlayers();
        // Fetch the names from the getPlayers command
        String[] names = result.substring(16, result.length() - 2).split(", ");
        for (String name: names) {
            view.getList().add(name.replace("\"", "") + "\n");
        }
    }

    class LobbyListener implements Runnable {
        private boolean running = true;
        @Override
        public void run() {
            while (running) {
                Platform.runLater(() -> {
                    try {
                        if (!Client.getInstance().getMatch().isEmpty()) {
                            running = false;
                            // Dit kan misschien weg, we weten nog niet of de server ook daadwerkelijk reversi stuurt als je ingeschreven staat op TicTacToe en vice versa...
                            if (Player.getInstance().getGame().equals("Tic-tac-toe")) {
                                createTicTacToe();
                            } else {
                                createReversi();
                            }
                        }
                    } catch (EmptyStackException e) {}
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
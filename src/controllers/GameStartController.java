package controllers;

import controllers.simpleGame.SimpleGameController;
import controllers.simpleGame.TicTacToeController;
import controllers.simpleGame.ReversiController;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import models.*;
import views.ReversiView;
import javafx.stage.Stage;
import lib.Parser;
import views.GameLobbyView;
import views.TicTacToeView;

import java.util.*;

/**
 * Scrollbar in de lobby
 * timeout op de alertbox na de game
 */

public class GameStartController {
    private GameLobbyView view;
    private Stage stage;

    public GameStartController(Stage primaryStage){
        view = new GameLobbyView();
        new Thread(new LobbyListener()).start();
        updateListView();

        stage = primaryStage;

        view.getStartButton().setOnMouseClicked(e -> {
           // Start een game..
        });

        view.getChallengeButton().setOnMouseClicked(e -> {
            ClientCommands.challengePlayer(view.getPlayer());
            //System.out.println(ClientCommands.challengePlayer(view.getPlayer()));
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
        TicTacToeController ttt = new TicTacToeController(new Game(3, 3), stage, new TicTacToeView(), gameInfo);
        if (Settings.getInstance().getAI()) {
            new AIController(ttt);
        }
    }

    public void createReversi() {
	    HashMap gameInfo = getGameInfo();
	    ReversiController reversi = new ReversiController(new Game(3, 3), stage, new ReversiView(), gameInfo);
        if (Settings.getInstance().getAI()) {
            new AIController(reversi);
        }
    }

    private void updateListView() {
        view.getList().clear();
        view.getListView().getItems().removeAll();

        String result = ClientCommands.getPlayers();
        // Fetch the names from the getPlayers command
        String[] names = result.substring(16, result.length() - 2).split(", ");



        for (String name: names) {
            name = name.replace("\"", "");
            if (!Player.getInstance().getName().equals(name)) {
                view.getList().add(name + "\n");
            }
        }
    }

    public void challengeAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            HashMap info = Parser.parse(Client.getInstance().getChallenges());
            //String response = Client.getInstance().getChallenges().pop();
            alert.setTitle(info.get("CHALLENGER").toString() + " challenges you");
            alert.setHeaderText(info.get("CHALLENGER").toString() + " challenges you for " + info.get("GAMETYPE"));
            alert.setContentText("If you want to accept click ok");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Client.getInstance().send("challenge accept " + info.get("CHALLENGENUMBER"));
            }
        });
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

                if (!Client.getInstance().getChallenges().isEmpty()) {
                    challengeAlert();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
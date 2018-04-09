package controllers;

import controllers.simpleGame.SimpleGameController;
import javafx.application.Platform;
import lib.GameStateNode;
import models.Game;

public class AIController extends Thread{
    private boolean playing = true;
    private SimpleGameController gameController;
    private Game game;

    public AIController(SimpleGameController gameController) {
        this.gameController = gameController;
        this.game = gameController.getGameModel();
        start();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void calculateOptions(int[] playfield) {
        boolean tempTurn = game.isYourTurn();
        int turnCounter = game.getTurn();
        GameStateNode currentNode = new GameStateNode(playfield);

        for (int x = 0; x < playfield.length; x++) {
            if (gameController.legalMove(playfield[x], currentNode.getPlayfield())) {
                int[] temp = playfield;
                temp[x] = (tempTurn) ? 1 : 2;
                currentNode.addNode(x, new GameStateNode(temp));
            }
        }

        System.out.println(currentNode);
    }

    @Override
    public void run() {
        while (playing) {
            if (game.isYourTurn()) {
                calculateOptions(game.getPlayField());
                for (int i = 0; i < game.getPlayField().length; i++) {
                    if (game.getPlayField()[i] == 0) {
                        int x = i;
                        Platform.runLater(() -> {
                            gameController.move(x);
                        });
                        break;
                    }
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
    }
}

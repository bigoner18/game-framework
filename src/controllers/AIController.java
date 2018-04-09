package controllers;

import controllers.simpleGame.SimpleGameController;
import javafx.application.Platform;
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

    @Override
    public void run() {
        while (playing) {
            System.out.println(game.isYourTurn());
            if (game.isYourTurn()) {
                System.out.println("test");
                for (int i = 0; i < game.getPlayField().length; i++) {
                    System.out.println(i);
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

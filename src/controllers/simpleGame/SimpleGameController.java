package controllers.simpleGame;

import models.Client;

import java.util.HashMap;
import controllers.GameSelectController;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import models.*;
import lib.Parser;
import views.GameView;
import javafx.scene.control.Alert.AlertType;

import java.util.*;

public abstract class SimpleGameController {
    protected Game gameModel;
    protected GameView gameView;
    protected String opponent;
    protected Stage primaryStage;

    public SimpleGameController(Game model, Stage primaryStage, GameView gameView, HashMap info) {
        gameModel = model;
        gameModel.setOpponent((String) info.get("OPPONENT")); // Misschien handig voor in de toekomst, kan anders wel weg..
        gameView.setOpponent((String) info.get("OPPONENT"));

        if (info.get("PLAYERTOMOVE").equals(Player.getInstance().getName()))
            gameModel.setYourTurn(true);
        else
            gameModel.setYourTurn(false);

        this.gameView = gameView;
        this.primaryStage = primaryStage;
        new Thread(new MoveListener()).start();
        primaryStage.setScene(this.gameView);
    }

    public void move(int i) {
        gameModel.updatePlayField(i);
        ClientCommands.sendMove(i);
        gameModel.incrementTurn();
        gameView.setTurn(gameModel.getOpponent());
        gameModel.setYourTurn(false);
        updateGame();

    }

    protected void setOnClick(int i) {
        Rectangle r = (Rectangle) gameView.getGrid().getChildren().get(i);
        r.setOnMouseClicked(e -> {
            try {
                //System.out.println(ClientCommands.sendMove(i));
                move(i);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            r.setDisable(true);
        });
    }

    protected GridPane generateGrid(int[] playField) {
        GridPane grid = new GridPane();
        for (int y = 0; y < gameModel.getGridHeight(); y++) {
            for (int x = 0; x < gameModel.getGridWidth(); x++) {
                int index = (y * gameModel.getGridWidth()) + x;
                Rectangle r = new Rectangle(50, 50);
                switch (playField[index]) {
                    case 0:
                        r.setFill(Color.WHITE);
                        break;
                    case 1:
                        r.setFill(Color.BLUE);
                        break;
                    case 2:
                        r.setFill(Color.RED);
                        break;
                }
                r.setStroke(Color.BLACK);
                grid.add(r, x, y);
            }
        }
        return grid;
    }

    public Game getGameModel() {
        return gameModel;
    }

    public void updateGame() {
        gameView.setGrid(generateGrid(gameModel.getPlayField()));
    }

    public void getScore() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            String response = Client.getInstance().getScore().pop();
            if (response.contains("WIN")) {
                alert.setHeaderText("Congratulations " + Player.getInstance().getName() + ", you won!!");
            } else if (response.contains("DRAW")) {
                alert.setHeaderText("The game between you and " + opponent + " ended in a draw.");
            } else {
                alert.setHeaderText("Sorry " + Player.getInstance().getName() + ", you lost..");
            }
            alert.setTitle("Score");
            alert.setContentText("You can back to the game select room.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                new GameSelectController(primaryStage);
            }
        });
    }

    public boolean legalMove(int index, int[] playfield) {
        return gameModel.isYourTurn() && playfield[index] == 0;
    }

    class MoveListener implements Runnable {
        boolean running = true;

        @Override
        public void run() {
            while(running) {
                if (!Client.getInstance().getMoves().empty()) {
                    HashMap info = Parser.parse(Client.getInstance().getMoves());
                    if (!info.get("PLAYER").equals(Player.getInstance().getName())) {
                        // Laat de AI op de movestack pushen...
                        System.out.println("TurnInfo: " + info);
                        gameModel.updatePlayField(Integer.valueOf((String) info.get("MOVE")));
                        gameModel.setYourTurn(true);
                        Platform.runLater(() -> {
                            updateGame();
                        });
                    }
                }
                if (Client.getInstance().getScore().size() > 0) {
                    getScore();
                    running = false;
                }
            }
        }
    }
}

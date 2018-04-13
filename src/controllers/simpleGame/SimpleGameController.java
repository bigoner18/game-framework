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
import javafx.scene.shape.Circle;

public abstract class SimpleGameController {
    protected Game gameModel;
    protected GameView gameView;
    protected String opponent;
    protected Stage primaryStage;
    protected static Color startColor;
    protected static Color oponentColor;
    protected Circle[] gridCircles;
    List<Integer> occupied = new ArrayList<>();
    List<Integer> possMoves = new ArrayList<>();
    Set<Integer> check = new HashSet<>();

    public SimpleGameController(Game model, Stage primaryStage, GameView gameView, HashMap info) {
        gameModel = model;
        gameModel.setOpponent((String) info.get("OPPONENT")); // Misschien handig voor in de toekomst, kan anders wel weg..
        gameView.setOpponent((String) info.get("OPPONENT"));

        if (info.get("PLAYERTOMOVE").equals(Player.getInstance().getName())) gameModel.setYourTurn(true);
        else gameModel.setYourTurn(false);

        this.gameView = gameView;
        this.primaryStage = primaryStage;
        primaryStage.setScene(this.gameView);
	
        if(!gameModel.isYourTurn()){
            startColor = Color.WHITE;
            oponentColor = Color.BLACK;
        } else {
            startColor = Color.BLACK;
            oponentColor = Color.WHITE;
        }

        GameView.setStartColor(startColor);
        GameView.setOpponentColor(oponentColor);
    }

    protected void setOnClick(int i) {
        Circle r = (Circle) gameView.getGrid().getChildren().get(i);

        if (!occupied.contains(i)) {
            r.setFill(Color.YELLOW);
        }

        r.setOnMouseClicked(e -> {
            try {
                System.out.println(ClientCommands.sendMove(i));
                System.out.println(legalMove(i));
                gameModel.updatePlayField(i);
                //ClientCommands.sendMove(i);
                gameModel.incrementTurn();
                gameView.setTurn(gameModel.getOpponent());
                gameModel.setYourTurn(false);
                occupied.add(i);
                updateGame();
                possMoves.clear();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            r.setDisable(true);
        });
    }

    protected GridPane generateGrid(int[] playField) {
        GridPane grid = new GridPane();
        gridCircles = new Circle[gameModel.getGridWidth() * gameModel.getGridHeight()];
        for (int y = 0; y < gameModel.getGridHeight(); y++) {
            for (int x = 0; x < gameModel.getGridWidth(); x++) {
                int index = (y * gameModel.getGridWidth()) + x;
                Rectangle r = new Rectangle(50, 50);
		        Circle c = new Circle(25);
                gridCircles[(y * gameModel.getGridWidth()) + x] = c;
                switch (playField[index]) {
                    case 0:
                        r.setFill(Color.GREEN);
			            c.setFill(Color.TRANSPARENT);
                        break;
                    case 1:
                        r.setFill(Color.GREEN);
			            c.setFill(oponentColor);
                        break;
                    case 2:
                        r.setFill(Color.GREEN);
			            c.setFill(startColor);
                        break;
                }
                r.setStroke(Color.RED);
                grid.add(r, x, y);
		        grid.add(c, x, y);
            }
        }
        return grid;
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

    protected boolean legalMove(int index) {
        return gameModel.getPlayField()[index] == 0;
    }

    class MoveListener implements Runnable {
        boolean running = true;

        @Override
        public void run() {
            while(running) {
                if (Client.getInstance().getMoves().size() > 0) {
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
    
    public static Color getStartColor(){
	return startColor;
    }
    
    public static Color getOpponentColor(){
	return oponentColor;
    }
}

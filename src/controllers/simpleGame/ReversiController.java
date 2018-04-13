package controllers.simpleGame;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.*;

import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import lib.Parser;
import models.Client;
import models.ClientCommands;
import models.Game;
import models.Player;
import views.GameView;

public class ReversiController extends SimpleGameController {
    int[] directions = {-9, -8, -7, -1, 1, 7, 8, 9};
    int[] leftDir = {-9, -1, 7, -8, 8};
    int[] rightDir = {-7, 1, 9, -8, 8};
    private List<Integer> occupied = new ArrayList<>();

    public ReversiController(Game model, Stage primaryStage, GameView gameView, HashMap info) {
        super(model, primaryStage, gameView, info);
        if (!gameModel.isYourTurn()) {
            gameModel.setPlayFieldAtIndex(27, 1);
            gameModel.setPlayFieldAtIndex(28, 2);
            gameModel.setPlayFieldAtIndex(35, 2);
            gameModel.setPlayFieldAtIndex(36, 1);
        } else {
            gameModel.setPlayFieldAtIndex(27, 2);
            gameModel.setPlayFieldAtIndex(28, 1);
            gameModel.setPlayFieldAtIndex(35, 1);
            gameModel.setPlayFieldAtIndex(36, 2);
        }
        occupied.add(27);
        occupied.add(28);
        occupied.add(35);
        occupied.add(36);

        primaryStage.setTitle("Reversi!");
        updateGame();
        new Thread(new MoveListener()).start();

    }

    public List getMovesList(int index) {
        List toChange = new ArrayList();
        if (super.legalMove(index)) {
            for (int i : directions) {
                try {
                    if (gameModel.getPlayField()[index + i] == (gameModel.isYourTurn() ? 2 : 1))
                        toChange.addAll(checkDir(i, index));
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        return toChange;
    }

    public List getMoveReceivesList(int index) {
        List toChange = new ArrayList();
        for (int i : directions) {
            try {
                if (gameModel.getPlayField()[index + i] == (gameModel.isYourTurn() ? 2 : 1))
                    toChange.addAll(checkDir(i, index));
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
        return toChange;
    }


    protected void setOnClick(int i) {
        Circle r = gridCircles[i];
        if (!occupied.contains(i)) r.setFill(Color.YELLOW);
        r.setOnMouseClicked(e -> {
            Client.getInstance().getMoves().add("{PLAYER: \"" + Player.getInstance().getName() + "\", MOVE: \"" + i + "\", DETAILS: \"\"}");
            ClientCommands.sendMove(i);
            gameModel.setYourTurn(false);
        });
    }

    public Set getPossibleList() {
        Set check = new HashSet();
        for (int item : occupied) {
            if (item % 8 == 0) check.addAll(getPossibleDirs(item, rightDir));
            else if (item + 1 % 8 == 0) check.addAll(getPossibleDirs(item, leftDir));
            else check.addAll(getPossibleDirs(item, directions));
        }
        possMoves.addAll(check);
        return check;
    }

    public Set getPossibleDirs(int item, int[] dir) {
        Set possibilities = new HashSet();
        for (int i : dir) {
            try {
                if (gameModel.getPlayField()[item + i] == 0) possibilities.add(item + i);
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
        return possibilities;
    }

    private List checkDir(int dir, int current) {
        try {
            ArrayList series = new ArrayList();
            current += dir;
            int currentValue = gameModel.getPlayField()[current];
            while (currentValue == (gameModel.isYourTurn() ? 2 : 1)) {
                if (current % 8 == 0 || (current + 1) % 8 == 0) {
                    if (dir == 8 || dir == -8) {
                        series.add(current);
                        current += dir;
                        currentValue = gameModel.getPlayField()[current];
                    } else
                        break;
                } else {
                    series.add(current);
                    current += dir;
                    currentValue = gameModel.getPlayField()[current];
                }
            }
            if (gameModel.getPlayField()[current] == (gameModel.isYourTurn() ? 1 : 2))
                return series;
        } catch (ArrayIndexOutOfBoundsException e) {}
        return new ArrayList();
    }

    public void doTurn (int i, List toChange) {
        gameModel.updatePlayField(i);
        gameModel.updatePlayField(toChange);
        gameModel.incrementTurn();
        occupied.add(i);
        updateGame();
    }

    class MoveListener implements Runnable {
        boolean running = true;

        @Override
        public void run() {
            while(running) {
                if (Client.getInstance().getMoves().size() > 0) {
                    HashMap info = Parser.parse(Client.getInstance().getMoves());
                    System.out.println("------------------------------------------------------------");
                    System.out.println("Player:  " + info.get("PLAYER"));
                    System.out.println("Move:    " + info.get("MOVE"));
                    System.out.println("Details: " + info.get("DETAILS"));
                    System.out.println("------------------------------------------------------------");
                    Platform.runLater(() -> {
                        int index = Integer.valueOf((String) info.get("MOVE"));
                        doTurn(index, getMoveReceivesList(index));
                    });
                }

                if (Client.getInstance().getTurns().size() > 0) {
                    Set possibleMoves = getPossibleList();
                    for (Iterator it = possibleMoves.iterator(); it.hasNext(); ) {
                        int move = (int) it.next();
                        List moveResult = getMovesList(move);
                        if (!moveResult.isEmpty()) {
                            Platform.runLater(() -> {
                                setOnClick(move);
                            });
                        }
                    }
                    gameModel.setYourTurn(true);
                    Client.getInstance().getTurns().removeLast();
                }

                if (Client.getInstance().getScore().size() > 0) {
                    getScore();
                    running = false;
                }
            }
        }
    }
}

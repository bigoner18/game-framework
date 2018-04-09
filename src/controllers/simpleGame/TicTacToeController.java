package controllers.simpleGame;

import javafx.stage.Stage;
import models.Game;
import views.GameView;

import java.util.HashMap;

public class TicTacToeController extends SimpleGameController {

    public TicTacToeController(Game model, Stage primaryStage, GameView gameView, HashMap info) {
        super(model, primaryStage, gameView, info);
        primaryStage.setTitle("Tic-Tac-Toe!");
        updateGame();
    }

    @Override
    public boolean legalMove(int index, int[] playfield) {
        return super.legalMove(index, playfield);
    }

    @Override
    public void updateGame() {
        super.updateGame();

        for (int i = 0; i < gameView.getGrid().getChildren().size(); i++) {
            if (legalMove(i, gameModel.getPlayField())) {
                setOnClick(i);
            }
        }
        primaryStage.setScene(this.gameView);
    }
}

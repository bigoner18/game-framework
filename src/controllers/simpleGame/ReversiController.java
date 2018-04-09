package controllers.simpleGame;

import java.util.HashMap;
import javafx.stage.Stage;
import models.Game;
import views.GameView;

public class ReversiController extends SimpleGameController{
    //private Game gameModel;

public ReversiController(Game model, Stage primaryStage, GameView gameView, HashMap info) {
        super(model, primaryStage, gameView, info);
        gameModel.setPlayFieldAtIndex(27, 1);
        gameModel.setPlayFieldAtIndex(28, 2);
        gameModel.setPlayFieldAtIndex(35, 2);
        gameModel.setPlayFieldAtIndex(36, 1);
        primaryStage.setTitle("Reversi!");
            updateGame();
        //gameModel = Game.getInstance();
    }

   public boolean legalMove(int index) {
        //if (super.legalMove(index) && nextToEnemy(index))
            return true;
        //return false;
    }

    public void updateGame() {
        super.updateGame();
        for (int i = 0; i < gameView.getGrid().getChildren().size(); i++) {
            if (legalMove(i)) {
                setOnClick(i);
            }
        }
    }
    
    public boolean nextToEnemy(int index){
    return gameModel.getPlayFieldAtIndex(index + 1) == 2 || 
            gameModel.getPlayFieldAtIndex(index - 1) == 2 ||
            gameModel.getPlayFieldAtIndex(index + 8) == 2 ||
            gameModel.getPlayFieldAtIndex(index - 8) == 2 ||
            gameModel.getPlayFieldAtIndex(index + 7) == 2 ||
            gameModel.getPlayFieldAtIndex(index - 7) == 2 ||
            gameModel.getPlayFieldAtIndex(index + 9) == 2 ||
            gameModel.getPlayFieldAtIndex(index - 9) == 2;

    }
    
    
    
    /*public boolean friendlyInLine(int index){
    checkDiagonal(index);
    checkVertical(index);
    checkHorizontal(index);   
    }
    
    public boolean checkDiagonal(index){
        
    }
    
    public boolean checkVertical(index) {
    
    }
    
    public boolean checkHorizontal(index) {
        
    }*/
}

package views;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import models.Player;

public class GameView extends Scene {
    BorderPane pane;
    static Label startColor = new Label();
    static Label oponentColor = new Label();
    static Label opponent = new Label();
    Label turn = new Label();
    static Label name = new Label(Player.getInstance().getName());


    public GameView() {
        super(new BorderPane());
        pane = (BorderPane) super.getRoot();
        // Create Scoreboard
        GridPane score = new GridPane();
        //score.setPrefSize(500,500); // added width and height
        score.add(new Label("Naam: "), 0, 0);
        score.add(name, 1, 0);
	score.add(startColor, 2, 0);

        score.add(new Label("Tegenstander: "), 0, 1);
        score.add(opponent, 1, 1);
	score.add(oponentColor, 2, 1);

        score.add(new Label("Aan de beurt: "), 0, 2);
        score.add(turn, 1, 2);
        HBox hbox = new HBox(8);
        hbox.getChildren().add(new Label("Footer Tekst"));

        pane.setTop(score);
        pane.setPrefSize(500,500);
        pane.setCenter(new GridPane());
        pane.setBottom(hbox);
    }

    public void setGrid(GridPane grid) {
        pane.setCenter(grid);
    }

    public GridPane getGrid() {
        return (GridPane) pane.getCenter();
    }

    public void setOpponent(String name) {
        opponent.setText(name);
    }
    
    public static void setStartColor(Color color){
	if(color == Color.WHITE){
	    startColor.setText("White");
	} else {
	    startColor.setText("Black");
	}	
    }
    
    public static void setOpponentColor(Color color){
	if(color == Color.WHITE){
	    oponentColor.setText("White");
	} else {
	    oponentColor.setText("Black");
	}
    }

    public void setTurn(String name) {
        turn.setText(name);
    }
}

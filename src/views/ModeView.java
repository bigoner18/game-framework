package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ModeView extends Scene {
    private GridPane pane;
    private Button AIButton;
    private Button playerButton;

    public ModeView() {
        super(new GridPane());
        pane = (GridPane) super.getRoot();
        pane.setPadding(new Insets(12.5, 12.5, 12.5, 12.5));
        pane.setVgap(5.5);
        pane.setAlignment(Pos.CENTER);
        pane.add(new Label("AI or player"), 0, 0);
        AIButton = new Button("AI");
        pane.add(AIButton, 0, 1);
        playerButton = new Button("Player");
        pane.add(playerButton, 0, 2);
        this.pane = pane;
    }

    public GridPane getPane() {
        return pane;
    }

    public Button getAIButton () {
        return AIButton;
    }

    public Button getPlayerButton() {
        return playerButton;
    }
}


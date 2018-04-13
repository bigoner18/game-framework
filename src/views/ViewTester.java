package views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ViewTester extends Application {
    public void start(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        pane.setCenter(new GridPane());
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Choose a game"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        System.out.println("{" + "SVR GAME MOVE {PLAYER: \"1\", MOVE: \"19\", DETAILS: \"\"}".split("\\{")[1]);
    }
}

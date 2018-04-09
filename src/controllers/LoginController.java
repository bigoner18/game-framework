package controllers;

import javafx.stage.Stage;
import models.ClientCommands;
import javafx.scene.control.Label;
import models.Player;
import views.LoginView;

public class LoginController {
    private LoginView view;


    public LoginController(Stage primaryStage){
        view = new LoginView();

        view.getLoginButton().setOnMouseClicked(e -> {
            String response = ClientCommands.login(view.getTextField().getText());
            Player.getInstance().setName(view.getTextField().getText());
            if (response.contains("ERR")) {
                Label error = new Label(response);
                view.getPane().add(error, 0, 3);
            } else {
                new ModeController(primaryStage);
                primaryStage.show();
            }
        });

        primaryStage.setTitle("Login");
        primaryStage.setScene(view);
    }
}
package game;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameLoop game1 = new GameLoop();
        game1.startWithMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

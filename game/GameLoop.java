package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

import static game.Field.*;

public class GameLoop {
    public static ArrayList<Character> players = new ArrayList<>();
    public static boolean gamePaused = false;
    public static double TIMESTART;
    public static int SCORE_PENALTY;

    Menu gameMenu;
    Field battleField;
    Stage mainStage, pauseStage, endStage;
    Scene startScene, battleScene, pauseGameScene, endGameScene;
    Pane battleLayout;
    Character player;
    InfoPanel infoPanel;

    public GameLoop() {
        gameMenu = new Menu();
        startScene = gameMenu.getMainMenu();
        mainStage = new Stage();
    }

    public void createField() {
        setDifficultySetting();
        battleField = new Field();
        battleLayout = battleField.getMainLayout();
        battleScene = new Scene(battleLayout, Field.W, Field.H + 50);
        pauseStage = gameMenu.pauseGameWindow();
        pauseGameScene = gameMenu.getPauseMenu();
        pauseStage.setScene(pauseGameScene);
        player = new Character();
        players.add(player);
        infoPanel = new InfoPanel();
        battleLayout.getChildren().addAll(infoPanel.createInfoPanel(), player);
        battleScene.setOnKeyPressed(event -> player.addKeyHandler(event));
        battleScene.setOnKeyReleased(event -> player.addNoKeyHandler());
    }

    public void startWithMenu() {
        mainStage.setScene(startScene);
        mainStage.setTitle("TreasureHunter Game");
        mainStage.centerOnScreen();
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.show();

        startScene.setOnMouseClicked(event -> {
            if (gameMenu.getDifficultyGameStatus() != 0) {
                createField();
                mainStage.setScene(battleScene);
                correctPosition(mainStage);
                gameActions();
            }
        });
    }

    public void startWithoutMenu() {
        createField();
        gameActions();
        mainStage.setScene(battleScene);
        mainStage.setTitle("TreasureHunter Game");
        correctPosition(mainStage);
        mainStage.initStyle(StageStyle.UNDECORATED);
        //mainStage.setResizable(false);
        mainStage.show();
    }

    private void restart(Boolean menuStatus) {
        treasures.clear();
        traps.clear();
        treasureWithArea.clear();
        trapWithArea.clear();
        players.clear();
        gameMenu.setStartNewGameStatus(false);
        gameMenu.setReturnMainMenuStatus(false);
        gamePaused = false;
        endStage.close();
        mainStage.close();
        if (menuStatus)
            new GameLoop().startWithMenu();
        else new GameLoop().startWithoutMenu();
    }

    private void pause() {
        gamePaused = true;
        pauseStage.show();
    }

    private void resume() {
        gameMenu.setResumeGameStatus(false);
        player.setPaused(false);
        gamePaused = false;
        infoPanel.getTimer().play();
        pauseStage.close();
    }

    private void gameActions() {
        battleScene.setOnKeyTyped(event -> {
            if (player.isPaused()) {
                pause();
            }
        });
        pauseGameScene.setOnMouseClicked(event -> {
            if (gameMenu.isResumeGameStatus()) {
                resume();
            }
        });
        pauseGameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                resume();
            }
        });
        setEndGameConditions();
    }

    private void setEndGameConditions() {
        infoPanel.getScoreLabel().textProperty().addListener(observable -> {
            if (player.getScore() < 0) {
                endActions(false);
            }
        });

        infoPanel.getTreasuresLabel().textProperty().addListener(observable -> {
            if (Field.treasures.size() == 0) {
                endActions(true);
            }
        });

        infoPanel.getTimerLabel().textProperty().addListener(observable -> {
            if (infoPanel.getTime().toSeconds() == 0.0) {
                endActions(false);
            }
        });
    }

    private void endActions(Boolean winStatus) {
        gamePaused = true;
        infoPanel.getScoreTL().stop();
        infoPanel.getTreasureTL().stop();
        infoPanel.getStepsTL().stop();
        player.getAnimation().stop();
        endStage = gameMenu.endGameWindow(winStatus);
        endGameScene = endStage.getScene();
        endStage.show();
        endGameScene.setOnMouseClicked(event -> {
            if (gameMenu.isStartNewGameStatus()) {
                restart(false);
            }
            if (gameMenu.isReturnMainMenuStatus()){
                restart(true);
            }
        });
    }

    private void setDifficultySetting() {
        int timeCorrector = Field.X_CELLS * Field.Y_CELLS + (Field.BONUS + Field.ENEMY) * 30;
        switch (gameMenu.getDifficultyGameStatus()){
            case 1:
                SCORE_PENALTY = 1;
                TIMESTART = Math.round(timeCorrector * 0.15);          // 90 sec for default game
                break;
            case 2:
                SCORE_PENALTY = 2;
                TIMESTART = Math.round(timeCorrector * 0.117);         // 70 sec for default game
                break;
            case 3:
                SCORE_PENALTY = 2;
                TIMESTART = Math.round(timeCorrector * 0.083);         // 50 sec for default game
                break;
        }
    }

    private void correctPosition(Stage mainStage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        mainStage.setX(primaryScreenBounds.getWidth() / 2 - W / 2);
        mainStage.setY(primaryScreenBounds.getHeight() / 2 - (H + 50) / 2);
    }

    public Field getBattleField() {
        return battleField;
    }
}

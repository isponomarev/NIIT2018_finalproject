package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static game.Field.*;

public class InfoPanel extends StackPane {
    private StackPane infoPanel;
    private Timeline timer;
    private Timeline treasureTL;
    private Timeline scoreTL;
    private Timeline stepsTL;
    private Label timerLabel;
    private Label treasuresLabel;
    private Label stepsLabel;
    private Label scoreLabel;
    private DoubleProperty timeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.seconds(GameLoop.TIMESTART);
    private ImageView timerIcon = new ImageView(new Image(getClass().getResourceAsStream("images/timer.png")));
    private ImageView treasureIcon = new ImageView(new Image(getClass().getResourceAsStream("images/treasure.png")));
    private ImageView stepsIcon = new ImageView(new Image(getClass().getResourceAsStream("images/steps.png")));
    private ImageView scoreIcon = new ImageView(new Image(getClass().getResourceAsStream("images/score.png")));

    public InfoPanel() {
        infoPanel = new StackPane();
        timerLabel = new Label();
        treasuresLabel = new Label();
        stepsLabel = new Label();
        scoreLabel = new Label();
        setTimeBar();
        setTresuaresBar();
        setStepsBar();
        setScoreBar();
    }

    public StackPane createInfoPanel() {
        infoPanel = new StackPane();
        infoPanel.setPrefSize(W, 50);
        infoPanel.relocate(0, H);
        setIconSize(timerIcon);
        setIconSize(treasureIcon);
        setIconSize(stepsIcon);
        setIconSize(scoreIcon);
        infoPanel.setStyle("-fx-background-color: #DEB887");
        HBox infoBox = new HBox(30);
        infoBox.setAlignment(Pos.CENTER);
        HBox timeBox = getItemPanel(timerIcon, timerLabel);
        HBox treasureBox = getItemPanel(treasureIcon, treasuresLabel);
        HBox stepsBox = getItemPanel(stepsIcon, stepsLabel);
        HBox scoreBox = getItemPanel(scoreIcon, scoreLabel);
        infoBox.getChildren().addAll(timeBox, treasureBox, stepsBox, scoreBox);
        infoPanel.getChildren().add(infoBox);
        return infoPanel;
    }

    private void setTimeBar() {
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setTranslateY(-5);
        timerLabel.setStyle("-fx-font-size: 3em;");
        timer = new Timeline(
                new KeyFrame(Duration.millis(100),
                        event -> {
                            if (GameLoop.gamePaused == true)
                                timer.pause();
                            Duration duration = ((KeyFrame) event.getSource()).getTime();
                            time = time.subtract(duration);
                            timeSeconds.set(time.toSeconds());
                            if (time.lessThan(Duration.seconds(10))) {
                                if (time.toMillis() % 200 == 0)
                                    timerLabel.setTextFill(Color.DARKRED);
                                else timerLabel.setTextFill(Color.RED);
                            }
                            if (time == Duration.seconds(0))
                                timer.stop();
                        }
                ));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

    }

    private void setTresuaresBar() {
        treasuresLabel.setTextFill(Color.BLACK);
        treasuresLabel.setTranslateY(-5);
        treasuresLabel.setStyle("-fx-font-size: 3em;");
        treasureTL = new Timeline(
                new KeyFrame(Duration.millis(100),
                        event -> {
                            treasuresLabel.setText("" + Field.treasures.size());
                        })
        );
        treasureTL.setCycleCount(Timeline.INDEFINITE);
        treasureTL.play();
    }

    private void setStepsBar() {
        stepsLabel.setTextFill(Color.BLACK);
        stepsLabel.setTranslateY(-5);
        stepsLabel.setStyle("-fx-font-size: 3em;");
        stepsTL = new Timeline(
                new KeyFrame(Duration.millis(100),
                        event -> {
                            stepsLabel.setText("" + GameLoop.players.get(0).getSteps());
                        })
        );
        stepsTL.setCycleCount(Timeline.INDEFINITE);
        stepsTL.play();
    }

    private void setScoreBar() {
        scoreLabel.setTextFill(Color.BLACK);
        scoreLabel.setTranslateY(-5);
        scoreLabel.setStyle("-fx-font-size: 3em;");
        scoreTL = new Timeline(
                new KeyFrame(Duration.millis(10),
                        event -> {
                            scoreLabel.setText("" + GameLoop.players.get(0).getScore());
                        })
        );
        scoreTL.setCycleCount(Timeline.INDEFINITE);
        scoreTL.play();
    }

    private void setIconSize(ImageView image) {
        image.setFitWidth(40);
        image.setFitHeight(40);
        image.setTranslateY(5);
    }

    private HBox getItemPanel(ImageView image, Label text) {
        HBox itembox = new HBox(5);
        itembox.getChildren().addAll(image, text);
        return itembox;
    }

    public Duration getTime() {
        return time;
    }

    public Label getTreasuresLabel() {
        return treasuresLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    public Timeline getTimer() {
        return timer;
    }

    public Timeline getTreasureTL() {
        return treasureTL;
    }

    public Timeline getScoreTL() {
        return scoreTL;
    }

    public Timeline getStepsTL() {
        return stepsTL;
    }
}


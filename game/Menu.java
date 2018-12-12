package game;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Menu extends Pane {
    private int difficultyGameStatus = 0;
    private boolean resumeGameStatus = false;
    private boolean returnMainMenuStatus = false;
    private boolean startNewGameStatus = false;
    private int tempWidth = Field.X_CELLS, tempHeight = Field.Y_CELLS, tempBonus = Field.BONUS, tempEnemy = Field.ENEMY;
    private ImageView arrowkeysIcon = new ImageView(new Image(getClass().getResourceAsStream("images/arrow_keys.png")));
    private ImageView spacekeyIcon = new ImageView(new Image(getClass().getResourceAsStream("images/space_key.png")));
    private ImageView escapekeyIcon = new ImageView(new Image(getClass().getResourceAsStream("images/escape_key.png")));
    private ImageView treasureIcon = new ImageView(new Image(getClass().getResourceAsStream("images/treasure_field.png")));
    private ImageView trapIcon = new ImageView(new Image(getClass().getResourceAsStream("images/trap_field.png")));
    private ImageView search1Icon = new ImageView(new Image(getClass().getResourceAsStream("images/searcharea1_field.png")));
    private ImageView search2Icon = new ImageView(new Image(getClass().getResourceAsStream("images/searcharea2_field.png")));
    private ImageView dangerIcon = new ImageView(new Image(getClass().getResourceAsStream("images/dangerzone_field.png")));
    private ImageView cleanIcon = new ImageView(new Image(getClass().getResourceAsStream("images/cleancell_field.png")));
    private ImageView hiddenIcon = new ImageView(new Image(getClass().getResourceAsStream("images/hiddencell_field.png")));
    private ImageView winLabel = new ImageView(new Image(getClass().getResourceAsStream("images/winlabel.png")));
    private ImageView loseLabel = new ImageView(new Image(getClass().getResourceAsStream("images/loselabel.png")));

    public Scene getMainMenu() {
        Pane mainLayout = new Pane();
        Image image = new Image(getClass().getResourceAsStream("images/mainmenu.png"));
        ImageView img = new ImageView(image);
        img.setFitWidth(600);
        img.setFitHeight(600);
        mainLayout.getChildren().add(img);
        // Main Menu
        MenuItem newGame = new MenuItem("NEW GAME");
        MenuItem options = new MenuItem("OPTIONS");
        MenuItem gameEditor = new MenuItem("MAP EDITOR");
        MenuItem exitGame = new MenuItem("EXIT");
        SubMenu mainMenu = new SubMenu(newGame, options, gameEditor, exitGame);
        // Options menu
        MenuItem keys = new MenuItem("CONTROL KEYS");
        MenuItem help = new MenuItem("HELP");
        MenuItem optionsBack = new MenuItem("BACK");
        SubMenu optionsMenu = new SubMenu(keys, help, optionsBack);
        // New game menu
        MenuItem easyGame = new MenuItem("EASY");
        MenuItem normalGame = new MenuItem("NORMAL");
        MenuItem hardGame = new MenuItem("HARD");
        MenuItem gameBack = new MenuItem("BACK");
        SubMenu newGameMenu = new SubMenu(easyGame, normalGame, hardGame, gameBack);
        // Map editor menu
        HBox widthPanel = editorOption("WIDTH", Field.X_CELLS, "");
        HBox heightPanel = editorOption("HEIGHT", Field.Y_CELLS, "");
        HBox treasurePanel = editorOption("TREASURES", Field.BONUS, "%");
        HBox trapPanel = editorOption("TRAPS", Field.ENEMY, "%");
        MenuItem apply = new MenuItem("APPLY");
        MenuItem editorBack = new MenuItem("BACK");
        SubMenu editorMenu = new SubMenu(widthPanel, heightPanel, treasurePanel, trapPanel, apply, editorBack);
        // Control keys menu
        arrowkeysIcon.setFitWidth(150);
        arrowkeysIcon.setFitHeight(100);
        spacekeyIcon.setFitWidth(150);
        spacekeyIcon.setFitHeight(50);
        escapekeyIcon.setFitWidth(150);
        escapekeyIcon.setFitHeight(50);
        HBox arrowPanel = keyOption("MOVEMENT KEYS", arrowkeysIcon);
        HBox spacePanel = keyOption("PICK UP TREASURE", spacekeyIcon);
        HBox escapePanel = keyOption("PAUSE GAME", escapekeyIcon);
        arrowPanel.setTranslateY(10);
        escapePanel.setTranslateY(20);
        spacePanel.setTranslateY(30);
        MenuItem keysBack = new MenuItem("BACK");
        keysBack.setTranslateY(50);
        SubMenu keysMenu = new SubMenu(keysBack, arrowPanel, escapePanel, spacePanel);
        keysMenu.setLayoutY(20);
        // Help menu
        HBox treasurecell = helpOption("TREASURE", treasureIcon);
        HBox search1cell = helpOption("ONE STEP TO TREASURE", search1Icon);
        HBox search2cell = helpOption("TWO STEPS TO TREASURE", search2Icon);
        HBox trapcell = helpOption("TRAP", trapIcon);
        HBox dangercell = helpOption("ONE STEP TO TRAP", dangerIcon);
        HBox cleancell = helpOption("CLEAN CELL", cleanIcon);
        HBox hiddencell = helpOption("HIDDEN CELL", hiddenIcon);
        MenuItem helpBack = new MenuItem("BACK");
        SubMenu helpMenu = new SubMenu(helpBack, treasurecell, search1cell, search2cell, trapcell, dangercell,
                cleancell, hiddencell);

        MenuBox menuBox = new MenuBox(mainMenu);

        newGame.setOnMouseClicked(event -> menuBox.setSubMenu(newGameMenu));
        easyGame.setOnMouseClicked(event -> difficultyGameStatus = 1);
        normalGame.setOnMouseClicked(event -> difficultyGameStatus = 2);
        hardGame.setOnMouseClicked(event -> difficultyGameStatus = 3);
        gameBack.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));

        options.setOnMouseClicked(event -> menuBox.setSubMenu(optionsMenu));
        keys.setOnMouseClicked(event -> menuBox.setSubMenu(keysMenu));
        keysBack.setOnMouseClicked(event -> menuBox.setSubMenu(optionsMenu));
        help.setOnMouseClicked(event -> menuBox.setSubMenu(helpMenu));
        helpBack.setOnMouseClicked(event -> menuBox.setSubMenu(optionsMenu));
        optionsBack.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));

        exitGame.setOnMouseClicked(event -> System.exit(0));

        gameEditor.setOnMouseClicked(event -> menuBox.setSubMenu(editorMenu));
        apply.setOnMouseClicked(event -> {
            displayChangesSaved().show();
            Field.setW(tempWidth * Cell.CELL_SIZE);
            Field.setH(tempHeight * Cell.CELL_SIZE);
            Field.setxCells(tempWidth);
            Field.setyCells(tempHeight);
            Field.setGrid(new Cell[tempWidth][tempHeight]);
            Field.setHiddenGrid(new Cell[tempWidth][tempHeight]);
            Field.setBONUS(tempBonus);
            Field.setENEMY(tempEnemy);
        });
        editorBack.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));

        mainLayout.getChildren().addAll(menuBox);

        Scene scene = new Scene(mainLayout, 600, 600);
        return scene;
    }

    public Scene getEndGameMenu(Boolean winStatus) {
        Label winState = new Label();
        if (winStatus)
            winState.setGraphic(winLabel);
        else winState.setGraphic(loseLabel);
        winState.setTranslateY(20);

        Pane endGameLayout = new Pane();
        endGameLayout.setPrefSize(340, 350);
        endGameLayout.setStyle("-fx-background-color: #DEB887");
        endGameLayout.setVisible(true);

        MenuItem againNewGame = new MenuItem("NEW GAME");
        againNewGame.setTranslateX(20);
        againNewGame.setTranslateY(140);
        MenuItem returnMainMenu = new MenuItem("MAIN MENU");
        returnMainMenu.setTranslateX(20);
        returnMainMenu.setTranslateY(210);
        MenuItem endGame = new MenuItem("EXIT GAME");
        endGame.setTranslateX(20);
        endGame.setTranslateY(280);

        againNewGame.setOnMouseClicked(event -> startNewGameStatus = true);
        returnMainMenu.setOnMouseClicked(event -> returnMainMenuStatus = true);
        endGame.setOnMouseClicked(event -> System.exit(0));
        endGameLayout.getChildren().addAll(winState, againNewGame, returnMainMenu, endGame);

        Scene scene = new Scene(endGameLayout, 340, 350);
        return scene;
    }

    public Stage endGameWindow(Boolean winStatus) {
        Scene scene = getEndGameMenu(winStatus);
        Stage endGameWindow = new Stage();
        endGameWindow.initModality(Modality.APPLICATION_MODAL);
        endGameWindow.setMinHeight(340);
        endGameWindow.setMinWidth(300);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        endGameWindow.setX(primaryScreenBounds.getWidth() / 2 - 340 / 2);
        endGameWindow.setY(primaryScreenBounds.getHeight() / 2 - 350 / 2);
        endGameWindow.initStyle(StageStyle.UNDECORATED);
        endGameWindow.setScene(scene);
        return endGameWindow;
    }

    public Scene getPauseMenu() {
        Pane pauseGameLayout = new Pane();
        pauseGameLayout.setPrefSize(340, 300);
        pauseGameLayout.setStyle("-fx-background-color: #DEB887");
        pauseGameLayout.setVisible(true);

        Label pauseText = new Label("GAME PAUSED");
        pauseText.setStyle("-fx-font-size: 3em;");
        pauseText.setTranslateX(30);
        pauseText.setTranslateY(50);

        MenuItem resumeGame = new MenuItem("RESUME GAME");
        resumeGame.setTranslateX(20);
        resumeGame.setTranslateY(155);
        MenuItem endGame = new MenuItem("EXIT GAME");
        endGame.setTranslateX(20);
        endGame.setTranslateY(230);

        resumeGame.setOnMouseClicked(event -> resumeGameStatus = true);
        endGame.setOnMouseClicked(event -> System.exit(0));
        pauseGameLayout.getChildren().addAll(resumeGame, endGame, pauseText);

        Scene scene = new Scene(pauseGameLayout, 340, 300);
        return scene;
    }

    public Stage pauseGameWindow() {
        Stage pauseGameWindow = new Stage();
        pauseGameWindow.initModality(Modality.APPLICATION_MODAL);
        pauseGameWindow.setMinHeight(300);
        pauseGameWindow.setMinWidth(340);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        pauseGameWindow.setX(primaryScreenBounds.getWidth() / 2 - 340 / 2);
        pauseGameWindow.setY(primaryScreenBounds.getHeight() / 2 - 300 / 2);
        pauseGameWindow.initStyle(StageStyle.UNDECORATED);
        return pauseGameWindow;
    }

    // Map editor menu string
    private HBox editorOption(String name, int value, String sign) {
        HBox itembox = new HBox(10);
        itembox.setAlignment(Pos.CENTER_LEFT);
        Text text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Courier", FontWeight.BOLD, 30));
        MenuItem labeltext = new MenuItem(name, 30);
        MenuItem label = new MenuItem(value, sign);
        MenuItem minus = new MenuItem(false);
        MenuItem plus = new MenuItem(true);
        int difference, minSize, maxSize;
        if (sign == "%") {
            minSize = 1;
            maxSize = 15;
            difference = 1;
        } else {
            minSize = 14;
            maxSize = 40;
            difference = 1;
        }
        minus.setOnMouseClicked(event -> {
            if (label.getValue() > minSize) {
                int tempValue = label.getValue() - difference;
                label.setValue(tempValue);
                setOption(name, tempValue);
            }
        });
        plus.setOnMousePressed(event -> {
            if (label.getValue() < maxSize) {
                int tempValue = label.getValue() + difference;
                label.setValue(tempValue);
                setOption(name, tempValue);
            }
        });
        itembox.getChildren().addAll(labeltext, label, minus, plus);
        return itembox;
    }

    // Control keys menu string
    private HBox keyOption(String lable, ImageView image) {
        HBox itembox = new HBox(40);
        itembox.setAlignment(Pos.BOTTOM_LEFT);
        Text text = new Text(lable);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Courier", FontWeight.BOLD, 25));
        text.setStroke(Color.BLACK);
        text.setTranslateY(-10);
        itembox.getChildren().addAll(image, text);
        return itembox;
    }

    // Help menu string
    private HBox helpOption(String lable, ImageView image) {
        HBox itembox = new HBox(40);
        itembox.setAlignment(Pos.CENTER_LEFT);
        Text text = new Text(lable);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Courier", FontWeight.BOLD, 25));
        text.setStroke(Color.BLACK);
        itembox.getChildren().addAll(image, text);
        return itembox;
    }

    // Class to create menu items
    private static class MenuItem extends StackPane {
        private Text text;
        private Rectangle bg;
        private int value;

        public MenuItem(String name) {
            bg = new Rectangle(300, 50, Color.WHITE);
            bg.setOpacity(0.5);

            text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Courier", FontWeight.BOLD, 25));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
            FillTransition anim = new FillTransition(Duration.seconds(0.5), bg);
            setOnMouseEntered(event -> {
                anim.setFromValue(Color.DARKGRAY);
                anim.setToValue(Color.GRAY);
                anim.setCycleCount(Animation.INDEFINITE);
                anim.setAutoReverse(true);
                anim.play();
            });
            setOnMouseExited(event -> {
                anim.stop();
                bg.setFill(Color.WHITE);
            });
        }

        // [Editor menu] item for button [-] and [+]
        public MenuItem(boolean status) {
            String statusSign = status ? "+" : "-";
            bg = new Rectangle(30, 30, Color.WHITE);
            bg.setOpacity(0.5);

            text = new Text(statusSign);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Courier", FontWeight.BOLD, 28));
            text.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
            FillTransition anim = new FillTransition(Duration.seconds(0.5), bg);
            setOnMouseEntered(event -> {
                anim.setFromValue(Color.DARKGRAY);
                anim.setToValue(Color.GRAY);
                anim.setCycleCount(Animation.INDEFINITE);
                anim.setAutoReverse(true);
                anim.play();
            });
            setOnMouseExited(event -> {
                anim.stop();
                bg.setFill(Color.WHITE);
            });
        }

        // [Editor menu] item for label showing current option
        public MenuItem(int value, String sign) {
            bg = new Rectangle(120, 30, Color.WHITE);
            bg.setOpacity(0.5);

            this.value = value;
            text = new Text(this.value + sign);
            Timeline textTL = new Timeline(
                    new KeyFrame(Duration.millis(100),
                            event -> {
                                text.setText(this.value + sign);
                            })
            );
            textTL.setCycleCount(Timeline.INDEFINITE);
            textTL.play();
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Courier", FontWeight.BOLD, 23));
            text.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
        }

        // [Editor menu] item for name of option
        public MenuItem(String name, int fontsize) {
            bg = new Rectangle(180, 30, Color.WHITE);
            bg.setOpacity(0.0);

            text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Courier", FontWeight.BOLD, fontsize));
            text.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(bg, text);
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    // Class for storing menu items
    private static class MenuBox extends Pane {
        static SubMenu subMenu;

        public MenuBox(SubMenu subMenu) {
            MenuBox.subMenu = subMenu;
            setVisible(true);
            Rectangle bg = new Rectangle(600, 600, Color.LIGHTYELLOW);
            bg.setOpacity(0.3);
            getChildren().addAll(bg, subMenu);
        }

        // Set the menu
        public void setSubMenu(SubMenu subMenu) {
            getChildren().remove(MenuBox.subMenu);
            MenuBox.subMenu = subMenu;
            getChildren().add(subMenu);
        }
    }

    // Class for layout menu items
    private static class SubMenu extends VBox {
        public SubMenu(MenuItem... items) {
            setSpacing(20);
            setTranslateX(150);
            setTranslateY(250);
            for (MenuItem item : items) {
                getChildren().addAll(item);
            }
        }

        // [Editor menu] create editorMap menu
        public SubMenu(HBox box1, HBox box2, HBox box3, HBox box4, MenuItem... items) {
            setSpacing(10);
            setTranslateX(100);
            setTranslateY(250);
            getChildren().addAll(box1, box2, box3, box4);
            for (MenuItem item : items) {
                getChildren().addAll(item);
            }
        }

        // [Options menu] create control keys menu and info menu
        public SubMenu(MenuItem item, HBox... hBoxes) {
            setSpacing(5);
            setTranslateX(100);
            setTranslateY(200);
            for (HBox box : hBoxes) {
                getChildren().addAll(box);
            }
            getChildren().add(item);
        }
    }

    // Set options for editor
    private void setOption(String name, int value) {
        switch (name) {
            case "WIDTH":
                setTempWidth(value);
                break;
            case "HEIGHT":
                setTempHeight(value);
                break;
            case "TREASURES":
                setTempBonus(value);
                break;
            case "TRAPS":
                setTempEnemy(value);
                break;
        }
    }

    // Popup menu when user apply editor options
    private static Stage displayChangesSaved() {
        Stage popupWindow = new Stage();
        popupWindow.initStyle(StageStyle.TRANSPARENT);
        popupWindow.setMinWidth(250);
        popupWindow.setMinHeight(50);

        Text text = new Text("CHANGES SAVED");
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        text.setFont(Font.font("Courier", FontWeight.BOLD, 25));

        StackPane layout = new StackPane();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(text);
        layout.setStyle("-fx-background-color: transparent ;");
        Scene scene = new Scene(layout, 250, 50, Color.TRANSPARENT);
        popupWindow.setScene(scene);
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> popupWindow.close());
        delay.play();
        return popupWindow;
    }

    public int getDifficultyGameStatus() {
        return difficultyGameStatus;
    }

    public boolean isResumeGameStatus() {
        return resumeGameStatus;
    }

    public void setResumeGameStatus(boolean resumeGameStatus) {
        this.resumeGameStatus = resumeGameStatus;
    }

    public boolean isReturnMainMenuStatus() {
        return returnMainMenuStatus;
    }

    public void setReturnMainMenuStatus(boolean returnMainMenuStatus) {
        this.returnMainMenuStatus = returnMainMenuStatus;
    }

    public boolean isStartNewGameStatus() {
        return startNewGameStatus;
    }

    public void setStartNewGameStatus(boolean startNewGameStatus) {
        this.startNewGameStatus = startNewGameStatus;
    }

    public void setTempWidth(int tempWidth) {
        this.tempWidth = tempWidth;
    }

    public void setTempHeight(int tempHeight) {
        this.tempHeight = tempHeight;
    }

    public void setTempBonus(int tempBonus) {
        this.tempBonus = tempBonus;
    }

    public void setTempEnemy(int tempEnemy) {
        this.tempEnemy = tempEnemy;
    }
}


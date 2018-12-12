package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static game.Cell.CELL_SIZE;
import static game.Field.*;

public class Character extends Pane {
    private ImageView imageView;
    private int count = 3;
    private int columns = 3;
    private int offsetX = 0;
    private int offsetY = 0;
    private int width = 38;
    private int height = 38;
    private int score = 0;
    private int steps = 0;
    private Cell removeTreasure = null;
    private boolean paused = false;

    private SpriteAnimation animation;

    public Character() {
        this.setCharacter();
        this.cleanMap();
        this.imageView = new ImageView(new Image(getClass().getResourceAsStream("images/sprite_sheet.png")));
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(imageView, Duration.millis(100), count, columns,
                offsetX, offsetY, width, height);
        getChildren().addAll(imageView);
    }

    // Set character on the field
    private void setCharacter() {
        this.setTranslateX(this.getTranslateX() + 1);
        int randomCoordY;
        while (true){
            randomCoordY = (int) Math.floor(Math.random() * Y_CELLS);
            if (grid[0][randomCoordY].getCellType() != Treasure.CELLTYPE.TREASURE
                    && grid[0][randomCoordY].getCellType() != Trap.CELLTYPE.TRAP) {
                randomCoordY *= CELL_SIZE;
                this.setTranslateY(this.getTranslateY() + randomCoordY + 1);
                break;
            } else continue;
        }
    }

    // Set the control keys
    protected void addKeyHandler(KeyEvent ke) {
        KeyCode keyCode = ke.getCode();
        if (keyCode.equals(KeyCode.UP) && this.getTranslateY() > 0 + CELL_SIZE) {
            this.animation.play();
            this.animation.setOffsetY(120);
            isFindTrap();
            moveY(-1);
        } else if (keyCode.equals(KeyCode.DOWN) && this.getTranslateY() < H - CELL_SIZE) {
            this.animation.play();
            this.animation.setOffsetY(0);
            isFindTrap();
            moveY(1);
        } else if (keyCode.equals(KeyCode.LEFT) && this.getTranslateX() > 0 + CELL_SIZE) {
            this.animation.play();
            this.animation.setOffsetY(40);
            isFindTrap();
            moveX(-1);
        } else if (keyCode.equals(KeyCode.RIGHT) && this.getTranslateX() < W - CELL_SIZE) {
            this.animation.play();
            this.animation.setOffsetY(80);
            isFindTrap();
            moveX(1);
        } else if (keyCode.equals(KeyCode.SPACE)) {
            isFindTreasure();
        } else if (keyCode.equals(KeyCode.ESCAPE)) {
            this.paused = true;
        } else if (keyCode.equals(KeyCode.F7)) {                // cheat-mod for presentation
            showFullMap();
    }
    }

    protected void addNoKeyHandler() {
        this.animation.stop();
    }

    private void moveX(int x) {
        boolean right = x > 0 ? true : false;
        if (right) this.setTranslateX(this.getTranslateX() + CELL_SIZE);
        else this.setTranslateX(this.getTranslateX() - CELL_SIZE);
        cleanMap();
    }

    private void moveY(int y) {
        boolean down = y > 0 ? true : false;
        if (down) this.setTranslateY(this.getTranslateY() + CELL_SIZE);
        else this.setTranslateY(this.getTranslateY() - CELL_SIZE);
        cleanMap();
    }

    private void cleanMap() {
        for (int y = 0; y < Y_CELLS; y++) {
            for (int x = 0; x < X_CELLS; x++) {
                if (this.getBoundsInParent().intersects(hiddenGrid[x][y].getBoundsInParent())) {
                    steps++;
                    hiddenGrid[x][y].openCell(hiddenGrid[x][y]);
                }
            }
        }
    }

    // [Cheat mod] for presentation
    private void showFullMap() {
        for (int y = 0; y < Y_CELLS; y++) {
            for (int x = 0; x < X_CELLS; x++) {
                    hiddenGrid[x][y].openCell(hiddenGrid[x][y]);
            }
        }
    }

    private void isFindTreasure() {
        Field.treasures.forEach((Cell cell) -> {
            if (this.getBoundsInParent().intersects(cell.getBoundsInParent())) {
                cell.openCell(cell);
                removeTreasure = cell;
                score++;
            }
        });
        Field.treasures.remove(removeTreasure);
    }

    private void isFindTrap() {
        Field.traps.forEach((Cell cell) -> {
            if (this.getBoundsInParent().intersects(cell.getBoundsInParent())) {
                score -= GameLoop.SCORE_PENALTY;
            }
        });
    }

    public SpriteAnimation getAnimation() {
        return animation;
    }

    public int getScore() {
        return score;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}

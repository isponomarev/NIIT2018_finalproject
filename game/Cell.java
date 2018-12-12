package game;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static game.Field.*;

public class Cell extends StackPane {
    enum CELLTYPE {
        HIDDEN,                                     // cell in the fog
        CLEAN,                                      // passed cell
        SEARCH_AREA1,                               // 1 step to treasure
        SEARCH_AREA2,                               // 2 step to treasure
        TREASURE,                                   // treasure
        DANGER_AREA1,                               // 1 step to trap
        TRAP;                                       // trap
    }
    public static final int CELL_SIZE = 40;         // size of cell

    private Image treasureIcon = new Image(getClass().getResourceAsStream("images/treasure_field.png"));
    private Image trapIcon = new Image(getClass().getResourceAsStream("images/trap_field.png"));

    private CELLTYPE cellType;                     // celltype
    private int x, y;                              // coordinates
    private Rectangle border;                      // border of cell

    public Cell(CELLTYPE cellType, int x, int y) {
        this.cellType = cellType;
        this.x = x;
        this.y = y;
        setTranslateX(x * CELL_SIZE);                                           // coordinate scaling
        setTranslateY(y * CELL_SIZE);
        border = new Rectangle(CELL_SIZE - 1, CELL_SIZE - 1);        // create visual cell
        border.setStroke(Color.BLACK);                                          // paint stroke
        colorRect(cellType);
        getChildren().addAll(border);
    }

    // Cell opening method
    public void openCell(Cell cell) {
        if (cell.getCellType() == CELLTYPE.TREASURE) {
            ArrayList<Cell> area = treasureWithArea.get(cell);
            cell.cleanCell();
            for (int i = 0; i < area.size(); i++) {
                area.get(i).cleanCell();
            }
            return;
        }
        cell.cleanCell();
    }

    // Cell cleaning method
    private void cleanCell() {
        grid[this.getX()][this.getY()].setCellType(CELLTYPE.CLEAN);
        this.setVisible(false);
    }

    // Method to change color of cell depending of type
    private void colorRect(CELLTYPE cellType) {
        switch (cellType) {
            case HIDDEN:
                border.setFill(Color.GRAY);
                break;
            case CLEAN:
                border.setFill(Color.LIGHTGRAY);
                break;
            case SEARCH_AREA1:
                border.setFill(Color.GREEN);
                break;
            case SEARCH_AREA2:
                border.setFill(Color.LIGHTGREEN);
                break;
            case TREASURE:
                //border.setFill(Color.YELLOW);
                border.setFill(new ImagePattern(treasureIcon));
                break;
            case DANGER_AREA1:
                border.setFill(Color.LIGHTCORAL);
                break;
            case TRAP:
                //border.setFill(Color.DARKRED);
                border.setFill(new ImagePattern(trapIcon));
                break;

        }
    }

    public void setCellType(CELLTYPE cellType) {
        this.cellType = cellType;
    }

    public CELLTYPE getCellType() {
        return cellType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        String info = cellType + " x:" + x + " y:" + y + "\n";
        return info;
    }
}

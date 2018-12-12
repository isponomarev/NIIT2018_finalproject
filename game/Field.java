package game;

import game.Cell.*;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static game.Trap.*;
import static game.Treasure.*;

public class Field extends Pane {
    public static int W = 800;                                      // width of battlefield
    public static int H = 600;                                      // height of battlefield

    public static int X_CELLS = W / Cell.CELL_SIZE;                 // cells horizontally
    public static int Y_CELLS = H / Cell.CELL_SIZE;                 // cells vertically

    public static Cell[][] grid = new Cell[X_CELLS][Y_CELLS];       // grid of cells
    public static Cell[][] hiddenGrid = new Cell[X_CELLS][Y_CELLS]; // grid of hidden cells

    public static int BONUS = 5;                                    // percent of treasuares
    public static int ENEMY = 5;                                    // percent of traps

    public static HashSet<Treasure> treasures = new HashSet<>();    // create base of treasures
    public static HashSet<Trap> traps = new HashSet<>();            // create base of traps

    public static HashMap<Treasure, ArrayList> treasureWithArea = new HashMap<>();   // create base of treasures area
    public static HashMap<Trap, ArrayList> trapWithArea = new HashMap<>();           // create base of traps area

    private Pane mainLayout;
    private Pane battleField;


    // Create main field of game
    public Field() {
        mainLayout = new Pane();
        battleField = new Pane();
        battleField.setPrefSize(W, H);
        mainLayout = createBattleField();
    }

    private Pane createBattleField(){
        // Create clean field
        for (int y = 0; y < Y_CELLS; y++) {
            for (int x = 0; x < X_CELLS; x++) {
                Cell cell = new Cell(CELLTYPE.CLEAN, x, y);
                grid[x][y] = cell;
                grid[x][y].setVisible(true);
                battleField.getChildren().add(cell);
            }
        }

        createDictTresuareWithArea();

        // Init treasure cell
        treasureWithArea.forEach((key, value) -> {
            grid[key.getX()][key.getY()].setCellType(CELLTYPE.TREASURE);
        });

        createDictTrapWithArea();

        // Set search area 2 (cell with two step up to treasure) on the field
        treasureWithArea.forEach((key, value) -> {
            ArrayList<Cell> area = treasureWithArea.get(key);
            for (int i = 0; i < area.size(); i++) {
                if (area.get(i).getCellType() == CELLTYPE.SEARCH_AREA2)
                    battleField.getChildren().add(area.get(i));
            }
        });

        // Set search area 1 (cell with one step up to treasure) on the field
        treasureWithArea.forEach((key, value) -> {
            ArrayList<Cell> area = treasureWithArea.get(key);
            for (int i = 0; i < area.size(); i++) {
                if (area.get(i).getCellType() == CELLTYPE.SEARCH_AREA1)
                    battleField.getChildren().add(area.get(i));
            }
        });


        // Set danger area (cell with one step up to trap) on the field
        trapWithArea.forEach((key, value) -> {
            ArrayList<Cell> dangerZone = trapWithArea.get(key);
            for (int i = 0; i < dangerZone.size(); i++) {
                    battleField.getChildren().add(dangerZone.get(i));
            }
        });

        // Set traps on the field
        trapWithArea.forEach((key, value) -> {
            battleField.getChildren().addAll(key);
        });

        // Set treasures on the field
        treasureWithArea.forEach((key, value) -> {
            battleField.getChildren().addAll(key);
        });

        // Set fog of war on the field
        for (int y = 0; y < Y_CELLS; y++) {
            for (int x = 0; x < X_CELLS; x++) {
                Cell cell = new Cell(CELLTYPE.HIDDEN, x, y);
                hiddenGrid[x][y] = cell;
                battleField.getChildren().add(cell);
            }
        }
        mainLayout.getChildren().add(battleField);
        return mainLayout;
    }


    // Create hashset of cells with treasure
    private HashSet<Treasure> setTreasures() {
        int treasureCount = (int) Math.floor(X_CELLS * Y_CELLS * BONUS/100);
        while (treasures.size() != treasureCount) {
            Treasure treasure = randomTreasureGenerator();
            treasures.add(treasure);
        }
        return treasures;
    }

    // Create hashset of cells with traps
    private HashSet<Trap> setTraps() {
        int trapCount = (int) Math.floor(X_CELLS * Y_CELLS * ENEMY/100);
        while (traps.size() != trapCount) {
            Trap trap = randomTrapGenerator();
            if (grid[trap.getX()][trap.getY()].getCellType() == CELLTYPE.TREASURE
                    || grid[trap.getX()][trap.getY()].getCellType() == CELLTYPE.SEARCH_AREA1)
                continue;
            else traps.add(trap);
        }
        return traps;
    }

    // Creating HashMap for storing treasures and their search area
    private void createDictTresuareWithArea() {
        HashSet<Treasure> treasures = setTreasures();
        for (Treasure item : treasures) {
            ArrayList<Cell> searchAreaOne = item.setTresuareAreaOne(item);
            for (Cell one : searchAreaOne) {
                grid[one.getX()][one.getY()].setCellType(CELLTYPE.SEARCH_AREA1);
            }
            treasureWithArea.put(item, searchAreaOne);
            ArrayList<Cell> searchAreaTwo = item.setTresuareAreaTwo(item);
            for (Cell two : searchAreaTwo) {
                grid[two.getX()][two.getY()].setCellType(CELLTYPE.SEARCH_AREA2);
            }
            treasureWithArea.get(item).addAll(searchAreaTwo);
        }
    }

    // Creating HashMap for storing traps and their search area
    private void createDictTrapWithArea() {
        HashSet<Trap> traps = setTraps();
        for (Trap item : traps) {
            grid[item.getX()][item.getY()].setCellType(CELLTYPE.TRAP);
            ArrayList<Cell> dangerArea = Trap.setDangerArea(item);
            for (Cell one : dangerArea) {
                grid[one.getX()][one.getY()].setCellType(CELLTYPE.DANGER_AREA1);
            }
            trapWithArea.put(item, dangerArea);
        }
    }

    public Pane getMainLayout() {
        return mainLayout;
    }

    public static void setW(int w) {
        W = w;
    }

    public static void setH(int h) {
        H = h;
    }

    public static void setxCells(int xCells) {
        X_CELLS = xCells;
    }

    public static void setyCells(int yCells) {
        Y_CELLS = yCells;
    }

    public static void setGrid(Cell[][] grid) {
        Field.grid = grid;
    }

    public static void setHiddenGrid(Cell[][] hiddenGrid) {
        Field.hiddenGrid = hiddenGrid;
    }

    public static void setBONUS(int BONUS) {
        Field.BONUS = BONUS;
    }

    public static void setENEMY(int ENEMY) {
        Field.ENEMY = ENEMY;
    }
}
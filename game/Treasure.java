package game;

import java.util.ArrayList;
import java.util.Objects;

import static game.Field.*;

public class Treasure extends Cell {
    public Treasure(int x, int y) {
        super(CELLTYPE.TREASURE, x, y);
    }

    // Create treasure with random coordinates
    public static Treasure randomTreasureGenerator(){
        int x = (int) Math.floor(Math.random() * X_CELLS);
        int y = (int) Math.floor(Math.random() * Y_CELLS);
        Treasure randCell = new Treasure(x, y);
        return randCell;
    }

    // Create search area for cell with one step up to treasure
    public ArrayList<Cell> setTresuareAreaOne(Cell cell) {
        ArrayList<Cell> searchAreaOne = new ArrayList<>();
        int[] areaOne = new int[]{
                      -1, 0,
                0, -1,      0, 1,
                       1, 0
        };

        for (int i = 0; i < areaOne.length; i++) {
            int dx = areaOne[i];
            int dy = areaOne[++i];
            int newX = cell.getX() + dx;
            int newY = cell.getY() + dy;
            if (newX >= 0 && newX < X_CELLS && newY >= 0 && newY < Y_CELLS) {
                Cell newCell = new Cell(CELLTYPE.SEARCH_AREA1, newX, newY);
                searchAreaOne.add(newCell);
            }
        }
        return searchAreaOne;
    }

    // Create search area for cell with two step up to treasure
    public ArrayList<Cell> setTresuareAreaTwo(Cell cell) {
        ArrayList<Cell> searchAreaTwo = new ArrayList<>();
        int[] areaTwo = new int[]{
                       -2, 0,
                -1, -1,      -1, 1,
        0, -2,                      0, 2,
                1, -1,       1, 1,
                        2, 0
        };

        for (int i = 0; i < areaTwo.length; i++) {
            int dx = areaTwo[i];
            int dy = areaTwo[++i];
            int newX = cell.getX() + dx;
            int newY = cell.getY() + dy;
            if (newX >= 0 && newX < X_CELLS && newY >= 0 && newY < Y_CELLS) {
                Cell newCell = new Cell(CELLTYPE.SEARCH_AREA2, newX, newY);
                searchAreaTwo.add(newCell);
            }
        }
        return searchAreaTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Treasure)) return false;
        Treasure treasure = (Treasure) o;
        return (this.getX() == treasure.getX() && this.getY() == treasure.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY(), this.getCellType());
    }
}


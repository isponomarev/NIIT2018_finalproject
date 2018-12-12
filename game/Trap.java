package game;

import java.util.ArrayList;
import java.util.Objects;

import static game.Field.*;

public class Trap extends Cell {
    public Trap(int x, int y) {
        super(CELLTYPE.TRAP, x, y);
    }

    // Create trap with random coordinates
    public static Trap randomTrapGenerator(){
        int x = (int) Math.floor(Math.random() * X_CELLS);
        int y = (int) Math.floor(Math.random() * Y_CELLS);
        Trap randCell = new Trap(x, y);
        return randCell;
    }

    // Create danger area for cell with one step up to trap
    protected static ArrayList<Cell> setDangerArea(Cell cell) {
        ArrayList<Cell> dangerArea = new ArrayList<>();
        int[] dangerZone = new int[]{
                      -1, 0,
                0, -1,      0, 1,
                       1, 0
        };

        for (int i = 0; i < dangerZone.length; i++) {
            int dx = dangerZone[i];
            int dy = dangerZone[++i];
            int newX = cell.getX() + dx;
            int newY = cell.getY() + dy;
            if (newX >= 0 && newX < X_CELLS && newY >= 0 && newY < Y_CELLS) {
                Cell newCell = new Cell(CELLTYPE.DANGER_AREA1, newX, newY);
                dangerArea.add(newCell);
            }
        }
        return dangerArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Trap)) return false;
        Trap trap = (Trap) o;
        return (this.getX() == trap.getX() && this.getY() == trap.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY(), this.getCellType());
    }
}

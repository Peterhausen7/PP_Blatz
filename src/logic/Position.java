package logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Position {

    int c;
    int r;

    public Position(int c, int r) {
        this.c = c;
        this.r = r;
    }

    /**
     * Checks if the Position is a corner of a x*x field
     * @param fieldSize - row/col size of a field
     * @return true if corner
     */
    public boolean notCorner(int fieldSize) {
        return ((c != 0) || ((r != 0) && (r != (fieldSize - 1))))
                && ((c != (fieldSize - 1)) || ((r != 0) && (r != (fieldSize - 1))));
    }

    /**
     * Checks if the Position is a edge of a x*x field
     * @param fieldSize - row/col size of a field
     * @return true if edge (not including corners)
     */
    public boolean isEdge(int fieldSize) {
        boolean result = false;
        if (this.notCorner(fieldSize)) {
            result = (c == 0) || (r == 0) || (r == (fieldSize - 1)) || (c == (fieldSize - 1));
        }
        return result;
    }

    public boolean isValidPos(int fieldSize) {
        return (c >= 0) && (r >= 0) && (c < fieldSize) && (r < fieldSize);
    }

    public boolean isPushablePos() {
        return ((c % 2) != 0) || ((r % 2) != 0);
    }

    /**
     * Returns the neighbours of a Position in clockwise order, starting with "9 o'clock"
     * so: [0] = left, [1] = top, [2] = right and [3] = bottom
     * If there is no neighbour for a side (corner or edge position), the index will contain null instead
     * @param fieldSize - row/col size of a field
     * @return Position array - size of 4 - with neighbours in clockwise order, starting with "9 o'clock"
     */
    public Map<Direction, Position> getNeighbours(int fieldSize) {
        Map<Direction, Position> result = new HashMap<>();
        result.put(Direction.LEFT, (c != 0) ? new Position(c - 1, r) : null);
        result.put(Direction.UP, (r != 0) ? new Position(c, r - 1) : null);
        result.put(Direction.RIGHT, (c != fieldSize - 1) ? new Position(c + 1, r) : null);
        result.put(Direction.DOWN, (r != fieldSize - 1) ? new Position(c, r + 1) : null);
       // Position[] result;
      // result = new Position[4];
       // result[0] = (c != 0) ? new Position(c - 1, r) : null;
      //  result[1] = (r != 0) ? new Position(c, r - 1) : null;
       // result[2] = (c != fieldSize - 1) ? new Position(c + 1, r) : null;
       // result[3] = (r != fieldSize - 1) ? new Position(c, r + 1) : null;

        return result;
    }

    public int getCol() {
        return c;
    }

    public int getRow() {
        return r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return c == position.c && r == position.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, r);
    }

    @Override
    public String toString() {
        return "Position{" +
                "c=" + c +
                ", r=" + r +
                '}';
    }
}

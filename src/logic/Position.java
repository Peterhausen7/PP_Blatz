package logic;

import java.util.*;

/**
 * Class representing a Position col and row
 */
public class Position {

    /** Amount of field sides this position supports (Only square shaped) */
    private final static int SIDES = 4;
    /** column of this position */
    private int c;
    /** row of this position */
    private int r;

    /**
     * Constructor creating a position
     * @param c - column of the position
     * @param r - row of the position
     */
    Position(int c, int r) {
        this.c = c;
        this.r = r;
    }

    /**
     * Checks if the Position is a corner of a x*x field
     * @param fieldSize - row/col size of a field
     * @return true if corner
     */
    boolean notCorner(int fieldSize) {
        return ((c != 0) || ((r != 0) && (r != (fieldSize - 1))))
                && ((c != (fieldSize - 1)) || ((r != 0) && (r != (fieldSize - 1))));
    }

    /**
     * Checks if the Position is a edge of a x*x field
     * @param fieldSize - row/col size of a field
     * @return true if edge (not including corners)
     */
    boolean isEdge(int fieldSize) {
        boolean result = false;
        if (notCorner(fieldSize)) {
            result = (c == 0) || (r == 0) || (r == (fieldSize - 1)) || (c == (fieldSize - 1));
        }
        return result;
    }

    /**
     * Checks if the position is a valid position of a x*x field
     * @param fieldSize - row/col size of the field
     * @return true if its a valid position
     */
    boolean isValidPos(int fieldSize) {
        return (c >= 0) && (r >= 0) && (c < fieldSize) && (r < fieldSize);
    }

    /**
     * Checks the position is on a pushable row or col of a x*x field
     * @param fieldSize - row/col size of the field
     * @return true if on pushable row/col
     */
    boolean isPushableRowCol(int fieldSize) {
        return notCorner(fieldSize) && ((c % 2) != 0 || (r % 2) != 0);
    }

    /**
     * Checks if the position is a pushable position on a x*x field (edge and on pushable row/col)
     * @param fieldSize - row/col size of the field
     * @return true if pushable pos
     */
    boolean isPushablePos(int fieldSize) {
        return isEdge(fieldSize) && isPushableRowCol(fieldSize);
    }

    /**
     * Checks if this position overlaps col/row with another
     * @param compare - the position to compare with
     * @param fieldSize - row/col size of a field
     * @return
     */
    boolean overlapsRowCol(Position compare, int fieldSize) {
        boolean result = false;
        if (isEdge(fieldSize)) {
            if (c == 0 || c == fieldSize - 1) {
                result = r == compare.r;
            } else if (r == 0) {
                result = c == compare.c;
            }
        } else {
            result = c == compare.c || r == compare.r;
        }
        return result;
    }

    /**
     * Gets all pushable positions of a x*x field
     * @param fieldSize - row/col size of field
     * @return
     */
    static List<Position> getAllPushablePos(int fieldSize) {
        Set<Position> edges = new HashSet<>(fieldSize * SIDES - SIDES);
        for (int col = 0; col < fieldSize; col++) {
            edges.add(new Position(col, 0));
            edges.add(new Position(col, fieldSize - 1));
        }
        for (int row = 0; row < fieldSize; row++) {
            edges.add(new Position(0, row));
            edges.add(new Position(fieldSize - 1, row));
        }
        List<Position> result = new ArrayList<>();
        for (Position pos : edges) {
            if (pos.isPushablePos(fieldSize)) {
                result.add(pos);
            }
        }
        return result;
    }

    /**
     * Returns the neighbours of a Position mapped to a directions.
     * If there is no neighbour for a direction (corner or edge position), the key will contain null instead
     * @param fieldSize - row/col size of a field
     * @return Map, mapping directions to positions
     */
    Map<Direction, Position> getNeighbours(int fieldSize) {
        Map<Direction, Position> result = new HashMap<>();
        result.put(Direction.LEFT, (c != 0) ? new Position(c - 1, r) : null);
        result.put(Direction.UP, (r != 0) ? new Position(c, r - 1) : null);
        result.put(Direction.RIGHT, (c != fieldSize - 1) ? new Position(c + 1, r) : null);
        result.put(Direction.DOWN, (r != fieldSize - 1) ? new Position(c, r + 1) : null);
        return result;
    }

    /**
     * Calculates the distance between 2 positions
     * @param from - position A
     * @param to - position B
     * @return the distance between the 2 positions
     */
    static int distanceBetween(Position from, Position to) {
        return Math.abs(from.getCol() - to.getCol()) + Math.abs(from.getRow() - to.getRow());
    }

    /**
     * Gets the col of this position
     * @return col
     */
    public int getCol() {
        return c;
    }

    /**
     * Gets the row of this position
     * @return row
     */
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

package logic;

import java.util.Objects;

/**
 * A corridor and its properties
 */
public class Corridor {

    /**
     * Corridor type
     */
    private final CorridorType type;

    /**
     * Rotation of corridor
     */
    private Rotation rotation;

    /**
     * Treasure of the corridor
     */
    private Treasure treasure;

    /**
     * Default no rot or treasure
     * @param type - type of corridor
     */
    Corridor(CorridorType type) {
        this.type = type;
        this.rotation = Rotation.NEUTRAL;
    }

    /**
     * Constructor fully specifying a corridor
     * @param type - type of corridor
     * @param rotation - rotation of corridor
     * @param treasure - treasure of corridor
     */
    Corridor(CorridorType type, Rotation rotation, Treasure treasure) {
        this.type = type;
        this.rotation = rotation;
        this.treasure = treasure;
    }

    /**
     * Checks if the corridor with its rotation connects with another corridor at the specified direction.
     *
     * For example: this=T fromCorr=I toDir=UP. While fromCorr/I has a path down,
     * this/T has no path toDir/UP to connect with it, thus false is returned.
     *
     * @param fromCorr - the other corridor
     * @param toDir - the direction to check the connection at
     * @return true if there is a path to the other corridor taking the provided direction
     */
    boolean pathFromExists(Corridor fromCorr, Direction toDir) {
        boolean result = false;
        Direction[] dirs = fromCorr.type.connectsTo(fromCorr.rotation);
        switch(toDir) {
            case UP:
                for (Direction direction : dirs) {
                    result = result || direction == Direction.DOWN;
                }
                break;
            case DOWN:
                for (Direction direction : dirs) {
                    result = result || direction == Direction.UP;
                }
                break;
            case RIGHT:
                for (Direction direction : dirs) {
                    result = result || direction == Direction.LEFT;
                }
                break;
            case LEFT:
                for (Direction direction : dirs) {
                    result = result || direction == Direction.RIGHT;
                }
                break;
        }
        return result;
    }

    /**
     * Checks if corridor has a treasure
     * @return - True if corridor has treasure
     */
    public boolean hasTreasure() {
        return treasure != null;
    }

    /**
     * Remove the treasure from this corridor (sets it to null)
     */
    void removeTreasure() {
        treasure = null;
    }

    /**
     * Sets the rotation of this corridor to rit
     * @param rot - rotation to set to
     */
    void setRotation(Rotation rot) {
        rotation = rot;
    }

    /**
     * Gets the type of this corridor
     * @return - type of this corridor
     */
    public CorridorType getType() {
        return type;
    }

    /**
     * Gets the rotation of this corridor
     * @return - the rotation of this corridor
     */
    public Rotation getRotation() {
        return rotation;
    }

    /**
     * Gets the treasure of this corridor
     * @return - the treasure of this corridor, or null if it has none
     */
    public Treasure getTreasure() {
        return (treasure != null) ? treasure : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corridor corridor = (Corridor) o;
        return rotation == corridor.rotation && treasure == corridor.treasure && type == corridor.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rotation, treasure);
    }

    @Override
    public String toString() {
        return "Corridor{" +
                "type=" + type +
                ", rotation=" + rotation +
                ", treasure=" + treasure +
                '}';
    }
}

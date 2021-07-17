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
     * Rotation of corridor, each int equals 90 degrees:
     * 0..3 = 0/90/180/270 360=0
     */
    private Rotation rotation;

    /**
     * Treasure of the corridor, 0 equals no treasure
     * the are treasures numbered 1-24
     */
    private Treasure treasure;

    /**
     * Default no rot or treasure
     * @param type - type of corridor
     */
    public Corridor(CorridorType type) {
        this.type = type;
        this.rotation = Rotation.NEUTRAL;
        this.treasure = null;
    }

    /**
     * Constructor fully specifying a corridor
     * @param type - type of corridor
     * @param rotation - rotation of corridor
     * @param treasure - treasure of corridor
     */
    public Corridor(CorridorType type, Rotation rotation, Treasure treasure) {
        this.type = type;
        this.rotation = rotation;

        //do we have to catch illegal arguments like wrong rotation and the below?
//        if (treasure < 0 || treasure > 24) {
//            throw new IllegalArgumentException("The treasure numbered"
//                    + treasure + "does not exist");
//        }
        this.treasure = treasure;
    }

    public boolean pathFromExists(Corridor fromCorr, Direction toDir) {
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

    public void removeTreasure() {
        treasure = null;
    }

    public void setRotation(Rotation rot) {
        rotation = rot;
    }



    //getters

    public CorridorType getType() {
        return type;
    }

    public Rotation getRotation() {
        return rotation;
    }

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

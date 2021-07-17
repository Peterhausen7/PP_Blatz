package logic;

enum Direction {
    UP, RIGHT, DOWN, LEFT;
}

/**
 * Possible corridors
 */
public enum CorridorType {
    I,
    L,
    T;

    Direction[] connectsTo(Rotation rot) {
        Direction result[];
        switch(this) {
            case I:
                result = new Direction[2];
                switch(rot) {
                    case LEFT:
                    case RIGHT:
                        result[0] = Direction.LEFT;
                        result[1] = Direction.RIGHT;
                        break;
                    case NEUTRAL:
                    case UPSIDE_DOWN:
                        result[0] = Direction.UP;
                        result[1] = Direction.DOWN;
                        break;
                }
                break;

            case L:
                result = new Direction[2];
                switch(rot) {
                    case NEUTRAL:
                        result[0] = Direction.UP;
                        result[1] = Direction.RIGHT;
                        break;
                    case RIGHT:
                        result[0] = Direction.DOWN;
                        result[1] = Direction.RIGHT;
                        break;
                    case UPSIDE_DOWN:
                        result[0] = Direction.LEFT;
                        result[1] = Direction.DOWN;
                        break;
                    case LEFT:
                        result[0] = Direction.LEFT;
                        result[1] = Direction.UP;
                        break;
                }
                break;

            case T:
                result = new Direction[3];
                switch(rot) {
                    case NEUTRAL:
                        result[0] = Direction.LEFT;
                        result[1] = Direction.RIGHT;
                        result[2] = Direction.DOWN;
                        break;
                    case RIGHT:
                        result[0] = Direction.LEFT;
                        result[1] = Direction.UP;
                        result[2] = Direction.DOWN;
                        break;
                    case UPSIDE_DOWN:
                        result[0] = Direction.LEFT;
                        result[1] = Direction.RIGHT;
                        result[2] = Direction.UP;
                        break;
                    case LEFT:
                        result[0] = Direction.UP;
                        result[1] = Direction.DOWN;
                        result[2] = Direction.RIGHT;
                        break;
                }
                break;

            default:
                result = null;
                break;
        }
        return result;
    }

}

package logic;

import java.util.List;

/**
 * Class containing the parameters needed to animate pushing a row/col
 */
public class PushAnimationParams {

    /** The direction of the push */
    private final Direction pushDir;
    /** The list of logical positions to push */
    private final List<Position> positions;
    /** The Corridor being pushed in */
    private final Corridor pushedCorridor;
    /** The list of treasures being pushed - as indices */
    private final List<Integer> treasureIndices;
    /** The list of players being pushed - as player numbers */
    private final List<Integer> playerNumbs;

    /**
     * Constructor setting all parameters needed for the push animation
     * @param pushDir - direction of the push
     * @param positions - list of logical positions to push
     * @param pushedCorridor - Corridor being pushed in
     * @param treasureIndices - list of treasures being pushed - as indices
     * @param playerNumbs - list of players being pushed - as player numbers
     */
    PushAnimationParams(Direction pushDir, List<Position> positions, Corridor pushedCorridor,
                        List<Integer> treasureIndices, List<Integer> playerNumbs) {
        this.pushDir = pushDir;
        this.positions = positions;
        this.pushedCorridor = pushedCorridor;
        this.treasureIndices = treasureIndices;
        this.playerNumbs = playerNumbs;
    }

    public Direction getPushDir() {
        return pushDir;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Corridor getPushedCorridor() {
        return pushedCorridor;
    }

    public List<Integer> getTreasureIndices() {
        return treasureIndices;
    }

    public List<Integer> getPlayerNumbs() {
        return playerNumbs;
    }
}

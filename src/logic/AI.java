package logic;

/**
 * Class for generic AI
 * Subclass of player
 */
public class AI extends Player {

    /* @TODO */

    public AI() {
        super();
    }

    @Override
    public PlayerType getType() {
        return PlayerType.AI;
    }

    @Override
    public void makePushMove(GameLogic game) {

    }

    @Override
    public void makeFigureMove() {

    }
}

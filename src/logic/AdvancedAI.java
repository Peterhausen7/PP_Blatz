package logic;

/**
 * Class for advanced AI (smorter)
 * Subclass of AI and therefore Player
 */
public class AdvancedAI extends AI {



    public AdvancedAI(Player player) {
        super(player);
    }


    public PlayerType getType() {
        return PlayerType.advancedAI;

    }

//    @Override
//    void findPushMove() {
//
//    }
//
//    @Override
//    void findFigureMove() {
//
//    }
}

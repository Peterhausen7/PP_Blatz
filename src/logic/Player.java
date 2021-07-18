package logic;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Class for a player, if not further specified by using SubClass AI/AdvancedAI,
 * this will be considered a human
 * (Will probably change this to interface or abstract)
 */
public class Player {

    private Position pos;
    private final Position startingCorner;
    private final String name;

    /** The treasures a player has to find, represented as numbers 1..24*/
    private final Queue<Treasure> treasures;

    /** Number of player, decides colour and starting corner */
    private final int playerNum;

    private final AI playerAI;

    public Player() {
        pos = new Position(0, 0);
        startingCorner = pos;
        name = null;
        treasures = new LinkedList<>();
        playerAI = null;
        playerNum = 1;
    }

    /**
     * Constructor setting up a player
     * posX/Y is just a filler, will probably make a class to represent a position
     * @param name - name of the player
     * @param treasures - treasures the player has to find
     * @param playerNum - the number of the player, used for colour/starting corner/flow control
     */
    public Player(String name, Queue<Treasure> treasures, int playerNum, int fieldSize, PlayerType playerType) {
        switch(playerNum) {
            case 1:
                pos = new Position(0, 0);
                break;
            case 2:
                pos = new Position(fieldSize - 1, 0);
                break;
            case 3:
                pos = new Position(fieldSize - 1, fieldSize - 1);
                break;
            case 4:
                pos = new Position(0, fieldSize - 1);
                break;
            default:
                throw new IllegalArgumentException(playerNum + " is not a valid player number");
        }
        startingCorner = pos;

        switch(playerType) {

            case AI:
                playerAI = new AI();
                break;
            case advancedAI:
                playerAI = new AdvancedAI();
                break;
            case Human:
            default:
                playerAI = null;
        }

        this.name = name;
        this.treasures = treasures;
        this.playerNum = playerNum;
    }


    /**
     * Makes a push move on the field,
     * AI needs to find a good move here
     */
    public void makePushMove(GameLogic game) {
        /* @TODO */
    }

    /**
     * Moves the figure on the field,
     * AI needs to find a good move here
     */
    public void makeFigureMove() {
        /* @TODO */
    }

    public boolean checkIfWon(Position pos) {
        if (treasures.isEmpty()) {
            return pos.equals(startingCorner);
        }
        return false;
    }

    /**
     * Collects the specified treasure, if it is the next target
     * @param treasure - the treasure to collect
     * @return true if the treasure has been collected (was the next target)
     */
    public boolean collectTreasure(Treasure treasure) {
        if (treasure.equals(nextTreasure())) {
            treasures.poll();
            return true;
        }
        return false;
    }


    /**
     * Adds treasure the player has to get to the list
     * @param treasure - Treasure to add
     */
    public void addTreasure(Treasure treasure) {
        treasures.add(treasure);
    }

    /**
     * Gets the next treasure the player has to find
     * @return - Enum-Entry of Treasure the player has to find
     */
    public Treasure nextTreasure() {
        return treasures.peek();
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    /** Getters - everything for now */


    public String getName() {
        return name;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public Position getPos() {
        return pos;
    }

    public Position getStartingCorner() {
        return startingCorner;
    }
}

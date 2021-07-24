package logic;

import java.util.Queue;

/**
 * Class for a player. If the AI attribute is not null, this player is an AI.
 */
public class Player {

    /** Current position of the player */
    private Position pos;
    /** starting corner of the player */
    private final Position startingCorner;
    /** Name of the player */
    private final String name;
    /** The treasures a player has to find */
    private final Queue<Treasure> treasures;
    /** Number of player, decides color and starting corner */
    private final int playerNum;
    /** AI of the player, if it is one */
    private final AI playerAI;

    /**
     * Creates an empty player, with just its position set. Used by the AI to find its next move
     * @param pos - the position of the player
     */
    Player(Position pos) {
        this.startingCorner = null;
        this.name = null;
        this.treasures = null;
        this.playerNum = -1;
        this.playerAI = null;
        this.pos = new Position(pos.getCol(), pos.getRow());
    }

    /**
     * Constructor for player.
     * @param name - name of player
     * @param treasures - treasures the player has to find
     * @param playerNum - players number
     * @param fieldSize - field size the player plays on
     * @param playerType - type of the player
     */
    Player(String name, Queue<Treasure> treasures, int playerNum, int fieldSize, PlayerType playerType) {
        switch(playerNum) {
            case 0:
                pos = new Position(0, 0);
                break;
            case 1:
                pos = new Position(fieldSize - 1, 0);
                break;
            case 2:
                pos = new Position(fieldSize - 1, fieldSize - 1);
                break;
            case 3:
                pos = new Position(0, fieldSize - 1);
                break;
            default:
                throw new IllegalArgumentException(playerNum + " is not a valid player number");
        }
        startingCorner = pos;
        this.name = name;
        this.treasures = treasures;
        this.playerNum = playerNum;
        switch(playerType) {
            case AI:
                playerAI = new AI(this);
                break;
            case advancedAI:
                playerAI = new AdvancedAI(this);
                break;
            default:
                playerAI = null;
        }
    }

    /**
     * Finds a move, if this player is not human
     * @param field - the field to find the move on
     * @return the move the AI found or null if this is a human
     */
    AIMove findMove(Field field) {
        if (playerAI != null) {
            return playerAI.findPushMove(field);
        }
        return null;
    }

    /**
     * Checks if the player has won (has reached its starting corner with no treasures left to find)
     * @param pos - the new position the player reached
     * @return true if the player won
     */
    boolean checkIfWon(Position pos) {
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
    boolean collectTreasure(Treasure treasure) {
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
    void addTreasure(Treasure treasure) {
        treasures.add(treasure);
    }

    /**
     * Returns the number of treasures left to find
     * @return number of treasures left
     */
    int getTreasuresLeft() {
        return treasures.size();
    }

    /**
     * Return the Queue of treasures this player has to find
     * @return the Queue of treasures
     */
    Queue<Treasure> getTargetTreasures() {
        return treasures;
    }

    /**
     * Gets the next treasure the player has to find
     * @return - Enum-Entry of Treasure the player has to find
     */
    Treasure nextTreasure() {
        return treasures.peek();
    }

    /**
     * Sets the position of the player
     * @param pos - position to set
     */
    void setPos(Position pos) {
        this.pos = pos;
    }

    /**
     * Gets the name of this player
     * @return - the name
     */
    String getName() {
        return name;
    }

    /**
     * Gets the player number of this player
     * @return the player number
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * Gets the position of this player
     * @return the position
     */
    Position getPos() {
        return pos;
    }

    /**
     * Gets the starting corner of this player
     * @return the starting corner
     */
    Position getStartingCorner() {
        return startingCorner;
    }

    /**
     * Gets the type of this player
     * @return the type
     */
    PlayerType getType() {
        if (playerAI != null) {
            return playerAI.getType();
        }
        return PlayerType.Human;
    }

    @Override
    public String toString() {
        return "Player{" +
                "pos=" + pos +
                ", startingCorner=" + startingCorner +
                ", name='" + name + '\'' +
                ", treasures=" + treasures +
                ", playerNum=" + playerNum +
                ", playerAI=" + playerAI +
                '}';
    }
}

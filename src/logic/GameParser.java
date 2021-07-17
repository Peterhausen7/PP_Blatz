package logic;

import java.util.ArrayList;

/**
 * Class to parse game from or to (for example) GSON
 */
public class GameParser {
    private ArrayList<Object[]> field = new ArrayList<Object[]>();
    private FreeWayCard freeWayCard;
    private int currentPlayer;
    private ArrayList<ParsedPlayer> players = new ArrayList <ParsedPlayer>();

    // Getter Methods
    public ArrayList<Object[]> getField() {
        return field;
    }

    public ArrayList<ParsedPlayer> getPlayers() {
        return players;
    }

    public FreeWayCard getFreeWayCard() {
        return freeWayCard;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }


    // Setter Methods
    public void setFreeWayCard(FreeWayCard freeWayCard) {
        this.freeWayCard = freeWayCard;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setField(ArrayList<Object[]> field) {
        this.field = field;
    }

    public void setPlayers(ArrayList<ParsedPlayer> players) {
        this.players = players;
    }
}

class ParsedPlayer {
    private boolean involved;
    private String name;
    private int directedBy;
    private ParsedPosition position;
    private ArrayList<Integer> treasureCards;


    // Getter Methods
    boolean isInvolved() {
        return involved;
    }

    String getName() {
        return name;
    }

    int getDirectedBy() {
        return directedBy;
    }

    ParsedPosition getPosition() {
        return position;
    }

    ArrayList<Integer> getTreasureCards() {
        return treasureCards;
    }


    // Setter Methods
    void setInvolved(boolean involved) {
        this.involved = involved;
    }

    void setName(String name) {
        this.name = name;
    }

    void setDirectedBy(int directedBy) {
        this.directedBy = directedBy;
    }

    void setPosition(ParsedPosition position) {
        this.position = position;
    }

    void setTreasureCards(ArrayList<Integer> treasureCards) {
        this.treasureCards = treasureCards;
    }
}

class FreeWayCard {
    private int type;
    private int rotated;
    private int treasure;
    ParsedPosition position;


    // Getter Methods
    public int getType() {
        return type;
    }

    public int getRotated() {
        return rotated;
    }

    public int getTreasure() {
        return treasure;
    }

    public ParsedPosition getPosition() {
        return position;
    }

    // Setter Methods
    public void setType(int type) {
        this.type = type;
    }

    public void setRotated(int rotated) {
        this.rotated = rotated;
    }

    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }

    public void setPosition(ParsedPosition positionObject) {
        this.position = positionObject;
    }
}

class ParsedPosition {
    private int x;
    private int y;


    // Getter Methods
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Setter Methods
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;

    }
}

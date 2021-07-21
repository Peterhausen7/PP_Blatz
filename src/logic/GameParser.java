package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse game from or to (for example) GSON
 */
class GameParser {
    private List<FieldCard[]> field;
    private FreeWayCard freeWayCard;
    private int currentPlayer;
    private List<ParsedPlayer> players;

    GameParser(List<FieldCard[]> field, FreeWayCard freeWayCard, int currentPlayer,
               List<ParsedPlayer> players) {
        this.field = field;
        this.freeWayCard = freeWayCard;
        this.currentPlayer = currentPlayer;
        this.players = players;
    }

    // Getter Methods
    public List<FieldCard[]> getField() {
        return field;
    }

    public List<ParsedPlayer> getPlayers() {
        return players;
    }

    public FreeWayCard getFreeWayCard() {
        return freeWayCard;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }



}

class ParsedPlayer {
    private boolean involved;
    private String name;
    private int directedBy;
    private ParsedPosition position;
    private List<Integer> treasureCards;

    ParsedPlayer(boolean involved, String name, int directedBy,
                 ParsedPosition position, List<Integer> treasureCards) {
        this.involved = involved;
        this.name = name;
        this.directedBy = directedBy;
        this.position = position;
        this.treasureCards = treasureCards;
    }

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

    List<Integer> getTreasureCards() {
        return treasureCards;
    }

}

class FieldCard {
    private int type;
    private int rotated;
    private int treasure;

    FieldCard(int type, int rotated, int treasure) {
        this.type = type;
        this.rotated = rotated;
        this.treasure = treasure;
    }

    int getType() {
        return type;
    }

    int getRotated() {
        return rotated;
    }

    int getTreasure() {
        return treasure;
    }
}

class FreeWayCard {
    private int type;
    private int rotated;
    private int treasure;
    ParsedPosition position;

    FreeWayCard(int type, int rotated, int treasure, ParsedPosition position) {
        this.type = type;
        this.rotated = rotated;
        this.treasure = treasure;
        this.position = position;
    }


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
}

class ParsedPosition {
    private int x;
    private int y;

    ParsedPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter Methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

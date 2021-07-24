package logic;

import java.util.List;

/**
 * Class to parse game from or to JSON with for example GSON
 */
class GameParser {
    private final List<ParsedCorridor[]> field;
    private final ParsedFreeWayCard freeWayCard;
    private final int currentPlayer;
    private final List<ParsedPlayer> players;

    GameParser(List<ParsedCorridor[]> field, ParsedFreeWayCard freeWayCard, int currentPlayer,
               List<ParsedPlayer> players) {
        this.field = field;
        this.freeWayCard = freeWayCard;
        this.currentPlayer = currentPlayer;
        this.players = players;
    }

    // Getter Methods
    public List<ParsedCorridor[]> getField() {
        return field;
    }

    public List<ParsedPlayer> getPlayers() {
        return players;
    }

    public ParsedFreeWayCard getFreeWayCard() {
        return freeWayCard;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }



}

/**
 * A parsed player
 */
class ParsedPlayer {
    private final boolean involved;
    private final String name;
    private final int directedBy;
    private final ParsedPosition position;
    private final List<Integer> treasureCards;

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

/**
 * A parsed Corridor
 */
class ParsedCorridor {
    private final int type;
    private final int rotated;
    private final int treasure;

    ParsedCorridor(int type, int rotated, int treasure) {
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

/**
 * A parsed free Corridor
 */
class ParsedFreeWayCard {
    private final int type;
    private final int rotated;
    private final int treasure;
    private final ParsedPosition position;

    ParsedFreeWayCard(int type, int rotated, int treasure, ParsedPosition position) {
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

/**
 * A parsed position
 */
class ParsedPosition {
    private final int x;
    private final int y;

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

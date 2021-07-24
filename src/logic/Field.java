package logic;

import java.util.*;

/**
 * Class representing the logical field of the game
 */
class Field {

    /** Logical field made out of corridors */
    private Corridor[][] field;

    /** The free corridor to be pushed into a col/row */
    private Corridor freeCorridor;

    /** The Position that can not be pushed on the next move */
    private Position blockedPos;

    /**
     * Constructor creating a field in the specified size
     * @param fieldSize - the size
     */
    Field(int fieldSize) {
        field = new Corridor[fieldSize][fieldSize];
    }

    /**
     * Constructor that creates a copy of an Object of this class. The copy is tested save for push methods
     * and thus should only be used for push and getter methods. (Corridor and Position classes are shallow copies)
     * @param copy - the Object to copy
     */
    Field(Field copy) {
        //Calls clone for evey array in field (Corridor[0-n]) and puts them into a new Corridor[][]
        this.field = Arrays.stream(copy.field).map(Corridor[]::clone).toArray(Corridor[][]::new);
        this.freeCorridor = copy.freeCorridor;
        this.blockedPos = copy.blockedPos;
    }

    /**
     * Constructor to create a field from a provided string. Only used for testing.
     * @param gui - GUI to display corridors
     * @param field - the field represented as a string
     */
    Field(GUIConnector gui, String field) {
        String[] rows = field.split("\\r?\\n");
        //a labyrinth field is always x*x. since this is also a
        //only used for testing, the string wont be checked for errors
        this.field = new Corridor[rows.length][rows.length];
        int r = 0;
        int c = 0;
        for (String row : rows) {
            String[] cards = row.split(",");
            for (String card : cards) {
                char corrType = card.charAt(0);

                Rotation rot = Rotation.values()[Character.getNumericValue(card.charAt(1))];
                int treasureParsed = Integer.parseInt(card.substring(2));
                Treasure treasure = (treasureParsed > 0) ? Treasure.values()[treasureParsed - 1]
                        : null;

                Corridor corr;
                switch(corrType) {
                    case 'L' :
                        corr = new Corridor(CorridorType.L, rot, treasure);
                        break;

                    case 'I' :
                        corr = new Corridor(CorridorType.I, rot, treasure);
                        break;

                    case 'T' :
                        corr = new Corridor(CorridorType.T, rot, treasure);
                        break;
                    default:
                        corr = new Corridor(CorridorType.T);
                }
                this.field[c][r] = corr;
                gui.displayCorridor(c, r, this.field[c][r]);
                c++;
            }
            c = 0;
            r++;
        }
        //Free corridor does not matter and is not included in the string
        freeCorridor = new Corridor(CorridorType.I);
    }

    /**
     * Creates a shuffled (random)field in default size (GameLogic constant) and displays it on the GUI.
     * @param treasureAmount - Amounts of treasures to be used
     * @param treasureList - List of all treasures in random order
     * @param gui - the GUI
     */
    void shuffleField(int treasureAmount, List<Treasure> treasureList, GUIConnector gui) {

        Random rng = new Random();
        freeCorridor = new Corridor(CorridorType.values()[rng.nextInt(3)]);

        field = new Corridor[GameLogic.FIELD_SIZE][GameLogic.FIELD_SIZE];

        //Sets the static positions like corners
        for (int i = 0; i < GameLogic.STATIC_POSITIONS.length; i++) {
            field[GameLogic.STATIC_POSITIONS[i].getCol()][GameLogic.STATIC_POSITIONS[i].getRow()] =
                    GameLogic.STATIC_CORRIDORS[i];
        }

        //fills all other spots with random corridor's containing no treasure
        for (int row = 0; row < GameLogic.FIELD_SIZE; row++) {
            for (int col = 0; col < GameLogic.FIELD_SIZE; col++) {
                if (field[col][row] == null) {
                    field[col][row] = new Corridor(CorridorType.values()[rng.nextInt(3)],
                            Rotation.values()[rng.nextInt(4)],
                            null);
                }
                gui.displayCorridor(col, row, field[col][row]);
            }
        }

        //randomly adds treasures from the treasureList to non corner corridors
        int listIndex = 0;
        while (listIndex < treasureAmount && listIndex < (GameLogic.FIELD_SIZE * GameLogic.FIELD_SIZE - 4)) {
            int col = rng.nextInt(GameLogic.FIELD_SIZE);
            int row = rng.nextInt(GameLogic.FIELD_SIZE);
            Corridor corr = field[col][row];
            if (new Position(col, row).notCorner(field.length) && !corr.hasTreasure()) {
                field[col][row] = new Corridor(corr.getType(), corr.getRotation(), treasureList.get(listIndex));
                listIndex++;
                gui.displayCorridor(col, row, field[col][row]);
            }
        }
        gui.displayFreeCorridor(freeCorridor);
    }

    /**
     * Finds and returns the path from a position to another
     * @param from - the from position
     * @param to - the target position
     * @return Array containing the valid path from "from" to "to", null if there is no valid path
     */
    Position[] findPath(Position from, Position to) {
        if (from.equals(to)) {
            return new Position[]{to};
        }
        Set<Position> visited = new HashSet<>();
        List<Position> path = new ArrayList<>();
        visited.add(from);
        findPathHelper(from, to, visited, path);
        Position[] result = new Position[path.size()];
        int currIndex = path.size() - 1;
        for (Position pos : path) {
            result[currIndex] = pos;
            currIndex--;
        }
        if (result.length > 0) {
            return result;
        } else {
            return null;
        }
    }


    /**
     * Helper for the findPath Method, gets recursively called, altering "visited" and "path"
     * @param from - the from position
     * @param to - the target position
     * @param visited - Set containing all visited Positions - is changed
     * @param path - List containing the path from "from" to "to" starting with to - is changed
     * @return true if there is a path from "from" to "to"
     */
    private boolean findPathHelper(Position from, Position to, Set<Position> visited, List<Position> path) {
        boolean result = false;
        Corridor cell = getCorridorAtPos(from);
        //All possible directions of the from corridor
        Direction[] possibleDirs = cell.getType().connectsTo(cell.getRotation());
        //All neighbours of the from position
        Map<Direction, Position> neighboursFrom = from.getNeighbours(field.length);

        for (Direction dir : possibleDirs) {
            Position neighbour;
            boolean reachable;
            neighbour = neighboursFrom.get(dir);
            //If there is a neighbour
            if (neighbour != null) {
                //checks if the neighbour is reachable
                reachable = cell.pathFromExists(getCorridorAtPos(neighbour), dir);
                //no path was found yet and the neighbour reachable and has not been visited yet
                if (!result && reachable && !visited.contains(neighbour)) {
                    visited.add(neighbour);
                    //if the neighbour is the target position, result=true
                    if (neighbour.equals(to)) {
                        result = true;
                    }
                    //otherwise continue recursion from the neighbour
                    else {
                        result = findPathHelper(neighbour, to, visited, path);
                    }
                    //on the way back, after reaching the target to, add all positions to the path
                    if (result) {
                        path.add(neighbour);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Adds the index of treasure of corridor "corr" to the list "treasureIndices", if corr has a treasure
     * @param treasureIndices - the list
     * @param corr - the corridor
     */
    private void addTreasureIndexIfExists(List<Integer> treasureIndices, Corridor corr) {
        if (corr.hasTreasure()) {
            treasureIndices.add(corr.getTreasure().getIndex());
        }
    }


    /**
     * Pushes from a position, only 1 Players position will be considered.
     * Mainly used by the AI to find its move
     * @param pos - Position to push at
     * @param player - Player that can move with the row/col
     */
    void pushFromPos(Position pos, Player player) {
        Player[] players = {player};
        int col = pos.getCol();
        int row = pos.getRow();
        if (col == 0) {
            pushLeftToRight(row, players);
        } else if (row == 0) {
            pushTopToBottom(col, players);
        } else if (col == field.length - 1) {
            pushRightToLeft(row, players);
        } else {
           pushBottomToTop(col, players);
        }
    }


    /**
     * Pushes the free corridor from top to bottom at column col
     * @param col - the column to push down at
     * @param players - the players who may need to be moved with the corridors
     * @return - all parameters needed for the push animation
     */
    PushAnimationParams pushTopToBottom(int col, Player[] players) {
        int lastRow = field[col].length - 1;
        //Saves the bottom most corridor as the to be new free corridor
        Corridor newFreeCorridor = field[col][lastRow];
        //Lists for animation
        List<Position> positions = new ArrayList<>();
        List<Integer> treasureIndices = new ArrayList<>();
        List<Integer> playersToMove = new ArrayList<>();

        /*
        Starting with the bottom corridor, changes it to the one before it
        and adds the position + treasure index to the lists
        */
        for (int row = lastRow; row > 0;  row--) {
            field[col][row] = field[col][row - 1];
            positions.add(new Position(col, row));
            addTreasureIndexIfExists(treasureIndices, field[col][row]);
        }

        //Old free corridor becomes first corridor
        field[col][0] = freeCorridor;
        positions.add(new Position(col, 0));
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //New free corridor
        freeCorridor = newFreeCorridor;
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //Check if players are on the affected col and moves them accordingly
        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.getCol() == col) {
                if (pos.getRow() == lastRow) {
                    player.setPos(new Position(col, 0));
                } else {
                    player.setPos(new Position(col, pos.getRow()+1));
                }
                playersToMove.add(player.getPlayerNum());
            }
        }
        //Sets the Position that can't be pushed
        blockedPos = new Position(col, lastRow);

        return new PushAnimationParams(Direction.DOWN, positions, field[col][0], treasureIndices, playersToMove);
    }

    /**
     * Pushes the free corridor from bottom to top at column col
     * @param col - the column to push up at
     * @param players - the players who may need to be moved with the corridors
     * @return - all parameters needed for the push animation
     */
    PushAnimationParams pushBottomToTop(int col, Player[] players) {
        int lastRow = field[col].length - 1;
        //Saves the top most corridor as the to be new free corridor
        Corridor newFreeCorridor = field[col][0];
        //Lists for animation
        List<Position> positions = new ArrayList<>();
        List<Integer> treasureIndices = new ArrayList<>();
        List<Integer> playersToMove = new ArrayList<>();

        /*
        Starting with the top corridor, changes it to the one after it
        and adds the position + treasure index to the lists
        */
        for (int row = 0; row < lastRow;  row++) {
            field[col][row] = field[col][row + 1];
            positions.add(new Position(col, row));
            addTreasureIndexIfExists(treasureIndices, field[col][row]);
        }

        //Old free corridor becomes last corridor
        field[col][lastRow] = freeCorridor;
        positions.add(new Position(col, lastRow));
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //New free corridor
        freeCorridor = newFreeCorridor;
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //Check if players are on the affected col and moves them accordingly
        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.getCol() == col) {
                if (pos.getRow() == 0) {
                    player.setPos(new Position(col, lastRow));
                } else {
                    player.setPos(new Position(col, pos.getRow()-1));
                }
                playersToMove.add(player.getPlayerNum());
            }
        }
        //Sets the Position that can't be pushed
        blockedPos = new Position(col, 0);

        return new PushAnimationParams(Direction.UP, positions, field[col][lastRow], treasureIndices, playersToMove);
    }

    /**
     * Pushes the free corridor from left to right at row row
     * @param row - the row to push left at
     * @param players - the players who may need to be moved with the corridors
     * @return - all parameters needed for the push animation
     */
    PushAnimationParams pushLeftToRight(int row, Player[] players) {
        int lastCol = field.length - 1;
        //Saves the right most corridor as the to be new free corridor
        Corridor newFreeCorridor = field[lastCol][row];
        //Lists for animation
        List<Position> positions = new ArrayList<>();
        List<Integer> treasureIndices = new ArrayList<>();
        List<Integer> playersToMove = new ArrayList<>();

        /*
        Starting with the right corridor, changes it to the one before it
        and adds the position + treasure index to the lists
        */
        for (int col = lastCol; col > 0;  col--) {
            field[col][row] = field[col - 1][row];
            positions.add(new Position(col, row));
            addTreasureIndexIfExists(treasureIndices, field[col][row]);
        }
        //Old free corridor becomes first corridor
        field[0][row] = freeCorridor;
        positions.add(new Position(0, row));
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //new free corridor
        freeCorridor = newFreeCorridor;
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //Check if players are on the affected row and moves them accordingly
        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.getRow() == row) {
                if (pos.getCol() == lastCol) {
                    player.setPos(new Position(0, row));
                } else {
                    player.setPos(new Position(pos.getCol() + 1, row));
                }
                playersToMove.add(player.getPlayerNum());
            }

        }

        //Sets the Position that can't be pushed
        blockedPos = new Position(lastCol, row);

        return new PushAnimationParams(Direction.RIGHT, positions, field[0][row], treasureIndices, playersToMove);
    }


    /**
     * Pushes the free corridor from right to left at row row
     * @param row - the row to push right at
     * @param players - the players who may need to be moved with the corridors
     * @return - all parameters needed for the push animation
     */
    PushAnimationParams pushRightToLeft(int row, Player[] players) {
        int lastCol = field.length - 1;
        //Saves the left most corridor as the to be new free corridor
        Corridor newFreeCorridor = field[0][row];
        //Lists for animation
        List<Position> positions = new ArrayList<>();
        List<Integer> treasureIndices = new ArrayList<>();
        List<Integer> playersToMove = new ArrayList<>();

         /*
        Starting with the left corridor, changes it to the one after it
        and adds the position + treasure index to the lists
        */
        for (int col = 0; col < lastCol;  col++) {
            field[col][row] = field[col + 1][row];
            positions.add(new Position(col, row));
            addTreasureIndexIfExists(treasureIndices, field[col][row]);
        }

        //Old free corridor becomes last corridor
        field[lastCol][row] = freeCorridor;
        positions.add(new Position(lastCol, row));
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //new free corridor
        freeCorridor = newFreeCorridor;
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //Check if players are on the affected row and moves them accordingly
        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.getRow() == row) {
                if (pos.getCol() == 0) {
                    player.setPos(new Position(lastCol, row));
                } else {
                    player.setPos(new Position(pos.getCol() - 1, row));
                }
                playersToMove.add(player.getPlayerNum());
            }
        }
        //Sets the Position that can't be pushed
        blockedPos = new Position(0, row);

        return new PushAnimationParams(Direction.LEFT, positions, field[lastCol][row], treasureIndices, playersToMove);
    }

    /**
     * Finds the current target of the provided player on the field
     * @param currPlayer - the player who's target is to be found
     * @return position of the players target or null if its not on the field
     */
    Position calcCurrentTarget(Player currPlayer) {
        Treasure target = currPlayer.nextTreasure();
        if (target == null) {
            return currPlayer.getStartingCorner();
        }
        for (int row = 0; row < field[0].length; row++) {
            for (int col = 0; col < field.length; col++) {
                if (target.equals(field[col][row].getTreasure())) {
                    return new Position(col, row);
                }
            }
        }
        return null;
    }

    /**
     * Creates a parsed field (representation of this.field that can be parsed with GSON)
     * @return the parsed field (List containing array's of FieldCards - the parsed version of corridors)
     */
    List<ParsedCorridor[]> createParsedField() {
        List<ParsedCorridor[]> parsedField = new ArrayList<>(field.length);

        for (int col = 0; col < field[0].length; col++) {
            ParsedCorridor[] parsedCards = new ParsedCorridor[field.length];
            for (int row = 0; row < field.length; row++) {
                Corridor corr = field[col][row];
                int parsedTreasure = corr.hasTreasure() ? corr.getTreasure().getIndex() + 1 : 0;
                ParsedCorridor parsedCard = new ParsedCorridor(corr.getType().ordinal(),
                        corr.getRotation().ordinal(), parsedTreasure);
                parsedCards[row] = parsedCard;
            }
            parsedField.add(parsedCards);
        }
        return parsedField;
    }

    /**
     * Sets up a field from its parsed version (representation of field parsed from GSON)
     * @param graphData - the parsed version of the field
     * @param gui - the GUI to display corridors
     */
    void setupFromParsed(GameParser graphData, GUIConnector gui) {
        field = new Corridor[graphData.getField().size()][graphData.getField().get(0).length];
        //setting the corridors
        for (int row = 0; row < field[0].length; row++) {
            for (int col = 0; col < field.length; col++) {
                ParsedCorridor parsedCorridor =  graphData.getField().get(col)[row];
                int treasureIndex = parsedCorridor.getTreasure();

                Treasure treasure = Treasure.getByParsedNumber(treasureIndex);
                CorridorType corrType;
                try {
                    corrType = CorridorType.values()[parsedCorridor.getType()];
                } catch (IndexOutOfBoundsException e ) {
                    throw new IllegalArgumentException(parsedCorridor.getType() + " is not a valid corridor type.");
                }
                Rotation rot;
                try {
                    rot = Rotation.values()[parsedCorridor.getRotated()];
                } catch (IndexOutOfBoundsException e)  {
                    throw new IllegalArgumentException(parsedCorridor.getRotated() + " is not a valid rotation.");
                }
                Corridor corr = new Corridor(corrType, rot, treasure);
                field[col][row] = corr;
                gui.displayCorridor(col, row, field[col][row]);
            }
        }
        //setting the free corridor
        int freeTreasureIndex = graphData.getFreeWayCard().getTreasure();
        Treasure freeTreasure = Treasure.getByParsedNumber(freeTreasureIndex);
        CorridorType corrType;
        try {
            corrType = CorridorType.values()[graphData.getFreeWayCard().getType()];
        } catch (IndexOutOfBoundsException e ) {
            throw new IllegalArgumentException(graphData.getFreeWayCard().getType() +
                    " is not a valid corridor type.");
        }
        Rotation rot;
        try {
            rot = Rotation.values()[graphData.getFreeWayCard().getRotated()];
        } catch (IndexOutOfBoundsException e)  {
            throw new IllegalArgumentException(graphData.getFreeWayCard().getRotated() +
                    " is not a valid rotation.");
        }
        freeCorridor = new Corridor(corrType, rot, freeTreasure);
        //setting the blocked position
        ParsedPosition parsedFreePos = graphData.getFreeWayCard().getPosition();
        Position blockedPos;
        int parsedCol = parsedFreePos.getX();
        int parsedRow = parsedFreePos.getY();
        int arrowCol;
        int arrowRow;

        if (parsedCol == -1) {
            blockedPos = new Position(0, parsedRow);
            arrowCol = 0;
            arrowRow = blockedPos.getRow() + 1;
        } else if (parsedRow == -1) {
            blockedPos = new Position(parsedCol, 0);
            arrowCol = blockedPos.getCol() + 1;
            arrowRow = 0;
        } else if (parsedCol == field.length) {
            blockedPos = new Position(parsedCol - 1, parsedRow);
            arrowCol = parsedCol + 1;
            arrowRow = blockedPos.getRow() + 1;
        } else if (parsedRow == field.length) {
            blockedPos = new Position(parsedCol, parsedRow - 1);
            arrowCol = blockedPos.getCol() + 1;
            arrowRow = parsedRow + 1;
        } else {
            throw new IllegalArgumentException("c: " + parsedCol + " r: " + parsedRow + " is not a valid " +
                    "Position for the freeCorridor");
        }

        if (!(parsedCol == -1 && parsedRow == -1)) {
            if (blockedPos.isPushablePos(field.length)) {
                this.blockedPos = blockedPos;
                gui.changeArrowCol(arrowCol, arrowRow, true);
            } else {
                throw new IllegalArgumentException("c: " + parsedCol + " r: " + parsedRow + " is not a valid " +
                        "Position for the freeCorridor");
            }
        }

        gui.displayFreeCorridor(freeCorridor);
    }

    /**
     * Rotates the free corridor to the left
     */
    void rotateFreeCorridorLeft() {
        switch (freeCorridor.getRotation()) {
            case NEUTRAL:
                freeCorridor.setRotation(Rotation.LEFT);
                break;
            case RIGHT:
                freeCorridor.setRotation(Rotation.NEUTRAL);
                break;
            case UPSIDE_DOWN:
                freeCorridor.setRotation(Rotation.RIGHT);
                break;
            case LEFT:
                freeCorridor.setRotation(Rotation.UPSIDE_DOWN);
                break;
        }
    }

    /**
     * Rotates the free corridor to the right
     */
    void rotateFreeCorridorRight() {
        switch (freeCorridor.getRotation()) {
            case NEUTRAL:
                freeCorridor.setRotation(Rotation.RIGHT);
                break;
            case RIGHT:
                freeCorridor.setRotation(Rotation.UPSIDE_DOWN);
                break;
            case UPSIDE_DOWN:
                freeCorridor.setRotation(Rotation.LEFT);
                break;
            case LEFT:
                freeCorridor.setRotation(Rotation.NEUTRAL);
                break;
        }
    }

    /**
     * Clears the blocked position (sets it to null)
     */
    void clearBlockedPos() {
        blockedPos = null;
    }

    /**
     * Gets the Corridor at the specified position
     * @param pos - the pos
     * @return the corridor at the position pos
     */
    Corridor getCorridorAtPos(Position pos) {
        return field[pos.getCol()][pos.getRow()];
    }

    /**
     * Gets the free corridor
     * @return the free corridor
     */
    Corridor getFreeCorridor() {
        return freeCorridor;
    }

    /**
     * Get the size of this field
     * @return the size of this field
     */
    int getFieldSize() {
        return field.length;
    }

    /**
     * Gets the blocked position
     * @return the blocked position
     */
    Position getBlockedPos() {
        return blockedPos;
    }

    /**
     * Gets the corridor at col c and row r
     * @param c - column c
     * @param r - row r
     * @return the corridor at [c][r]
     */
    Corridor getCorridorAtX(int c, int r) {
        return field[c][r];
    }

    /**
     * Gets the logical field of corridors
     * @return - the field
     */
    Corridor[][] getField() {
        return field;
    }

    /**
     * Prints the logical field to a string
     * @return string representation of the field
     */
    private String printField() {
        StringBuilder result = new StringBuilder();
        for (Corridor[] corridors : field) {
            for (Corridor corridor : corridors) {
                result.append(corridor).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "Field{" +
                "field=" + printField() +
                ", freeCorridor=" + freeCorridor +
                ", blockedPos=" + blockedPos +
                '}';
    }
}

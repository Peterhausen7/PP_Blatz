package logic;

import java.io.File;
import java.util.*;

/**
 * Class containing the logic of the game
 */
public class GameLogic {

    /** Default Field size per the game rules 7*7 */
    public static final int COLS = 7;
    public static final int ROWS = 7;
    /** Default amount of treasures */
    public static final int AMOUNT_OF_TREASURES = 24;

    /** Log file */
    private final File log;

    private final GUIConnector gui;

    /** Logical field made out of corridors */
    private final Corridor[][] field;

    /** The free corridor to be pushed into a col/row*/
    private Corridor freeCorridor;

    private Position blockedPos;

    /** The players */
    private final Player[] players;

    /** The players index who's turn it currently is*/
    private int currentPlayer;

    /** helps with flow control */
    private boolean pushed = false;

    private boolean inAnimation = false;

    private Position targetLocation;

    /**
     * Constructor for random field, bare bones (no real players etc)
     * later this will get all information from user input on GUI and build a field accordingly (correct players
     * , corridors, treasures etc.)
     * @param gui - connection to the GUI
     */
    public GameLogic(GUIConnector gui) {


        Random rng = new Random();
        this.gui = gui;
        freeCorridor = new Corridor(CorridorType.values()[rng.nextInt(3)]);

        players = new Player[4];
        for (int idx = 0; idx < players.length; idx++) {
            players[idx] = new Player();
        }
        currentPlayer = 0;

        field = new Corridor[COLS][ROWS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                field[col][row] = new Corridor(CorridorType.values()[rng.nextInt(3)],
                        Rotation.values()[rng.nextInt(4)],
                        Treasure.values()[rng.nextInt(25)]);
                gui.displayCorridor(col, row, field[col][row]);

            }
        }



        //for now
        gui.displayFreeCorridor(freeCorridor);
        gui.setFiguresToCorners();

        saveGameFromGSON();


        /* @TODO */
        log = null;


    }

    /**
     * Creates a game, all params are given
     * @param gui - connection to the GUI
     * @param field - preset Field of corridors
     */
    public GameLogic(GUIConnector gui, Corridor[][] field, Player[] players, File log, Corridor freeCorridor) {
        this.gui = gui;
        this.field = field;
        this.players = players;
        this.log = log;
        this.freeCorridor = freeCorridor;

        currentPlayer = 0;

        for (int row = 0; row < this.field[0].length; row++) {
            for (int col = 0; col < this.field.length; col++) {
                 this.gui.displayCorridor(col, row, this.field[col][row]);
            }
        }


        //for now
        gui.setFiguresToCorners();
        gui.displayFreeCorridor(freeCorridor);


        /* @TODO */

    }

    /**
     * Creates a game. The field is input as a string, it is
     * assumed the string is correct, as this is just used for testing
     * @param gui - connection to the GUI
     * @param field - the field consisting of corridors
     */
    public GameLogic(GUIConnector gui, String field, Player[] players) {
        this.gui = gui;
        this.players = players;
        currentPlayer = 0;

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

        freeCorridor = new Corridor(CorridorType.I);

        gui.setFiguresToCorners();
        gui.displayFreeCorridor(freeCorridor);
        /* @TODO */
        log = null;
    }


    public void makeTurn() {

    }

    /**
     * Pushes the free corridor into a row or col
     * @param col - column of edge to push
     * @param row - row of edge to push
     */
    public void pushCorridor(int col, int row) {

        if (new Position(col, row).isPushablePos() && (blockedPos == null ||
                (blockedPos.c != col || blockedPos.r != row))) {
            unblockOnGUI();
            if (col == 0) {
                pushLeftToRight(row);
            } else if (row == 0) {
                pushTopToBottom(col);
            } else if (col == field.length - 1) {
                pushRightToLeft(row);
            } else {
                pushBottomToTop(col);
            }
            clearTargetLoc();
            gui.displayFreeCorridor(freeCorridor);
            pushed = true;
        }
    }


    /**
     * Pushes the free corridor from top to bottom at column col
     * @param col - column to push down at
     */
    private void pushTopToBottom(int col) {
        int lastRow = field[col].length - 1;
        Corridor newFreeCorridor = field[col][lastRow];
        List<Position> positions = new ArrayList<>();
        for (int row = lastRow; row > 0;  row--) {
            field[col][row] = field[col][row - 1];
            positions.add(new Position(col, row));
            //gui.displayCorridor(col, row, field[col][row]);
        }
        field[col][0] = freeCorridor;
        positions.add(new Position(col, 0));
       // gui.displayCorridor(col, 0, field[col][0]);
        freeCorridor = newFreeCorridor;

        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.c == col) {
                if (pos.r == lastRow) {
                    player.setPos(new Position(col, 0));
                } else {
                    player.setPos(new Position(col, pos.r+1));
                }
            }
            //gui.displayFigure(player.getPlayerNum(), player.getPos().c, player.getPos().r);
        }
        gui.animatePush(Direction.DOWN, positions, field[col][0]);

        blockedPos = new Position(col, lastRow);
        gui.changeArrowCol(col+1, lastRow+2, true);
    }

    /**
     * Pushes the free corridor from bottom to top at column col
     * @param col - column to push up at
     */
    private void pushBottomToTop(int col) {
        int lastRow = field[col].length - 1;
        Corridor newFreeCorridor = field[col][0];
        for (int row = 0; row < lastRow;  row++) {
            field[col][row] = field[col][row + 1];
            gui.displayCorridor(col, row, field[col][row]);
        }
        field[col][lastRow] = freeCorridor;
        gui.displayCorridor(col, lastRow, field[col][lastRow]);
        freeCorridor = newFreeCorridor;

        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.c == col) {
                if (pos.r == 0) {
                    player.setPos(new Position(col, lastRow));
                } else {
                    player.setPos(new Position(col, pos.r-1));
                }
            }
            gui.displayFigure(player.getPlayerNum(), player.getPos().c, player.getPos().r);
        }

        blockedPos = new Position(col, 0);
        gui.changeArrowCol(col+1, 0, true);
    }

    /**
     * Pushes the free corridor from left to right at row row
     * @param row - row to push right at
     */
    private void pushLeftToRight(int row) {
        int lastCol = field.length - 1;
        Corridor newFreeCorridor = field[lastCol][row];
        for (int col = lastCol; col > 0;  col--) {
            field[col][row] = field[col - 1][row];
            gui.displayCorridor(col, row, field[col][row]);
        }
        field[0][row] = freeCorridor;
        gui.displayCorridor(0, row, field[0][row]);
        freeCorridor = newFreeCorridor;

        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.r == row) {
                if (pos.c == lastCol) {
                    player.setPos(new Position(0, row));
                } else {
                    player.setPos(new Position(pos.c + 1, row));
                }
            }
            gui.displayFigure(player.getPlayerNum(), player.getPos().c, player.getPos().r);
        }

        blockedPos = new Position(lastCol, row);
        gui.changeArrowCol(lastCol+2, row+1, true);
    }


    /**
     * Pushes the free corridor from right to left at row row
     * @param row - row to push left at
     */
    private void pushRightToLeft(int row) {
        int lastCol = field.length - 1;
        Corridor newFreeCorridor = field[0][row];
        for (int col = 0; col < lastCol;  col++) {
            field[col][row] = field[col + 1][row];
            gui.displayCorridor(col, row, field[col][row]);
        }
        field[lastCol][row] = freeCorridor;
        gui.displayCorridor(lastCol, row, field[lastCol][row]);
        freeCorridor = newFreeCorridor;

        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.r == row) {
                if (pos.c == 0) {
                    player.setPos(new Position(lastCol, row));
                } else {
                    player.setPos(new Position(pos.c - 1, row));
                }
            }
            gui.displayFigure(player.getPlayerNum(), player.getPos().c, player.getPos().r);
        }

        blockedPos = new Position(0, row);
        gui.changeArrowCol(0, row+1, true);
    }

    private void unblockOnGUI() {
        if (blockedPos != null && blockedPos.isValidPos(field.length)) {
            int col = blockedPos.c;
            int row = blockedPos.r;
            if (col == 0) {
                row++;
            } else if (row == 0) {
                col++;
            }
            else if (col == field.length - 1) {
                row++;
                col++;
                col++;
            } else {
                col++;
                row++;
                row++;
            }
            gui.changeArrowCol(col, row, false);
        }
    }

    /**
     * Moves the figure of player to pos.
     * @param player - player figure to move
     * @param target - Position to move to
     */
    public void moveFigureOfPlayer(Player player, Position target) {
        Position[] path = findPath(player.getPos(), target);
        if (path != null) {
            inAnimation = true;
            gui.animateFigure(player, path,this);


//            Corridor targetCorr = getCorridorAtPos(target);
//            if (targetCorr.hasTreasure()) {
//                if (player.collectTreasure(targetCorr.getTreasure())) {
//                    gui.removeTreasureFromGrid(targetCorr.getTreasure());
//                    targetCorr.removeTreasure();
//                    gui.displayCorridor(target.c, target.r, targetCorr);
//
//                }
//            }

            //nextTurn();

        }
    }

    public void moveAnimationFinished(Player player, Position target) {
        player.setPos(target);
        Corridor targetCorr = getCorridorAtPos(target);
        if (targetCorr.hasTreasure()) {
            if (player.collectTreasure(targetCorr.getTreasure())) {
                gui.removeTreasureFromGrid(targetCorr.getTreasure());
                targetCorr.removeTreasure();
                gui.displayCorridor(target.c, target.r, targetCorr);

            }
        }


        if (player.checkIfWon(target)) {
            gameWon(player);
        }

        nextTurn();
        inAnimation = false;
    }

    /**
     * Finds and returns the path from a position to another
     * @param from - the from position
     * @param to - the target position
     * @return Array containing the valid path from "from" to "to", null if there is no valid path
     */
    public Position[] findPath(Position from, Position to) {
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
    public boolean findPathHelper(Position from, Position to, Set<Position> visited, List<Position> path) {
        boolean result = false;
        Corridor cell = field[from.c][from.r];
        Direction[] possibleDirs = cell.getType().connectsTo(cell.getRotation());
        Map<Direction, Position> neighboursFrom = from.getNeighbours(field.length);

        for (Direction dir : possibleDirs) {
            Position neighbour;
            boolean reachable;
            neighbour = neighboursFrom.get(dir);

            if (neighbour != null) {
                reachable = cell.pathFromExists(field[neighbour.c][neighbour.r], dir);
                if (!result && reachable && !visited.contains(neighbour)) {
                    visited.add(neighbour);
                    if (neighbour.equals(to)) {
                        result = true;
                    }
                    else {
                        result = findPathHelper(neighbour, to, visited, path);
                    }
                    if (result) {
                        path.add(neighbour);
                    }
                }
            }
        }
        return result;
    }

    /**
     * React to click on grid
     * @param col - col of cell clicked
     * @param row - row of cell clicked
     */
    public void gridClicked(int col, int row, int gridSize) {
        if (!inAnimation) {
            Position pos = new Position(col, row);
            if (pushed) {
                //Not edge/corner click on the GUI grid
                if (!pos.isEdge(gridSize) && pos.notCorner(gridSize)) {
                    Position trg = new Position(col - 1, row - 1);
                    moveFigureOfPlayer(getCurrentPlayer(), trg);

                }
            } else {
                //Edge click on GUI grid
                if (pos.isEdge(gridSize)) {
                    int colToPush = col;
                    int rowToPush = row;
                    if (row == 0) {
                        colToPush--;
                    } else if (col == 0) {
                        rowToPush--;
                    } else if (col == gridSize - 1) {
                        rowToPush--;
                        colToPush = field.length - 1;
                    } else {
                        colToPush--;
                        rowToPush = field.length - 1;
                    }
                    Position pushPos = new Position(colToPush, rowToPush);
                    if (pushPos.isEdge(field.length)) {
                        pushCorridor(colToPush, rowToPush);
                    }
                }
            }
        }
    }

    public void cellEntered(int col, int row, int gridSize) {
        if (!inAnimation) {
            Position pos = new Position(col, row);
            if (!pos.isEdge(gridSize) && pos.notCorner(gridSize)) {
                if (findPath(getCurrentPlayer().getPos(), new Position(col-1, row-1)) != null) {
                    gui.highlightCellGreen(col, row);
                } else {
                    gui.highlightCellRed(col, row);
                }
            }
        }
    }

    public void hKeyPressed(boolean released) {
        if (targetLocation == null) {
            targetLocation = calcCurrentTarget(field, getCurrentPlayer());
        }
        if (targetLocation == null) {
            if (released) {
                gui.endHighlightOfCorr(-1, -1, freeCorridor.getTreasure());
            } else {
                gui.highlightCorrBlue(-1, -1, freeCorridor.getTreasure());
            }
        } else {
            Treasure target = getCurrentPlayer().nextTreasure();
            if (released) {
                gui.endHighlightOfCorr(targetLocation.c, targetLocation.r, target);
            } else {
                gui.highlightCorrBlue(targetLocation.c, targetLocation.r, target);
            }
        }
    }

    private static Position calcCurrentTarget(Corridor[][] field, Player currPlayer) {
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



    private void nextPlayer() {
      //  int oldPlayerNum = players[currentPlayer].getPlayerNum();
        int oldPlayerNum = getCurrentPlayer().getPlayerNum();
//        if (currentPlayer == players.length - 1) {
//            currentPlayer = 0;
//        } else {
//            currentPlayer++;
//        }
        if (++currentPlayer >= players.length) {
            currentPlayer = 0;
        }

        clearTargetLoc();
        gui.highlightNewPlayer(oldPlayerNum, getCurrentPlayer().getPlayerNum());
    }

    private void clearTargetLoc() {
        if (targetLocation != null) {
            gui.endHighlightOfCorr(targetLocation.c, targetLocation.r, getCorridorAtPos(targetLocation).getTreasure());
            targetLocation = null;
        } else {
            gui.endHighlightOfCorr(-1, -1, freeCorridor.getTreasure());
        }
    }

    /**
     * Saves the game state to json/gson
     * creates GameParser instance, wich the can be easily converted to json,
     * with toJson()
     */
    public void saveGameFromGSON() {
//        Gson gson = new GsonBuilder().create();
//        GameParser game = new GameParser();
//        game.setCurrentPlayer(3);
//        game.setFreeWayCard(new FreeWayCard());
//        game.field = new ArrayList<Object>(Collections.singleton(this.field));
//        File file = new File("src/logic/saveFiles/test.txt");
//
//
//        try {
//            Writer writer = new FileWriter(file);
//            gson.toJson(game, writer);
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        /* @TODO */
    }

    /**
     * Loads a game state from json/gson
     * creates GameParser instance from the file, from wich the game logic is build
     */
    public void loadGameToGSON() {

        /* @TODO */
    }

    /**
     * Something happens when a player wins
     */
    public void gameWon(Player player) {
        gui.endGameDialogue(player.getName());
    }

    /**
     * Ends the game (not by winning)
     */
    public void endGame() {
        /* @TODO */
    }


    /**
     * Sets the free corridor
     * @param corr - the corr to set
     * @return - the new freeCorridor
     */
    public Corridor setFreeCorridor(Corridor corr) {
        freeCorridor = corr;
        return freeCorridor;
    }

    public void rotateFreeCorridorLeft() {
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
        gui.displayFreeCorridor(freeCorridor);
    }

    public void rotateFreeCorridorRight() {
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
        gui.displayFreeCorridor(freeCorridor);
    }

    /**
     * Signals that a turn is over
     */
    public void nextTurn() {
        if (pushed) {
            pushed = false;
            nextPlayer();
        }
    }

    /**
     * Prints s to the log
     * @param s - the string to be added
     */
    public void log(String s) {
        /* @TODO */
    }


    //bunch of getters for now

    public int getNumOfPlayers() {
        return players.length;
    }

    public Player getPlayerX(int index) {
        return players[index];
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    public Corridor getCorridorAtX(int c, int r) {
        return field[c][r];
    }

    public Corridor getCorridorAtPos(Position pos) {
        if (pos.isValidPos(field.length)) {
            return field[pos.c][pos.r];
        }
        return null;
    }

    public Corridor[][] getField() {
        return field;
    }
}

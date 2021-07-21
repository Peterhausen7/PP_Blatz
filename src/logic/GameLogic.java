package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

/**
 * Class containing the logic of the game
 */
public class GameLogic {


    private final Position[] STATIC_POSITIONS = {
            new Position(0, 0),
            new Position(2, 0),
            new Position(4, 0),
            new Position(6, 0),
            new Position(0, 2),
            new Position(2, 2),
            new Position(4, 2),
            new Position(6, 2),
            new Position(0, 4),
            new Position(2, 4),
            new Position(4, 4),
            new Position(6, 4),
            new Position(0, 6),
            new Position(2, 6),
            new Position(4, 6),
            new Position(6, 6)
    };

    private final Corridor[] STATIC_CORRIDORS = {
            new Corridor(CorridorType.L, Rotation.RIGHT, null),
            new Corridor(CorridorType.T, Rotation.NEUTRAL, null),
            new Corridor(CorridorType.T, Rotation.NEUTRAL, null),
            new Corridor(CorridorType.L, Rotation.UPSIDE_DOWN, null),
            new Corridor(CorridorType.T, Rotation.LEFT, null),
            new Corridor(CorridorType.T, Rotation.LEFT, null),
            new Corridor(CorridorType.T, Rotation.NEUTRAL, null),
            new Corridor(CorridorType.T, Rotation.RIGHT, null),
            new Corridor(CorridorType.T, Rotation.LEFT, null),
            new Corridor(CorridorType.T, Rotation.UPSIDE_DOWN, null),
            new Corridor(CorridorType.T, Rotation.RIGHT, null),
            new Corridor(CorridorType.T, Rotation.RIGHT, null),
            new Corridor(CorridorType.L, Rotation.NEUTRAL, null),
            new Corridor(CorridorType.T, Rotation.UPSIDE_DOWN, null),
            new Corridor(CorridorType.T, Rotation.UPSIDE_DOWN, null),
            new Corridor(CorridorType.L, Rotation.LEFT, null),
    };
    /** Default Field size per the game rules 7*7 */
    public static final int COLS = 7;
    public static final int ROWS = 7;
    /** Default amount of treasures */
    public static final int AMOUNT_OF_TREASURES = 24;



    /** Log file */
    private final File log;

    private final GUIConnector gui;

    /** Logical field made out of corridors */
    private Corridor[][] field;

    /** The free corridor to be pushed into a col/row*/
    private Corridor freeCorridor;

    private Position blockedPos;

    /** The players */
    private Player[] players;

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



        this.gui = gui;

        gui.setFiguresToCorners();


//        newGame(List<String> names, List<Integer> playerNums, List<PlayerType> types, int treasurePerPlayer) {
//            List<Treasure> treasureList = setupPlayers(names, playerNums, types, treasurePerPlayer);

        List<String> names = new ArrayList<>(4);
        names.add("Name 1");
        names.add("Name 2");
        names.add("Name 3");
        names.add("Name 4");

        List<Integer> playerNums = new ArrayList<>(4);
        playerNums.add(0);
        playerNums.add(1);
        playerNums.add(2);
        playerNums.add(3);

        List<PlayerType> types = new ArrayList<>(4);
        types.add(PlayerType.Human);
        types.add(PlayerType.Human);
        types.add(PlayerType.Human);
        types.add(PlayerType.Human);

        newGame(names, playerNums, types, 6);


//        Random rng = new Random();
//        freeCorridor = new Corridor(CorridorType.values()[rng.nextInt(3)]);
//
//
//        String playerName = "Player ";
//        int playerNum = 1;
//        int listIndex = 0;
//        int counter = 0;
//        List<Treasure> treasureList = new ArrayList<>();
//        Collections.addAll(treasureList, Treasure.values());
//        Collections.shuffle(treasureList);
//
//        players = new Player[4];
//        for (int idx = 0; idx < players.length; idx++) {
//            Queue<Treasure> trsrsToFind = new LinkedList<>();
//            for (int index = listIndex; index < counter + 6 && index < treasureList.size(); index++) {
//                trsrsToFind.add(treasureList.get(index));
//                listIndex++;
//            }
//            counter = listIndex;
//            players[idx] = new Player(playerName + playerNum, trsrsToFind,playerNum, COLS, PlayerType.Human);
//            playerNum++;
//        }
//
//        currentPlayer = 0;
//
//
//        field = new Corridor[COLS][ROWS];
//        for (int i = 0; i < STATIC_POSITIONS.length; i++) {
//            field[STATIC_POSITIONS[i].getCol()][STATIC_POSITIONS[i].getRow()] = STATIC_CORRIDORS[i];
//        }
//
//        for (int row = 0; row < ROWS; row++) {
//            for (int col = 0; col < COLS; col++) {
//
//                if (field[col][row] == null) {
//                    field[col][row] = new Corridor(CorridorType.values()[rng.nextInt(3)],
//                            Rotation.values()[rng.nextInt(4)],
//                            null);
//                }
//                gui.displayCorridor(col, row, field[col][row]);
//            }
//        }
//
//        listIndex = 0;
//        while (listIndex < AMOUNT_OF_TREASURES) {
//            int col = rng.nextInt(COLS);
//            int row = rng.nextInt(ROWS);
//            Corridor corr = field[col][row];
//            if (!corr.hasTreasure()) {
//                field[col][row] = new Corridor(corr.getType(), corr.getRotation(), treasureList.get(listIndex));
//                listIndex++;
//                gui.displayCorridor(col, row, field[col][row]);
//            }
//        }
//
//
//
//        //for now
//        gui.displayFreeCorridor(freeCorridor);
//        gui.setFiguresToCorners();
//        displayAllPlayers();


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
        displayAllPlayers();



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
//        for (Player player : players) {
//            gui.displayFigure(player.getPlayerNum(), player.getStartingCorner().getCol(),
//                    player.getStartingCorner().getRow());
//        }
        displayAllPlayers() ;
        gui.displayFreeCorridor(freeCorridor);
        /* @TODO */
        log = null;
    }


    private List<Treasure> setupPlayers(List<String> names, List<Integer> playerNums,
                              List<PlayerType> types, int treasurePerPlayer) {
        int listIndex = 0;
        int counter = 0;
        List<Treasure> treasureList = new ArrayList<>();
        Collections.addAll(treasureList, Treasure.values());
        Collections.shuffle(treasureList);

        players = new Player[types.size()];
        for (int idx = 0; idx < players.length; idx++) {
            Queue<Treasure> trsrsToFind = new LinkedList<>();
            for (int index = listIndex;
                 index < counter + treasurePerPlayer && index < treasurePerPlayer * players.length; index++) {
                trsrsToFind.add(treasureList.get(index));
                listIndex++;
            }
            counter = listIndex;
            players[idx] = new Player(names.get(idx), trsrsToFind, playerNums.get(idx), COLS, types.get(idx));
        }

        return treasureList;
    }

    private void shuffleField(int treasureAmount, List<Treasure> treasureList) {

        Random rng = new Random();
        freeCorridor = new Corridor(CorridorType.values()[rng.nextInt(3)]);

        field = new Corridor[COLS][ROWS];

        for (int i = 0; i < STATIC_POSITIONS.length; i++) {
            field[STATIC_POSITIONS[i].getCol()][STATIC_POSITIONS[i].getRow()] = STATIC_CORRIDORS[i];
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                if (field[col][row] == null) {
                    field[col][row] = new Corridor(CorridorType.values()[rng.nextInt(3)],
                            Rotation.values()[rng.nextInt(4)],
                            null);
                }
                gui.displayCorridor(col, row, field[col][row]);
            }
        }

        int listIndex = 0;
        while (listIndex < treasureAmount && listIndex < (COLS * ROWS - 4)) {
            int col = rng.nextInt(COLS);
            int row = rng.nextInt(ROWS);
            Corridor corr = field[col][row];
            if (new Position(col, row).notCorner(field.length) && !corr.hasTreasure()) {
                field[col][row] = new Corridor(corr.getType(), corr.getRotation(), treasureList.get(listIndex));
                listIndex++;
                gui.displayCorridor(col, row, field[col][row]);
            }
        }
        gui.displayFreeCorridor(freeCorridor);
    }

    private void cleanAttributes() {
        currentPlayer = 0;
        inAnimation = false;
        pushed = false;
        unblockOnGUI();
        blockedPos = null;
        targetLocation = null;
    }

    public void newGame(List<String> names, List<Integer> playerNums, List<PlayerType> types, int treasurePerPlayer) {
        List<Treasure> treasureList = setupPlayers(names, playerNums, types, treasurePerPlayer);
        cleanAttributes();

//        currentPlayer = 0;
//        inAnimation = false;
//        pushed = false;
//        unblockOnGUI();
//        blockedPos = null;
//        targetLocation = null;

        gui.cleanUpGui(getCurrentPlayer().getPlayerNum());
        shuffleField(treasurePerPlayer * players.length, treasureList);
        displayAllPlayers();

        for (Player player : players) {
            gui.setupPlayerInfo(player.getPlayerNum(), player.getName(), player.getTreasuresLeft());
        }
    }

    private void displayAllPlayers() {
        for (Player player : players) {
            gui.displayFigure(player.getPlayerNum(), player.getPos().getCol(),
                    player.getPos().getRow());
        }
    }


    public void makeTurn() {

    }

    /**
     * Pushes the free corridor into a row or col
     * @param col - column of edge to push
     * @param row - row of edge to push
     */
    public void pushCorridor(int col, int row) {

        if (new Position(col, row).isPushablePos(field.length) && (blockedPos == null ||
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
            //gui.displayFreeCorridor(freeCorridor);
            pushed = true;
        }
    }

    public void pushAnimationFinished(List<Position> positions) {
        for (Position pos : positions) {
            gui.displayCorridor(pos.getCol(), pos.getRow(), getCorridorAtPos(pos));
        }
        gui.displayFreeCorridor(freeCorridor);
        displayAllPlayers();

        for (Player player : players) {
            checkAndCollectTreasure(player, player.getPos());
        }

        inAnimation = false;
    }

    private void addTreasureIndexIfExists(List<Integer> treasureIndices, Corridor corr) {
        if (corr.hasTreasure()) {
            treasureIndices.add(corr.getTreasure().getIndex());
        }
    }

    /**
     * Pushes the free corridor from top to bottom at column col
     * @param col - column to push down at
     */
    private void pushTopToBottom(int col) {
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

        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.c == col) {
                if (pos.r == lastRow) {
                    player.setPos(new Position(col, 0));
                } else {
                    player.setPos(new Position(col, pos.r+1));
                }
                playersToMove.add(player.getPlayerNum());
            }
        }
        inAnimation = true;
        gui.animatePush(Direction.DOWN, positions, field[col][0], treasureIndices, playersToMove, this);

        //Sets the Position that can't be pushed
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
        //Lists for animation
        List<Position> positions = new ArrayList<>();
        List<Integer> treasureIndices = new ArrayList<>();
        List<Integer> playersToMove = new ArrayList<>();

        for (int row = 0; row < lastRow;  row++) {
            field[col][row] = field[col][row + 1];
            positions.add(new Position(col, row));
            addTreasureIndexIfExists(treasureIndices, field[col][row]);
        }

        field[col][lastRow] = freeCorridor;
        positions.add(new Position(col, lastRow));
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        freeCorridor = newFreeCorridor;
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.c == col) {
                if (pos.r == 0) {
                    player.setPos(new Position(col, lastRow));
                } else {
                    player.setPos(new Position(col, pos.r-1));
                }
                playersToMove.add(player.getPlayerNum());
            }
        }

        inAnimation = true;
        gui.animatePush(Direction.UP, positions, field[col][lastRow], treasureIndices, playersToMove, this);

        //Sets the Position that can't be pushed
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
        //Lists for animation
        List<Position> positions = new ArrayList<>();
        List<Integer> treasureIndices = new ArrayList<>();
        List<Integer> playersToMove = new ArrayList<>();

        for (int col = lastCol; col > 0;  col--) {
            field[col][row] = field[col - 1][row];
            positions.add(new Position(col, row));
            addTreasureIndexIfExists(treasureIndices, field[col][row]);
        }
        field[0][row] = freeCorridor;
        positions.add(new Position(0, row));
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        freeCorridor = newFreeCorridor;
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.r == row) {
                if (pos.c == lastCol) {
                    player.setPos(new Position(0, row));
                } else {
                    player.setPos(new Position(pos.c + 1, row));
                }
                playersToMove.add(player.getPlayerNum());
            }

        }

        inAnimation = true;
        gui.animatePush(Direction.RIGHT, positions, field[0][row], treasureIndices, playersToMove, this);

        //Sets the Position that can't be pushed
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
        //Lists for animation
        List<Position> positions = new ArrayList<>();
        List<Integer> treasureIndices = new ArrayList<>();
        List<Integer> playersToMove = new ArrayList<>();

        for (int col = 0; col < lastCol;  col++) {
            field[col][row] = field[col + 1][row];
            positions.add(new Position(col, row));
            addTreasureIndexIfExists(treasureIndices, field[col][row]);
        }

        field[lastCol][row] = freeCorridor;
        positions.add(new Position(lastCol, row));
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        freeCorridor = newFreeCorridor;
        addTreasureIndexIfExists(treasureIndices, freeCorridor);

        //Putting this into a separate method would require 7 parameters
        for (Player player : players) {
            Position pos = player.getPos();
            if (pos.r == row) {
                if (pos.c == 0) {
                    player.setPos(new Position(lastCol, row));
                } else {
                    player.setPos(new Position(pos.c - 1, row));
                }
                playersToMove.add(player.getPlayerNum());
            }

        }

        inAnimation = true;
        gui.animatePush(Direction.LEFT, positions, field[lastCol][row], treasureIndices, playersToMove,this);

        //Sets the Position that can't be pushed
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
            player.setPos(target);
        }
    }

    private void checkAndCollectTreasure(Player player, Position target) {

        Corridor targetCorr = getCorridorAtPos(target);
        if (targetCorr.hasTreasure()) {
            if (player.collectTreasure(targetCorr.getTreasure())) {
                gui.removeTreasureFromGrid(targetCorr.getTreasure());
                targetCorr.removeTreasure();
                gui.displayCorridor(target.c, target.r, targetCorr);
                gui.updateTreasuresLeft(player.getPlayerNum(), player.getTreasuresLeft());
            }
        }
    }

    public void moveAnimationFinished(Player player, Position target) {
        checkAndCollectTreasure(player, target);

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
    public void saveGameFromGSON() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<FieldCard[]> parsedField = new ArrayList<>(field.length);

        for (int col = 0; col < field[0].length; col++) {
            FieldCard[] parsedCards = new FieldCard[field.length];
            for (int row = 0; row < field.length; row++) {
                Corridor corr = field[col][row];
                int parsedTreasure = corr.hasTreasure() ? corr.getTreasure().getIndex() + 1 : 0;
                FieldCard parsedCard = new FieldCard(corr.getType().ordinal(),
                        corr.getRotation().ordinal(), parsedTreasure);
                parsedCards[row] = parsedCard;
            }
            parsedField.add(parsedCards);
        }

        int parsedFreeTreasure = freeCorridor.hasTreasure() ? freeCorridor.getTreasure().getIndex() + 1 : 0;
        int blockedX;
        int blockedY;
        if (blockedPos == null) {
            blockedX = -1;
            blockedY = -1;
        } else if (blockedPos.getCol() == 0) {
            blockedX = -1;
            blockedY = blockedPos.getRow();
        } else if (blockedPos.getRow() == 0) {
            blockedX = blockedPos.getCol();
            blockedY = -1;
        } else if (blockedPos.getCol() == field.length - 1) {
            blockedX = field.length;
            blockedY = blockedPos.getRow();
        } else {
            blockedX = blockedPos.getCol();
            blockedY = field.length;
        }

        FreeWayCard parsedFreeCard = new FreeWayCard(freeCorridor.getType().ordinal(),
                freeCorridor.getRotation().ordinal(), parsedFreeTreasure, new ParsedPosition(blockedX, blockedY));

        int parsedCurrentPlayer = getCurrentPlayer().getPlayerNum();

//        ParsedPlayer(boolean involved, String name, int directedBy,
//        ParsedPosition position, ArrayList<Integer> treasureCards) {

        ParsedPlayer[] parsedPlayers = new ParsedPlayer[4];
        for(int idx = 0; idx < players.length; idx++) {
            List<Integer> treasures = new ArrayList<>();
            for (Treasure treasure : players[idx].getTargetTreasures()) {
                treasures.add(treasure.getIndex() + 1);
            }
            parsedPlayers[players[idx].getPlayerNum()] = new ParsedPlayer(true,
                    players[idx].getName(), players[idx].getType().ordinal(),
                   new ParsedPosition(players[idx].getPos().getCol(), players[idx].getPos().getRow()) , treasures);
        }
        for (int idx = 0; idx < parsedPlayers.length; idx++) {
            if (parsedPlayers[idx] == null) {
                List<Integer> empty = Collections.emptyList();
                parsedPlayers[idx] = new ParsedPlayer(false, "Spieler " + (idx + 1),
                        0, new ParsedPosition(0, 0), empty);
            }
        }

        List<ParsedPlayer> listParsedPlayers = new ArrayList<>();
        Collections.addAll(listParsedPlayers, parsedPlayers);

        GameParser parsedGame = new GameParser(parsedField, parsedFreeCard, parsedCurrentPlayer, listParsedPlayers);


        File file = new File("src/logic/saveFiles/test.json");

        Writer writer = new FileWriter(file);
        gson.toJson(parsedGame, writer);
        writer.flush();
        writer.close();

    }

    /**
     * Loads a game state from json/gson
     * creates GameParser instance from the file, from wich the game logic is build
     */
    public void loadGameToGSON(File f) throws FileNotFoundException {

       // File f = new File("src/logic/saveFiles/LabyrinthTest.json");
        Reader r = new FileReader(f);

        Gson gson = new Gson();
        GameParser graphData = gson.fromJson(r, GameParser.class);

        cleanAttributes();

        int parsedCurrentPlayer = graphData.getCurrentPlayer();
        List<Integer> numOfParticipants = new ArrayList<>();
        int counter = 0;
        for (ParsedPlayer parsedPlayer : graphData.getPlayers()) {
            if (parsedPlayer.isInvolved()) {
                numOfParticipants.add(counter);
            }
            counter++;
        }

        players = new Player[numOfParticipants.size()];
        for (int index = 0; index < players.length; index++) {
            ParsedPlayer parsedPlayer = graphData.getPlayers().get(numOfParticipants.get(index));

            Queue<Treasure> treasureQueue = new LinkedList<>();
            for (int treasureNum : parsedPlayer.getTreasureCards()) {
                treasureQueue.add(Treasure.values()[treasureNum - 1]);
            }

            players[index] = new Player(parsedPlayer.getName(), treasureQueue, numOfParticipants.get(index),
                    field.length, PlayerType.values()[parsedPlayer.getDirectedBy()]);
            players[index].setPos(new Position(parsedPlayer.getPosition().getX(), parsedPlayer.getPosition().getY()));
            if (players[index].getPlayerNum() == parsedCurrentPlayer) {
                currentPlayer = index;
            }
        }

        gui.cleanUpGui(getCurrentPlayer().getPlayerNum());

        field = new Corridor[graphData.getField().size()][graphData.getField().get(0).length];
        for (int row = 0; row < field[0].length; row++) {
            for (int col = 0; col < field.length; col++) {
                FieldCard fieldCard =  graphData.getField().get(col)[row];
                int treasureIndex = fieldCard.getTreasure();
                Treasure treasure;
                if (treasureIndex == 0) {
                    treasure = null;
                } else if (treasureIndex > 0 && treasureIndex <= AMOUNT_OF_TREASURES) {
                    treasure = Treasure.values()[treasureIndex - 1];
                } else {
                    throw new IllegalArgumentException(treasureIndex + " is not a valid Treasure number");
                }
                Corridor corr = new Corridor(CorridorType.values()[fieldCard.getType()],
                        Rotation.values()[fieldCard.getRotated()],
                        treasure);
                field[col][row] = corr;
                gui.displayCorridor(col, row, field[col][row]);
            }
        }
        int freeTreasureIndex = graphData.getFreeWayCard().getTreasure();
        Treasure freeTreasure;
        if (freeTreasureIndex == 0) {
            freeTreasure = null;
        } else if (freeTreasureIndex > 0 && freeTreasureIndex <= AMOUNT_OF_TREASURES) {
            freeTreasure = Treasure.values()[freeTreasureIndex - 1];
        } else {
            throw new IllegalArgumentException(freeTreasureIndex + " is not a valid Treasure number");
        }
        freeCorridor = new Corridor(CorridorType.values()[graphData.getFreeWayCard().getType()],
                Rotation.values()[graphData.getFreeWayCard().getRotated()],
                freeTreasure);
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
        displayAllPlayers();
        for (Player player : players) {
            gui.setupPlayerInfo(player.getPlayerNum(), player.getName(), player.getTreasuresLeft());
        }


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

package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.*;

/**
 * Class containing the logic of the game labyrinth
 */
public class GameLogic {

    /** All static positions of a regular game */
    static final Position[] STATIC_POSITIONS = {
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

    /** All corridors at static positions of a regular game */
    static final Corridor[] STATIC_CORRIDORS = {
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

    /** Default Field size per the game rules, always square 7*7 */
    public static final int FIELD_SIZE = 7;

    /** Default amount of treasures */
    public static final int AMOUNT_OF_TREASURES = 24;

    /** Log file */
    private final File log;

    /** GUI to display the game */
    private final GUIConnector gui;

    /** Field of this game */
    private final Field field;

    /** The players */
    private Player[] players;

    /** The players array index who's turn it currently is */
    private int currentPlayer;

    /** helps with flow control, signals if a push operation has been done */
    private boolean pushed = false;

    /** signals if the GUI is currently in animation */
    private boolean inAnimation = false;

    /** The target of the currentPlayer, so it does not need to be recalculated everytime */
    private Position targetLocation;

    /** The next move of the currentPlayer, used by the AI after a push operation */
    private Position nextMove;

    /**
     * Constructor for a default game.
     * A default game uses default size, the static corridors and default amount of treasures.
     * The non static corridors will be random.
     * The players will be 4 Human's with default names.
     * @param gui - connection to the GUI
     */
    public GameLogic(GUIConnector gui) {
        this.gui = gui;

        gui.setupFigures();

        List<String> names = new ArrayList<>(4);
        names.add("Name 1");
        names.add("Name 2");
        names.add("Name 3");
        names.add("Name 4");

        List<Integer> playerNumbs = new ArrayList<>(4);
        playerNumbs.add(0);
        playerNumbs.add(1);
        playerNumbs.add(2);
        playerNumbs.add(3);

        List<PlayerType> types = new ArrayList<>(4);
        types.add(PlayerType.Human);
        types.add(PlayerType.Human);
        types.add(PlayerType.Human);
        types.add(PlayerType.Human);

        field = new Field(FIELD_SIZE);

        newGame(names, playerNumbs, types, 6);

        /* @TODO */
        log = null;
    }

    /**
     * Creates a game. The field is input as a string, it is
     * assumed the string is correct, as this is just used for testing
     * @param gui - connection to the GUI
     * @param field - the field consisting of corridors
     */
    GameLogic(GUIConnector gui, String field, Player[] players) {
        this.gui = gui;
        this.players = players;
        currentPlayer = 0;

        this.field = new Field(gui, field);

        gui.setupFigures();
        displayAllPlayers() ;
        gui.displayFreeCorridor(this.field.getFreeCorridor());

        /* @TODO */
        log = null;
    }

    /**
     * Creates a new game with all passed attributes. Field is random, using the default static corridors etc.
     * @param names - names for the players
     * @param playerNumbs - numbers of the players
     * @param types - types of the players
     * @param treasurePerPlayer - treasure amount each player has to find
     */
    public void newGame(List<String> names, List<Integer> playerNumbs, List<PlayerType> types, int treasurePerPlayer) {
        List<Treasure> treasureList = setupPlayers(names, playerNumbs, types, treasurePerPlayer);
        cleanAttributes();

        gui.cleanUpGui(getCurrentPlayer().getPlayerNum());
        field.shuffleField(treasurePerPlayer * players.length, treasureList, gui);
        displayAllPlayers();

        for (Player player : players) {
            gui.setupPlayerInfo(player.getPlayerNum(), player.getName(), player.getTreasuresLeft());
        }
    }

    /**
     * Sets up the players from the given parameters.
     * @param names - List of player names
     * @param playerNumbs - list of player numbers (not player array indices)
     * @param types - list of player types
     * @param treasurePerPlayer - treasures to find per player
     * @return List of all treasures in random order used for the players targets
     */
    private List<Treasure> setupPlayers(List<String> names, List<Integer> playerNumbs,
                              List<PlayerType> types, int treasurePerPlayer) {
        int listIndex = 0;
        int counter = 0;
        List<Treasure> treasureList = new ArrayList<>();
        Collections.addAll(treasureList, Treasure.values());
        Collections.shuffle(treasureList);

        players = new Player[types.size()];
        for (int idx = 0; idx < players.length; idx++) {
            Queue<Treasure> trsrsToFind = new LinkedList<>();
            /*
            for as long as less then treasuresPerPlayer have been added, adds another treasure to the
            target queue of the player from the treasureList
            */
            for (int index = listIndex;
                 index < counter + treasurePerPlayer && index < treasurePerPlayer * players.length; index++) {
                trsrsToFind.add(treasureList.get(index));
                listIndex++;
            }
            counter = listIndex;
            players[idx] = new Player(names.get(idx), trsrsToFind, playerNumbs.get(idx), FIELD_SIZE, types.get(idx));
        }
        return treasureList;
    }

    /**
     * Clears all flow control attributes, used when starting or loading a game
     */
    private void cleanAttributes() {
        currentPlayer = 0;
        inAnimation = false;
        pushed = false;
        nextMove = null;
        unblockOnGUI();
        field.clearBlockedPos();
        targetLocation = null;
    }

    /**
     * Signals that a turn is over
     */
    public void nextTurn() {
        if (!inAnimation && pushed) {
            pushed = false;
            nextPlayer();
        }
    }
    
    /**
     * Signaling that the current player is done with its turn -> next players turn
     */
    private void nextPlayer() {
        int oldPlayerNum = getCurrentPlayer().getPlayerNum();
        if (++currentPlayer >= players.length) {
            currentPlayer = 0;
        }
        clearTargetLoc();
        gui.highlightNewPlayer(oldPlayerNum, getCurrentPlayer().getPlayerNum());
        makeTurn();
    }

    /**
     * Makes a turn, if the current player is not Human. If the AI for some reason didn't find a move,
     * the current player will be treated as human and wait for GUI input.
     */
    public void makeTurn() {
        if (getCurrentPlayer().getType() != PlayerType.Human) {
            AIMove foundMove = getCurrentPlayer().findMove(new Field(field));
            if (foundMove != null) {
                Position pushPos = foundMove.getPushPos();
                nextMove = foundMove.getPlayerMove();
                field.getFreeCorridor().setRotation(foundMove.getFreeCorridorRot());
                pushCorridor(pushPos);
            }
        }
    }

    /**
     * Pushes the free corridor at the specified pushPos
     * @param pushPos - the position to push at
     */
    private void pushCorridor(Position pushPos) {
        Position blockedPos = field.getBlockedPos();
        int col = pushPos.getCol();
        int row = pushPos.getRow();
        if (pushPos.isPushablePos(field.getFieldSize()) && (blockedPos == null ||
                (blockedPos.getCol() != col || blockedPos.getRow() != row))) {
            unblockOnGUI();
            PushAnimationParams pushParams;
            if (col == 0) {
                pushParams = field.pushLeftToRight(row, players);
                gui.changeArrowCol(field.getFieldSize() + 1, row + 1, true);
            } else if (row == 0) {
                pushParams = field.pushTopToBottom(col, players);
                gui.changeArrowCol(col + 1, field.getFieldSize() + 1, true);
            } else if (col == field.getFieldSize() - 1) {
                pushParams = field.pushRightToLeft(row, players);
                gui.changeArrowCol(0, row + 1, true);
            } else {
                pushParams = field.pushBottomToTop(col, players);
                gui.changeArrowCol(col+1, 0, true);
            }
            clearTargetLoc();
            pushed = true; //@TODO might affect stuff
            inAnimation = true;
            gui.animatePush(pushParams, this);

        }
    }

    /**
     * Method to be called after the push animation has finished
     * @param positions - the positions that got pushed
     */
    public void pushAnimationFinished(List<Position> positions) {
        for (Position pos : positions) {
            gui.displayCorridor(pos.getCol(), pos.getRow(), getCorridorAtPos(pos));
        }
        gui.displayFreeCorridor(field.getFreeCorridor());
        displayAllPlayers();

        for (Player player : players) {
            checkAndCollectTreasure(player, player.getPos());
        }
        inAnimation = false;
        if (nextMove != null) {
            moveFigureOfPlayer(getCurrentPlayer(), nextMove);
        }
    }

    /**
     * Moves the figure of player to pos.
     * @param player - player figure to move
     * @param target - Position to move to
     */
    private void moveFigureOfPlayer(Player player, Position target) {
        Position[] path = field.findPath(player.getPos(), target);
        if (path != null) {
            if (player.getPos().equals(target)) {
                moveAnimationFinished(player, target);
            } else {
                Position oldPos = player.getPos();
                player.setPos(target);
                inAnimation = true;
                gui.animateFigure(player, oldPos, path,this);

            }
        }
    }

    /**
     * Method to be called after the move animation finishes
     * @param player - player that moved
     * @param target - position the player moved to
     */
    public void moveAnimationFinished(Player player, Position target) {
        checkAndCollectTreasure(player, target);
        nextMove = null;
        inAnimation = false;
        if (player.checkIfWon(target)) {
            gameWon(player);
        } else {
            nextTurn();
        }
    }

    /**
     * Collect the treasure at Position "target", if it's the player's "player" next target.
     * Handles the GUI work related to collecting a treasure.
     * @param player - the player to collect the treasure
     * @param target - the position to collect at
     */
    private void checkAndCollectTreasure(Player player, Position target) {
        Corridor targetCorr = getCorridorAtPos(target);
        if (targetCorr != null && targetCorr.hasTreasure()) {
            if (player.collectTreasure(targetCorr.getTreasure())) {
                gui.removeTreasureFromGrid(targetCorr.getTreasure());
                targetCorr.removeTreasure();
                gui.displayCorridor(target.getCol(), target.getRow(), targetCorr);
                gui.updateTreasuresLeft(player.getPlayerNum(), player.getTreasuresLeft());
            }
        }
    }

    /**
     * Rotates the free corridor to the left
     */
    public void rotateFreeCorridorLeft() {
        field.rotateFreeCorridorLeft();
        Corridor freeCorridor = field.getFreeCorridor();
        gui.displayFreeCorridor(freeCorridor);
    }

    /**
     * Rotates the free corridor to the right
     */
    public void rotateFreeCorridorRight() {
        field.rotateFreeCorridorRight();
        Corridor freeCorridor = field.getFreeCorridor();
        gui.displayFreeCorridor(freeCorridor);
    }

    /**
     * Clears the target location (the highlighted cell wich is the currentPlayers target)
     */
    private void clearTargetLoc() {
        if (targetLocation != null) {
            Corridor targetCorr = getCorridorAtPos(targetLocation);
            if (targetCorr != null) {
                gui.endHighlightOfCorr(targetLocation.getCol(), targetLocation.getRow(),
                        targetCorr.getTreasure());
                targetLocation = null;
            }
        } else {
            gui.endHighlightOfCorr(-1, -1, field.getFreeCorridor().getTreasure());
        }
    }

    /**
     * Unblocks the blockedPos on the GUI. (Paints the arrow green)
     */
    private void unblockOnGUI() {
        Position blockedPos = field.getBlockedPos();
        if (blockedPos != null && blockedPos.isValidPos(field.getFieldSize())) {
            int col = blockedPos.getCol();
            int row = blockedPos.getRow();
            if (col == 0) {
                row++;
            } else if (row == 0) {
                col++;
            }
            else if (col == field.getFieldSize() - 1) {
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
     * Display's all players on the GUI
     */
    private void displayAllPlayers() {
        for (Player player : players) {
            gui.displayFigure(player.getPlayerNum(), player.getPos().getCol(),
                    player.getPos().getRow());
        }
    }

    /**
     * React to click on grid
     * @param col - col of cell clicked
     * @param row - row of cell clicked
     * @param gridSize - size of the GUI grid (larger the field)
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
                        colToPush = field.getFieldSize() - 1;
                    } else {
                        colToPush--;
                        rowToPush = field.getFieldSize() - 1;
                    }
                    Position pushPos = new Position(colToPush, rowToPush);
                    if (pushPos.isEdge(field.getFieldSize())) {
                        pushCorridor(pushPos);
                    }
                }
            }
        }
    }

    /**
     * React to the mouse entering a cell
     * @param col - col entered
     * @param row - row entered
     * @param gridSize - size of the GUI grid (larger the field)
     */
    public void cellEntered(int col, int row, int gridSize) {
        if (!inAnimation) {
            Position pos = new Position(col, row);
            if (!pos.isEdge(gridSize) && pos.notCorner(gridSize)) {
                if (field.findPath(getCurrentPlayer().getPos(), new Position(col-1, row-1)) != null) {
                    gui.highlightCellGreen(col, row);
                } else {
                    gui.highlightCellRed(col, row);
                }
            }
        }
    }

    /**
     * Reacts to the H-Key being pressed/held down
     * @param released - boolean signaling if the key got released
     */
    public void hKeyPressed(boolean released) {
        if (targetLocation == null) {
            targetLocation = field.calcCurrentTarget(getCurrentPlayer());
        }
        if (targetLocation == null) {
            if (released) {
                gui.endHighlightOfCorr(-1, -1, field.getFreeCorridor().getTreasure());
            } else {
                gui.highlightCorrBlue(-1, -1, field.getFreeCorridor().getTreasure());
            }
        } else {
            Treasure target = getCurrentPlayer().nextTreasure();
            if (released) {
                gui.endHighlightOfCorr(targetLocation.getCol(), targetLocation.getRow(), target);
            } else {
                gui.highlightCorrBlue(targetLocation.getCol(), targetLocation.getRow(), target);
            }
        }
    }

    /**
     * Opens a dialogue that the player has won
     * @param player - the player that won
     */
    private void gameWon(Player player) {
        gui.endGameDialogue(player.getName());
    }

    /**
     * Prints s to the log
     * @param s - the string to be added
     */
    public void log(String s) {
        /* @TODO */
    }

    /**
     * Saves the game state to json with gson
     * creates GameParser instance - wich the can be easily converted to json using gson.
     */
    public void saveGameToJSON() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<ParsedCorridor[]> parsedField = field.createParsedField();

        Corridor freeCorridor = field.getFreeCorridor();
        Position blockedPos = field.getBlockedPos();
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
        } else if (blockedPos.getCol() == field.getFieldSize() - 1) {
            blockedX = field.getFieldSize();
            blockedY = blockedPos.getRow();
        } else {
            blockedX = blockedPos.getCol();
            blockedY = field.getFieldSize();
        }

        ParsedFreeWayCard parsedFreeCard = new ParsedFreeWayCard(freeCorridor.getType().ordinal(),
                freeCorridor.getRotation().ordinal(), parsedFreeTreasure, new ParsedPosition(blockedX, blockedY));

        int parsedCurrentPlayer = getCurrentPlayer().getPlayerNum();

        ParsedPlayer[] parsedPlayers = new ParsedPlayer[4];
        for (Player player : players) {
            List<Integer> treasures = new ArrayList<>();
            for (Treasure treasure : player.getTargetTreasures()) {
                treasures.add(treasure.getIndex() + 1);
            }
            parsedPlayers[player.getPlayerNum()] = new ParsedPlayer(true,
                    player.getName(), player.getType().ordinal(),
                    new ParsedPosition(player.getPos().getCol(), player.getPos().getRow()), treasures);
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
     * Loads a game state from json with gson.
     * Creates GameParser instance from the file, with wich the game can be build.
     */
    public void loadGameFromJSON(File f) throws FileNotFoundException, IllegalArgumentException,
            com.google.gson.JsonParseException {

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
                    field.getFieldSize(), PlayerType.values()[parsedPlayer.getDirectedBy()]);
            players[index].setPos(new Position(parsedPlayer.getPosition().getX(), parsedPlayer.getPosition().getY()));
            if (players[index].getPlayerNum() == parsedCurrentPlayer) {
                currentPlayer = index;
            }
        }
        gui.cleanUpGui(getCurrentPlayer().getPlayerNum());
        field.setupFromParsed(graphData, gui);
        displayAllPlayers();
        for (Player player : players) {
            gui.setupPlayerInfo(player.getPlayerNum(), player.getName(), player.getTreasuresLeft());
        }
    }

    /**
     * Gets the number of players
     * @return the number of players
     */
    public int getNumOfPlayers() {
        return players.length;
    }

    /**
     * Gets the current player
     * @return the current player
     */
    Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    /**
     * Gets the corridor at position pos
     * @param pos - the position
     * @return the corridor at pos
     */
    private Corridor getCorridorAtPos(Position pos) {
        if (pos.isValidPos(field.getFieldSize())) {
            return field.getCorridorAtPos(pos);
        }
        return null;
    }

    /**
     * Gets the field of this game.
     * @return the field
     */
    Corridor[][] getField() {
        return field.getField();
    }

    /**
     * Gets player at index, used for testing.
     * @param index - index of player
     * @return player at index
     */
    Player getPlayerX(int index) {
        return players[index];
    }

    /**
     * Gets the corridor at column c and row r. Used for testing
     * @param c - column of corridor
     * @param r - row of corridor
     * @return the corridor at [c][r]
     */
    Corridor getCorridorAtX(int c, int r) {
        return field.getCorridorAtX(c, r);
    }

    public void showError(Exception e) {
        gui.displayErrorAlert(e);
    }
}

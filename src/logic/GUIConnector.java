package logic;

public interface GUIConnector {

    /**
     * Sets up 4 figures to be used on the GUI
     */
    void setupFigures();

    /**
     * Shows image of corridor at location
     * @param col - Column the corridor is displayed at
     * @param row - Row the corridor is displayed at
     * @param corr - Corridor to be displayed
     */
    void displayCorridor(int col, int row, Corridor corr);

    /**
     * Shows the image of the free corridor
     * @param corr - corridor to be displayed
     */
    void displayFreeCorridor(Corridor corr);

    /**
     * Shows the figure of the player with the playerNumber at the location
     * @param playerNumber - number of the player
     * @param col - column where the player gets displayed at
     * @param row - row where the player gets displayed at
     */
    void displayFigure(int playerNumber, int col, int row);

    /**
     * Highlights a new player on the GUI
     * @param oldPlayerNum - number of the old player (to unhighlight)
     * @param newPlayerNum - number of new player to highlight
     */
    void highlightNewPlayer(int oldPlayerNum, int newPlayerNum);

    /**
     * Removes the treasure from the grid
     * @param treasure - the treasure to remove
     */
    void removeTreasureFromGrid(Treasure treasure);

    /**
     * Animates a push operation
     * @param params - the parameters needed for a push animation
     * @param game - the game instance to call animationFinished
     */
    void animatePush(PushAnimationParams params, GameLogic game);

    /**
     * Animates figure movement
     * @param player - the players figure to move
     * @param oldPos - the old position of the player (before moving)
     * @param positions - the path of the figure takes
     * @param game - the game instance to call animationFinished
     */
    void animateFigure(Player player, Position oldPos, Position[] positions, GameLogic game);

    /**
     * Sets up the players information on the GUI
     * @param playerNum - the players number
     * @param name - the players name
     * @param treasures - the players treasures left
     */
    void setupPlayerInfo(int playerNum, String name, int treasures);

    /**
     * Cleans the GUI elements related to the playerNum
     * @param playerNum - number of the player
     */
    void cleanUpGui(int playerNum);

    /**
     * Highlights a cell on the GUI in green
     * @param col - col of cell on GUI
     * @param row - row of cell on GUI
     */
    void highlightCellGreen(int col, int row);

    /**
     * Highlights a cell on the GUI in red
     * @param col - col of cell on GUI
     * @param row - row of cell on GUI
     */
    void highlightCellRed(int col, int row);

    /**
     * Ends the highlight of a corridor with its treasure
     * @param col - col of cell on GUI
     * @param row - row of cell on GUI
     * @param treasure - the treasure of the corridor
     */
    void endHighlightOfCorr(int col, int row, Treasure treasure);

    /**
     * Highlights a corridor in blue and highlight its treasure
     * @param col - col of cell on GUI
     * @param row - row of cell on GUI
     * @param treasure - the treasure of the corridor
     */
    void highlightCorrBlue(int col, int row, Treasure treasure);

    /**
     * Changes the colour of the arrows signaling valid pushPositions.
     * Blocked false = green, true = red.
     * @param col - col of cell on GUI
     * @param row - row of cell on GUI
     * @param blocked - true if red, false if green
     */
    void changeArrowCol(int col, int row, boolean blocked);

    /**
     * Updates the treasures left of a player on the GUI
     * @param playerNum - the players number
     * @param treasures - the players treasures to update
     */
    void updateTreasuresLeft(int playerNum, int treasures);

    /**
     * Opens a dialogue with the winners name.
     */
    void endGameDialogue(String winner);

    void displayErrorAlert(Exception e);

}

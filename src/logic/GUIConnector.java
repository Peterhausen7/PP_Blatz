package logic;


import java.util.List;

public interface GUIConnector {

    /**
     * temp method to display all 4 figures at the corners of the field
     */
    void setFiguresToCorners();

    /**
     * Shows image of corridor at location
     * @param col - Column the corridor is displayed at
     * @param row - Row the corridor is displayed at
     * @param corr - Corridor to be displayed
     */
    void displayCorridor(int col, int row, Corridor corr);

    void displayFreeCorridor(Corridor corr);

    void animateFreeCorridorRotation(Corridor corr);

    /**
     * Shows figure at location pos
     */
    void displayFigure(int playerNumber, int col, int row);

    void highlightNewPlayer(int oldPlayerNum, int newPlayerNum);

    void removeTreasureFromGrid(Treasure treasure);

    /**
     * Animates the push of a col/row
     * @param
     */
    void animatePush(Direction pushDir, List<Position> positions, Corridor pushedCorridor);

    /**
     * Animates figure movement
     */
    void animateFigure(Player player, Position[] path, GameLogic game);

    /**
     * Sets the player name on the GUI
     * @param player - players name to set
     */
    void setPlayerName(Player player);

    /**
     * Highlights a cell on the GUI
     * @param col - col of cell on GUI
     * @param row - row of cell on GUI
     */
    void highlightCellGreen(int col, int row);

    void highlightCellRed(int col, int row);

    void endHighlightOfCorr(int col, int row, Treasure treasure);

    void highlightCorrBlue(int col, int row, Treasure treasure);

    void changeArrowCol(int col, int row, boolean blocked);

    /**
     * Updates the treasures left of a player on the GUI
     * @param player - the players treasures to update
     */
    void updateTreasuresLeft(Player player);



    /** Updates the speed of the animation (can also turn it off)
     *
     */
    void updateAnimationSpeed();

    /**
     * Displays the game rules
     * (probably just open the PDF)
     */
    void displayRules();

    /**
     * Displays the game controls
     */
    void displayControls();

    /**
     * @TODO Some kind of window or dialogue when the game ends
     */
    void endGameDialogue(String winner);

}

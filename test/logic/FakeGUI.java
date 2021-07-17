package logic;

import logic.Corridor;
import logic.GUIConnector;
import logic.Player;

public class FakeGUI implements GUIConnector {

    @Override
    public void setFiguresToCorners() {

    }

    @Override
    public void displayCorridor(int col, int row, Corridor corr) {

    }

    @Override
    public void displayFreeCorridor(Corridor corr) {

    }

    @Override
    public void animateFreeCorridorRotation(Corridor corr) {

    }

    @Override
    public void displayFigure(int playerNumber, int col, int row) {

    }

    @Override
    public void highlightNewPlayer(int oldPlayerNum, int newPlayerNum) {

    }

    @Override
    public void removeTreasureFromGrid(Treasure treasure) {

    }

    @Override
    public void animatePush(Corridor corr) {

    }

    @Override
    public void animateFigure() {

    }

    @Override
    public void setPlayerName(Player player) {

    }

    @Override
    public void highlightCellGreen(int col, int row) {

    }

    @Override
    public void highlightCellRed(int col, int row) {

    }

    @Override
    public void endHighlightOfCorr(int col, int row, Treasure treasure) {

    }

    @Override
    public void highlightCorrBlue(int col, int row, Treasure treasure) {

    }

    @Override
    public void changeArrowCol(int col, int row, boolean blocked) {

    }


    @Override
    public void updateTreasuresLeft(Player player) {

    }

    @Override
    public void updateAnimationSpeed() {

    }

    @Override
    public void displayRules() {

    }

    @Override
    public void displayControls() {

    }

    @Override
    public void endGameDialogue(String winner) {

    }
}

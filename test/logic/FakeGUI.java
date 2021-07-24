package logic;

public class FakeGUI implements GUIConnector {

    @Override
    public void setupFigures() {
    }

    @Override
    public void displayCorridor(int col, int row, Corridor corr) {
    }

    @Override
    public void displayFreeCorridor(Corridor corr) {
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
    public void animatePush(PushAnimationParams params, GameLogic game) {
    }

    @Override
    public void animateFigure(Player player, Position oldPos, Position[] positions, GameLogic game) {
    }

    @Override
    public void setupPlayerInfo(int playerNum, String name, int treasures) {
    }

    @Override
    public void cleanUpGui(int playerNum) {
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
    public void updateTreasuresLeft(int playerNum, int treasures) {
    }


    @Override
    public void endGameDialogue(String winner) {
    }

    @Override
    public void displayErrorAlert(Exception e) {
    }
}

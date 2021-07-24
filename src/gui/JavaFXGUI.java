package gui;

import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import logic.*;

import java.util.List;

public class JavaFXGUI implements GUIConnector {

    private static final int ROT_DEGREES = 90;

    private static final double ANIMATION_FACTOR_PUSH = 5000;

    private static final double ANIMATION_FACTOR_FIGURE = 2000;

    /**
     * Images of corridors, order as in Enum CorridorType
     */
    private static final Image[] IMGS_OF_CORRIDORS = new Image[] {
            new Image("gui/images/I.png"),
            new Image("gui/images/L.png"),
            new Image("gui/images/T.png")
    };

    /**
     * Images of treasures
     */
    private static final Image[] IMGS_OF_TREASURES = new Image[] {
            new Image("gui/images/s1.png"),
            new Image("gui/images/s2.png"),
            new Image("gui/images/s3.png"),
            new Image("gui/images/s4.png"),
            new Image("gui/images/s5.png"),
            new Image("gui/images/s6.png"),
            new Image("gui/images/s7.png"),
            new Image("gui/images/s8.png"),
            new Image("gui/images/s9.png"),
            new Image("gui/images/s10.png"),
            new Image("gui/images/s11.png"),
            new Image("gui/images/s12.png"),
            new Image("gui/images/s13.png"),
            new Image("gui/images/s14.png"),
            new Image("gui/images/s15.png"),
            new Image("gui/images/s16.png"),
            new Image("gui/images/s17.png"),
            new Image("gui/images/s18.png"),
            new Image("gui/images/s19.png"),
            new Image("gui/images/s20.png"),
            new Image("gui/images/s21.png"),
            new Image("gui/images/s22.png"),
            new Image("gui/images/s23.png"),
            new Image("gui/images/s24.png")
    };

    /**
     * The gridpane used for the GUI
     */
    private final GridPane gridPane;

    /**
     * Image views corresponding to each gridpane cell
     */
    private final ImageView[][] imageViews;

    private final ImageView[] treasures;

    private final ImageView[] freeCorridorIVs;

    private final HBox[] playerBoxes;

    private final Label[] playerLabels;

    private final Label[] treasureLabels;

    /**
     * The player figures represented as circles, all 4 for now
     */
    private final Circle[] figures = new Circle[4];

    private final Button rotateLeftBtn;
    private final Button rotateRightBtn;
    private final Button endTurnBtn;

    private final CheckBox checkFigureAnimation;

    private final CheckBox checkPushAnimation;

    private final Slider figureAnimSlider;

    private final Slider pushAnimSlider;

    public JavaFXGUI(GridPane gridPane, ImageView[][] imageViews, int treasureCount, ImageView[] freeCorridorIVs,
                     HBox[] playerBoxes, Button rotateLeftBtn,
                     Button rotateRightBtn, Button endTurnBtn, Label[] playerLabels, Label[] treasureLabels,
                     CheckBox checkFigureAnimation, CheckBox checkPushAnimation,
                     Slider figureAnimSlider, Slider pushAnimSlider) {


        this.gridPane = gridPane;
        this.imageViews = imageViews;
        this.freeCorridorIVs = freeCorridorIVs;
        this.playerBoxes = playerBoxes;
        this.treasures = new ImageView[treasureCount];
        this.rotateLeftBtn = rotateLeftBtn;
        this.rotateRightBtn = rotateRightBtn;
        this.endTurnBtn = endTurnBtn;
        this.endTurnBtn.setDisable(true);
        this.playerLabels = playerLabels;
        this.treasureLabels = treasureLabels;
        this.checkFigureAnimation = checkFigureAnimation;
        this.checkPushAnimation = checkPushAnimation;
        this.figureAnimSlider = figureAnimSlider;
        this.pushAnimSlider = pushAnimSlider;
        for (int index = 0; index < treasureCount; index++) {
          //  this.treasures[index] = new ImageView();
            this.treasures[index] = new ImageView(IMGS_OF_TREASURES[index]);
            this.treasures[index].fitWidthProperty().bind(gridPane.widthProperty()
                    .divide(gridPane.getColumnConstraints().size()));
            this.treasures[index].fitHeightProperty().bind(gridPane.heightProperty()
                    .divide(gridPane.getRowConstraints().size()));
            this.treasures[index].setMouseTransparent(true);
            this.treasures[index].setVisible(false);
        }
    }



    @Override
    public void setupFigures() {
        for (int i = 0; i < figures.length; i++) {
            figures[i] = new Circle(0, 0 ,0);
            figures[i].radiusProperty().bind(gridPane.widthProperty().divide(gridPane.getColumnCount() * 5));
            GridPane.setHalignment(figures[i], HPos.CENTER);
        }
        figures[0].setFill(Color.YELLOW);
        gridPane.add(figures[0], 1, 1);

        figures[1].setFill(Color.BLUE);
        gridPane.add(figures[1], 7, 1);

        figures[2].setFill(Color.GREEN);
        gridPane.add(figures[2], 7, 7);

        figures[3].setFill(Color.RED);
        gridPane.add(figures[3], 1, 7);

        for (Circle figure : figures) {
            figure.setVisible(false);
            figure.setMouseTransparent(true);
        }
    }

    private void setImageViewToGridCell(ImageView imgView, int col, int row) {
        //imageViews[col][row] = imgView;
        GridPane.setColumnIndex(imgView, col);
        GridPane.setRowIndex(imgView, row);
        imgView.setTranslateX(0);
        imgView.setTranslateY(0);
        imgView.setManaged(true);
    }

    public void setImageRot(Corridor corr, ImageView imgView) {
        int rotAmount = 0;
        //I am using this instead of ordinal(), so the rotation of the img does not break when more hypothetical
        //rotations are added to the enum.
        switch (corr.getRotation()) {
            case RIGHT:
                rotAmount = 1;
                break;
            case UPSIDE_DOWN:
                rotAmount = 2;
                break;
            case LEFT:
                rotAmount = 3;
                break;
        }
        imgView.setRotate(ROT_DEGREES * rotAmount);
        imgView.setRotate(corr.getRotation().ordinal() * 90);
    }

    @Override
    public void removeTreasureFromGrid(Treasure treasure) {
        gridPane.getChildren().remove(treasures[treasure.getIndex()]);
    }

    private void displayTreasureAtCell(int treasureIndex, int col, int row) {
        ImageView imgView = treasures[treasureIndex];
        if (imgView.isVisible()) {
            setImageViewToGridCell(imgView, col, row);
        } else {
            gridPane.add(imgView, col, row);
            imgView.setVisible(true);
        }
        imgView.setTranslateX(0);
        imgView.setTranslateY(0);
    }

    @Override
    public void displayCorridor(int col, int row, Corridor corr) {
        if (corr == null) {
            imageViews[col+1][row+1].setImage(null);
        } else {
            Image img = IMGS_OF_CORRIDORS[corr.getType().ordinal()];
            imageViews[col+1][row+1].setImage(img);
            imageViews[col+1][row+1].setTranslateX(0);
            imageViews[col+1][row+1].setTranslateY(0);
            setImageRot(corr, imageViews[col+1][row+1]);


//            if (corr.hasTreasure()) {
//
//                int treasureIndex = corr.getTreasure().getIndex();
//                ImageView imgView = treasures[treasureIndex];
//                if (imgView.isVisible()) {
//                    setImageViewToGridCell(treasures[treasureIndex], col+1, row+1);
//                } else {
//                    gridPane.add(imgView, col+1, row+1);
//                    imgView.setVisible(true);
//                }
//            }

            if (corr.hasTreasure()) {
                displayTreasureAtCell( corr.getTreasure().getIndex(), col + 1, row + 1);
            }
        }
    }

    @Override
    public void displayFreeCorridor(Corridor corr) {
        freeCorridorIVs[0].setImage(IMGS_OF_CORRIDORS[corr.getType().ordinal()]);
        setImageRot(corr, freeCorridorIVs[0]);
        if (corr.hasTreasure()) {
            int treasureIndex = corr.getTreasure().getIndex();
            freeCorridorIVs[1].setImage(IMGS_OF_TREASURES[treasureIndex]);
            gridPane.getChildren().remove(treasures[treasureIndex]);
            treasures[treasureIndex].setVisible(false);
        } else {
            freeCorridorIVs[1].setImage(null);
        }
    }

    @Override
    public void displayFigure(int playerNumber, int col, int row) {
        Circle figure =  figures[playerNumber];
        figure.setVisible(true);
        GridPane.setColumnIndex(figure, col+1);
        GridPane.setRowIndex(figure, row+1);
        figure.toFront();
    }

    @Override
    public void highlightNewPlayer(int oldPlayerNum, int newPlayerNum) {
        playerBoxes[oldPlayerNum].setStyle("-fx-background-color: #a8a8a8");
        playerBoxes[newPlayerNum].setStyle("-fx-background-color: lime");
    }

    @Override
    public void changeArrowCol(int col, int row, boolean blocked) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(1.0);
        lighting.setSpecularExponent(1.0);
        lighting.setSurfaceScale(1.0);
        Color color = (blocked) ? Color.INDIANRED : Color.LIME;
        lighting.setLight(new Light.Distant(100, 100, color));
        imageViews[col][row].setEffect(lighting);
    }

    private void highlightImg(ImageView imgView, Color col) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(100, 100, col));
        imgView.setEffect(lighting);
    }

    @Override
    public void highlightCellGreen(int col, int row) {
        highlightImg(imageViews[col][row], Color.LIME);
    }

    @Override
    public void highlightCellRed(int col, int row) {
        highlightImg(imageViews[col][row], Color.INDIANRED);
    }

    @Override
    public void highlightCorrBlue(int col, int row, Treasure treasure) {
        ImageView imgView;
        if (col < 0 || row < 0) {
            imgView = freeCorridorIVs[0];
            freeCorridorIVs[1].setScaleX(1.5);
            freeCorridorIVs[1].setScaleY(1.5);
        } else {
            imgView = imageViews[col+1][row+1];
        }

        if (treasure != null) {
            treasures[treasure.getIndex()].setScaleX(1.25);
            treasures[treasure.getIndex()].setScaleY(1.25);
        }
        highlightImg(imgView, Color.LIGHTBLUE);
    }

    @Override
    public void endHighlightOfCorr(int col, int row, Treasure treasure) {
        if (col < 0 || row < 0) {
            for (ImageView imgView : freeCorridorIVs) {
                imgView.setEffect(null);
                imgView.setScaleX(1);
                imgView.setScaleY(1);
            }
        } else {
            imageViews[col+1][row+1].setEffect(null);
        }
        if (treasure != null) {
            treasures[treasure.getIndex()].setScaleX(1);
            treasures[treasure.getIndex()].setScaleY(1);
        }
    }

    private void toggleRotateButtons(boolean disable) {
        rotateLeftBtn.setDisable(disable);
        rotateRightBtn.setDisable(disable);
        //endTurnBtn.setDisable(disable);
    }

    private void toggleEndTurnButton(Boolean disable) {
        endTurnBtn.setDisable(disable);
    }

    @Override
    public void animateFigure(Player player, Position oldPos, Position[] positions, GameLogic game) {
        final double rowHeight = gridPane.getHeight() / gridPane.getRowCount();
        final double colWidth = gridPane.getWidth() / gridPane.getColumnCount();
        final double oldPosX = oldPos.getCol() * colWidth;
        final double oldPosY = oldPos.getRow() * rowHeight;
        final int playerNum = player.getPlayerNum();

        double speed = checkFigureAnimation.isSelected() ?
                ANIMATION_FACTOR_FIGURE / figureAnimSlider.getValue() : 0;

        toggleRotateButtons(true);
        toggleEndTurnButton(true);

        PathTransition pathTransition = new PathTransition();
        Path path = new Path();

        path.getElements().add(new MoveTo(0, 0));
        for (int index = 0; index < positions.length; index++) {
            double xMove = positions[index].getCol() * colWidth - oldPosX;
            double yMove = positions[index].getRow() * rowHeight - oldPosY;

            path.getElements().add(new LineTo(xMove, yMove));
        }

        pathTransition.setOnFinished(e -> {
            figures[playerNum].setTranslateX(0);
            figures[playerNum].setTranslateY(0);
            GridPane.setColumnIndex(figures[playerNum], positions[positions.length-1].getCol() + 1);
            GridPane.setRowIndex(figures[playerNum], positions[positions.length-1].getRow() + 1);
            toggleRotateButtons(false);
            game.moveAnimationFinished(player, positions[positions.length-1]);
        });

        pathTransition.setDuration(Duration.millis(speed * positions.length));
        pathTransition.setNode(figures[playerNum]);
        pathTransition.setPath(path);


        pathTransition.play();
    }

    @Override
    public void animatePush(PushAnimationParams params,  GameLogic game) {
        Direction pushDir = params.getPushDir();
        List<Position> positions = params.getPositions();
        Corridor pushedCorridor = params.getPushedCorridor();
        List<Integer> treasureIndices = params.getTreasureIndices();
        List<Integer> playersToMove = params.getPlayerNumbs();
        final double rowHeight = gridPane.getHeight() / gridPane.getRowCount();
        final double colWidth = gridPane.getWidth() / gridPane.getColumnCount();
        double pushAmount = 0;
        int col = 0;
        int row = 0;

        Duration speed = checkPushAnimation.isSelected() ?
                Duration.millis(ANIMATION_FACTOR_PUSH / pushAnimSlider.getValue())  : Duration.ZERO;

        toggleRotateButtons(true);
        ParallelTransition moveAll = new ParallelTransition();
        ImageView freeCorridor = new ImageView(IMGS_OF_CORRIDORS[pushedCorridor.getType().ordinal()]);
        setImageRot(pushedCorridor, freeCorridor);
        TranslateTransition moveFreeCorr = new TranslateTransition(speed, freeCorridor);

        switch (pushDir) {
            case RIGHT:
                row = positions.get(0).getRow() + 1;
                pushAmount = colWidth;
                moveFreeCorr.byXProperty().set(pushAmount);
                break;
            case DOWN:
                col = positions.get(0).getCol() + 1;
                pushAmount = rowHeight;
                moveFreeCorr.byYProperty().set(pushAmount);
                break;
            case LEFT:
                col = imageViews.length - 1;
                row = positions.get(0).getRow() + 1;
                pushAmount = -colWidth;
                moveFreeCorr.byXProperty().set(pushAmount);
                break;
            case UP:
                col = positions.get(0).getCol() + 1;
                row = imageViews.length - 1;
                pushAmount = -rowHeight;
                moveFreeCorr.byYProperty().set(pushAmount);
                break;
        }
        moveAll.getChildren().add(moveFreeCorr);

        gridPane.add(freeCorridor, col, row);
        freeCorridor.fitWidthProperty().bind(gridPane.widthProperty()
                .divide(gridPane.getColumnConstraints().size()));
        freeCorridor.fitHeightProperty().bind(gridPane.heightProperty()
                .divide(gridPane.getRowConstraints().size()));

        if (pushedCorridor.hasTreasure()) {
            displayTreasureAtCell(pushedCorridor.getTreasure().getIndex(), col, row);
        }

        for (Position pos : positions) {
            ImageView imgView = imageViews[pos.getCol() + 1][pos.getRow() + 1];
            pushAnimationHelper(moveAll, imgView, pushDir, pushAmount, speed);
        }

        for (Integer index : treasureIndices) {
            ImageView imgView = treasures[index];
            pushAnimationHelper(moveAll, imgView, pushDir, pushAmount, speed);
        }

        for (Integer index : playersToMove) {
            pushAnimationHelper(moveAll, figures[index], pushDir, pushAmount, speed);
        }

        moveAll.setOnFinished(e -> {
            gridPane.getChildren().remove(freeCorridor);
            for (Integer index : playersToMove) {
               figures[index].setTranslateX(0);
               figures[index].setTranslateY(0);
            }
            toggleRotateButtons(false);
            toggleEndTurnButton(false);
            game.pushAnimationFinished(positions);
        });

        moveAll.play();

    }

    private void pushAnimationHelper(ParallelTransition moveAll, Node imgView,
                                     Direction pushDir, double pushAmount, Duration speed) {

        TranslateTransition movePos = new TranslateTransition(speed, imgView);
        switch (pushDir) {
            case UP:
            case DOWN:
                movePos.byYProperty().set(pushAmount);
                break;
            case LEFT:
            case RIGHT:
                movePos.byXProperty().set(pushAmount);
                break;
        }
        moveAll.getChildren().add(movePos);
    }


    @Override
    public void cleanUpGui(int playerNum) {
        for (ImageView treasure : treasures) {
            gridPane.getChildren().remove(treasure);
            treasure.setVisible(false);
        }
        for (Circle figure : figures) {
            figure.setVisible(false);
        }
        for (HBox box : playerBoxes) {
            box.setStyle("-fx-background-color: #a8a8a8");
            box.setVisible(false);
        }
        playerBoxes[playerNum].setStyle("-fx-background-color: lime");
    }






    @Override
    public void setupPlayerInfo(int playerNum, String name, int treasures) {
        playerLabels[playerNum].setText(name);
        treasureLabels[playerNum].setText("" + treasures);
        playerBoxes[playerNum].setVisible(true);
    }



    @Override
    public void updateTreasuresLeft(int playerNum, int treasures) {
        treasureLabels[playerNum].setText("" + treasures);
    }


    @Override
    public void endGameDialogue(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Spieler " + winner + " hat gewonnen!", ButtonType.OK);
        alert.show();
        alert.setOnHidden( e -> {
            if (alert.getResult() == ButtonType.OK || alert.getResult() == null) {
                FXMLDocumentcontroller.gameSettingsWindow.show();
            }
        });
    }

    @Override
    public void displayErrorAlert(Exception e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid save file");
        errorAlert.setContentText("Error while parsing save file: " + e);
        errorAlert.showAndWait();
    }


}

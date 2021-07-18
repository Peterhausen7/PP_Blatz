package gui;

import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;

public class JavaFXGUI implements GUIConnector {

    private static final int ROT_DEGREES = 90;

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

    /**
     * The player figures represented as circles, all 4 for now
     */
    private final Circle[] figures = new Circle[4];



    public JavaFXGUI(GridPane gridPane, ImageView[][] imageViews, int treasureCount, ImageView[] freeCorridorIVs,
                     HBox[] playerBoxes) {
        this.gridPane = gridPane;
        this.imageViews = imageViews;
        this.freeCorridorIVs = freeCorridorIVs;
        this.playerBoxes = playerBoxes;
        this.treasures = new ImageView[treasureCount];
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





    //all 4 for now, later this will be done according to amount of players
    @Override
    public void setFiguresToCorners() {
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
            figure.toFront();
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

    @Override
    public void displayCorridor(int col, int row, Corridor corr) {
        if (corr == null) {
            imageViews[col+1][row+1].setImage(null);
        } else {
            Image img = IMGS_OF_CORRIDORS[corr.getType().ordinal()];
            imageViews[col+1][row+1].setImage(img);

//            int rotAmount = 0;
//            //I am using this instead of ordinal(), so the rotation of the img does not break when more hypothetical
//            //rotations are added to the enum.
//            switch (corr.getRotation()) {
//                case RIGHT:
//                    rotAmount = 1;
//                    break;
//                case UPSIDE_DOWN:
//                    rotAmount = 2;
//                    break;
//                case LEFT:
//                    rotAmount = 3;
//                    break;
//            }
//            imageViews[col+1][row+1].setRotate(ROT_DEGREES * rotAmount);
//            imageViews[col+1][row+1].setRotate(corr.getRotation().ordinal() * 90);
            setImageRot(corr, imageViews[col+1][row+1]);

            if (corr.hasTreasure()) {
                //-1 since treasure start from 1, arrays from 0

//                img = IMGS_OF_TREASURES[corr.getTreasure()-1];
//
//                gridPane.getChildren().remove(treasures[corr.getTreasure()-1]);
//
//                ImageView imgView = new ImageView(img);
//                //binds treasure img size to gridpane, just like with corridors
//                imgView.fitWidthProperty().bind(gridPane.widthProperty()
//                        .divide(gridPane.getColumnConstraints().size()));
//                imgView.fitHeightProperty().bind(gridPane.heightProperty()
//                        .divide(gridPane.getRowConstraints().size()));
//                imgView.setMouseTransparent(true);
//                gridPane.add(imgView, col+1, row+1);
//
//                treasures[corr.getTreasure()-1] = imgView;

                //gridPane.getChildren().remove(treasures[corr.getTreasure()-1]);
                //gridPane.add(treasures[corr.getTreasure()-1], col+1, row+1);
                int treasureIndex = corr.getTreasure().getIndex();
                ImageView imgView = treasures[treasureIndex];
                if (imgView.isVisible()) {
                    setImageViewToGridCell(treasures[treasureIndex], col+1, row+1);
                } else {
                    gridPane.add(imgView, col+1, row+1);
                    imgView.setVisible(true);
                }
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
        Circle figure =  figures[playerNumber-1];
        figure.setVisible(true);
        GridPane.setColumnIndex(figure, col+1);
        GridPane.setRowIndex(figure, row+1);
        figure.toFront();
    }

    @Override
    public void highlightNewPlayer(int oldPlayerNum, int newPlayerNum) {
        playerBoxes[oldPlayerNum - 1].setStyle("-fx-background-color: #a8a8a8");
        playerBoxes[newPlayerNum - 1].setStyle("-fx-background-color: lime");
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

    @Override
    public void animateFigure(Player player, Position[] positions, GameLogic game) {
        final double rowHeight = gridPane.getHeight() / gridPane.getRowCount();
        final double colWidth = gridPane.getWidth() / gridPane.getColumnCount();
        final double oldPosX = player.getPos().getCol() * colWidth;
        final double oldPosY = player.getPos().getRow() * rowHeight;
        final int playerNum = player.getPlayerNum() - 1;

        GridPane.setColumnIndex(figures[playerNum], 1);
        GridPane.setRowIndex(figures[playerNum], 1);

        PathTransition pathTransition = new PathTransition();
        Path path = new Path();

        path.getElements().add(new MoveTo(oldPosX, oldPosY));
        for (int index = 0; index < positions.length; index++) {
            double xMove = positions[index].getCol() * colWidth;
            double yMove = positions[index].getRow() * rowHeight;

            path.getElements().add(new LineTo(xMove, yMove));
        }

        pathTransition.setOnFinished(e -> {
            figures[playerNum].setTranslateX(0);
            figures[playerNum].setTranslateY(0);
            GridPane.setColumnIndex(figures[playerNum], positions[positions.length-1].getCol() + 1);
            GridPane.setRowIndex(figures[playerNum], positions[positions.length-1].getRow() + 1);
            game.moveAnimationFinished(player, positions[positions.length-1]);
        });

        pathTransition.setDuration(Duration.millis(1000 * positions.length));
        //pathTransition.setDuration(Duration.millis(0));
        pathTransition.setNode(figures[playerNum]);
        pathTransition.setPath(path);

        pathTransition.play();
    }

    @Override
    public void animatePush(Direction pushDir, List<Position> positions, Corridor pushedCorridor) {
        final double rowHeight = gridPane.getHeight() / gridPane.getRowCount();
        final double colWidth = gridPane.getWidth() / gridPane.getColumnCount();
        double pushAmount = 0;
        int col = 0;
        int row = 0;
        ParallelTransition moveAll = new ParallelTransition();
        ImageView freeCorridor = new ImageView(IMGS_OF_CORRIDORS[pushedCorridor.getType().ordinal()]);
        TranslateTransition moveFreeCorr = new TranslateTransition(Duration.millis(1000), freeCorridor);
        switch (pushDir) {
            case RIGHT:
                row = positions.get(0).getRow();
                pushAmount = colWidth;
                moveFreeCorr.byXProperty().set(pushAmount);
                break;
            case DOWN:
                col = positions.get(0).getCol();
                pushAmount = rowHeight;
                moveFreeCorr.byYProperty().set(pushAmount);
                break;
            case LEFT:
                col = imageViews.length;
                row = positions.get(0).getRow();
                pushAmount = -colWidth;
                moveFreeCorr.byXProperty().set(pushAmount);
                break;
            case UP:
                col = positions.get(0).getCol();
                row = imageViews.length;
                pushAmount = -rowHeight;
                moveFreeCorr.byYProperty().set(pushAmount);
                break;
        }
        moveAll.getChildren().add(moveFreeCorr);
        //@TODO wrong rotation, treaures not moving, players not moving, col offset by 1
        gridPane.add(freeCorridor, col, row);
        freeCorridor.fitWidthProperty().bind(gridPane.widthProperty()
                .divide(gridPane.getColumnConstraints().size()));
        freeCorridor.fitHeightProperty().bind(gridPane.heightProperty()
                .divide(gridPane.getRowConstraints().size()));




        for (Position pos:positions) {
            ImageView imgView = imageViews[pos.getCol() + 1][pos.getRow() + 1];
            //imgView.setManaged(false);
            TranslateTransition movePos = new TranslateTransition(Duration.millis(1000), imgView);

            switch (pushDir) {
                case UP:
                    movePos.byYProperty().set(-rowHeight);
                    break;
                case DOWN:
                    movePos.byYProperty().set(rowHeight);
                    break;
                case LEFT:
                    movePos.byXProperty().set(-colWidth);
                    break;
                case RIGHT:
                    movePos.byXProperty().set(colWidth);
                    break;
            }
            moveAll.getChildren().add(movePos);
        }

        moveAll.play();
    }


    @Override
    public void animateFreeCorridorRotation(Corridor corr) {

    }








    @Override
    public void setPlayerName(Player player) {

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Spieler " + winner + " hat gewonnen!", ButtonType.OK);
        alert.show();
        alert.setOnHidden( e -> {
            if (alert.getResult() == ButtonType.OK || alert.getResult() == null) {
                FXMLDocumentcontroller.gameSettingsWindow.show();
            }
        });
    }
}

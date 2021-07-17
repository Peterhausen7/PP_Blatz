package gui;

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
import javafx.scene.shape.Circle;
import logic.Corridor;
import logic.GUIConnector;
import logic.Player;
import logic.Treasure;

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
    public void animateFreeCorridorRotation(Corridor corr) {

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
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK || alert.getResult() == null) {
            FXMLDocumentcontroller.gameSettingsWindow.show();
        }
    }
}

package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.GameLogic;
import logic.Player;
import logic.PlayerType;
import logic.Treasure;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

/**
 * Controller for the main FXML document/game window
 */
public class FXMLDocumentcontroller implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private VBox vBoxWrappingGridPane;

    @FXML
    private HBox hBoxWrappingVBox;

    @FXML
    private ImageView freeCorridorIV;

    @FXML
    private ImageView freeCorrTreasureIV;

    @FXML
    private Button btnRotateRight;

    @FXML
    private Button btnRotateLeft;

    @FXML
    private Button btnEndTurn;

    @FXML
    private Label labelPlayerOne;

    @FXML
    private Label labelPlayerTwo;

    @FXML
    private Label labelPlayerThree;

    @FXML
    private Label labelPlayerFour;

    @FXML
    private HBox hBoxPlayerOne;

    @FXML
    private HBox hBoxPlayerTwo;

    @FXML
    private HBox hBoxPlayerThree;

    @FXML
    private HBox hBoxPlayerFour;

    private static final Image PUSH_ARROW = new Image("gui/images/Arrow.png");

    private final ImageView[] pushArrows = new ImageView[12];

    private final HBox[] playerBoxes = new HBox[4];

    private final ImageView[] freeCorridorIVs = new ImageView[2];

    private GameLogic game;

    static Stage gameSettingsWindow;

    /**
     * All ImageViews in the cells of the grid. Larger then the logical field,
     * to have room for animations etc.
     */
    private final ImageView[][] imageViews = new ImageView[GameLogic.COLS + 2][GameLogic.ROWS + 2];



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        freeCorridorIVs[0] = freeCorridorIV;
        freeCorridorIVs[1] = freeCorrTreasureIV;
        playerBoxes[0] = hBoxPlayerOne;
        playerBoxes[1] = hBoxPlayerTwo;
        playerBoxes[2] = hBoxPlayerThree;
        playerBoxes[3] = hBoxPlayerFour;
        createGrid();
        createGameTemp();
        setupPushArrows();
        //createGame();
    }




    /**
     * Bind's the grid to the surrounding boxes, to keep it quadratic
     * Creates ImageView for each cell and writes it to imageViews array
     */
    private void createGrid() {

        //grdPn width and height both are bound to width of wrapping box
        gridPane.prefWidthProperty().bind(vBoxWrappingGridPane.widthProperty());
        gridPane.prefHeightProperty().bind(vBoxWrappingGridPane.widthProperty());

        //bind the width of the vBox to the height of the wrapping hBox
        vBoxWrappingGridPane.prefWidthProperty().bind(hBoxWrappingVBox.heightProperty());


        int colcount = gridPane.getColumnConstraints().size();
        int rowcount = gridPane.getRowConstraints().size();
//        int colcount = imageViews.length;
//        int rowcount = imageViews.length;

        // bind each Imageview to a cell of the gridpane
        for (int r = 0; r < rowcount; r++) {
            for (int c = 0; c < colcount; c++) {
                //creates an empty imageview
                imageViews[c][r] = new ImageView();

                final int col = c;
                final int row = r;
                imageViews[c][r].setOnMouseClicked(e -> {
                    game.gridClicked(col, row, colcount);
                });
                imageViews[c][r].setOnMouseEntered( e -> {
                    game.cellEntered(col, row, colcount);
                });
                imageViews[c][r].setOnMouseExited( e -> {
                    imageViews[col][row].setEffect(null);
                });
                imageViews[c][r].setPickOnBounds(true);
                gridPane.add(imageViews[c][r], c, r);


                //the image shall resize when the cell resizes
                imageViews[c][r].fitWidthProperty().bind(gridPane.widthProperty().divide(colcount));
                imageViews[c][r].fitHeightProperty().bind(gridPane.heightProperty().divide(rowcount));

            }
        }
    }

    private void setupPushArrows() {
        final float scale = 0.8f;

        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(1.0);
        lighting.setSpecularExponent(1.0);
        lighting.setSurfaceScale(1.0);
        lighting.setLight(new Light.Distant(100, 100, Color.LIME));

        for (int row = 2; row < imageViews.length-1; row += 2) {
            final ImageView imgView = imageViews[imageViews.length-1][row];
            imgView.setImage(PUSH_ARROW);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> {
                scalePushArrow(imgView, 1);
            });
            imgView.setOnMouseExited(e -> {
                scalePushArrow(imgView, scale);
            });

           imgView.setEffect(lighting);

        }
        for (int row = 2; row < imageViews.length-1; row += 2) {
            final ImageView imgView = imageViews[0][row];
            imgView.setImage(PUSH_ARROW);
            imgView.setRotate(180);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> {
                scalePushArrow(imgView, 1);
            });
            imgView.setOnMouseExited(e -> {
                scalePushArrow(imgView, scale);
            });

            imgView.setEffect(lighting);

        }
        for (int col = 2; col < imageViews.length-1; col += 2) {
            final ImageView imgView =  imageViews[col][0];
            imgView.setImage(PUSH_ARROW);
            imgView.setRotate(-90);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> {
                scalePushArrow(imgView, 1);
            });
            imgView.setOnMouseExited(e -> {
                scalePushArrow(imgView, scale);
            });

            imgView.setEffect(lighting);
        }
        for (int col = 2; col < imageViews.length-1; col += 2) {
            final ImageView imgView = imageViews[col][imageViews.length-1];
            imgView.setImage(PUSH_ARROW);
            imgView.setRotate(90);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> {
                scalePushArrow(imgView, 1);
            });
            imgView.setOnMouseExited(e -> {
                scalePushArrow(imgView, scale);
            });

            imgView.setEffect(lighting);
        }
    }

    private void scalePushArrow(ImageView imgView, double scale) {
        imgView.setScaleX(scale);
        imgView.setScaleY(scale);
    }



    /**
     * creates a game, temporary method to see stuff on the GUI
     */
    private void createGameTemp() {
        JavaFXGUI gui = new JavaFXGUI(gridPane, imageViews, GameLogic.AMOUNT_OF_TREASURES, freeCorridorIVs, playerBoxes);
        /* mock game for now, just random corridors, 4 figures in each corner*/
     //   game = new GameLogic(gui);

//       game = new GameLogic(gui,"L100,I101,T000,I102,L200\n" +
//                "I003,I104,I005,I106,I007\n" +
//                "T300,I108,T000,I109,T100\n" +
//                "I010,I111,I012,I113,I014\n" +
//                "L000,I115,T200,I116,L300", new Player[4]);

//        game = new GameLogic(gui,"L100,I100,T000,L200,T000,L100,L200\n" +
//                "I000,L100,I000,T100,I000,T100,L300\n" +
//                "T300,L100,T300,I000,T000,L300,T100\n" +
//                "T200,I000,I000,T100,I000,I000,L100\n" +
//                "T300,L100,T200,L000,T100,L100,T100\n" +
//                "I000,I000,L100,L200,L100,I000,L100\n" +
//                "L000,T100,T200,T100,T200,I100,L300", new Player[4]);
//
        Player[] players = new Player[4];
        Queue<Treasure> targets = new LinkedList<>();
        targets.add(Treasure.T06);
        players[0] = new Player("P1", targets, 1, imageViews.length - 2, PlayerType.Human);
        players[1] = new Player("P2", new LinkedList<>(), 2, imageViews.length - 2, PlayerType.Human);
        players[2] = new Player("P3", new LinkedList<>(), 3, imageViews.length - 2, PlayerType.Human);
        players[3] = new Player("P4", new LinkedList<>(), 4, imageViews.length - 2, PlayerType.Human);
                game = new GameLogic(gui,"L101,I100,T000,L200,T000,L100,L200\n" +
                "I000,L100,I000,T100,I002,T100,L300\n" +
                "T300,L107,T300,I013,T014,L300,T100\n" +
                "T206,I012,I005,T100,I003,I000,L115\n" +
                "T300,L100,T200,L008,T118,L111,T100\n" +
                "I009,I020,L100,L219,L110,I004,L100\n" +
                "L016,T100,T200,T100,T217,I100,L300", players);



        /* game from string*/
//        game = new GameLogic(gui, "L100,I101,T000,I102,L200\n" +
//                                       "I003,I104,I005,I106,I007\n" +
//                                       "T300,I108,T000,I109,T100\n" +
//                                       "I010,I111,I012,I113,I014\n" +
//                                       "L000,I115,T200,I116,L300", new Player[4]);

    }

    /**
     * creates a real game
     */
    private void createGame() {

    }

    public void showGameSettingsWindow(int numOfTreasures, int numOfPlayers) {
        //if the window hasn't been opened yet, it creates the stage
        if (gameSettingsWindow == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLGameSettings.fxml"));

            gameSettingsWindow = new Stage();
            try {
                gameSettingsWindow.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameSettingsWindow.setAlwaysOnTop(true);
            gameSettingsWindow.initModality(Modality.APPLICATION_MODAL);
            gameSettingsWindow.setMinHeight(425);
            gameSettingsWindow.setMinWidth(475);


            FXMLGameSettingsController ctrler = fxmlLoader.getController();
            ctrler.setupCBoxes(numOfTreasures, numOfPlayers);

            gameSettingsWindow.showAndWait();
            //this might be redundant, since modality takes care of it?
        } else if (gameSettingsWindow.isShowing()) {
            gameSettingsWindow.toFront();
            //else it shows it again
        } else {
            gameSettingsWindow.show();
        }
    }

    public void setTreasureHighlightEvent() {
        Scene scene = gridPane.getScene();
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.H) {
                game.hKeyPressed(false);
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.H) {
                game.hKeyPressed(true);
            }
        });
    }




    /**
     * Handles click on the "Spiel - Einstellungen" button
     * opens the start new game with game settings window
     */
    @FXML
    private void menuStartClick() {
        showGameSettingsWindow(GameLogic.AMOUNT_OF_TREASURES, game.getNumOfPlayers());
    }

    @FXML
    private void rotateLeft() {
        game.rotateFreeCorridorLeft();
    }

    @FXML
    private void rotateRight() {
        game.rotateFreeCorridorRight();
    }

    @FXML
    private void endTurn() {
        game.nextTurn();
    }
}

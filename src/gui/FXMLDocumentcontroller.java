package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.GameLogic;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the main FXML document/game window
 */
public class FXMLDocumentcontroller implements Initializable {

    /** GridPane representing the field */
    @FXML
    private GridPane gridPane;

    @FXML
    private VBox vBoxWrappingGridPane;

    @FXML
    private HBox hBoxWrappingVBox;
    /** ImageView of the free corridor */
    @FXML
    private ImageView freeCorridorIV;
    /** ImageView of the treasure of the free corridor */
    @FXML
    private ImageView freeCorrTreasureIV;
    /** The rotate right button*/
    @FXML
    private Button btnRotateRight;
    /** the rotate left button*/
    @FXML
    private Button btnRotateLeft;
    /** the end turn button */
    @FXML
    private Button btnEndTurn;
    /** the label of player one's name */
    @FXML
    private Label labelPlayerOne;
    /** the label of player two's name */
    @FXML
    private Label labelPlayerTwo;
    /** the label of player three's name */
    @FXML
    private Label labelPlayerThree;
    /** the label of player four's name */
    @FXML
    private Label labelPlayerFour;
    /** the label of player one's treasures left */
    @FXML
    private Label labelPlayerOneTreasure;
    /** the label of player two's treasures left */
    @FXML
    private Label labelPlayerTwoTreasure;
    /** the label of player three's treasures left */
    @FXML
    private Label labelPlayerThreeTreasure;
    /** the label of player four's treasures left */
    @FXML
    private Label labelPlayerFourTreasure;
    /** the hbox holding player one's information */
    @FXML
    private HBox hBoxPlayerOne;
    /** the hbox holding player two's information */
    @FXML
    private HBox hBoxPlayerTwo;
    /** the hbox holding player three's information */
    @FXML
    private HBox hBoxPlayerThree;
    /** the hbox holding player four's information */
    @FXML
    private HBox hBoxPlayerFour;
    /** Checkbox for turning on/off the figure animation */
    @FXML
    private CheckBox checkFigureAnimation;
    /** Checkbox for turning on/off the push animation */
    @FXML
    private CheckBox checkPushAnimation;
    /** The slider for the figure animation speed */
    @FXML
    private Slider figureAnimSlider;
    /** the slider for the push animation speed */
    @FXML
    private Slider pushAnimSlider;

    /** Custom of the animation checkboxes and sliders */
    @FXML
    private CustomMenuItem customMenuOne;
    @FXML
    private CustomMenuItem customMenuTwo;
    @FXML
    private CustomMenuItem customMenuFigure;
    @FXML
    private CustomMenuItem customMenuPush;

    /** Image of the push arrow */
    private static final Image PUSH_ARROW = new Image("gui/images/Arrow.png");
    /** The game instance */
    static GameLogic game;
    /**  The game settings window instance */
    static Stage gameSettingsWindow;
    /** min width/height of grid cell */
    private static int MIN_WIDTH_HEIGHT_OF_CELL = 10;
    /** min width/height of grid cell */
    private static int PREF_WITH_HEIGHT_OF_CELL = 100;

    /** the HBoxes of the players */
    private final HBox[] playerBoxes = new HBox[4];
    /** The imageView's representing a free corridor */
    private final ImageView[] freeCorridorIVs = new ImageView[2];
    /** The labels of player names */
    private final Label[] playerLabels = new Label[4];
    /** The labels of players treasures left */
    private final Label[] treasureLabels = new Label[4];


    /**
     * All ImageViews in the cells of the grid. Larger then the logical field,
     * to have room for animations etc.
     */
    private final ImageView[][] imageViews = new ImageView[GameLogic.FIELD_SIZE + 2][GameLogic.FIELD_SIZE + 2];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        freeCorridorIVs[0] = freeCorridorIV;
        freeCorridorIVs[1] = freeCorrTreasureIV;
        playerBoxes[0] = hBoxPlayerOne;
        playerBoxes[1] = hBoxPlayerTwo;
        playerBoxes[2] = hBoxPlayerThree;
        playerBoxes[3] = hBoxPlayerFour;
        playerLabels[0] = labelPlayerOne;
        playerLabels[1] = labelPlayerTwo;
        playerLabels[2] = labelPlayerThree;
        playerLabels[3] = labelPlayerFour;
        treasureLabels[0] = labelPlayerOneTreasure;
        treasureLabels[1] = labelPlayerTwoTreasure;
        treasureLabels[2] = labelPlayerThreeTreasure;
        treasureLabels[3] = labelPlayerFourTreasure;
        createGrid();
        createGame();
        setupPushArrows();
        setupAnimationMenu();
    }

    /**
     * Bind's the grid to the surrounding boxes, to keep it quadratic
     * Creates ImageView for each cell and writes it to imageViews array
     * Creates mouse click, enter and exit events for the imageViews.
     */
    private void createGrid() {
        int colcount = imageViews.length;
        int rowcount = imageViews.length;

        //sets up the column and row constraints for the grid
        for (int idx = 0; idx < colcount; idx++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setMinWidth(MIN_WIDTH_HEIGHT_OF_CELL);
            colConst.setHgrow(Priority.SOMETIMES);
            colConst.setPrefWidth(PREF_WITH_HEIGHT_OF_CELL);
            RowConstraints rowConst = new RowConstraints();
            rowConst.setMinHeight(MIN_WIDTH_HEIGHT_OF_CELL);
            rowConst.setPrefHeight(PREF_WITH_HEIGHT_OF_CELL);
            rowConst.setVgrow(Priority.SOMETIMES);
            gridPane.getColumnConstraints().add(colConst);
            gridPane.getRowConstraints().add(rowConst);
        }
        //grdPn width and height both are bound to width of wrapping box
        gridPane.prefWidthProperty().bind(vBoxWrappingGridPane.widthProperty());
        gridPane.prefHeightProperty().bind(vBoxWrappingGridPane.widthProperty());
        //bind the width of the vBox to the height of the wrapping hBox
        vBoxWrappingGridPane.prefWidthProperty().bind(hBoxWrappingVBox.heightProperty());
        // bind each Imageview to a cell of the gridpane
        for (int r = 0; r < rowcount; r++) {
            for (int c = 0; c < colcount; c++) {
                //creates an empty imageview
                imageViews[c][r] = new ImageView();
                final int col = c;
                final int row = r;
                //sets the imageview events
                imageViews[c][r].setOnMouseClicked(e -> game.gridClicked(col, row, colcount));
                imageViews[c][r].setOnMouseEntered( e -> game.cellEntered(col, row, colcount));
                imageViews[c][r].setOnMouseExited( e -> imageViews[col][row].setEffect(null));
                imageViews[c][r].setPickOnBounds(true);
                gridPane.add(imageViews[c][r], c, r);
                //the image shall resize when the cell resizes
                imageViews[c][r].fitWidthProperty().bind(gridPane.widthProperty().divide(colcount));
                imageViews[c][r].fitHeightProperty().bind(gridPane.heightProperty().divide(rowcount));

            }
        }
    }

    /**
     * creates a game
     */
    private void createGame() {
        JavaFXGUI gui = new JavaFXGUI(gridPane, imageViews, GameLogic.AMOUNT_OF_TREASURES, freeCorridorIVs,
                playerBoxes, btnRotateLeft, btnRotateRight, btnEndTurn, playerLabels, treasureLabels,
                checkFigureAnimation, checkPushAnimation, figureAnimSlider, pushAnimSlider);
        game = new GameLogic(gui);
    }

    /**
     * Sets up the push arrows
     */
    private void setupPushArrows() {
        final float scale = 0.8f;
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(1.0);
        lighting.setSpecularExponent(1.0);
        lighting.setSurfaceScale(1.0);
        lighting.setLight(new Light.Distant(100, 100, Color.LIME));

        for (int row = 2; row < imageViews.length-2; row += 2) {
            final ImageView imgView = imageViews[imageViews.length-1][row];
            imgView.setImage(PUSH_ARROW);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> scalePushArrow(imgView, 1));
            imgView.setOnMouseExited(e -> scalePushArrow(imgView, scale));

            imgView.setEffect(lighting);
        }
        for (int row = 2; row < imageViews.length-2; row += 2) {
            final ImageView imgView = imageViews[0][row];
            imgView.setImage(PUSH_ARROW);
            imgView.setRotate(180);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> scalePushArrow(imgView, 1));
            imgView.setOnMouseExited(e -> scalePushArrow(imgView, scale));

            imgView.setEffect(lighting);
        }
        for (int col = 2; col < imageViews.length-2; col += 2) {
            final ImageView imgView =  imageViews[col][0];
            imgView.setImage(PUSH_ARROW);
            imgView.setRotate(-90);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> scalePushArrow(imgView, 1));
            imgView.setOnMouseExited(e -> scalePushArrow(imgView, scale));

            imgView.setEffect(lighting);
        }
        for (int col = 2; col < imageViews.length-2; col += 2) {
            final ImageView imgView = imageViews[col][imageViews.length-1];
            imgView.setImage(PUSH_ARROW);
            imgView.setRotate(90);
            scalePushArrow(imgView, scale);

            imgView.setOnMouseEntered(e -> scalePushArrow(imgView, 1));
            imgView.setOnMouseExited(e -> scalePushArrow(imgView, scale));

            imgView.setEffect(lighting);
        }
    }

    /**
     * Sets up the animation submenu
     */
    private void setupAnimationMenu() {
        customMenuOne.setHideOnClick(false);
        customMenuTwo.setHideOnClick(false);
        customMenuFigure.setHideOnClick(false);
        customMenuPush.setHideOnClick(false);
        checkFigureAnimation.setOnAction(e -> {
            if (checkFigureAnimation.isSelected()) {
                figureAnimSlider.setDisable(false);
            } else {
                figureAnimSlider.setDisable(true);
            }
        });
        checkPushAnimation.setOnAction(e -> {
            if (checkPushAnimation.isSelected()) {
                pushAnimSlider.setDisable(false);
            } else {
                pushAnimSlider.setDisable(true);
            }
        });
    }

    /**
     * Method called when mouse enters or exits a push arrow. Highlights it by scaling
     * @param imgView - image view to highlight
     * @param scale - amount to scale
     */
    private void scalePushArrow(ImageView imgView, double scale) {
        imgView.setScaleX(scale);
        imgView.setScaleY(scale);
    }

    /**
     * Opens the game settings window
     */
    void showGameSettingsWindow() {
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

            gameSettingsWindow.showAndWait();
            //this might be redundant, since modality takes care of it?
        } else if (gameSettingsWindow.isShowing()) {
            gameSettingsWindow.toFront();
            //else it shows it again
        } else {
            gameSettingsWindow.show();
        }
    }

    /**
     * Sets the event for the H key press.
     */
    void setTreasureHighlightEvent() {
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
        showGameSettingsWindow();
    }

    /**
     * Handles click on the rotate left button. Rotates the free corridor 90 to the left
     */
    @FXML
    private void rotateLeft() {
        game.rotateFreeCorridorLeft();
    }

    /**
     * Handles click on the rotate left button. Rotates the free corridor 90 to the right
     */
    @FXML
    private void rotateRight() {
        game.rotateFreeCorridorRight();
    }

    /**
     * Handles the "Zug beenden" button, ends the current players turn. (Skip his movement phase)
     */
    @FXML
    private void endTurn() {
        btnEndTurn.setDisable(true);
        game.nextTurn();

    }

    /**
     * Saves the game to json
     * @throws IOException - something went wrong with the file
     */
    @FXML
    private void saveGame() throws IOException {
        game.saveGameToJSON();
    }
}

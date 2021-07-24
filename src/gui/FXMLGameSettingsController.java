package gui;

import com.google.gson.JsonParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import logic.GameLogic;
import logic.PlayerType;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Controller class for the game settings/game start window
 */
public class FXMLGameSettingsController implements Initializable {

    private final String[] typesOfPlayers = {
            "Mensch", "KI", "KI++"
    };

    /**
     * Choice boxes for type of the players (human, AI, advancedAI/AI++)
     */
    @FXML
    private ChoiceBox<String> typeOfPlayer1;

    @FXML
    private ChoiceBox<String> typeOfPlayer2;

    @FXML
    private ChoiceBox<String> typeOfPlayer3;

    @FXML
    private ChoiceBox<String> typeOfPlayer4;

    @FXML
    private CheckBox checkBoxOne;

    @FXML
    private CheckBox checkBoxTwo;

    @FXML
    private CheckBox checkBoxThree;

    @FXML
    private CheckBox checkBoxFour;

    @FXML
    private TextField textFieldOne;

    @FXML
    private TextField textFieldTwo;

    @FXML
    private TextField textFieldThree;

    @FXML
    private TextField textFieldFour;

    @FXML
    private Button buttonStart;

    private ChoiceBox<String>[] choiceBoxesTypes;

    private CheckBox[] checkBoxesParticipating;

    private TextField[] textFieldsNames;

    /**
     * Choice box to select treasure's per player for the game
     */
    @FXML
    private ChoiceBox<Integer> treasureCBox;

    @FXML
    private VBox settingsVBox;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxesTypes = new ChoiceBox[4];
        choiceBoxesTypes[0] = typeOfPlayer1;
        choiceBoxesTypes[1] = typeOfPlayer2;
        choiceBoxesTypes[2] = typeOfPlayer3;
        choiceBoxesTypes[3] = typeOfPlayer4;

        checkBoxesParticipating = new CheckBox[]{checkBoxOne, checkBoxTwo, checkBoxThree, checkBoxFour};

        textFieldsNames = new TextField[]{textFieldOne, textFieldTwo, textFieldThree, textFieldFour};

        setupGameSettingsWindow();
    }

    /**
     * setups the window
     */
     private void setupGameSettingsWindow() {
        ObservableList<String> playerTypesList = FXCollections.observableList(Arrays.asList(typesOfPlayers));

        ArrayList<ChoiceBox<String>> playerCboxes = new ArrayList<>(choiceBoxesTypes.length);
        playerCboxes.add(typeOfPlayer1);
        playerCboxes.add(typeOfPlayer2);
        playerCboxes.add(typeOfPlayer3);
        playerCboxes.add(typeOfPlayer4);
        for (ChoiceBox<String> cBox : playerCboxes) {
            cBox.setItems(playerTypesList);
            cBox.setValue(typesOfPlayers[0]);
        }

        for (CheckBox cBox : checkBoxesParticipating) {
            cBox.setOnAction(e -> {
                setActivatedNodes();
                calcAvailTreasures();
            });
        }
    }

    private void setActivatedNodes() {
         for (int idx = 0; idx < checkBoxesParticipating.length; idx++) {
             if (checkBoxesParticipating[idx].isSelected()) {
                 textFieldsNames[idx].setDisable(false);
                 choiceBoxesTypes[idx].setDisable(false);
             } else {
                 textFieldsNames[idx].setDisable(true);
                 choiceBoxesTypes[idx].setDisable(true);
             }
         }
    }

    private void calcAvailTreasures() {
        int activePlayers = 0;
        for (CheckBox cBox : checkBoxesParticipating) {
            if (cBox.isSelected()) {
                activePlayers++;
            }
        }

        if (activePlayers != 0) {
            Set<Integer> trsrOpts = new HashSet<>();
            for (int i = 1; i <= GameLogic.AMOUNT_OF_TREASURES; i++) {
                trsrOpts.add(i/activePlayers);
            }
            trsrOpts.remove(0);
            List<Integer> trsrOptsList = new ArrayList<>(trsrOpts);
            ObservableList<Integer> treasureList = FXCollections.observableList(trsrOptsList);
            treasureCBox.setItems(treasureList);
            treasureCBox.setValue(treasureList.get(treasureList.size() - 1));
            treasureCBox.setDisable(false);
            buttonStart.setDisable(false);
        } else {
            //treasureCBox.setItems(FXCollections.emptyObservableList());
            treasureCBox.setDisable(true);
            buttonStart.setDisable(true);
        }
    }

    @FXML
    private void loadGame() {
        //Step 1: Figure out where we are currently are, so we can open the dialog in
        //the same directory the jar is located. See also
        //https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
        File currDir = null;
        try {
            currDir = new File(FXMLGameSettingsController.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        } catch (URISyntaxException ex) {
            //oops... ¯\_(ツ)_/¯
            //guess we won't be opening the dialog in the right directory
        }
        //Step 2: Put it together
        FileChooser fileChooser = new FileChooser();
        if (currDir != null) {
            //ensure the dialog opens in the correct directory
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.setTitle("Open JSON Graph-File");
        //Step 3: Open the Dialog (set window owner, so nothing in the original window
        //can be changed)
        File selectedFile = fileChooser.showOpenDialog( checkBoxOne.getScene().getWindow());

        try {
            FXMLDocumentcontroller.game.loadGameFromJSON(selectedFile);
        } catch (FileNotFoundException | IllegalArgumentException | JsonParseException e) {
            FXMLDocumentcontroller.gameSettingsWindow.close();
            FXMLDocumentcontroller.game.showError(e);
        }
        FXMLDocumentcontroller.gameSettingsWindow.close();
    }

    @FXML
    private void startGame() {
         //name, number, type, numoftrsrs

        List<String> names = new ArrayList<>();
        List<Integer> playerNums = new ArrayList<>();
        List<PlayerType> types = new ArrayList<>();
        int treasuresPerPlayer = treasureCBox.getValue();

        for (int idx = 0; idx < checkBoxesParticipating.length; idx++) {
            if (checkBoxesParticipating[idx].isSelected()) {
                names.add(textFieldsNames[idx].getText());
                playerNums.add(idx);
                String typeName = choiceBoxesTypes[idx].getValue();
                PlayerType type = PlayerType.Human;
                if (typeName.equals(typesOfPlayers[1])) {
                    type = PlayerType.AI;
                } else if (typeName.equals(typesOfPlayers[2])) {
                    type = PlayerType.advancedAI;
                }
                types.add(type);
            }
        }

        FXMLDocumentcontroller.game.newGame(names, playerNums, types, treasuresPerPlayer);
        FXMLDocumentcontroller.gameSettingsWindow.close();
    }



}

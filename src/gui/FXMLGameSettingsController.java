package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.*;

/**
 * Controller class for the game settings/game start window
 */
public class FXMLGameSettingsController implements Initializable {

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

    /**
     * Choice box to select treasure's per player for the game
     */
    @FXML
    private ChoiceBox<Integer> treasureCBox;

    @FXML
    private VBox settingsVBox;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //setupCBoxes();
    }

    public VBox getSettingsVBox() {
        return settingsVBox;
    }



    /**
     * setups the choice boxes, no real logic here right now, just to see it on the GUI already
     * @param treasures - max treasures of the game
     * @param numOfPlayers - number of players
     */
     void setupCBoxes(int treasures, int numOfPlayers) {
        String[] playerTypes = {"Mensch", "KI", "KI++"};
        ObservableList<String> playerTypesList = FXCollections.observableList(Arrays.asList(playerTypes));

        ArrayList<ChoiceBox<String>> playerCboxes = new ArrayList<>(4);
        playerCboxes.add(typeOfPlayer1);
        playerCboxes.add(typeOfPlayer2);
        playerCboxes.add(typeOfPlayer3);
        playerCboxes.add(typeOfPlayer4);
        for (ChoiceBox<String> cBox : playerCboxes) {
            cBox.setItems(playerTypesList);
            cBox.setValue(playerTypes[0]);
        }


        /* @TODO do this getting player number from the ticked boxes*/
         /* This is just to visually see stuff on the GUI with the mock game for now*/
        Set<Integer> trsrOpts = new HashSet<>();
        for (int i = 1; i <= treasures; i++) {
            trsrOpts.add(i/numOfPlayers);
        }
        trsrOpts.remove(0);
        List<Integer> trsrOptsList = new ArrayList<>(trsrOpts);
        ObservableList<Integer> treasureList = FXCollections.observableList(trsrOptsList);
        treasureCBox.setItems(treasureList);
        treasureCBox.setValue(treasureList.get(treasureList.size()-1));
    }
}

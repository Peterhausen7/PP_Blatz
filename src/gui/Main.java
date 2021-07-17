package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Labyrinth the game");
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(680);
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        FXMLDocumentcontroller ctrl = loader.getController();
        ctrl.showGameSettingsWindow(24, 4);
        ctrl.setTreasureHighlightEvent();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

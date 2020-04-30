package kpi.manfredi.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class JavaFxMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Context.getInstance().setPrimaryStage(primaryStage);

        Context.SceneController sceneController = Context.getInstance().getSceneController();
        sceneController.addScene(Screen.HOME, FXMLLoader.load(getClass().getResource("/screens/HomeScreen.fxml")));
        sceneController.addScene(Screen.MAIN, FXMLLoader.load(getClass().getResource("/screens/MainScreen.fxml")));

        sceneController.activateScene(Screen.HOME);

        primaryStage.setTitle("Knitwear");
        primaryStage.show();
    }
}

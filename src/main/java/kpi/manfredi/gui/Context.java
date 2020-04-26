package kpi.manfredi.gui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

class Context {

    private static int MIN_WIDTH = 400;
    private static int MIN_HEIGHT = 280;

    private Stage primaryStage;
    private SceneController sceneController;

    private static Context instance;

    static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    SceneController getSceneController() {
        if (sceneController == null) {
            sceneController = new SceneController();
        }
        return sceneController;
    }

    class SceneController {
        private HashMap<Screen, Pane> sceneMap = new HashMap<>();
        private Scene scene;

        private SceneController() {
        }

        void addScene(Screen name, Pane pane) {
            sceneMap.put(name, pane);
        }

        protected void removeScene(Screen name) {
            sceneMap.remove(name);
        }

        void activateScene(Screen name) {
            scene = new Scene(sceneMap.get(name));
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(Math.max(scene.getWidth(), MIN_WIDTH));
            primaryStage.setMinHeight(Math.max(scene.getHeight(), MIN_HEIGHT));
        }
    }
}

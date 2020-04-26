package kpi.manfredi.gui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * This class is used to contain configurations to manage application
 */
public class Context {

    private static int MIN_WIDTH = 400;
    private static int MIN_HEIGHT = 200;

    private Stage primaryStage;
    private SceneController sceneController;
    private List<Locale> availableLocales;
    private Locale currentLocale;

    private static Context instance;

    /**
     * This method is used to return instance of {@code Context} class
     *
     * @return instance of {@code Context} class
     */
    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    private Context() {
        availableLocales = new LinkedList<>();
        availableLocales.add(new Locale("eng"));
        availableLocales.add(new Locale("ukr"));
        currentLocale = new Locale("ukr");  // default locale
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * This method is used to return an instance of {@code SceneController}
     *
     * @return an instance of {@code SceneController}
     */
    SceneController getSceneController() {
        if (sceneController == null) {
            sceneController = new SceneController();
        }
        return sceneController;
    }

    /**
     * This method is used to return available locales
     *
     * @return available locales
     */
    List<Locale> getAvailableLocales() {
        return availableLocales;
    }

    /**
     * This method is used to return current locale setting
     *
     * @return current locale
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * This method is used to set current locale setting
     *
     * @param currentLocale locale to set
     */
    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    /**
     * This class is used to switching between scenes
     */
    class SceneController {
        private HashMap<Screen, Pane> sceneMap = new HashMap<>();
        private Scene scene;

        private SceneController() {
        }

        /**
         * This method is used to add scene to map of scenes
         *
         * @param key  key of screen
         * @param pane pane to add
         */
        void addScene(Screen key, Pane pane) {
            sceneMap.put(key, pane);
        }

        /**
         * This method is used to remove scene from map of scenes
         *
         * @param key key of screen to remove
         */
        void removeScene(Screen key) {
            sceneMap.remove(key);
        }

        /**
         * This method is used to activate certain screen
         *
         * @param key key of screen that will be activated
         */
        void activateScene(Screen key) {
            scene = new Scene(sceneMap.get(key));
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(Math.max(scene.getWidth(), MIN_WIDTH));
            primaryStage.setMinHeight(Math.max(scene.getHeight(), MIN_HEIGHT));
        }
    }
}

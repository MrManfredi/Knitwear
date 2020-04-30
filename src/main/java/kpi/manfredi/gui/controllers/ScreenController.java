package kpi.manfredi.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This class is used to switching between scenes
 */
public abstract class ScreenController {

    private final static Logger logger = LoggerFactory.getLogger(ScreenController.class);

    private static final int MIN_WIDTH = 400;
    private static final int MIN_HEIGHT = 200;

    /**
     * This method is used to activate certain screen.
     *
     * @param path  path to screen that will be activated
     * @param stage stage in which screen will be shown
     */
    public static void activateScreen(String path, Stage stage) {

        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(ScreenController.class.getResource(path)));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }

        stage.setScene(scene);
        stage.setMinWidth(Math.max(scene.getWidth(), MIN_WIDTH));
        stage.setMinHeight(Math.max(scene.getHeight(), MIN_HEIGHT));
    }
}

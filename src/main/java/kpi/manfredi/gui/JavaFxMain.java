package kpi.manfredi.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import kpi.manfredi.gui.controllers.ScreenController;

/**
 * This class is used to create application
 *
 * @author manfredi
 */
public class JavaFxMain extends Application {

    /**
     * This method is used to start application
     *
     * @param primaryStage primary stage of application
     */
    @Override
    public void start(Stage primaryStage) {

        Context.getInstance().setPrimaryStage(primaryStage);

        ScreenController.activateScreen(
                Screen.HOME.getPath(),
                primaryStage
        );

        primaryStage.setX(100);
        primaryStage.setY(50);
        primaryStage.setTitle("Knitwear");
        primaryStage.show();
    }
}

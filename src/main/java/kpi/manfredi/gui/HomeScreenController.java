package kpi.manfredi.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {
    @FXML
    private Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setOnAction(event -> Context.getInstance().getSceneController().activateScene(Screen.MAIN));
    }
}

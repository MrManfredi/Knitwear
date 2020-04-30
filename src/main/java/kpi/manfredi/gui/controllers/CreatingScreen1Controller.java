package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.utils.MessageUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreatingScreen1Controller implements Initializable {
    @FXML
    private Label headerLabel;

    @FXML
    private Label numberOfCombsLabel;

    @FXML
    private TextField numberOfCombsTf;

    @FXML
    private Button cancelButton;

    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        cancelButton.setOnAction(event -> {
            try {
                Context.getInstance().getSceneController().addScene(Screen.HOME, FXMLLoader.load(getClass().getResource("/screens/HomeScreen.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Context.getInstance().getSceneController().activateScene(Screen.HOME);
        });
    }

    private void refreshLocalization() {
        headerLabel.setText(MessageUtil.getMessage("creating.header"));
        numberOfCombsLabel.setText(MessageUtil.getMessage("number.of.combs"));
        cancelButton.setText(MessageUtil.getMessage("button.cancel"));
        nextButton.setText(MessageUtil.getMessage("button.next"));
    }
}

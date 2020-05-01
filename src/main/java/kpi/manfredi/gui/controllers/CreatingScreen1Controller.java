package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.MessageUtil;

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
        initCancelButtonListener();
        initNextButtonListener();
    }

    /**
     * This method is used to update the name of each component on the screen
     */
    private void refreshLocalization() {
        headerLabel.setText(MessageUtil.getMessage("creating.header"));
        numberOfCombsLabel.setText(MessageUtil.getMessage("number.of.combs"));
        cancelButton.setText(MessageUtil.getMessage("button.cancel"));
        nextButton.setText(MessageUtil.getMessage("button.next"));
    }

    private void initCancelButtonListener() {
        cancelButton.setOnAction(event -> ScreenController.activateScreen(
                Screen.HOME.getPath(),
                Context.getInstance().getPrimaryStage()
        ));
    }

    private void initNextButtonListener() {
        nextButton.setOnAction(event -> {
            try {
                int numberOfCombs = Integer.parseInt(numberOfCombsTf.getText());
                if (numberOfCombs < 1) {
                    throw new NumberFormatException();
                } else {
                    Context.getInstance().setNumberOfCombs(numberOfCombs);
                }
            } catch (NumberFormatException e) {
                DialogsUtil.showAlert(
                        Alert.AlertType.WARNING,
                        MessageUtil.getMessage("warning.title"),
                        MessageUtil.getMessage("number.format.exception")
                );
                return;
            }
            ScreenController.activateScreen(
                    Screen.CREATING2.getPath(),
                    Context.getInstance().getPrimaryStage());
        });
    }
}

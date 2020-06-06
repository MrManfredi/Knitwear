package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.MessageUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplaySettingsScreenController implements Initializable {

    @FXML
    private Label displaySettings;

    @FXML
    private Slider lineThicknessSlider;

    @FXML
    private Label lineThicknessLabel;

    @FXML
    private Label cellSizeLabel;

    @FXML
    private Slider cellSizeSlider;

    @FXML
    private Slider loopRadiusSlider;

    @FXML
    private Label loopRadiusLabel;

    @FXML
    private Button finishButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        lineThicknessSlider.setValue(Context.getInstance().getLineThickness());
        cellSizeSlider.setValue(Context.getInstance().getCellSize());
        loopRadiusSlider.setValue(Context.getInstance().getLoopRadius());
        setFinishButtonListener();
    }

    /**
     * This method is used to update the name of each component on the screen
     */
    private void refreshLocalization() {
        displaySettings.setText(MessageUtil.getMessage("display.settings"));
        lineThicknessLabel.setText(MessageUtil.getMessage("line.thickness"));
        cellSizeLabel.setText(MessageUtil.getMessage("cell.size"));
        loopRadiusLabel.setText(MessageUtil.getMessage("loop.radius"));
        finishButton.setText(MessageUtil.getMessage("button.finish"));
    }

    private void setFinishButtonListener() {
        finishButton.setOnAction(actionEvent -> {
            if (cellSizeSlider.getValue() > loopRadiusSlider.getValue() * 2) {
                Context.getInstance().setLineThickness(lineThicknessSlider.getValue());
                Context.getInstance().setCellSize(cellSizeSlider.getValue());
                Context.getInstance().setLoopRadius(loopRadiusSlider.getValue());
                ScreenController.activateScreen(Screen.MAIN.getPath(), Context.getInstance().getPrimaryStage());
            } else {
                DialogsUtil.showAlert(
                        Alert.AlertType.INFORMATION,
                        MessageUtil.getMessage("warning.title"),
                        MessageUtil.getMessage("display.settings.info")
                );
            }
        });
    }
}

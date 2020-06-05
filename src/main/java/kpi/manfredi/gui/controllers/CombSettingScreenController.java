package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.model.Comb;
import kpi.manfredi.model.Data;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.MessageUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class CombSettingScreenController implements Initializable {

    private Data data;

    @FXML
    private Label headerLabel;

    @FXML
    private Button newCombButton;

    @FXML
    private HBox container;

    @FXML
    private Button finishButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        setNewCombButtonListener();
        initData();
        initFinishButtonListener();
    }

    /**
     * This method is used to update the name of each component on the screen
     */
    private void refreshLocalization() {
        headerLabel.setText(MessageUtil.getMessage("record.editing.label"));
        newCombButton.setText(MessageUtil.getMessage("new.comb"));
        finishButton.setText(MessageUtil.getMessage("button.finish"));
    }

    private void initData() {
        data = Context.getInstance().getData();
        Context.getInstance().setCombPanelsList(container.getChildren());

        int i = 1;
        for (Comb comb : data.getComb()) {
            CombPanel combPanel = new CombPanel(comb);
            combPanel.setCombNumber(i++);
            container.getChildren().add(combPanel);
        }
    }

    private void setNewCombButtonListener() {
        newCombButton.setOnAction(actionEvent -> {
            Comb comb = new Comb();
            comb.setVisible(true);
            comb.getColor().add(Color.BLACK.toString());
            data.getComb().add(comb);
            CombPanel combPanel = new CombPanel(comb);
            combPanel.setCombNumber(container.getChildren().size() + 1);
            container.getChildren().add(combPanel);
        });
    }

    private void initFinishButtonListener() {
        finishButton.setOnAction(actionEvent -> {
            for (Node panel : container.getChildren()) {
                if (panel instanceof CombPanel && !((CombPanel) panel).updateComb()) {
                    return; // errors in input
                }
            }

            if (isVisibleCombPresent()) {
                ScreenController.activateScreen(Screen.MAIN.getPath(), Context.getInstance().getPrimaryStage());
            } else {
                DialogsUtil.showAlert(Alert.AlertType.WARNING,
                        MessageUtil.getMessage("warning.title"),
                        MessageUtil.getMessage("comb.visibility.error"));
            }
        });
    }

    private boolean isVisibleCombPresent() {
        for (Comb comb : data.getComb()) {
            if (comb.isVisible()) {
                return true;
            }
        }
        return false;
    }
}

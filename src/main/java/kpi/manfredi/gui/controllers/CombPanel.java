package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.MessageUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CombPanel extends VBox implements Initializable {

    private int combNumber;

    @FXML
    private Label combNameLabel;

    @FXML
    private TableView<?> combSettingsTable;

    @FXML
    private TableColumn<?, ?> numberColumn;

    @FXML
    private TableColumn<?, ?> aColumn;

    @FXML
    private TableColumn<?, ?> bColumn;

    @FXML
    private CheckBox visibleCheckBox;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    public CombPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/panels/CombSettingsPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        addButton.setOnAction(actionEvent -> {
            DialogsUtil.showAlert(Alert.AlertType.INFORMATION, "Nice", "Hello, bro #" + combNumber);
        });
    }

    /**
     * This method is used to update the name of each component on the screen
     */
    private void refreshLocalization() {
        combNameLabel.setText(MessageUtil.getMessage("comb.label"));
        visibleCheckBox.setText(MessageUtil.getMessage("visible.choice.box"));
        addButton.setText(MessageUtil.getMessage("button.add"));
        removeButton.setText(MessageUtil.getMessage("button.remove"));
    }

    public void setCombNumber(int combNumber) {
        this.combNumber = combNumber;
        combNameLabel.setText(MessageUtil.getMessage("comb.label") + " " + combNumber);
    }
}

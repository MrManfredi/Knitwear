package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.utils.MessageUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class CreatingScreen2Controller implements Initializable {

    @FXML
    private Label headerLabel;

    @FXML
    private TableView<?> dataTable;

    @FXML
    private TableColumn<?, ?> numberColumn;

    @FXML
    private TableColumn<?, ?> masonryHeightColumn;

    @FXML
    private TableColumn<?, ?> combSealColumn;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        initBackButtonListener();
    }

    /**
     * This method is used to update the name of each component on the screen
     */
    private void refreshLocalization() {
        headerLabel.setText(MessageUtil.getMessage("creating.header"));
        masonryHeightColumn.setText(MessageUtil.getMessage("masonry.height"));
        combSealColumn.setText(MessageUtil.getMessage("comb.seal"));
        backButton.setText(MessageUtil.getMessage("button.back"));
        nextButton.setText(MessageUtil.getMessage("button.next"));
    }

    private void initBackButtonListener() {
        backButton.setOnAction(event -> ScreenController.activateScreen(
                Screen.CREATING1.getPath(),
                Context.getInstance().getPrimaryStage()
        ));
    }
}

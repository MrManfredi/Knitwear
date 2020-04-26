package kpi.manfredi.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import kpi.manfredi.utils.MessageUtil;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author manfredi
 */
public class HomeScreenController implements Initializable {
    @FXML
    private Button createButton;

    @FXML
    private Button openButton;

    @FXML
    private ChoiceBox<Locale> languageChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        initLanguageChangeListener();
        createButton.setOnAction(event -> Context.getInstance().getSceneController().activateScene(Screen.MAIN));
    }

    private void refreshLocalization() {
        createButton.setText(MessageUtil.getMessage("create.new.project"));
        openButton.setText(MessageUtil.getMessage("open.existing.project"));
    }

    private void initLanguageChangeListener() {
        languageChoiceBox.getItems().addAll(Context.getInstance().getAvailableLocales());
        languageChoiceBox.getSelectionModel().select(Context.getInstance().getCurrentLocale());
        languageChoiceBox.setOnAction(actionEvent -> {
            Context.getInstance().setCurrentLocale(languageChoiceBox.getSelectionModel().getSelectedItem());
            refreshLocalization();
        });
    }

}

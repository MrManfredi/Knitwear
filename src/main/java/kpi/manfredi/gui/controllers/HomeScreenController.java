package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.model.Data;
import kpi.manfredi.model.Storage;
import kpi.manfredi.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author manfredi
 */
public class HomeScreenController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(HomeScreenController.class);

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
        initCreateButtonListener();
        setOpenButtonListener();
    }

    /**
     * This method is used to update the name of each component on the screen
     */
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

    private void initCreateButtonListener() {
        createButton.setOnAction(event -> ScreenController.activateScreen(
                Screen.CREATING1.getPath(),
                Context.getInstance().getPrimaryStage()
        ));
    }

    private void setOpenButtonListener() {
        openButton.setOnAction(actionEvent -> {
            Data data = null;

            try {
                data = Storage.getData();
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }

            if (data != null) {
                Context.getInstance().setData(data);
                ScreenController.activateScreen(
                        Screen.COMB_SETTINGS.getPath(),
                        Context.getInstance().getPrimaryStage());
            }
        });
    }

}

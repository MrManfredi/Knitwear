package kpi.manfredi.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.utils.MessageUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class CreatingScreen2Controller implements Initializable {

    @FXML
    private Label headerLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private TableView<Input> dataTable;

    @FXML
    private TableColumn<Input, Integer> numberColumn;

    @FXML
    private TableColumn<Object, String> masonryHeightColumn;

    @FXML
    private TableColumn<Object, String> combSealColumn;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        initBackButtonListener();
        initTable();
    }

    /**
     * This method is used to update the name of each component on the screen
     */
    private void refreshLocalization() {
        headerLabel.setText(MessageUtil.getMessage("creating.header"));
        infoLabel.setText(MessageUtil.getMessage("press.enter.label"));
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

    private void initTable() {
        setSellFactory();
        ObservableList<Input> listOfEmptyElements = getListOfEmptyElements(Context.getInstance().getNumberOfCombs());
        dataTable.setItems(listOfEmptyElements);
    }

    private void setSellFactory() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("numberColumn"));
        masonryHeightColumn.setCellValueFactory(new PropertyValueFactory<>("masonryHeightColumn"));
        combSealColumn.setCellValueFactory(new PropertyValueFactory<>("combSealColumn"));

        masonryHeightColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        combSealColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public ObservableList<Input> getListOfEmptyElements(int numberOfElements) {
        ObservableList<Input> inputs = FXCollections.observableArrayList();
        for (int i = 0; i < numberOfElements; i++) {
            inputs.add(new Input(i + 1, "", ""));
        }
        return inputs;
    }

    public static class Input {
        private Integer numberColumn;
        private String masonryHeightColumn;
        private String combSealColumn;

        public Input(Integer numberColumn, String masonryHeightColumn, String combSealColumn) {
            this.numberColumn = numberColumn;
            this.masonryHeightColumn = masonryHeightColumn;
            this.combSealColumn = combSealColumn;
        }

        public Integer getNumberColumn() {
            return numberColumn;
        }

        public void setNumberColumn(Integer numberColumn) {
            this.numberColumn = numberColumn;
        }

        public String getMasonryHeightColumn() {
            return masonryHeightColumn;
        }

        public void setMasonryHeightColumn(String masonryHeightColumn) {
            this.masonryHeightColumn = masonryHeightColumn;
        }

        public String getCombSealColumn() {
            return combSealColumn;
        }

        public void setCombSealColumn(String combSealColumn) {
            this.combSealColumn = combSealColumn;
        }

    }
}

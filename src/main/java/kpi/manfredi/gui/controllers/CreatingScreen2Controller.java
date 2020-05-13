package kpi.manfredi.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.model.Comb;
import kpi.manfredi.model.Data;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.MessageUtil;

import java.net.URL;
import java.util.List;
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
    private TableColumn<Input, String> masonryHeightColumn;

    @FXML
    private TableColumn<Input, String> combSealColumn;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLocalization();
        initBackButtonListener();
        initNextButtonListener();
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

    private void initNextButtonListener() {
        nextButton.setOnAction(actionEvent -> {
            ObservableList<Input> tableItems = dataTable.getItems();
            Data data = new Data();

            for (Input item : tableItems) {
                int numOfSteps;
                int numOfColors;
                try {
                    numOfSteps = Integer.parseInt(item.masonryHeightColumn);
                    numOfColors = Integer.parseInt(item.combSealColumn);

                    if (numOfSteps < 1 || numOfColors < 1) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    DialogsUtil.showAlert(
                            Alert.AlertType.WARNING,
                            MessageUtil.getMessage("warning.title"),
                            MessageUtil.getMessage("number.format.exception"),
                            MessageUtil.formatMessage("line.error", item.numberColumn)
                    );
                    return;
                }

                data.getComb().add(initNewComb(numOfSteps, numOfColors));
            }
            Context.getInstance().setData(data);
            System.out.println();
            ScreenController.activateScreen(Screen.COMB_SETTINGS.getPath(), Context.getInstance().getPrimaryStage());
        });
    }

    private Comb initNewComb(int numOfSteps, int numOfColors) {
        Comb comb = new Comb();

        List<Comb.Row> rows = comb.getRow();
        for (int i = 0; i < numOfSteps; i++) {
            rows.add(new Comb.Row());
        }

        List<String> colors = comb.getColor();
        for (int i = 0; i < numOfColors; i++) {
            colors.add("#000000");
        }

        return comb;
    }

    private void initTable() {
        setSellFactory();
        ObservableList<Input> listOfEmptyElements = getListOfEmptyElements(Context.getInstance().getNumberOfCombs());
        dataTable.setItems(listOfEmptyElements);
    }

    private void setSellFactory() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("numberColumn"));

        masonryHeightColumn.setCellValueFactory(new PropertyValueFactory<>("masonryHeightColumn"));
        masonryHeightColumn.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setMasonryHeightColumn(t.getNewValue())
        );

        combSealColumn.setCellValueFactory(new PropertyValueFactory<>("combSealColumn"));
        combSealColumn.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setCombSealColumn(t.getNewValue())
        );

        masonryHeightColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        combSealColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public ObservableList<Input> getListOfEmptyElements(int numberOfElements) {
        ObservableList<Input> inputs = FXCollections.observableArrayList();
        for (int i = 0; i < numberOfElements; i++) {
            inputs.add(new Input(i + 1, "1", "1"));
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

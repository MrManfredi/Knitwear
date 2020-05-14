package kpi.manfredi.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import kpi.manfredi.model.Comb;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.MessageUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CombPanel extends VBox implements Initializable {

    private int combNumber;
    private Comb comb;

    @FXML
    private Label combNameLabel;

    @FXML
    private TableView<TableItem> combSettingsTable;

    @FXML
    private TableColumn<TableItem, Integer> numberColumn;

    @FXML
    private TableColumn<TableItem, String> aColumn;

    @FXML
    private TableColumn<TableItem, String> bColumn;

    @FXML
    private CheckBox visibleCheckBox;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    public CombPanel(Comb comb) {
        this.comb = comb;
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
        initTable();
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

    private void initTable() {
        setSellFactory();
        ObservableList<TableItem> rows = FXCollections.observableArrayList();
        for (int i = 0; i < comb.getRow().size(); i++) {
            rows.add(new TableItem(i + 1, "1", "1"));
        }
        combSettingsTable.setItems(rows);
    }

    private void setSellFactory() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        aColumn.setCellValueFactory(new PropertyValueFactory<>("a"));
        aColumn.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setA(t.getNewValue())
        );

        bColumn.setCellValueFactory(new PropertyValueFactory<>("b"));
        bColumn.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setB(t.getNewValue())
        );

        aColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void setCombNumber(int combNumber) {
        this.combNumber = combNumber;
        combNameLabel.setText(MessageUtil.getMessage("comb.label") + " " + combNumber);
    }

    /**
     * This method is used to read data from table to {@code Comb} entity.
     * Shows alerts when errors occurs in input data/
     *
     * @return {@code false} when errors occurs in input data
     * <br> {@code true} when everything is successful
     */
    public boolean updateComb() {
        int i = 0;
        for (TableItem tableItem : combSettingsTable.getItems()) {
            Comb.Row row;
            if (i < comb.getRow().size()) {
                row = comb.getRow().get(i);
            } else {
                row = new Comb.Row();
                comb.getRow().add(row);
            }

            try {
                int a = Integer.parseInt(tableItem.getA());
                int b = Integer.parseInt(tableItem.getB());

                if (a < 0 || b < 0) {
                    throw new NumberFormatException();
                }

                if (Math.abs(a - b) > 1) {
                    throw new IOException();
                }
                row.setA(a);
                row.setB(b);
            } catch (NumberFormatException e) {
                DialogsUtil.showAlert(
                        Alert.AlertType.WARNING,
                        MessageUtil.getMessage("warning.title"),
                        MessageUtil.getMessage("non.negative.number"),
                        MessageUtil.formatMessage("comb.and.row.number", combNumber, tableItem.getNumber())
                );
                return false;
            } catch (IOException diffEx) {
                DialogsUtil.showAlert(
                        Alert.AlertType.WARNING,
                        MessageUtil.getMessage("warning.title"),
                        MessageUtil.getMessage("numbers.difference"),
                        MessageUtil.formatMessage("comb.and.row.number", combNumber, tableItem.getNumber())
                );
                return false;
            }
            i++;
        }
        return true;
    }

    public static class TableItem {
        private Integer number;
        private String a;
        private String b;

        public TableItem(Integer number, String a, String b) {
            this.number = number;
            this.a = a;
            this.b = b;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }
}

package com.labs;

import com.labs.domain.TreeNodeEntry;
import com.labs.impl.BtreeServiceImpl;
import com.labs.impl.FileServiceImpl;
import com.labs.services.BTreeService;
import com.labs.services.FileService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ApplicationController {
    private String FILE_NAME;
    private final BTreeService bTreeService;
    private FileService fileService;
    private final String INVALID_INPUT_STYLE = "invalid";
    @FXML
    private TableView<TreeNodeEntry> entryTable = new TableView<>();
    @FXML
    private TextField keyInput;
    @FXML
    private TextField valueInput;
    @FXML
    private Button loadButton;
    @FXML
    public Button addEntryButton;
    @FXML
    public Button deleteEntryButton;
    @FXML
    public Button searchEntryButton;
    @FXML
    private TextField searchInput;

    public ApplicationController() {
        this.fileService = new FileServiceImpl();
        this.bTreeService = new BtreeServiceImpl();
        this.FILE_NAME = bTreeService.getRandomTree(BTreeService.DEFAULT_LENGTH);
    }

    @FXML
    void initialize() {
        this.bTreeService.loadTree(this.FILE_NAME);
        setTableLayout();
        updateTableData();
    }

    @FXML
    protected void onAddEntryButtonClick() {
        if (isKeyValueFormValid()) {
            Integer key = Integer.parseInt(keyInput.getText());
            String value = valueInput.getText();
            bTreeService.insertEntry(FILE_NAME, new TreeNodeEntry(key, value));
            resetInputs();
            updateTableData();
            changeInputStatus(keyInput, true);
            changeInputStatus(valueInput, true);
        } else {
            changeInputStatus(keyInput, isKeyInputValid(keyInput));
            changeInputStatus(valueInput, isIsValueValid());
        }
    }

    @FXML
    protected void onSearchEntryButtonClick() {
        if (isKeyInputValid(searchInput)) {
            searchInput.getStyleClass().remove(INVALID_INPUT_STYLE);
            TreeNodeEntry entry = bTreeService.getEntryByKey(Integer.parseInt(searchInput.getText()));
            ObservableList<TreeNodeEntry> entries = FXCollections.observableArrayList(entry);
            entryTable.setItems(entries);
        } else {
            searchInput.getStyleClass().add(INVALID_INPUT_STYLE);
        }
    }

    @FXML
    protected void onSearchInputChange() {
        if (searchInput.getText().isEmpty()) {
            updateTableData();
        }
    }

    @FXML
    protected void onDeleteEntryButtonClick() {
        ObservableList<TreeNodeEntry> entries = entryTable.getSelectionModel().getSelectedItems();
        if (!entries.isEmpty()) {
            TreeNodeEntry entry = entries.stream().findFirst().get();
            bTreeService.deleteEntry(FILE_NAME, entry.getKey());
            updateTableData();
            resetSearch();
        }
    }

    @FXML
    protected void onLoadNewEntryButtonClick() {
        selectFile();
    }

    @FXML
    protected void onKeyInputValidate() {
        changeInputStatus(keyInput, isKeyInputValid(keyInput));
    }

    @FXML
    protected void onValueInputValidate() {
        changeInputStatus(valueInput, isIsValueValid());
    }

    private boolean isKeyValueFormValid() {
        return isKeyInputValid(keyInput) && isIsValueValid();
    }

    private void loadTableView() {
        setTableLayout();
        bTreeService.loadTree(FILE_NAME);
        updateTableData();
    }

    private void resetSearch() {
        searchInput.clear();
        updateTableData();
    }

    private boolean isIsValueValid() {
        String valueText = valueInput.getText().trim();
        return !valueText.isEmpty() && valueText.length() < 20;
    }

    private boolean isKeyInputValid(TextField keyInput) {
        String keyText = keyInput.getText();
        boolean isKeyValid = keyText != null && !keyText.isEmpty();
        try {
            int key = Integer.parseInt(keyText);
            if (key < 0) {
                isKeyValid = false;
            }
        } catch (NumberFormatException e) {
            isKeyValid = false;
        }
        return isKeyValid;
    }

    private void changeInputStatus(TextField textField, boolean isValid) {
        if (textField.getText().isEmpty()) {
            isValid = true;
        }
        if (isValid) {
            textField.getStyleClass().remove(INVALID_INPUT_STYLE);
        } else {
            textField.getStyleClass().add(INVALID_INPUT_STYLE);
        }
    }

    private void resetInputs() {
        keyInput.clear();
        valueInput.clear();
        keyInput.getStyleClass().removeAll();
        valueInput.getStyleClass().removeAll();
    }

    private void updateTableData() {
        ObservableList<TreeNodeEntry> entries = FXCollections.observableArrayList(bTreeService.getAllEntries(FILE_NAME));
        entryTable.setItems(entries);
    }

    private void setTableLayout() {
        if (entryTable.getColumns().isEmpty()) {
            TableColumn<TreeNodeEntry, Integer> keyCol = new TableColumn<>("KEY");
            TableColumn<TreeNodeEntry, String> valueCol = new TableColumn<>("VALUE");
            entryTable.getColumns().addAll(keyCol, valueCol);
            keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
            valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        }
    }

    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        Stage stage = (Stage) loadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && fileService.isFileValid(selectedFile.getName())) {
            this.FILE_NAME = selectedFile.getName();
            loadTableView();
        }
    }
}

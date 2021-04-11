/**
 * CovidImpfplaner
 * Dieses Programm unterstützt Arztpraxen bei der Organisation der Impfungen
 * gegen COVID-19
 *
 * CovidImpfPlaner  Copyright (C) 2021  Philipp Käsgen
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *
 * pkaesgen(AT)freenet.de
 */
package kaesgen.med;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("CovidImpfPlanerView.fxml")
public class MainSceneController implements Initializable {

    /**
     * Boolean variable to prevent the application from closing without saving
     * changes. It tracks the state "changed" by observing manipulations to the
     * database.
     */
    private boolean changeMonitor;

    /**
     * Getter method for {@link #changeMonitor}.
     * @return current "changed" state
     */
    public boolean getChangeMonitor() {
        return changeMonitor;
    }

    /**
     * List of PatientEntries which lie under the TableView.
     */
    private ObservableList<PatientEntry> patients;

    /**
     * The stage.
     */
    private Stage stage;

    /** THE table. */
    @FXML
    private TableView<PatientEntry> table1;

    /** create new patient. */
    @FXML
    private Button createNewPatientEntry;

    /** edit patient. */
    @FXML
    private MenuItem editPatientEntry;

    /** delete patient. */
    @FXML
    private MenuItem deletePatientEntry;

    /** function to determine the least amount of vaccine. */
    @FXML
    private MenuItem calculateOrder;

    /** function to determine the remaining vaccine after vaccinating
     * second time patients.
     */
    @FXML
    private MenuItem remainingVaccine;

    /** open save dialog. */
    @FXML
    private MenuItem saveFile;

    /** open load dialog. */
    @FXML
    private MenuItem loadFile;

    /** about. */
    @FXML
    private MenuItem aboutBtn;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> registrationDate;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> id;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> lastName;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> firstName;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> birthday;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> additionalInfo;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> landline;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, String> mobile;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, LocalDate> firstVaccinationDate;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, Boolean> firstVaccinationDone;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, LocalDate> secondVaccinationDate;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, Boolean> secondVaccinationDone;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, VaccineBrand> firstVaccineBrand;

    /** column in table. */
    @FXML
    private TableColumn<PatientEntry, VaccineBrand> secondVaccineBrand;

    @FXML
    private void createNewPatientClicked() {
        System.out.println("Registering patient");

        changeMonitor = true;
        // open another view with textboxes

        PatientEntry patient1 = new PatientEntry(5, "Schmidt", "Hans",
            LocalDate.of(1970, 1, 1), "COPD", "01234/234512",
            "", null, false, null, null, false, null);
        PatientEntry patient2 = new PatientEntry(5, "Ludwig", "Karl",
            LocalDate.of(1950, 5, 4), "Krebs", "01234/234512",
            "", null, false, null, null, false, null);

        // sanity check

        // check for duplicates

        patients.add(patient1);
        patients.add(patient2);

    }

    @FXML
    private void calculateOrderClicked() {
        System.out.println("Calculating minimal vaccine order");

    }

    @FXML
    private void remainingVaccineClicked() {
        System.out.println(
            "Calculating remaining vaccine for first time patients");

    }

    @FXML
    private void about() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);

        about.setTitle("Über");
        about.setHeaderText("CovidImpfplaner");
        about.setContentText(
        "Dieses Programm unterstützt Arztpraxen bei der Organisation der "
        + "Impfungen gegen COVID-19\n\n"
        + "CovidImpfPlaner  Copyright (C) 2021  Philipp Käsgen\n\n"
        + "This program is free software: you can redistribute it and/or "
        + "modify it under the terms of the GNU General Public License as "
        + "published by the Free Software Foundation, either version 3 of "
        + "the License, or (at your option) any later version.\n\n"

        + "This program is distributed in the hope that it will be useful, "
        + "but WITHOUT ANY WARRANTY; without even the implied warranty of "
        + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the "
        + "GNU General Public License for more details.\n\n"

        + "You should have received a copy of the GNU General Public License "
        + "along with this program.  If not, see "
        + "<http://www.gnu.org/licenses/>.");

        about.showAndWait();
    }

    // ########################################################################

    /**
     * Inquire password to encrypt patient file, then make a thread to write
     * the file.
     */
    @FXML
    public void saveFileClicked() {
        System.out.println("Saving file");

        // Open dialog for saving a file

        FileChooser fc = new FileChooser();

        File file = fc.showSaveDialog(stage);

        if (file != null) {

            Dialog<String> pwDialog = new Dialog<>();
            pwDialog.setTitle("Passwortabfrage");

            ButtonType decryptBtnT = new ButtonType(
                "Verschlüsseln und speichern");
            pwDialog.getDialogPane().getButtonTypes()
                .addAll(decryptBtnT, ButtonType.CANCEL);

            VBox vbox = new VBox();

            Label msg = new Label();

            PasswordField pwField1 = new PasswordField();
            pwField1.setPromptText("Passwort eingeben");
            PasswordField pwField2 = new PasswordField();
            pwField2.setPromptText("Passwort wiederholen");

            vbox.getChildren().addAll(msg, pwField1, pwField2);

            Node decryptButton = pwDialog.getDialogPane()
                .lookupButton(decryptBtnT);
            decryptButton.setDisable(true);

            pwField2.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                if (!pwField1.getText().equals(newValue)) {
                    msg.setText("Unterschiedliche Passwörter");
                } else {
                    msg.setText("");
                    decryptButton.setDisable(false);
                }
            });

            pwField1.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                if (newValue.length() < 8) {
                    msg.setText("Passwort zu kurz");
                } else if (!pwField2.getText().equals(newValue)) {
                    msg.setText("Unterschiedliche Passwörter");
                } else {
                    msg.setText("");
                    decryptButton.setDisable(false);
                }
            });

            pwDialog.getDialogPane().setContent(vbox);

            pwDialog.setResultConverter(dialogButton -> {
                if (dialogButton == decryptBtnT
                    && pwField1.getText().equals(pwField2.getText())) {
                    return pwField1.getText();
                }
                return null;
            });

            Optional<String> pwFtr = pwDialog.showAndWait();

            pwFtr.ifPresent(pw -> {

                Task<Void> setPatientEntriesTask = new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        (new FileHandler()).saveFile(pw,
                            file.getAbsolutePath(), patients);
                        return null;
                    }
                };

                try {
                    System.out.println("Writing data to"
                        + file.getAbsolutePath());

                    new Thread(setPatientEntriesTask).start();

                    changeMonitor = false;
                } catch (Exception ex) {
                    System.out.println("Saving file failed");
                }
            });
        }

    }

    // ########################################################################

    @FXML
    private void loadFileClicked() {
        if (changeMonitor) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.NO);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Speichern");
            alert.setHeaderText("Änderungen wurden noch nicht gespeichert.");
            alert.setContentText(
                String.format("Änderungen vor dem Laden speichern?"));

            Optional<ButtonType> res = alert.showAndWait();

            res.ifPresent(bt -> {
                if (bt.equals(ButtonType.YES)) {
                    saveFileClicked();
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Speichern");
                    msg.setHeaderText("Nicht speichern");
                    msg.setContentText(
                        String.format("Änderungen werden verworfen."));
                    msg.showAndWait();

                }
            });
        }

        System.out.println("Loading file");

        FileChooser fc = new FileChooser();

        // Open dialog for opening a file
        File file = fc.showOpenDialog(stage);

        if (file != null) {

            Dialog<String> pwDialog = new Dialog<>();
            pwDialog.setTitle("Passwortabfrage");

            ButtonType decryptBtnT = new ButtonType(
                "Entschlüsseln und öffnen");
            pwDialog.getDialogPane().getButtonTypes()
            .addAll(decryptBtnT, ButtonType.CANCEL);

            VBox vbox = new VBox();

            PasswordField pwField = new PasswordField();
            pwField.setPromptText("Passwort eingeben");

            vbox.getChildren().add(pwField);

            pwDialog.getDialogPane().setContent(vbox);

            pwDialog.setResultConverter(dialogButton -> {
                if (dialogButton == decryptBtnT) {
                    return pwField.getText();
                }
                return null;
            });

            Optional<String> pwFtr = pwDialog.showAndWait();

            pwFtr.ifPresent(pw -> {

                Task<List<PatientEntry>> getPatientEntriesTask =
                new Task<List<PatientEntry>>() {
                    @Override
                    protected List<PatientEntry> call() throws Exception {
                        return (new FileHandler())
                            .loadFile(PatientEntry.class,
                            pw, file.getCanonicalPath());
                    }
                };

                try {
                    new Thread(getPatientEntriesTask).start();
                    List<PatientEntry> loadedFile = getPatientEntriesTask.get();
                    patients.clear();

                    patients.addAll(loadedFile);

                    changeMonitor = false;

                } catch (Exception e) {
                    System.out.println(
                        "Loading failed. Probably wrong password");

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setContentText(
"Öffnen fehlgeschlagen. Entweder falsches Passwort oder inkompatible Datei.");

                    alert.showAndWait();

                }

            });
        } else {
            System.out.println("Invalid file chosen by user");
        }

    }

    // ########################################################################

    /**
     * Default constructor.
     */
    @Autowired
    public MainSceneController() {

    }

    /**
     * initialize the controller.
     */
    @Override
    public final void initialize(final URL url, final ResourceBundle rb) {

        this.stage = new Stage();

        this.stage.setTitle("CovidImpfplaner");

        patients = FXCollections.observableArrayList();
        table1.setItems(patients);

        changeMonitor = false;

        TableViewSelectionModel<PatientEntry> selectionModel =
            table1.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        registrationDate
            .setCellValueFactory(
            new PropertyValueFactory<>("registrationDate"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setCellFactory(TextFieldTableCell.<PatientEntry>forTableColumn());
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastName
            .setCellFactory(TextFieldTableCell.<PatientEntry>forTableColumn());
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstName
            .setCellFactory(TextFieldTableCell.<PatientEntry>forTableColumn());
        birthday
            .setCellValueFactory(new PropertyValueFactory<>("birthday"));
        birthday
            .setCellFactory(TextFieldTableCell.<PatientEntry>forTableColumn());
        additionalInfo
            .setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
        additionalInfo
            .setCellFactory(TextFieldTableCell.<PatientEntry>forTableColumn());
        landline.setCellValueFactory(new PropertyValueFactory<>("landline"));
        landline
        .setCellFactory(TextFieldTableCell.<PatientEntry>forTableColumn());
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobile
        .setCellFactory(TextFieldTableCell.<PatientEntry>forTableColumn());

        firstVaccinationDone
            .setCellValueFactory(
            new PropertyValueFactory<>("firstVaccinationDone"));

        firstVaccinationDate
            .setCellValueFactory(
            new PropertyValueFactory<>("firstVaccinationDate"));

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {

                firstVaccinationDate.setCellFactory(tc -> {

                    DateTableCell<PatientEntry> cell =
                        new DateTableCell<PatientEntry>(tc);

                    cell.valueProperty()
                        .addListener((obs, oldValue, newValue) ->
                        ((PatientEntry) cell.getTableRow().getItem())
                            .setFirstVaccinationDate(newValue));

                    return cell;
                });

                return null;
            }
        }).start();

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {

                firstVaccinationDone.setCellFactory(p -> {
                    CheckBox checkBox = new CheckBox();
                    TableCell<PatientEntry, Boolean> cell =
                    new TableCell<PatientEntry, Boolean>() {
                        @Override
                        public void updateItem(final Boolean item,
                        final boolean empty) {
                            if (empty) {
                                setGraphic(null);
                            } else {
                                checkBox.setSelected(item);
                                setGraphic(checkBox);
                            }
                        }
                    };

                    checkBox.selectedProperty()
                        .addListener((obs, wasSelected, isSelected) ->
                        ((PatientEntry) cell.getTableRow().getItem())
                            .setFirstVaccinationDone(isSelected));

                    cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                    cell.setAlignment(Pos.CENTER);

                    return cell;
                });
                return null;
            }
        }).start();

        secondVaccinationDate.setCellValueFactory(
            new PropertyValueFactory<>("secondVaccinationDate"));

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {

                secondVaccinationDate.setCellFactory(tc -> {
                    DateTableCell<PatientEntry> cell =
                    new DateTableCell<PatientEntry>(tc);
                    cell.valueProperty()
                        .addListener((obs, oldValue, newValue) ->
                            ((PatientEntry) cell.getTableRow().getItem())
                                .setSecondVaccinationDate(newValue));

                    return cell;
                });
                return null;
            }
        }).start();

        secondVaccinationDone.setCellValueFactory(
            new PropertyValueFactory<>("secondVaccinationDone"));
        new Thread(new Task<Void>() {
            @Override
            protected Void call() {

                secondVaccinationDone.setCellFactory(p -> {

                    CheckBox checkBox = new CheckBox();

                    TableCell<PatientEntry, Boolean> cell =
                    new TableCell<PatientEntry, Boolean>() {
                        @Override
                        public void updateItem(final Boolean item,
                        final boolean empty) {
                            if (empty) {
                                setGraphic(null);
                            } else {
                                checkBox.setSelected(item);
                                setGraphic(checkBox);
                            }
                        }
                    };

                    checkBox.selectedProperty()
                        .addListener((obs, wasSelected, isSelected) ->
                        ((PatientEntry) cell.getTableRow().getItem())
                            .setSecondVaccinationDone(isSelected));

                    cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                    cell.setAlignment(Pos.CENTER);

                    return cell;
                });
                return null;
            }
        }).start();

        firstVaccineBrand
        .setCellValueFactory(new PropertyValueFactory<>("firstVaccine"));

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {

                firstVaccineBrand.setCellFactory(tc -> {

                    ComboBox<VaccineBrand> comboBox = new ComboBox<>();

                    TableCell<PatientEntry, VaccineBrand> cell =
                    new TableCell<>() {
                        @Override
                        public void updateItem(final VaccineBrand item,
                        final boolean empty) {
                            if (empty) {
                                setGraphic(null);
                            } else {
                                comboBox.getSelectionModel().select(item);
                                setGraphic(comboBox);
                            }
                        }
                    };
                    comboBox.getItems().setAll(VaccineBrand.values());

                    comboBox.getSelectionModel().selectedItemProperty()
                        .addListener((obs, wasSelected, isSelected) ->
                        ((PatientEntry) cell.getTableRow().getItem())
                        .setFirstVaccine(isSelected));

                    cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                    cell.setAlignment(Pos.CENTER);

                    return cell;
                });
                return null;
            }
        }).start();

        secondVaccineBrand
            .setCellValueFactory(new PropertyValueFactory<>("secondVaccine"));
        new Thread(new Task<Void>() {
            @Override
            protected Void call() {

                secondVaccineBrand.setCellFactory(tc -> {

                    ComboBox<VaccineBrand> comboBox = new ComboBox<>();

                    TableCell<PatientEntry, VaccineBrand> cell =
                    new TableCell<>() {
                        @Override
                        public void updateItem(final VaccineBrand item,
                        final boolean empty) {
                            if (empty) {
                                setGraphic(null);
                            } else {
                                comboBox.getSelectionModel()
                                .select(item);
                                setGraphic(comboBox);
                            }
                        }
                    };

                    comboBox.getItems().setAll(VaccineBrand.values());

                    comboBox.getSelectionModel().selectedItemProperty()
                        .addListener((obs, wasSelected, isSelected) ->
                        ((PatientEntry) cell.getTableRow().getItem())
                        .setSecondVaccine(isSelected));

                    cell.setContentDisplay(ContentDisplay
                        .GRAPHIC_ONLY);

                    cell.setAlignment(Pos.CENTER);

                    return cell;

                });

                return null;
            }
        }).start();

        // #####################################################################
        // Context Menu
        table1.setRowFactory(tv -> {
            TableRow<PatientEntry> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();
            menu.getItems().add(new MenuItem("Optionen"));

            MenuItem edit = new MenuItem("Bearbeiten");
            edit.setOnAction(e -> { //TODO

            });

            MenuItem delete = new MenuItem("Löschen");
            delete.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Wirklich löschen?");
                alert.setHeaderText(
        "Gelöschte Patienteneinträge können nicht wiederhergestellt werden.");

                Optional<ButtonType> option = alert.showAndWait();

                option.ifPresent(o -> {
                    if (o == ButtonType.OK) {
                        table1.getItems().remove(row.getItem());
                        selectionModel.clearSelection();
                        changeMonitor = true;
                    }
                });

            });
            menu.getItems().add(delete);

            row.contextMenuProperty().bind(Bindings
                .when(row.emptyProperty()).then((ContextMenu) null)
                .otherwise(menu));

            return row;
        });

    }

}

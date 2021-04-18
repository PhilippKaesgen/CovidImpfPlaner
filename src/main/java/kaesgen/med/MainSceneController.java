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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
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


        // open another view with textboxes

        Dialog<PatientEntry> dialog = new Dialog<>();
        dialog.setTitle("Neuen Patienten anmelden");


        dialog.getDialogPane().getButtonTypes()
        .addAll(ButtonType.OK);

        Node btn = dialog.getDialogPane()
            .lookupButton(ButtonType.OK);
        btn.setDisable(true);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(5);
        gridpane.setVgap(5);

        Label msg = new Label();
        TextField idT = new TextField();
        idT.setPromptText("Patienten-ID");
        TextField lastNameT = new TextField();
        lastNameT.setPromptText("Nachname");
        TextField firstNameT = new TextField();
        firstNameT.setPromptText("Vorname");
        TextField birthdayT = new TextField();
        birthdayT.setPromptText("Geburtsdatum");
        TextField additionalInfoT = new TextField();
        additionalInfoT.setPromptText("Zusatzinformationen");
        TextField landlineT = new TextField();
        landlineT.setPromptText("Festnetznummer");
        TextField mobileT = new TextField();
        mobileT.setPromptText("Mobiltelefonnr.");

        gridpane.add(msg, 0, 0);

        gridpane.add(idT, 0, 1);
        gridpane.add(lastNameT, 0, 2);
        gridpane.add(firstNameT, 0, 3);
        gridpane.add(birthdayT, 0, 4);
        gridpane.add(additionalInfoT, 0, 5);
        gridpane.add(landlineT, 0, 6);
        gridpane.add(mobileT, 0, 7);


        // sanity check
        idT.textProperty().addListener((obs, newValue, oldValue) -> {
            btn.setDisable(true);
            msg.setText("Ungültige Eingaben");
            try {
                Integer.parseInt(newValue);
                btn.setDisable(false);
            } catch (Exception e) {
                if (PatientEntry.isName(firstNameT.getText())
                    && PatientEntry.isName(lastNameT.getText())
                    && (new LocalDateConverter())
                        .fromString(birthdayT.getText()) != null
                    && (PatientEntry.isPhoneNumber(landlineT.getText())
                    || PatientEntry.isPhoneNumber(mobileT.getText()))) {
                        btn.setDisable(false);
                        msg.setText("");
                    }

            }
        });

        firstNameT.textProperty().addListener((obs, newValue, oldValue) -> {
            btn.setDisable(true);
            msg.setText("Ungültige Eingaben");
            try {
                Integer.parseInt(idT.getText());
                btn.setDisable(false);
            } catch (Exception e) {
                if (PatientEntry.isName(newValue)
                    && PatientEntry.isName(lastNameT.getText())
                    && (new LocalDateConverter())
                        .fromString(birthdayT.getText()) != null
                    && (PatientEntry.isPhoneNumber(landlineT.getText())
                    || PatientEntry.isPhoneNumber(mobileT.getText()))) {
                        btn.setDisable(false);
                        msg.setText("");
                    }

            }
        });

        lastNameT.textProperty().addListener((obs, oldValue, newValue) -> {
            btn.setDisable(true);
            msg.setText("Ungültige Eingaben");
            try {
                Integer.parseInt(idT.getText());
                btn.setDisable(false);
            } catch (Exception e) {
                if (PatientEntry.isName(newValue)
                    && PatientEntry.isName(firstNameT.getText())
                    && (new LocalDateConverter())
                        .fromString(birthdayT.getText()) != null
                    && (PatientEntry.isPhoneNumber(landlineT.getText())
                    || PatientEntry.isPhoneNumber(mobileT.getText()))) {
                        btn.setDisable(false);
                        msg.setText("");
                    }
            }
        });

        birthdayT.textProperty().addListener((obs, oldValue, newValue) -> {
            btn.setDisable(true);
            msg.setText("Ungültige Eingaben");
            try {
                Integer.parseInt(idT.getText());
                btn.setDisable(false);
            } catch (Exception e) {
                if (PatientEntry.isName(lastNameT.getText())
                    && PatientEntry.isName(firstNameT.getText())
                    && (new LocalDateConverter()).fromString(newValue) != null
                    && (PatientEntry.isPhoneNumber(landlineT.getText())
                    || PatientEntry.isPhoneNumber(mobileT.getText()))) {
                        btn.setDisable(false);
                        msg.setText("");
                    }

            }
        });

        landlineT.textProperty().addListener((obs, oldValue, newValue) -> {
            btn.setDisable(true);
            msg.setText("Ungültige Eingaben");
            try {
                Integer.parseInt(idT.getText());
                btn.setDisable(false);
            } catch (Exception e) {
                if (PatientEntry.isName(lastNameT.getText())
                    && PatientEntry.isName(firstNameT.getText())
                    && (new LocalDateConverter())
                        .fromString(birthdayT.getText()) != null
                    && (PatientEntry.isPhoneNumber(newValue)
                    || PatientEntry.isPhoneNumber(mobileT.getText()))) {
                        btn.setDisable(false);
                        msg.setText("");
                    }

            }
        });

        mobileT.textProperty().addListener((obs, oldValue, newValue) -> {
            btn.setDisable(true);
            msg.setText("Ungültige Eingaben");
            try {
                Integer.parseInt(idT.getText());
                btn.setDisable(false);
            } catch (Exception e) {
                if (PatientEntry.isName(lastNameT.getText())
                    && PatientEntry.isName(firstNameT.getText())
                    && (new LocalDateConverter())
                        .fromString(birthdayT.getText()) != null
                    && (PatientEntry.isPhoneNumber(landlineT.getText())
                    || PatientEntry.isPhoneNumber(newValue))) {
                        btn.setDisable(false);
                        msg.setText("");
                    }
            }
        });

        dialog.getDialogPane().setContent(gridpane);

        dialog.setResultConverter(okbutton -> {
            if (okbutton == ButtonType.OK) {

                int temp = 0;
                try {
                    temp = Integer.parseInt(idT.getText());
                } catch (Exception e) {
                }

                PatientEntry p = new PatientEntry(temp,
                    lastNameT.getText(), firstNameT.getText(),
                    (new LocalDateConverter()).fromString(birthdayT.getText()),
                    additionalInfoT.getText(),
                    landlineT.getText(), mobileT.getText(),
                    null, false, null, null, false, null);

                // check for duplicates
                for (PatientEntry p1 : patients) {
                    if (p1.equals(p)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Fehler");
                        alert.setHeaderText(
                            "Identischen Patienteneintrag gefunden");

                        alert.showAndWait();

                        return null;
                    }
                }

                return p;
            }
            return null;
        });

        Optional<PatientEntry> pFtr = dialog.showAndWait();

        pFtr.ifPresent(p -> {
            if (p != null) {
                patients.add(p);
                changeMonitor = true;
            }
        });


    }

    // ########################################################################

    @FXML
    private void whosComingClicked() {
        Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Terminfilter");
        dialog.setHeaderText(
            "Für welchen Zeitraum sollen die Patienten mit Terminen\n"
            + "gefunden werden?");

        dialog.getDialogPane().getButtonTypes()
        .addAll(ButtonType.OK);

        Node btn = dialog.getDialogPane()
            .lookupButton(ButtonType.OK);
        btn.setDisable(true);

        HBox vbox = new HBox();

        Label msg = new Label();

        //TextField startDate = new TextField();
        DatePicker startDate = new DatePicker();
        startDate.setPromptText("Startdatum dd.mm.jjjj");
        startDate.setConverter(new LocalDateConverter());

        //TextField endDate = new TextField();
        DatePicker endDate = new DatePicker();
        endDate.setPromptText("Enddatum dd.mm.jjjj");
        endDate.setConverter(new LocalDateConverter());

        startDate.valueProperty().addListener(
        (obs, oldValue, newValue) -> {

            btn.setDisable(true);
            msg.setText("");
            if (endDate.getValue() != null && newValue != null
                && newValue.compareTo(endDate.getValue()) > 0) {
                msg.setText(
                    "Startdatum darf nicht nach dem Enddatum liegen.");
            } else {
                btn.setDisable(false);
            }

        });

        endDate.valueProperty().addListener(
        (obs, oldValue, newValue) -> {

            btn.setDisable(true);
            msg.setText("");
            if (startDate.getValue() != null && newValue != null
                && startDate.getValue().compareTo(newValue) > 0) {
                msg.setText(
                    "Enddatum darf nicht vor dem Startdatum liegen.");
            } else {
                btn.setDisable(false);
            }
        });

        dialog.setResultConverter(okbutton -> {
            if (okbutton == ButtonType.OK) {
                return new Pair<>(startDate.getValue(), endDate.getValue());
            }
            return null;
        });

        vbox.getChildren().addAll(startDate, endDate);

        dialog.getDialogPane().setContent(vbox);


        Optional<Pair<LocalDate, LocalDate>> datesFtr = dialog.showAndWait();

        datesFtr.ifPresent(dates -> {
            LocalDate start = dates.getKey();
            LocalDate end = dates.getValue();

            Task<Pair<List<PatientEntry>, Map<String, Integer>>> getScheduledPatients =  new Task<>() {
                @Override
                protected Pair<List<PatientEntry>, Map<String, Integer>> call() throws Exception {
                    List<PatientEntry> scheduledPatients = new ArrayList<>();
                    Map<String, Integer> vaccineCounters = new HashMap<>();
                    for (VaccineBrand v : VaccineBrand.values()) {
                        vaccineCounters.put(v.getValue(), 0);
                    }

                    for (PatientEntry p : patients) {
                        if (p.getFirstVaccinationDate() != null
                        && p.getFirstVaccinationDate().compareTo(start) >= 0
                        && p.getFirstVaccinationDate().compareTo(end) <= 0
                        || p.getSecondVaccinationDate() != null 
                        && p.getSecondVaccinationDate().compareTo(start) >= 0
                        && p.getSecondVaccinationDate().compareTo(end) <= 0) {

                            scheduledPatients.add(p);
                        }

                        if (p.getFirstVaccinationDate() != null
                        && p.getFirstVaccinationDate().compareTo(start) >= 0
                        && p.getFirstVaccinationDate().compareTo(end) <= 0) {
                            vaccineCounters.put(p.getFirstVaccine().getValue(), vaccineCounters.get(p.getFirstVaccine().getValue()) +1); 
                        }

                        if (p.getSecondVaccinationDate() != null 
                        && p.getSecondVaccinationDate().compareTo(start) >= 0
                        && p.getSecondVaccinationDate().compareTo(end) <= 0) {
                            vaccineCounters.put(p.getSecondVaccine().getValue(), vaccineCounters.get(p.getSecondVaccine().getValue()) +1);
                        }
                    }
                    return new Pair<>(scheduledPatients, vaccineCounters);
                }
            };

            new Thread(getScheduledPatients).start();

            try {
                Pair<List<PatientEntry>, Map<String, Integer>> temp = getScheduledPatients.get();
                List<PatientEntry> list = temp.getKey();
                Map<String, Integer> vaccines = temp.getValue();

                patients.removeAll(list);
                patients.addAll(0, list);
                Alert orderList = new Alert(Alert.AlertType.INFORMATION);
                orderList.setTitle("Patientenliste");
                orderList.setHeaderText("Zeitraum: " + start + " bis " + end);
                //orderList.setContentText(
                String content = list.size() + " Patienten haben einen Termin "
                + "und wurden an den Anfang der Tabelle geschoben.\n\n"
                + "Folgende Impfstoffe werden benötigt:\n\n";
                for (String vn : vaccines.keySet()) {
                    if (vaccines.get(vn) > 0) {
                        content += vn + ": " + vaccines.get(vn) + "\n";
                    }
                }

                orderList.setContentText(content);
                orderList.showAndWait();
            } catch (Exception e) {
            }


        });

    }

    // ########################################################################

    @FXML
    private void calculateOrderClicked() {
        System.out.println("Calculating minimal vaccine order");

        Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Vorbestellung");
        dialog.setHeaderText(
            "Für welchen Zeitraum soll Impfstoff bestellt werden?");

        dialog.getDialogPane().getButtonTypes()
        .addAll(ButtonType.OK);

        Node btn = dialog.getDialogPane()
            .lookupButton(ButtonType.OK);
        btn.setDisable(true);

        HBox vbox = new HBox();

        Label msg = new Label();

        //TextField startDate = new TextField();
        DatePicker startDate = new DatePicker();
        startDate.setPromptText("Startdatum dd.mm.jjjj");
        startDate.setConverter(new LocalDateConverter());

        //TextField endDate = new TextField();
        DatePicker endDate = new DatePicker();
        endDate.setPromptText("Enddatum dd.mm.jjjj");
        endDate.setConverter(new LocalDateConverter());

        startDate.valueProperty().addListener(
        (obs, oldValue, newValue) -> {

            btn.setDisable(true);
            msg.setText("");
            if (endDate.getValue() != null && newValue != null
                && newValue.compareTo(endDate.getValue()) > 0) {
                msg.setText(
                    "Startdatum darf nicht nach dem Enddatum liegen.");
            } else {
                btn.setDisable(false);
            }

        });

        endDate.valueProperty().addListener(
        (obs, oldValue, newValue) -> {

            btn.setDisable(true);
            msg.setText("");
            if (startDate.getValue() != null && newValue != null
                && startDate.getValue().compareTo(newValue) > 0) {
                msg.setText(
                    "Enddatum darf nicht vor dem Startdatum liegen.");
            } else {
                btn.setDisable(false);
            }
        });

        dialog.setResultConverter(okbutton -> {
            if (okbutton == ButtonType.OK) {
                return new Pair<>(startDate.getValue(), endDate.getValue());
            }
            return null;
        });

        vbox.getChildren().addAll(startDate, endDate);

        dialog.getDialogPane().setContent(vbox);


        Optional<Pair<LocalDate, LocalDate>> datesFtr = dialog.showAndWait();

        datesFtr.ifPresent(dates -> {

            Map<VaccineBrand, Task<Integer>> threadPool = new HashMap<>();

            for (VaccineBrand v : VaccineBrand.values()) {
                Task<Integer> temp = new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        return (new PatientListFunctions())
                            .calculateOrder(patients, v, dates.getKey(),
                            dates.getValue());
                    }
                };

                threadPool.put(v, temp);

                (new Thread(temp)).start();
            }

            try {
                Alert orderList = new Alert(Alert.AlertType.INFORMATION);
                orderList.setTitle("Bestellliste mit Mindestmengen");
                orderList.setHeaderText("Für den Zeitraum vom "
                    + dates.getKey() + " bis " + dates.getValue()
                    + " werden folgende Mengen benötigt:");

                String contentText = "";
                for (VaccineBrand v : VaccineBrand.values()) {
                    contentText += v.getValue() + ": "
                        + threadPool.get(v).get() + "\n";
                }

                orderList.setContentText(contentText);
                orderList.showAndWait();
            } catch (Exception e) {
            }

        });
    }

    // ########################################################################

    @FXML
    private void remainingVaccineClicked() {
        System.out.println(
            "Calculating remaining vaccine for first time patients");

        Dialog<Pair<Pair<LocalDate, LocalDate>, List<Integer>>> dialog
            = new Dialog<>();
        dialog.setTitle("Übrige Impfdosen");
        dialog.setHeaderText("Für welchen Zeitraum sollen unter Angabe der "
        + "ge-\nlieferten Impfstoffe die Impfdosen nach Abzug der\n"
        + "vorgemerkten Zweitimpfungen berechnet werden?");

        dialog.getDialogPane().getButtonTypes()
        .addAll(ButtonType.OK);

        Node btn = dialog.getDialogPane()
            .lookupButton(ButtonType.OK);
        btn.setDisable(true);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(5);
        gridpane.setVgap(5);

        Label msg = new Label();

        //TextField startDate = new TextField();
        DatePicker startDate = new DatePicker();
        startDate.setPromptText("Startdatum dd.mm.jjjj");
        startDate.setConverter(new LocalDateConverter());

        //TextField endDate = new TextField();
        DatePicker endDate = new DatePicker();
        endDate.setPromptText("Enddatum dd.mm.jjjj");
        endDate.setConverter(new LocalDateConverter());


        gridpane.add(startDate, 0, 0);
        gridpane.add(endDate, 1, 0);
        gridpane.add(msg, 0, 1);


        Map<VaccineBrand, TextField> deliveries = new HashMap<>();
        for (VaccineBrand v : VaccineBrand.values()) {
            gridpane.add(new Label(v.getValue()), 0, v.ordinal() + 2);
            TextField temp = new TextField();
            deliveries.put(v, temp);
            gridpane.add(temp, 1, v.ordinal() + 2);
        }

        startDate.valueProperty().addListener(
        (obs, oldValue, newValue) -> {

            btn.setDisable(true);
            msg.setText("");
            if (endDate.getValue() != null && newValue != null
                && newValue.compareTo(endDate.getValue()) > 0) {
                msg.setText(
                    "Startdatum darf nicht nach dem Enddatum liegen.");
            } else {
                btn.setDisable(false);
            }

        });

        endDate.valueProperty().addListener(
        (obs, oldValue, newValue) -> {

            btn.setDisable(true);
            msg.setText("");
            if (startDate.getValue() != null && newValue != null
                && startDate.getValue().compareTo(newValue) > 0) {
                msg.setText(
                    "Enddatum darf nicht vor dem Startdatum liegen.");
            } else {
                btn.setDisable(false);
            }
        });

        dialog.setResultConverter(okbutton -> {
            if (okbutton == ButtonType.OK) {
                List<Integer> deliveryNumbers = new ArrayList<>();
                for (VaccineBrand v : VaccineBrand.values()) {
                    TextField t = deliveries.get(v);
                    int temp = 0;
                    try {
                        temp = Integer.parseInt(t.getText());
                    } catch (Exception e) {
                        temp = 0;
                    } finally {
                        deliveryNumbers.add(temp);
                    }
                }
                return new Pair<>(
                    new Pair<>(startDate.getValue(), endDate.getValue()),
                        deliveryNumbers);
            }
            return null;
        });

        dialog.getDialogPane().setContent(gridpane);


        Optional<Pair<Pair<LocalDate, LocalDate>, List<Integer>>> datesFtr =
            dialog.showAndWait();

        datesFtr.ifPresent(fields -> {

            Map<VaccineBrand, Task<Integer>> threadPool = new HashMap<>();

            for (int i = 0; i < VaccineBrand.values().length; i++) {
                VaccineBrand v = VaccineBrand.values()[i];
                int delivered = fields.getValue().get(i);
                Task<Integer> temp = new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        return (new PatientListFunctions())
                            .remainingVaccine(patients, v,
                            delivered, fields.getKey().getKey(),
                            fields.getKey().getValue());
                    }
                };

                threadPool.put(v, temp);

                (new Thread(temp)).start();
            }

            try {
                Alert remainingVaccines =
                    new Alert(Alert.AlertType.INFORMATION);
                remainingVaccines.setTitle("Übrige Impfdosen");
                remainingVaccines.setHeaderText("Für den Zeitraum vom "
                    + fields.getKey().getKey() + " bis "
                    + fields.getKey().getValue()
                    + " sind folgende Impfdosen für Erstimpfungen übrig:");

                String contentText = "";
                for (VaccineBrand v : VaccineBrand.values()) {
                    contentText += v.getValue() + ": "
                        + threadPool.get(v).get() + "\n";
                }

                remainingVaccines.setContentText(contentText);
                remainingVaccines.showAndWait();
            } catch (Exception e) {
            }

        });

    }

    // ########################################################################

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
                decryptButton.setDisable(!pwField1.getText().equals(newValue));
                if (newValue.length() < 8) {
                    msg.setText("Passwort zu kurz");
                    decryptButton.setDisable(true);
                } else if (!pwField1.getText().equals(newValue)) {
                    msg.setText("Unterschiedliche Passwörter");
                } else {
                    msg.setText("");
                }
            });

            pwField1.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                decryptButton.setDisable(!pwField2.getText().equals(newValue));
                if (newValue.length() < 8) {
                    msg.setText("Passwort zu kurz");
                    decryptButton.setDisable(true);
                } else if (!pwField2.getText().equals(newValue)) {
                    msg.setText("Unterschiedliche Passwörter");
                } else {
                    msg.setText("");
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


        patients = FXCollections.observableArrayList();
        table1.setItems(patients);

        changeMonitor = false;

        registrationDate
            .setCellValueFactory(
            new PropertyValueFactory<>("registrationDate"));
        registrationDate.setComparator((a, b) -> {

            DateTimeFormatter dt = DateTimeFormatter
                .ofPattern("dd.MM.yyyy HH:mm:ss");
            LocalDateTime t1 = LocalDateTime.parse(a, dt);
            LocalDateTime t2 = LocalDateTime.parse(b, dt);

            return t1.compareTo(t2);
        });

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
        birthday.setComparator((a, b) -> {

            LocalDate t1 = LocalDate.parse(a, PatientEntry.DATE_FORMATTER);
            LocalDate t2 = LocalDate.parse(b, PatientEntry.DATE_FORMATTER);

            return t1.compareTo(t2);
        });
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
                        .addListener((obs, oldValue, newValue) -> {
                        ((PatientEntry) cell.getTableRow().getItem())
                            .setFirstVaccinationDate(newValue);

                        changeMonitor = true;
                        });

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
                        .addListener((obs, wasSelected, isSelected) -> {
                            TableRow<PatientEntry> r = cell.getTableRow();
                            PatientEntry pat = (PatientEntry) r.getItem();
                            pat.setFirstVaccinationDone(isSelected);
                            if (isSelected) {
                                r.setStyle("-fx-background-color:orange");
                            }
                            else {
                                r.setStyle("-fx-background-color:white");
                            }
                            changeMonitor = wasSelected != isSelected;;
                        });
                            

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
                        .addListener((obs, oldValue, newValue) -> {
                            ((PatientEntry) cell.getTableRow().getItem())
                                .setSecondVaccinationDate(newValue);
                        
                        changeMonitor = true;
                    });

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
                        .addListener((obs, wasSelected, isSelected) -> {
                        TableRow<PatientEntry> r = cell.getTableRow();
                        PatientEntry pat = (PatientEntry) r.getItem();
                        pat.setSecondVaccinationDone(isSelected);
                        if (isSelected && pat.isFirstVaccinationDone()){
                            r.setStyle("-fx-background-color:green");
                        }
                        else {
                            r.setStyle("-fx-background-color:orange");
                        }
                        changeMonitor = wasSelected != isSelected;
                    });

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

        TableViewSelectionModel<PatientEntry> selectionModel =
            table1.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

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

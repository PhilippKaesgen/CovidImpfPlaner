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

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

public class DateTableCell<T> extends TableCell<T, LocalDate> {
    /**
     * The DatePicker widget. Looks fancy.
     */
    private final DatePicker datePicker;

    /**
     * Constructor for the custom DateTableCell. Derived from the
     * ColorPicker cell on github.
     * @param column to colonize with DateTableCells
     */
    public DateTableCell(final TableColumn<T, LocalDate> column) {
        this.datePicker = new DatePicker();
        this.datePicker.editableProperty().bind(column.editableProperty());
        this.datePicker.disableProperty().bind(column.editableProperty().not());
        this.datePicker.setOnShowing(event -> {
            final TableView<T> tableView = getTableView();
            tableView.getSelectionModel().select(getTableRow().getIndex());
            tableView.edit(
                tableView.getSelectionModel().getSelectedIndex(), column);
        });
        this.datePicker.valueProperty().addListener(
        (observable, oldValue, newValue) -> {
            if (isEditing()) {
                commitEdit(newValue);
            }
        });
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.datePicker.setConverter(new StringConverter<LocalDate>() {

            @Override public String toString(final LocalDate date) {
                if (date != null) {
                    return PatientEntry.DATE_FORMATTER.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(final String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, PatientEntry.DATE_FORMATTER);
                } else {
                    return null;
                }
            }
        });

    }

    @Override
    protected final void updateItem(final LocalDate item, final boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            this.datePicker.setValue(item);
            this.setGraphic(this.datePicker);
        }
    }

    /**
     * ObjectProperty of the DateTableCell.
     * @return ObjectProperty of LocalDate
     */
    public ObjectProperty<LocalDate> valueProperty() {
        return this.datePicker.valueProperty();
    }

    /**
     * Getter method for the contained date.
     * @return current LocalDate
     */
    public final LocalDate getValue() {
        return this.datePicker.getValue();
    }

    /**
     * Setter method for the contained date.
     * @param value new LocalDate
     */
    public final void setValue(final LocalDate value) {
        this.datePicker.setValue(value);
    }
}

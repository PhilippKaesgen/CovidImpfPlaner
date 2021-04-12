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

import javafx.util.StringConverter;

public class LocalDateConverter extends StringConverter<LocalDate> {

    @Override public final String toString(final LocalDate date) {
        if (date != null) {
            return PatientEntry.DATE_FORMATTER.format(date);
        } else {
            return "";
        }
    }

    @Override public final LocalDate fromString(final String string) {
        try {
            return LocalDate.parse(string, PatientEntry.DATE_FORMATTER);
        } catch(Exception e) {
            return null;
        }
    }
}
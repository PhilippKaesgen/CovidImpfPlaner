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
import java.util.List;

public class PatientListFunctions {

    /**
     * Based on the vaccine and time frame, this function calculates how many
     * vaccine doses have to be ordered at least to vaccinate the second time
     * vaccination patients.
     * @param patients
     * @param vaccine
     * @param startDate
     * @param endDate
     * @return minimum amount of vaccine doses to order
     */
    public final int calculateOrder(final List<PatientEntry> patients,
        final VaccineBrand vaccine, final LocalDate startDate,
        final LocalDate endDate) {

        int result = 0;

        for (PatientEntry p : patients) {
            if (!p.isSecondVaccinationDone()
            && p.getSecondVaccinationDate() != null
            && p.getSecondVaccinationDate().compareTo(startDate) >= 0
            && p.getSecondVaccinationDate().compareTo(endDate) <= 0
            && p.getSecondVaccine().getValue().equals(vaccine.getValue())) {
                result++;
            }
        }

        return result;
    }

    /**
     * Based on the vaccine and time frame, this function calculates how many
     * vaccine doses are left for first time vaccination patients.
     * @param patients
     * @param vaccine
     * @param list
     * @param startDate
     * @param endDate
     * @return number of left vaccination doses
     */
    public final int remainingVaccine(final List<PatientEntry> patients,
        final VaccineBrand vaccine, final int list,
        final LocalDate startDate, final LocalDate endDate) {

        int result = list;

        for (PatientEntry p : patients) {
            if (!p.isSecondVaccinationDone()
            && p.getSecondVaccinationDate() != null
            && p.getSecondVaccinationDate().compareTo(startDate) >= 0
            && p.getSecondVaccinationDate().compareTo(endDate) <= 0
            && p.getSecondVaccine().getValue().equals(vaccine.getValue())) {
                result--;
            }
        }

        return result;
    }
}

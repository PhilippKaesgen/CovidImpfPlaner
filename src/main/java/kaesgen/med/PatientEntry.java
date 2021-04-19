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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PatientEntry  implements Serializable {

    /**
     * pattern for the date formatter.
     */
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    /**
     * public date formatter for unified date formats.
     */
    public static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern(DATE_PATTERN);


    //   Serialization Field   ################################################

    private static final long serialVersionUID = 133742L;

    /**
     * registration date for the vaccination queue.
     */
    private LocalDateTime registrationDate;
    /**
     * patient's ID in the practice's data base.
     */
    private int id;
    /**
     * patient's last name.
     */
    private String lastName;
    /**
     * patient's first name.
     */
    private String firstName;
    /**
     * patient's birthday.
     */
    private LocalDate birthday;
    /**
     * additional information for priority.
     */
    private String additionalInfo;
    /**
     * patient's landline number.
     */
    private String landline;
    /**
     * patient's mobile phone number.
     */
    private String mobile;
    /**
     * first vaccination appointment.
     */
    private LocalDate firstVaccinationDate;
    /**
     * first vaccination done.
     */
    private boolean firstVaccinationDone;
    /**
     * vaccine brand of first vaccination.
     */
    private VaccineBrand firstVaccine;
    /**
     * second vaccination appointment.
     */
    private LocalDate secondVaccinationDate;
    /**
     * second vaccination done.
     */
    private boolean secondVaccinationDone;
    /**
     * vaccine brand of second vaccination.
     */
    private VaccineBrand secondVaccine;

    //#########################################################################

    // Transient

    /**
     * Constructor for a patient entry. The registration date is set
     * automatically.
     * @param id
     * @param lastName
     * @param firstName
     * @param birthday
     * @param additionalInfo
     * @param landline
     * @param mobile
     * @param firstVaccinationDate
     * @param firstVaccinationDone
     * @param firstVaccine
     * @param secondVaccinationDate
     * @param secondVaccinationDone
     * @param secondVaccine
     */
    public PatientEntry(
        final int id,
        final String lastName,
        final String firstName,
        final LocalDate birthday,
        final String additionalInfo,
        final String landline,
        final String mobile,
        final LocalDate firstVaccinationDate,
        final boolean firstVaccinationDone,
        final VaccineBrand firstVaccine,
        final LocalDate secondVaccinationDate,
        final boolean secondVaccinationDone,
        final VaccineBrand secondVaccine
        ) {

        this.registrationDate = LocalDateTime.now();
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.additionalInfo = additionalInfo;
        this.landline = landline;
        this.birthday = birthday;
        this.mobile = mobile;
        this.firstVaccinationDate = firstVaccinationDate;
        this.firstVaccinationDone = firstVaccinationDone;
        this.secondVaccinationDate = secondVaccinationDate;
        this.secondVaccinationDone = secondVaccinationDone;
        this.firstVaccine = firstVaccine;
        this.secondVaccine = secondVaccine;
    }

    //getter
    /**
     * Getter for the patient's registration date for the vaccination.
     * @return String
     */
    public String getRegistrationDate() {
        String pattern = DATE_PATTERN + " HH:mm:ss";
        DateTimeFormatter dt = DateTimeFormatter.ofPattern(pattern);

        return dt.format(this.registrationDate);
    }

    /**
     * Getter for the patient's registration date, but for sorting purposes.
     * @return LocalDateTime
     */
    public LocalDateTime getRegistrationDateTime() {
        return this.registrationDate;
    }

    /**
     * Getter for the patient's ID number under which they are filed
     * in the practice's data base.
     * @return String
     */
    public String getId() {
        return Integer.toString(id);
    }

    /**
     * Getter for the patient's last name.
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for the patient's first name.
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for the patient's additional information like priority.
     * @return String
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Getter for the patient's landline number.
     * @return String
     */
    public String getLandline() {
        return landline;
    }

    /**
     * Getter for the patient's mobile phone number.
     * @return String
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Getter for the patient's birthday.
     * @return date
     */
    public String getBirthday() {
        return DATE_FORMATTER.format(this.birthday);
    }

    /**
     * Getter for the first vaccination appointment.
     * @return first appointment
     * @throws java.time.DateTimeException
     */
    public LocalDate getFirstVaccinationDate()
    throws java.time.DateTimeException {
        return firstVaccinationDate;
    }

    /**
     * Getter for polling the first vaccination status.
     * @return boolean
     */
    public boolean isFirstVaccinationDone() {
        return firstVaccinationDone;
    }

    /**
     * Getter for the second vaccination appointment.
     * @return second appointment
     * @throws java.time.DateTimeException
     */
    public LocalDate getSecondVaccinationDate()
    throws java.time.DateTimeException {
        return secondVaccinationDate;
    }

    /**
     * Getter for polling the second vaccination status.
     * @return boolean
     */
    public boolean isSecondVaccinationDone() {
        return secondVaccinationDone;
    }

    /**
     * Getter for the first vaccine brand.
     * @return Enum of vaccine brands
     */
    public VaccineBrand getFirstVaccine() {
        return firstVaccine;
    }

    /**
     * Getter for the second vaccine brand.
     * @return Enum of vaccine brands
     */
    public VaccineBrand getSecondVaccine() {
        return secondVaccine;
    }

    /*
     * Editing Manifest
     *
     * registration Date:
     * not editable at all times
     *
     * id, ... mobile:
     * editable on request and if the second vaccination is not done and
     *
     * first vaccination date:
     * editable until the first vaccination is done and after sanity check is
     * passed
     *
     * first vaccination done:
     * editable after a vaccination date is scheduled
     *
     * second vaccination date:
     * editable after the first vaccination is done and the vaccine brand is
     * chosen
     *
     * second vaccination done:
     * editable after the second vaccination date is scheduled
     *
     * vaccine brand:2x
     *
    */


    /**
     * Setter for the patient's ID number under which they are filed in
     * the practice's data base.
     * @param arg
     */
    public void setId(final String arg) {
        this.id = Integer.parseInt(arg);
    }

    /**
     * Setter for the patient's last name.
     * @param arg
     */
    public void setLastName(final String arg) {
        if (isName(arg)) {
            this.lastName = arg;
        }
    }

    /**
     * Setter for the patient's first name.
     * @param arg
     */
    public void setFirstName(final String arg) {
        if (isName(arg)) {
            this.firstName = arg;
        }
    }

    /**
     * Setter for optional additional information like priority for the
     * vaccination.
     * @param arg
     */
    public void setAdditionalInfo(final String arg) {
        this.additionalInfo = arg;
    }

    /**
     * Setter for the patient's landline number.
     * @param arg
     */
    public void setLandline(final String arg) {
        if (isPhoneNumber(arg)) {
            this.landline = arg;
        }
    }

    /**
     * Setter for the patient's mobile phone number.
     * @param arg
     */
    public void setMobile(final String arg) {
        if (isPhoneNumber(arg)) {
            this.mobile = arg;
        }
    }

    /**
     * Setter for the patient's birthday.
     * @param arg
     */
    public void setBirthday(final String arg) {
        try {
            this.birthday = LocalDate.from(DATE_FORMATTER.parse(arg));
        } catch (Exception e) {
        }
    }

    /**
     * Setter for scheduling the first vaccination appointment.
     * @param date
     */
    public void setFirstVaccinationDate(final LocalDate date) {
        if (!firstVaccinationDone) {
            this.firstVaccinationDate = date;
        }
    }

    /**
     * Setter for confirming the first vaccination.
     * @param b
     */
    public void setFirstVaccinationDone(final boolean b) {
        if (firstVaccinationDate != null && firstVaccine != null) {
            this.firstVaccinationDone = b;
        }
    }

    /**
     * Setter for scheduling the second vaccination appointment.
     * @param date
     */
    public void setSecondVaccinationDate(final LocalDate date) {
        if (!secondVaccinationDone) {
            this.secondVaccinationDate = date;
        }
    }

    /**
     * Setter for confirming the second vaccination.
     * @param b
     */
    public void setSecondVaccinationDone(final boolean b) {
        if (firstVaccinationDone && secondVaccinationDate != null
            && secondVaccine != null) {
            this.secondVaccinationDone = b;
        }
    }

    /**
     * Setter for first vaccine brand.
     * @param vaccine
     */
    public void setFirstVaccine(final VaccineBrand vaccine) {
        if (!firstVaccinationDone) {
            this.firstVaccine = vaccine;
        }
    }

    /**
     * Setter for second vaccine brand.
     * @param vaccine
     */
    public void setSecondVaccine(final VaccineBrand vaccine) {
        if (!secondVaccinationDone) {
            this.secondVaccine = vaccine;
        }
    }

    //#########################################################################

    /*
     * Sanity Manifest
     *
     * a patient id or (first name, last name, birthdate and
     * (landline or mobile)) are given
    */

    /**
     * Reports whether a given String is a phone number.
     * @param n
     * @return boolean
     */
    public static boolean isPhoneNumber(final String n) {

        char separator = '-';

        if (n.length() < 10) {
            return false;
        }

        char[] nArr = n.toCharArray();

        if (nArr[0] != '0') {
            return false;
        }

        if (!Character.isDigit(nArr[1])
            || !Character.isDigit(nArr[2])
            || !Character.isDigit(nArr[3])) {
            return false;
        }

        if (!Character.isDigit(nArr[4]) && nArr[4] != separator) {
            return false;
        }

        if (Character.isDigit(nArr[4]) && nArr[5] != separator
            || nArr[4] == separator && !Character.isDigit(nArr[5])) {
            return false;
        }

        for (int i = 6; i < n.length(); i++) {
            if (!Character.isDigit(nArr[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reports whether a given String is a name.
     * @param name
     * @return boolean
     */
    public static boolean isName(final String name) {
        if (name.equals("")) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isWhitespace(c) && c != '-') {
                return false;
            }
        }

        return true;
    }

    /**
     * Reports whether the patient entry fields are conclusive.
     * @return boolean
     */
    public final boolean isPatientEntrySound() {

        if (!isName(this.firstName)) {
            return false;
        }

        if (!isName(this.lastName)) {
            return false;
        }

        if (!isPhoneNumber(this.landline) && !isPhoneNumber(this.mobile)) {
            return false;
        }

        return true;
    }

    /**
     * Returns the String representation of the patient.
     * @return String
     */
    public final String toString() {
        return firstName + " " + lastName + " (Tel: " + landline + ", Mobil: "
            + mobile + "), Zusatzinformationen: " + additionalInfo;
    }

    /**
     * Determines whether this and another PatientEntry are equal.
     * @param p2
     * @return boolean
     */
    public final boolean equals(final PatientEntry p2) {
        return this.firstName.equals(p2.getFirstName())
            && this.lastName.equals(p2.getLastName())
            && this.getBirthday().compareTo(p2.getBirthday()) == 0;
    }

}

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

public enum VaccineBrand {
    /** Astrazeneca. */
    Astrazeneca("Astrazeneca"),
    /** BioNTech. */
    BioNTech("BioNTech"),
    /** Johnson & Johnson. */
    JohnsonNJohnson("Johnson & Johnson"),
    /** Moderna. */
    Moderna("Moderna"),
    /** Novavax. */
    Novavax("Novavax"),
    /** Sputnik. */
    SputnikV("Sputnik V");

    /**
     * private field String.
     */
    private String value;

    /**
     * Private enum constructor.
     * @param arg String
     */
    VaccineBrand(final String arg) {
        this.value = arg;
    }

    /**
     * Getter for the internal String.
     * @return String
     */
    public String getValue() {
        return value;
    }

 }

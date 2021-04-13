# CovidImpfplaner

Mit Beginn der Impfungen gegen Covid-19 stehen Hausärzte vor organisatorischen
Herausforderungen: Mehrere hundert Patienten melden sich für die Eintragung
in die Impfliste an und müssen bei Verfügbarkeit in der Reihenfolge der
Anmeldung Impftermine erhalten, sobald der Impfstoff verfügbar ist. Bevor
aber Patienten ihre Erstimpfung erhalten, müssen erst die Zweitimpflinge
versorgt werden. Dazu kommen unterschiedliche Impfstoffhersteller mit 
sich ändernden Empfehlungen des Robert-Koch-Instituts. Das reinste Chaos.

Mit dem CovidImpfPlaner will ich es Arztpraxen erleichtern, die Anflut
von **Impfanmeldungen zu bewältigen**, um eine möglichst schnelle Durchimpfung
der Bevölkerung zu erreichen.

## Arbeitsablauf der Arztpraxen beim Impfen

Beim mir bekannten Arbeitsablauf für Covid-19-Impfungen bestellt eine
Arztpraxis ca. eine Woche im Voraus Impfdosen. Ein paar Tage später meldet sich
die Apotheke zurück, wie viele Impfdosen sie tatsächlich liefern kann. Mit
dieser Information rufen die Arztpraxen zuerst die Zweitimpflinge an und
bestellen sie ein. Den übrigen Impfstoff verimpfen sie bei Erstimpflingen.

## Funktionen

Folgende Funktionen werden geboten:
1. Patientendaten hinzufügen: Patienten-ID aus der praxiseigenen Datenbank,
Vor- und Nachname, Geburtsdatum, Telefonnummern, zusätzliches Informationsfeld
für Priorisierung o.ä.
![](images/anmelden.gif)
2. Impftermine vereinbaren und abhaken, wenn die Impfung vollzogen wurde.
![](images/termin.gif)
3. Sortierung der Patienten nach verschiedenen Kriterien.
![](images/sortieren.gif)
4. Die Mindestbestellmenge für einen wählbaren Zeitraum aus den angemeldeten
Zweitimpflingen bestimmen können.
![](images/mindestbestellung.gif)
5. Aus der Rückmeldung der Apotheke über die tatsächlich lieferbaren Impfdosen
die möglichen Erstimpflinge für einen wählbaren Zeitraum berechnen.
![](images/uebrig.gif)
6. "Wer kommt wann?" Heraussuchen der Patienten, die einen Termin in einem
vom Benutzer wählbaren Zeitraum haben.
![](images/werkommt.gif)
3. Speichern der Anmeldedaten in einer passwortgeschützten Datei (Passwort
wird vom Benutzer beim Speichern festgelegt und muss zum Öffnen erneut
eingegeben werden).
![](images/speichern.gif)

## FAQ

### Warum wird der Impftermin nicht richtig gespeichert?
Beim Eintragen von Terminen geht das Programm davon aus, dass zuerst Impfdatum
und Impfstoff gewählt werden und dann erst der Haken gesetzt wird. Der Haken
bei jeder Impfung kann umgekehrt auch nur dann abgespeichert werden, wenn zuvor
Impfdatum und Impfstoff angegeben wurden. Der Haken beim zweiten Impftermin
wird auch nur dann gespeichert, wenn die erste Impfung gültig abgehakt wurde.
Dieser Mechanismus dient zur
Sicherstellung, dass nicht aus Versehen unlogische Angaben gemacht werden.

### Woher bekomme ich das Passwort zum Speichern?
Das Passwort wird vom Benutzer, d.h. der Arztpraxis, festgelegt. Es muss beim Speichern zur
Vermeidung von Tippfehlern bei der Eingabe zweimal eingegeben werden. Dasselbe
Passwort muss zum Öffnen der Datei eingegeben werden. Das Passwort darf nicht
an Dritte weitergegeben werden.

### Muss immer dasselbe Passwort zum Speichern derselben Datei eingegeben werden?
Nein. Bei jedem Speichervorgang kann ein neues Passwort festgelegt werden.

### Ein Patient hat abgesagt und wurde aus der Liste gelöscht. Nun möchte er doch wieder geimpft werden. Kann er auf den alten Listenplatz zurück?
Nein.

### Der OK-Knopf in der Eingabemaske für neue Patienten ist ausgegraut?
Ein Patienteneintrag ist nur dann gültig, wenn entweder die praxisinterne
Patientennummer angegeben wird, oder Nach- und Vorname sowie Geburtsdatum und
mindestens eine Telefonnummer angegeben wurde. Dieser Mechanismus stellt sicher,
dass nur eindeutig identifizierbare Patienten in die Liste aufgenommen werden.

### Beim Berechnen der übrigen Impfstoffe werden negative Zahlen angezeigt?
In diesem Fall wird weniger Impfstoff von der Apotheke geliefert als für die
Zweitimpfungen im angegebenen Zeitraum benötigt werden. Entweder muss bei der
Apotheke noch einmal so viel Impfstoff nachgefragt werden, dass die Differenz
ausgeglichen wird, oder Termine für Zweitimpfungen verschoben werden.

## Anregungen und Support

Für Anregungen zur Verbesserung des Programms bin ich offen!

Der CovidImpfPlaner ist ein Java-Programm, d.h. es funktioniert mit
**Windows**, **Linux** und **MacOS**. Support-Anfragen gerne hier:
pkaesgen(AT)freenet.de

## Lizenz

CovidImpfPlaner  Copyright (C) 2021  Philipp Käsgen
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses.

pkaesgen(AT)freenet.de
Install

(s. www.codejava.net)

download JAVA 11 oder neuer:
https://jdk.java.net/11

certutil -hashfile openjdk-11.0.2_windows-x64_bin.zip SHA256

nach C:\Program Files\Java\jdk-11.0.2 entpacken

Umgebungsvariable PATH auf C:\Program Files\Java\jdk-11.0.2\bin setzen


-p $ModuleFileDir$/lib/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.web --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
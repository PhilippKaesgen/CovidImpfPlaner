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

import java.util.Optional;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;

public class App extends Application {
    /** Application context for connecting controller and main app. */
    private ConfigurableApplicationContext applicationContext;

    /** Weaver for handling both controller and .fxml files. Nice. */
    private FxWeaver fxWeaver;

    /** Main controller for CovidImpfPlaner.fxml. */
    private MainSceneController msc;

    @Override
    public final void start(final Stage stage) {

        fxWeaver = applicationContext.getBean(FxWeaver.class);

        FxControllerAndView<MainSceneController, VBox> mscp = fxWeaver
            .load(MainSceneController.class);

        msc = mscp.getController();

        Parent root = mscp.getView().get();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.getScene().getWindow()
            .addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
            this::closeWindowEvent);
        stage.show();
        stage.setTitle("CovidImpfplaner");

    }

    @Override
    public final void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
            .sources(SpringBootExport.class).run(args);
    }

    @Override
    public void stop() {
    }

    private void closeWindowEvent(final WindowEvent event) {
        System.out.println("Window close request ...");

        // if the dataset has changed, alert the user with a popup
        if (msc.getChangeMonitor()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.NO);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Speichern");
            alert.setHeaderText("Änderungen wurden noch nicht gespeichert.");
            alert.setContentText(String
                .format("Änderungen vor dem Beenden speichern?"));

            Optional<ButtonType> res = alert.showAndWait();

            res.ifPresent(btn -> {
                if (btn.equals(ButtonType.YES)) {
                    msc.saveFileClicked();
                    event.consume();
                } else if (btn.equals(ButtonType.NO)) {
                    this.applicationContext.close();
                    Platform.exit();
                } else {
                    event.consume();
                }

            });
        }
    }

}

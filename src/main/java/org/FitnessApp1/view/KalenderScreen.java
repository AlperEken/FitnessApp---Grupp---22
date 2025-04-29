package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;

public class KalenderScreen {

    private YearMonth aktuellMånad;
    private GridPane kalenderGrid;
    private Label månadLabel;
    private VBox root;

    public KalenderScreen() {
        aktuellMånad = YearMonth.now(); // Starta med nuvarande månad
        skapaKalender();
    }

    public void visaFönster() {
        Stage stage = new Stage();
        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Kalender");
        stage.setScene(scene);
        stage.show();
    }

    private void skapaKalender() {
        root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);

        Button föregåendeMånad = new Button("<");
        Button nästaMånad = new Button(">");
        månadLabel = new Label();
        uppdateraMånadLabel();

        föregåendeMånad.setOnAction(e -> {
            aktuellMånad = aktuellMånad.minusMonths(1);
            uppdateraKalender();
        });

        nästaMånad.setOnAction(e -> {
            aktuellMånad = aktuellMånad.plusMonths(1);
            uppdateraKalender();
        });

        header.getChildren().addAll(föregåendeMånad, månadLabel, nästaMånad);

        kalenderGrid = new GridPane();
        kalenderGrid.setHgap(5);
        kalenderGrid.setVgap(5);
        kalenderGrid.setAlignment(Pos.CENTER);

        root.getChildren().addAll(header, kalenderGrid);
        fyllKalender();
    }

    private void uppdateraKalender() {
        kalenderGrid.getChildren().clear();
        uppdateraMånadLabel();
        fyllKalender();
    }

    private void uppdateraMånadLabel() {
        månadLabel.setText(aktuellMånad.getMonth() + " " + aktuellMånad.getYear());
    }

    private void fyllKalender() {
        // Veckodagsrubriker
        DayOfWeek[] dagar = DayOfWeek.values();
        for (int i = 0; i < 7; i++) {
            Label label = new Label(dagar[i].toString().substring(0, 3));
            kalenderGrid.add(label, i, 0);
        }

        LocalDate förstaDag = aktuellMånad.atDay(1);
        int startKolumn = förstaDag.getDayOfWeek().getValue() % 7;
        int antalDagar = aktuellMånad.lengthOfMonth();

        int rad = 1;
        int kolumn = startKolumn;

        for (int dag = 1; dag <= antalDagar; dag++) {
            Button dagKnapp = new Button(String.valueOf(dag));
            dagKnapp.setPrefSize(40, 40);

            // Här kan du lägga till logik för anteckningar om du vill
            kalenderGrid.add(dagKnapp, kolumn, rad);

            kolumn++;
            if (kolumn > 6) {
                kolumn = 0;
                rad++;
            }
        }
    }
}

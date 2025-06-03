package org.FitnessApp1.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
<<<<<<< Updated upstream
import javafx.scene.paint.Color;
=======
>>>>>>> Stashed changes
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.FitnessApp1.Main;
import org.FitnessApp1.controller.MainMenuController;
import org.FitnessApp1.model.*;
<<<<<<< Updated upstream
=======
import javafx.scene.paint.Color;
>>>>>>> Stashed changes

import java.time.LocalDate;
import java.util.*;

public class CalorieLogScreen {

    private final BorderPane root;
<<<<<<< Updated upstream
=======
    private final VBox layout;
>>>>>>> Stashed changes
    private final TextField matField;
    private final TextField kalorierField;
    private final DatePicker datumPicker;
    private final Button sparaButton;
    private final Text dagensSummaText;
    private final ListView<String> loggLista;
    private final ProgressBar kaloriProgressBar;
    private final Text procentText;
    private final Map<String, Integer> loggTextTillID = new HashMap<>();

    public CalorieLogScreen() {
<<<<<<< Updated upstream
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #e6f0ff);");

        // === Hemikon och rubrik ===
=======
        // === UI Root ===
        root = new BorderPane();
        layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(white, #e6f0ff);");

        // === Header ===
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));

>>>>>>> Stashed changes
        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitHeight(30);
        homeIcon.setFitWidth(30);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> {
            MainMenuScreen menuScreen = new MainMenuScreen(SessionManager.getUsername());
            new MainMenuController(menuScreen, Main.getPrimaryStage());
            Main.getPrimaryStage().setScene(new Scene(menuScreen.getRoot(), 800, 600));
        });
<<<<<<< Updated upstream
        homeIcon.setOnMouseEntered(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        homeIcon.setOnMouseExited(e -> homeIcon.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));
        BorderPane.setMargin(homeIcon, new Insets(20, 0, 0, 20));
        root.setTop(homeIcon);

        VBox layout = new VBox(15);
        layout.getChildren().clear(); // Rensa tidigare innehåll om det finns
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));

        // === Titel ===
        VBox headerBox = new VBox(5);
        headerBox.setPadding(new Insets(10, 0, 10, 0));
        Text title = new Text("Logga kalorier");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setFill(Color.web("#1A3E8B"));
        Label subtitle = new Label("Följ ditt dagliga kaloriintag och nå dina mål.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setStyle("-fx-text-fill: #555;");
        headerBox.getChildren().addAll(title, subtitle);
        layout.getChildren().add(headerBox);

        // === Kortvy ===
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12px;
            -fx-border-radius: 12px;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);
        """);

        Label totalLabel = new Label("Dagens totala kalorier:");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        dagensSummaText = new Text();

        Label loggarLabel = new Label("Loggar för valt datum:");
        loggarLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loggLista = new ListView<>();
        loggLista.setPrefHeight(150);
=======

        VBox titleBox = new VBox(3);
        Label title = new Label("Logga kalorier");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #1A3E8B;");
        Label subtitle = new Label("Följ ditt dagliga kaloriintag och nå dina mål.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setStyle("-fx-text-fill: #555;");
        titleBox.getChildren().addAll(title, subtitle);

        header.getChildren().addAll(homeIcon, titleBox);
        layout.getChildren().add(header);

        // === Inputs & Data ===
        dagensSummaText = new Text();
        loggLista = new ListView<>();
        loggLista.setPrefHeight(150);
        // så listan inte blir för hög på liten skärm
        VBox matBox = new VBox(3);
        Label matLabel = new Label("Vad har du ätit?");
        matLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        matField = new TextField();
        matField.setPromptText("Exempel: Kyckling, ris, yoghurt...");
        matBox.getChildren().addAll(matLabel, matField);


        VBox kalorierBox = new VBox(3);
        Label kalorierLabel = new Label("Kaloriinnehåll");
        kalorierLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        kalorierField = new TextField();
        kalorierField.setPromptText("Exempel: 250");
        kalorierBox.getChildren().addAll(kalorierLabel, kalorierField);

        datumPicker = new DatePicker(LocalDate.now());
        datumPicker.setPromptText("Välj datum");

        sparaButton = new Button("Spara intag");

        // === Progress ===
        kaloriProgressBar = new ProgressBar(0);
        kaloriProgressBar.setPrefWidth(150);
        procentText = new Text();
        HBox progressBox = new HBox(10, kaloriProgressBar, procentText);
        progressBox.setAlignment(Pos.CENTER_LEFT);
>>>>>>> Stashed changes

        // === Contextmeny för redigera & ta bort ===
        ContextMenu contextMenu = new ContextMenu();
        MenuItem redigeraItem = new MenuItem("Redigera");
        MenuItem taBortItem = new MenuItem("Ta bort");
        contextMenu.getItems().addAll(redigeraItem, taBortItem);
        loggLista.setContextMenu(contextMenu);

<<<<<<< Updated upstream
        redigeraItem.setOnAction(e -> redigeraValdLogg());
        taBortItem.setOnAction(e -> taBortValdLogg());

        // === Fält ===
        Label matLabel = new Label("Vad har du ätit?");
        matLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        matField = new TextField();
        matField.setPromptText("Exempel: Kyckling, ris, yoghurt...");

        Label kalorierLabel = new Label("Kaloriinnehåll");
        kalorierLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        kalorierField = new TextField();
        kalorierField.setPromptText("Exempel: 250");

        Label datumLabel = new Label("Datum:");
        datumLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        datumPicker = new DatePicker(LocalDate.now());

        // === Knapp ===
        sparaButton = new Button("Spara intag");
        sparaButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6;");
        sparaButton.setOnMouseEntered(e -> sparaButton.setStyle("-fx-background-color: #0F2A5C; -fx-text-fill: white; -fx-background-radius: 6;"));
        sparaButton.setOnMouseExited(e -> sparaButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6;"));
        sparaButton.setOnAction(e -> sparaIntag());

        // === Progress ===
        kaloriProgressBar = new ProgressBar(0);
        kaloriProgressBar.setPrefWidth(150);
        procentText = new Text();
        HBox progressBox = new HBox(10, kaloriProgressBar, procentText);
        progressBox.setAlignment(Pos.CENTER_LEFT);

        // === Bygg ihop kortet ===
        card.getChildren().addAll(
                totalLabel,
                dagensSummaText,
                loggarLabel,
                loggLista,
                matLabel, matField,
                kalorierLabel, kalorierField,
=======
        // === Händelser ===
        redigeraItem.setOnAction(event -> redigeraValdLogg());
        taBortItem.setOnAction(event -> taBortValdLogg());

        sparaButton.setOnAction(e -> sparaIntag());

        VBox card = new VBox(12);
        card.setMinWidth(300);
        card.setSpacing(10); // lite extra luft mellan elementen

        card.setPadding(new Insets(20));
        card.setMaxWidth(600);
        card.setPrefWidth(0.9 * 800); // Responsiv känsla om du vill
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("""
    -fx-background-color: white;
    -fx-background-radius: 12px;
    -fx-border-radius: 12px;
    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);
""");

        Label totalLabel = new Label("Dagens totala kalorier:");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label loggarLabel = new Label("Loggar för valt datum:");
        loggarLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

//        Label matLabel = new Label("Vad har du ätit?");
//        matLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//
//        Label kalorierLabel = new Label("Kalorier:");
//        kalorierLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label datumLabel = new Label("Datum:");
        datumLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        sparaButton.setStyle("-fx-background-color: #1A3E8B; -fx-text-fill: white; -fx-background-radius: 6;");

        card.getChildren().addAll(
                totalLabel, dagensSummaText,
                loggarLabel, loggLista,
                matBox,
                kalorierBox,
>>>>>>> Stashed changes
                datumLabel, datumPicker,
                sparaButton,
                progressBox
        );

<<<<<<< Updated upstream
        VBox wrapper = new VBox(card);
        wrapper.setPadding(new Insets(20));
        wrapper.setAlignment(Pos.CENTER);

        layout.getChildren().add(wrapper);

        StackPane container = new StackPane(layout);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.TOP_CENTER);

        root.setCenter(container);
=======


        HBox cardWrapper = new HBox(card);
        cardWrapper.setAlignment(Pos.CENTER);
        cardWrapper.setPrefWidth(Double.MAX_VALUE); // gör att den fyller hela bredden

        layout.getChildren().add(cardWrapper);

        layout.setPrefHeight(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

// NYA rader för att undvika blå bakgrund
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setBackground(Background.EMPTY);

// Sätt rätt bakgrund på layout manuellt
        layout.setBackground(new Background(new BackgroundFill(
                Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY
        )));
        root.setCenter(scrollPane);
>>>>>>> Stashed changes

        uppdateraLoggLista();
        uppdateraDagensSumma();
    }

    public Parent getRoot() {
        return root;
    }

<<<<<<< Updated upstream
    // === Logik-metoder ===
=======
    // === Metoder ===

>>>>>>> Stashed changes
    private void sparaIntag() {
        try {
            String mat = matField.getText();
            int kalorier = Integer.parseInt(kalorierField.getText());
            LocalDate datum = datumPicker.getValue();
            int kontoID = SessionManager.getAktivtKontoID();

            CalorieLog logg = new CalorieLog(datum, mat, kalorier, kontoID);
            boolean sparat = new CalorieLogDAO().addLog(logg);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultat");
            alert.setHeaderText(null);
<<<<<<< Updated upstream
            alert.setContentText(sparat ? "Sparat: " + mat + " (" + kalorier + " kcal)" : "Kunde inte spara.");
=======
            alert.setContentText(sparat ? " Sparat: " + mat + " (" + kalorier + " kcal)" : " Kunde inte spara.");
>>>>>>> Stashed changes
            alert.showAndWait();

            matField.clear();
            kalorierField.clear();
            uppdateraDagensSumma();
            uppdateraLoggLista();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fel");
            alert.setContentText("Fyll i både vad du ätit och kalorier (som siffra).");
            alert.showAndWait();
        }
    }

    private void redigeraValdLogg() {
        String selected = loggLista.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String[] delar = selected.split(" \\(");
        String beskrivning = delar[0];
        int kalorier = Integer.parseInt(delar[1].replace(" kcal)", "").trim());

        TextInputDialog dialog = new TextInputDialog(beskrivning + ";" + kalorier);
        dialog.setTitle("Redigera logg");
        dialog.setHeaderText("Redigera beskrivning och kalorier (separera med semikolon)");
        dialog.setContentText("Exempel: Kyckling med ris;600");

        dialog.showAndWait().ifPresent(input -> {
            try {
                String[] ny = input.split(";");
                String nyBeskrivning = ny[0].trim();
                int nyKalorier = Integer.parseInt(ny[1].trim());

                int kontoID = SessionManager.getAktivtKontoID();
                int loggID = loggTextTillID.get(selected);
                CalorieLog logg = new CalorieLog(loggID, datumPicker.getValue(), nyBeskrivning, nyKalorier, kontoID);
                new CalorieLogDAO().updateLogs(logg);

                uppdateraDagensSumma();
                uppdateraLoggLista();
            } catch (Exception ex) {
                Alert fel = new Alert(Alert.AlertType.ERROR);
                fel.setContentText("Felaktigt format. Använd semikolon.");
                fel.showAndWait();
            }
        });
    }

    private void taBortValdLogg() {
        String selected = loggLista.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Bekräfta borttagning");
        confirm.setContentText("Vill du verkligen ta bort denna logg?");
        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                int loggID = loggTextTillID.get(selected);
                new CalorieLogDAO().deleteLog(loggID);
                uppdateraDagensSumma();
                uppdateraLoggLista();
            }
        });
    }

    private void uppdateraDagensSumma() {
        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) return;

        LocalDate datum = datumPicker.getValue();
        int total = new CalorieLogDAO().countTotalCalories(datum, kontoID);
        Account account = new AccountDAO().getAccountByID(kontoID);
        int dagligtMal = account.getDagligtMal();

        int kvar = dagligtMal - total;
        dagensSummaText.setText("Totalt: " + total + " kcal\nKvar till mål: " + kvar + " kcal");

        double progress = (double) total / dagligtMal;
        kaloriProgressBar.setProgress(progress);
        int procent = (int) (progress * 100);
        procentText.setText(procent + "%");
    }

    private void uppdateraLoggLista() {
        loggLista.getItems().clear();
        loggTextTillID.clear();

        int kontoID = SessionManager.getAktivtKontoID();
        if (kontoID == -1) return;

        List<CalorieLog> loggar = new CalorieLogDAO().getLogsForDate(datumPicker.getValue(), kontoID);

        for (CalorieLog logg : loggar) {
            String text = logg.getBeskrivning() + " (" + logg.getKalorier() + " kcal)";
            loggLista.getItems().add(text);
            loggTextTillID.put(text, logg.getLoggID());

<<<<<<< Updated upstream
=======
            // Fade-in på varje rad
>>>>>>> Stashed changes
            FadeTransition ft = new FadeTransition(Duration.millis(300), loggLista);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }
}

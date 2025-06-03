package org.FitnessApp1.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.FitnessApp1.model.GymLocation;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;

public class GoogleMapsGymCardView extends ScrollPane {

    public GoogleMapsGymCardView(List<GymLocation> gyms, Runnable goHomeAction) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(white, #e6f0ff);");

<<<<<<< Updated upstream
        // Ny header med titel + tagline
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));

        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitHeight(28);
        homeIcon.setFitWidth(28);
        homeIcon.setStyle("-fx-cursor: hand;");
        homeIcon.setOnMouseClicked(e -> goHomeAction.run());

        VBox titleBox = new VBox(3);
        Label title = new Label("Utegym i Malmö");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
=======
        // Header med hemikon, rubrik och linje
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 0, 20));

        ImageView homeIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeIcon.setFitHeight(30);
        homeIcon.setFitWidth(30);
        homeIcon.setStyle("""
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0.2, 0, 1);
            """);
        homeIcon.setOnMouseClicked(e -> goHomeAction.run());

        VBox titleBox = new VBox(5);
        Label title = new Label("Utegym i Malmö");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
>>>>>>> Stashed changes
        title.setStyle("-fx-text-fill: #1A3E8B;");

        Label subtitle = new Label("Hitta din nästa träningsplats i det fria.");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: #555;");

<<<<<<< Updated upstream
        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(homeIcon, titleBox);


=======

        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(homeIcon, titleBox);

>>>>>>> Stashed changes
        // Gymkort
        FlowPane content = new FlowPane();
        content.setPadding(new Insets(10, 20, 20, 20));
        content.setHgap(20);
        content.setVgap(20);
        content.setPrefWrapLength(1000);
        content.setAlignment(Pos.TOP_CENTER);

        for (GymLocation gym : gyms) {
            VBox card = new VBox(8);
            card.setPadding(new Insets(10));
            card.setStyle("""
                -fx-border-color: #ccc;
                -fx-border-radius: 12px;
                -fx-background-color: white;
                -fx-background-radius: 12px;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);
                """);

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(gym.getImagePath())));
            imageView.setFitWidth(320);
            imageView.setFitHeight(180);
            imageView.setPreserveRatio(false);
            imageView.setStyle("-fx-cursor: hand;");
            imageView.setOnMouseClicked(e -> openInGoogleMaps(gym.getLat(), gym.getLon()));

            Label nameLabel = new Label(gym.getName());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #0077cc; -fx-underline: true; -fx-cursor: hand;");
            nameLabel.setOnMouseClicked(e -> openInGoogleMaps(gym.getLat(), gym.getLon()));

            // Hover-effekt
            card.setOnMouseEntered(e -> card.setStyle("""
                -fx-border-color: #0077cc;
                -fx-border-radius: 12px;
                -fx-background-color: #f9f9f9;
                -fx-background-radius: 12px;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 4);
                """));
            card.setOnMouseExited(e -> card.setStyle("""
                -fx-border-color: #ccc;
                -fx-border-radius: 12px;
                -fx-background-color: white;
                -fx-background-radius: 12px;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);
                """));

            card.getChildren().addAll(imageView, nameLabel);
            content.getChildren().add(card);

            // Fade-in animation
            FadeTransition ft = new FadeTransition(Duration.millis(400), card);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }

        root.getChildren().addAll(header, content);
        setContent(root);
        setFitToWidth(true);
    }

    private void openInGoogleMaps(double lat, double lon) {
        String url = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ScrollPane getRoot() {
        return this;
    }
}

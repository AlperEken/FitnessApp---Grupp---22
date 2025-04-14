package org.FitnessApp1.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.FitnessApp1.model.GymLocation;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;

public class GoogleMapsGymCardView extends ScrollPane {

    public GoogleMapsGymCardView(List<GymLocation> gyms) {
        FlowPane content = new FlowPane ();
        content.setPadding(new Insets(20));
        content.setHgap(20);
        content.setVgap(20);
        content.setPrefWrapLength(1000);
        setContent(content);
        setFitToWidth(true);

        for (GymLocation gym : gyms) {
            VBox card = new VBox();
            card.setSpacing(5);
            card.setPadding(new Insets(10));
            card.setStyle("""
                -fx-border-color: #ccc;
                -fx-border-radius: 10px;
                -fx-background-color: #fdfdfd;
                -fx-background-radius: 10px;
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

            card.getChildren().addAll(imageView, nameLabel);
            content.getChildren().add(card);
        }
    }

    private void openInGoogleMaps(double lat, double lon) {
        String url = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

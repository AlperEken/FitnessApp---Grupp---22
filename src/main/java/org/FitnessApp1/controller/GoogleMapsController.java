package org.FitnessApp1.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.FitnessApp1.model.GymLocation;
import org.FitnessApp1.view.GoogleMapsGymCardView;

import java.util.List;

public class GoogleMapsController {

    public void showGym() {
        Stage stage = new Stage();
        stage.setTitle("Utegym i Malmö");

        List<GymLocation> gyms = List.of(
                new GymLocation("Ribersborg Utegym", 55.60032686885852, 12.9590796069194, "/images/Ribersborg.jpg"),
                new GymLocation("Bulltofta Utegym", 55.59798500362362, 13.065033766551943, "/images/Bulltofta.jpg"),
                new GymLocation("Pildammsparken Utegym",  55.59038983277246,12.996603428453424, "/images/Pildammsparken.jpg"),
                new GymLocation("Hyllie Utegym", 55.5609909735987, 12.977343859646124, "/images/Hyllie.jpg"),
                new GymLocation("Rörsjöparken Utegym", 55.605748275337206, 13.017682664421155, "/images/Rörsjöparken.jpg")
        );
        GoogleMapsGymCardView view = new GoogleMapsGymCardView(gyms);
        Scene scene = new Scene(view, 800, 500);

        stage.setScene(scene);
        stage.show();
    }
}

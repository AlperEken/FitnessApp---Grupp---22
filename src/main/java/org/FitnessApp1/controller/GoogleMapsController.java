package org.FitnessApp1.controller;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.FitnessApp1.model.GymLocation;
import org.FitnessApp1.model.SessionManager;
import org.FitnessApp1.view.GoogleMapsGymCardView;
import org.FitnessApp1.view.MainMenuScreen;

import java.util.List;

public class GoogleMapsController {

    public void showGym( Stage primaryStage) {
        Stage stage = new Stage();
        stage.setTitle("Utegym i Malmö");

        List<GymLocation> gyms = List.of(
                new GymLocation("Ribersborg Utegym", 55.60032686885852, 12.9590796069194, "/images/Ribersborg.jpg"),
                new GymLocation("Bulltofta Utegym", 55.59798500362362, 13.065033766551943, "/images/Bulltofta.jpg"),
                new GymLocation("Pildammsparken Utegym",  55.59038983277246,12.996603428453424, "/images/Pildammsparken.jpg"),
                new GymLocation("Hyllie Utegym", 55.5609909735987, 12.977343859646124, "/images/Hyllie.jpg"),
                new GymLocation("Rörsjöparken Utegym", 55.605748275337206, 13.017682664421155, "/images/Rörsjöparken.jpg"),
                new GymLocation("Sibbarp Utegym", 55.575012916302, 12.910007058978954, "/images/Sibbarp.png"),
                new GymLocation("Bellevue Utegym", 55.584392511567145, 12.970413803044629, "/images/Bellevueparken.png"),
                new GymLocation("Lindängens Utegym", 55.56149578873109, 13.014408203301276, "/images/Lindängsparken.png"),
                new GymLocation("Klagshamns Utegym", 55.52426537206233, 12.90199449449149, "/images/Klagshamn.png"),
                new GymLocation("Nydala Utegym", 55.572282959713085, 13.018263558377786, "/images/Nydalaparken.png")
        );
        GoogleMapsGymCardView view = new GoogleMapsGymCardView(gyms, () -> {
            MainMenuScreen mainMenuScreen = new MainMenuScreen(SessionManager.getUsername());
            MainMenuController mainMenuController = new MainMenuController(mainMenuScreen, primaryStage);
            Scene scene = new Scene(mainMenuScreen.getRoot(), 800, 600);
            primaryStage.setScene(scene);
        });


        BorderPane root = new BorderPane();
        root.setCenter(view.getRoot()); //ScrollPane från vyklassen

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Utegym i Malmö");
        primaryStage.show();
    }
}

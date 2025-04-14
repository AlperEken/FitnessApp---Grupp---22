package org.FitnessApp1.model;


public class GymLocation {
    private final String name;
    private final double lat;
    private final double lon;
    private final String imagePath;

    public GymLocation(String name, double lat, double lon, String imagePath) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getImagePath() {
        return imagePath;
    }
}

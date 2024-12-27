package ipleiria.eec.pdm.pdm_app.manager;

import com.google.android.gms.maps.model.LatLng;

public class Trip {
    private String destination;
    private String distance;
    private String fuelCost;
    private LatLng startLocation;
    private LatLng endLocation;

    public Trip(String destination, String distance, String fuelCost, LatLng startLocation, LatLng endLocation) {
        this.destination = destination;
        this.distance = distance;
        this.fuelCost = fuelCost;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    // Getters and setters
    public String getDestination() {
        return destination;
    }

    public String getDistance() {
        return distance;
    }

    public String getFuelCost() {
        return fuelCost;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public double getStartLatitude() {
        return startLocation.latitude;
    }

    public double getStartLongitude() {
        return startLocation.longitude;
    }

    public double getEndLatitude() {
        return endLocation.latitude;
    }

    public double getEndLongitude() {
        return endLocation.longitude;
    }
}

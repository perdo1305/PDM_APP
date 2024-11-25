package ipleiria.eec.pdm.pdm_app.manager;

public class Trip {
    private String destination;
    private String distance;
    private String fuelCost;

    public Trip(String destination, String distance, String fuelCost) {
        this.destination = destination;
        this.distance = distance;
        this.fuelCost = fuelCost;
    }

    public String getDestination() {
        return destination;
    }

    public String getDistance() {
        return distance;
    }

    public String getFuelCost() {
        return fuelCost;
    }
}

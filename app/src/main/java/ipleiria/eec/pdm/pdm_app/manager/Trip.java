package ipleiria.eec.pdm.pdm_app.manager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a trip with destination, distance, fuel cost, and start and end locations.
 */
public class Trip {
    private String destination;
    private String distance;
    private String fuelCost;
    private LatLng startLocation;
    private LatLng endLocation;

    /**
     * Constructs a new Trip with the specified details.
     *
     * @param destination the destination of the trip
     * @param distance the distance of the trip
     * @param fuelCost the fuel cost of the trip
     * @param startLocation the start location of the trip
     * @param endLocation the end location of the trip
     */
    public Trip(String destination, String distance, String fuelCost, LatLng startLocation, LatLng endLocation) {
        this.destination = destination;
        this.distance = distance;
        this.fuelCost = fuelCost;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    /**
     * Returns the destination of the trip.
     *
     * @return the destination of the trip
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the distance of the trip.
     *
     * @return the distance of the trip
     */
    public String getDistance() {
        return distance;
    }

    /**
     * Returns the fuel cost of the trip.
     *
     * @return the fuel cost of the trip
     */
    public String getFuelCost() {
        return fuelCost;
    }

    /**
     * Returns the start location of the trip.
     *
     * @return the start location of the trip
     */
    public LatLng getStartLocation() {
        return startLocation;
    }

    /**
     * Returns the end location of the trip.
     *
     * @return the end location of the trip
     */
    public LatLng getEndLocation() {
        return endLocation;
    }

    /**
     * Returns the latitude of the start location.
     *
     * @return the latitude of the start location
     */
    public double getStartLatitude() {
        return startLocation.latitude;
    }

    /**
     * Returns the longitude of the start location.
     *
     * @return the longitude of the start location
     */
    public double getStartLongitude() {
        return startLocation.longitude;
    }

    /**
     * Returns the latitude of the end location.
     *
     * @return the latitude of the end location
     */
    public double getEndLatitude() {
        return endLocation.latitude;
    }

    /**
     * Returns the longitude of the end location.
     *
     * @return the longitude of the end location
     */
    public double getEndLongitude() {
        return endLocation.longitude;
    }
}
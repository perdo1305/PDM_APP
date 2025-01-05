package ipleiria.eec.pdm.pdm_app.manager;

/**
 * Represents a vehicle with its details.
 */
public class Vehicle {
    private String name;
    private String details;
    private String photoUri;
    private String licensePlate;

    /**
     * Constructs a new Vehicle with the specified details.
     *
     * @param name the name of the vehicle
     * @param details the details of the vehicle
     * @param photoUri the URI of the vehicle's photo
     * @param licensePlate the license plate of the vehicle
     */
    public Vehicle(String name, String details, String photoUri, String licensePlate) {
        this.name = name;
        this.details = details;
        this.photoUri = photoUri;
        this.licensePlate = licensePlate;
    }

    /**
     * Returns the name of the vehicle.
     *
     * @return the name of the vehicle
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the vehicle.
     *
     * @param name the new name of the vehicle
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the details of the vehicle.
     *
     * @return the details of the vehicle
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets the details of the vehicle.
     *
     * @param details the new details of the vehicle
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Returns the URI of the vehicle's photo.
     *
     * @return the URI of the vehicle's photo
     */
    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Sets the URI of the vehicle's photo.
     *
     * @param photoUri the new URI of the vehicle's photo
     */
    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    /**
     * Returns the license plate of the vehicle.
     *
     * @return the license plate of the vehicle
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the license plate of the vehicle.
     *
     * @param licensePlate the new license plate of the vehicle
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
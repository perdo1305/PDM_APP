package ipleiria.eec.pdm.pdm_app.manager;

public class Vehicle {
    private String name;
    private String details;
    private String photoUri;
    private String licensePlate;

    public Vehicle(String name, String details, String photoUri, String licensePlate) {
        this.name = name;
        this.details = details;
        this.photoUri = photoUri;
        this.licensePlate = licensePlate;
    }

    // Getters and setters for all fields
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
}
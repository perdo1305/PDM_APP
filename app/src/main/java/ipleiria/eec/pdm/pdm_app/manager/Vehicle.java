package ipleiria.eec.pdm.pdm_app.manager;

public class Vehicle {
    private String name;
    private String details;
    private String photoUri;
    private String licensePlate;
    private int vehicleId; // Optional: Use for database primary key


    public Vehicle(int vehicleId, String name, String details, String photoUri, String licensePlate) {
        this.name = name;
        this.details = details;
        this.photoUri = photoUri;
        this.licensePlate = licensePlate;
        this.vehicleId  = vehicleId;
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

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

}
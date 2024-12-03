package ipleiria.eec.pdm.pdm_app.manager;

public class Vehicle {
    private String name;
    private String details;
    private String photoUri; // New field for the photo

    public Vehicle(String name, String details, String photoUri) {
        this.name = name;
        this.details = details;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }


}

package ipleiria.eec.pdm.pdm_app.manager;

public class Maintenance {
    private int maintenanceId; // Primary key for database
    private int vehicleId;     // Foreign key linking to Vehicle
    private String serviceType;
    private String serviceDate;
    private String serviceCost;

    public Maintenance(int vehicleId, String serviceType, String serviceDate, String serviceCost) {
        this.vehicleId = vehicleId;
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.serviceCost = serviceCost;
    }

    // Getters and Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public String getServiceDate() { return serviceDate; }
    public void setServiceDate(String serviceDate) { this.serviceDate = serviceDate; }
    public String getServiceCost() { return serviceCost; }
    public void setServiceCost(String serviceCost) { this.serviceCost = serviceCost; }
    public int getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(int maintenanceId) { this.maintenanceId = maintenanceId; }
}
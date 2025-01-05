package ipleiria.eec.pdm.pdm_app.manager;

/**
 * Represents a maintenance record for a vehicle.
 */
public class Maintenance {
    private String serviceType;
    private String serviceDate;
    private String serviceCost;

    /**
     * Constructs a new Maintenance record with the specified details.
     *
     * @param serviceType the type of the service
     * @param serviceDate the date of the service
     * @param serviceCost the cost of the service
     */
    public Maintenance(String serviceType, String serviceDate, String serviceCost) {
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.serviceCost = serviceCost;
    }

    /**
     * Returns the type of the service.
     *
     * @return the type of the service
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the type of the service.
     *
     * @param serviceType the new type of the service
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Returns the date of the service.
     *
     * @return the date of the service
     */
    public String getServiceDate() {
        return serviceDate;
    }

    /**
     * Sets the date of the service.
     *
     * @param serviceDate the new date of the service
     */
    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * Returns the cost of the service.
     *
     * @return the cost of the service
     */
    public String getServiceCost() {
        return serviceCost;
    }

    /**
     * Sets the cost of the service.
     *
     * @param serviceCost the new cost of the service
     */
    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }
}
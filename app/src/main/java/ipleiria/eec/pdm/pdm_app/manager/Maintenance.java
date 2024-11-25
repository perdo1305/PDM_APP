package ipleiria.eec.pdm.pdm_app.manager;

public class Maintenance {
    private String serviceType;
    private String serviceDate;
    private String serviceCost;

    public Maintenance(String serviceType, String serviceDate, String serviceCost) {
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.serviceCost = serviceCost;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }
}

package ipleiria.eec.pdm.pdm_app.manager;

/**
 * Mostra os registos de manutenção de um veículo.
 */
public class Maintenance {
    private int maintenanceId; // Primary key for database
    private int vehicleId;     // Foreign key linking to Vehicle
    private String serviceType;
    private String serviceDate;
    private String serviceCost;

    /**
     * Cria uma nova manutenção com os detalhes especificados.
     *
     * @param serviceType tipo de serviço
     * @param serviceDate data do serviço
     * @param serviceCost custo do serviço
     */
    public Maintenance(int vehicleId, String serviceType, String serviceDate, String serviceCost) {
        this.vehicleId = vehicleId;
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.serviceCost = serviceCost;
    }

    /**
     * Devolve o tipo de serviço.
     *
     * @return o tipo de serviço
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Define o tipo de serviço.
     *
     * @param serviceType o novo tipo de serviço
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Devolve a data do serviço.
     *
     * @return a data do serviço
     */
    public String getServiceDate() {
        return serviceDate;
    }

    /**
     * Define a data do serviço.
     *
     * @param serviceDate a nova data do serviço
     */
    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * Devolve o custo do serviço.
     *
     * @return o custo do serviço
     */
    public String getServiceCost() {
        return serviceCost;
    }

    /**
     * Define o custo do serviço.
     *
     * @param serviceCost o novo custo do serviço
     */
    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }

    /**
     * Devolve o ID da manutenção.
     * @return o ID da manutenção
     */
    public int getMaintenanceId() { return maintenanceId; }

    /**
     * Define o ID da manutenção.
     * @param maintenanceId o novo ID da manutenção
     */
    public void setMaintenanceId(int maintenanceId) { this.maintenanceId = maintenanceId; }

    /**
     * Devolve o ID do veículo.
     * @return o ID do veículo
     */
    public int getVehicleId() { return vehicleId; }

    /**
     * Define o ID do veículo.
     * @param vehicleId o novo ID do veículo
     */
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
}

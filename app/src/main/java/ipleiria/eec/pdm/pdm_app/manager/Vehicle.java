package ipleiria.eec.pdm.pdm_app.manager;

/**
 * Representa um veículo com seus detalhes.
 */
public class Vehicle {
    private String name;
    private String details;
    private String photoUri;
    private String licensePlate;
    private int vehicleId; // Optional: Use for database primary key

    /**
     * Constrói um novo Vehicle com os detalhes especificados.
     *
     * @param name o nome do veículo
     * @param details os detalhes do veículo
     * @param photoUri o URI da foto do veículo
     * @param licensePlate a matrícula do veículo
     */

    public Vehicle(int vehicleId, String name, String details, String photoUri, String licensePlate) {
        this.name = name;
        this.details = details;
        this.photoUri = photoUri;
        this.licensePlate = licensePlate;
        this.vehicleId  = vehicleId;
    }

    /**
     * Retorna o nome do veículo.
     *
     * @return o nome do veículo
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do veículo.
     *
     * @param name o novo nome do veículo
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna os detalhes do veículo.
     *
     * @return os detalhes do veículo
     */
    public String getDetails() {
        return details;
    }

    /**
     * Define os detalhes do veículo.
     *
     * @param details os novos detalhes do veículo
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Retorna o URI da foto do veículo.
     *
     * @return o URI da foto do veículo
     */
    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Define o URI da foto do veículo.
     *
     * @param photoUri o novo URI da foto do veículo
     */
    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    /**
     * Retorna a matrícula do veículo.
     *
     * @return a matrícula do veículo
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Define a matrícula do veículo.
     *
     * @param licensePlate a nova matrícula do veículo
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
}

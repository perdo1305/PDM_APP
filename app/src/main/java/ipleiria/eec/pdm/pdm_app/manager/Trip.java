package ipleiria.eec.pdm.pdm_app.manager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Representa uma viagem com destino, distância, custo de combustível e localizações de início e fim.
 */
public class Trip {
    private String destination;
    private String distance;
    private String fuelCost;
    private LatLng startLocation;
    private LatLng endLocation;

    /**
     * Constrói uma nova viagem com os detalhes especificados.
     *
     * @param destination o destino da viagem
     * @param distance a distância da viagem
     * @param fuelCost o custo de combustível da viagem
     * @param startLocation a localização de início da viagem
     * @param endLocation a localização de fim da viagem
     */
    public Trip(String destination, String distance, String fuelCost, LatLng startLocation, LatLng endLocation) {
        this.destination = destination;
        this.distance = distance;
        this.fuelCost = fuelCost;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    /**
     * Devolve o destino da viagem.
     *
     * @return o destino da viagem
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Devolve a distância da viagem.
     *
     * @return a distância da viagem
     */
    public String getDistance() {
        return distance;
    }

    /**
     * Devolve o custo de combustível da viagem.
     *
     * @return o custo de combustível da viagem
     */
    public String getFuelCost() {
        return fuelCost;
    }

    /**
     * Devolve a localização de início da viagem.
     *
     * @return a localização de início da viagem
     */
    public LatLng getStartLocation() {
        return startLocation;
    }

    /**
     * Devolve a localização de fim da viagem.
     *
     * @return a localização de fim da viagem
     */
    public LatLng getEndLocation() {
        return endLocation;
    }

    /**
     * Devolve a latitude da localização de início.
     *
     * @return a latitude da localização de início
     */
    public double getStartLatitude() {
        return startLocation.latitude;
    }

    /**
     * Devolve a longitude da localização de início.
     *
     * @return a longitude da localização de início
     */
    public double getStartLongitude() {
        return startLocation.longitude;
    }

    /**
     * Devolve a latitude da localização de fim.
     *
     * @return a latitude da localização de fim
     */
    public double getEndLatitude() {
        return endLocation.latitude;
    }

    /**
     * Devolve a longitude da localização de fim.
     *
     * @return a longitude da localização de fim
     */
    public double getEndLongitude() {
        return endLocation.longitude;
    }
}

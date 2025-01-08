package ipleiria.eec.pdm.pdm_app.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;

/**
 * Classe Adapter para exibir uma lista de veículos em um RecyclerView.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<Vehicle> vehicleList;
    private OnVehicleClickListener onVehicleClickListener;

    /**
     * Constrói um novo VehicleAdapter com a lista especificada de veículos.
     *
     * @param vehicleList a lista de veículos a ser exibida
     */
    public VehicleAdapter(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    /**
     * Define o listener para eventos de clique nos veículos.
     *
     * @param listener o listener a ser definido
     */
    public void setOnVehicleClickListener(OnVehicleClickListener listener) {
        this.onVehicleClickListener = listener;
    }

    /**
     * Interface para manipular eventos de clique nos veículos.
     */
    public interface OnVehicleClickListener {
        /**
         * Chamado quando um veículo é clicado para edição.
         *
         * @param position a posição do veículo clicado
         * @param vehicle o veículo clicado
         */
        void onEditVehicle(int position, Vehicle vehicle);

        /**
         * Chamado quando um veículo é pressionado por longo tempo para exclusão.
         *
         * @param position a posição do veículo pressionado
         */
        void onDeleteVehicle(int position);
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_list_item, parent, false);
        return new VehicleViewHolder(view);
    }

    /**
     * Vincula os dados do veículo ao ViewHolder.
     *
     * @param holder o ViewHolder do veículo
     * @param position a posição do veículo na lista
     */
    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.vehicleName.setText(vehicle.getName());
        holder.vehicleDetails.setText(vehicle.getDetails());
        holder.vehicleLicensePlate.setText(vehicle.getLicensePlate());
        Glide.with(holder.itemView.getContext()).load(vehicle.getPhotoUri()).into(holder.vehiclePhoto);

        holder.itemView.setOnClickListener(v -> {
            if (onVehicleClickListener != null) {
                onVehicleClickListener.onEditVehicle(position, vehicle);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onVehicleClickListener != null) {
                onVehicleClickListener.onDeleteVehicle(position);
            }
            return true;
        });
    }

    /**
 * Retorna o número total de veículos na lista.
 *
 * @return o número total de veículos
 */
    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    /**
     * Classe ViewHolder para itens de veículo.
     */
    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleName, vehicleDetails, vehicleLicensePlate;
        ImageView vehiclePhoto;

        /**
         * Constrói um novo VehicleViewHolder.
         *
         * @param itemView a view do item do veículo
         */
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.vehicle_name);
            vehicleDetails = itemView.findViewById(R.id.vehicle_details);
            vehicleLicensePlate = itemView.findViewById(R.id.vehicle_license_plate);
            vehiclePhoto = itemView.findViewById(R.id.vehicle_photo);
        }
    }
}

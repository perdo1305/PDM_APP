/**
 * Esta classe representa um adaptador para uma lista de veículos.
 * Fornece funcionalidade para exibir, editar e remover veículos da lista.
 * <p>
 * O adaptador usa um RecyclerView para exibir a lista de veículos.
 * Cada veículo é representado por um ViewHolder que contém o nome e os detalhes do veículo.
 * <p>
 * O adaptador também fornece uma interface para tratar eventos de clique nos veículos.
 * Isso inclui a edição de um veículo quando ele é tocado e a exclusão de um veículo quando ele é pressionado por muito tempo.
 *
 * @see Vehicle
 */


package ipleiria.eec.pdm.pdm_app.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<Vehicle> vehicleList;
    private OnVehicleClickListener onVehicleClickListener;

    /**
     * ViewHolder para veículos.
     * Permite a edição e remoção de veículos.
     * @see Vehicle
     */
    public VehicleAdapter(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    /**
     * ViewHolder para veículos.
     * Permite a edição e remoção de veículos.
     * @see Vehicle
     */
    public void setOnVehicleClickListener(OnVehicleClickListener listener) {
        this.onVehicleClickListener = listener;
    }

    /**
     * Interface para lidar com eventos de clique em veículos.
     */
    public interface OnVehicleClickListener {
        void onEditVehicle(int position, Vehicle vehicle);
        void onDeleteVehicle(int position);
    }

    /**
     * Cria um novo ViewHolder para veículos.
     * @param parent ViewGroup pai
     * @param viewType tipo de view
     * @return novo ViewHolder
     */
    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_list_item, parent, false);
        return new VehicleViewHolder(view);
    }


    /**
     * Atualiza o conteúdo de um item na lista de veículos.
     * @param holder ViewHolder do veículo
     * @param position posição do veículo na lista
     */
    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.vehicleName.setText(vehicle.getName());
        holder.vehicleDetails.setText(vehicle.getDetails());

        // Tap to edit vehicle
        holder.itemView.setOnClickListener(v -> {
            if (onVehicleClickListener != null) {
                onVehicleClickListener.onEditVehicle(position, vehicle);
            }
        });

        // Long press to delete vehicle
        holder.itemView.setOnLongClickListener(v -> {
            if (onVehicleClickListener != null) {
                onVehicleClickListener.onDeleteVehicle(position);
            }
            return true;
        });
    }


    /**
     * Obtem o número de veículos na lista.
     * @return número de veículos
     */
    @Override
    public int getItemCount() {
        return vehicleList.size();
    }


    /**
     * ViewHolder para veículos.
     * Permite a edição e remoção de veículos.
     * @see Vehicle
     */
    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleName, vehicleDetails;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.vehicle_name);
            vehicleDetails = itemView.findViewById(R.id.vehicle_details);
        }
    }
}

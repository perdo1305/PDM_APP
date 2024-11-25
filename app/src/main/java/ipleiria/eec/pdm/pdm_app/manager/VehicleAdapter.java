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

    public VehicleAdapter(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    private OnVehicleClickListener onVehicleClickListener;

    public void setOnVehicleClickListener(OnVehicleClickListener listener) {
        this.onVehicleClickListener = listener;
    }

    public interface OnVehicleClickListener {
        void onEditVehicle(int position, Vehicle vehicle);
        void onDeleteVehicle(int position);
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_list_item, parent, false);
        return new VehicleViewHolder(view);
    }

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



    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleName, vehicleDetails;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.vehicle_name);
            vehicleDetails = itemView.findViewById(R.id.vehicle_details);
        }
    }
}

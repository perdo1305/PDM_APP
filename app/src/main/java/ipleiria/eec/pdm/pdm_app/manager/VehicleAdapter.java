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
 * Adapter class for displaying a list of vehicles in a RecyclerView.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<Vehicle> vehicleList;
    private OnVehicleClickListener onVehicleClickListener;

    /**
     * Constructs a new VehicleAdapter with the specified list of vehicles.
     *
     * @param vehicleList the list of vehicles to display
     */
    public VehicleAdapter(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    /**
     * Sets the listener for vehicle click events.
     *
     * @param listener the listener to set
     */
    public void setOnVehicleClickListener(OnVehicleClickListener listener) {
        this.onVehicleClickListener = listener;
    }

    /**
     * Interface for handling vehicle click events.
     */
    public interface OnVehicleClickListener {
        /**
         * Called when a vehicle is clicked for editing.
         *
         * @param position the position of the clicked vehicle
         * @param vehicle the clicked vehicle
         */
        void onEditVehicle(int position, Vehicle vehicle);

        /**
         * Called when a vehicle is long-clicked for deletion.
         *
         * @param position the position of the long-clicked vehicle
         */
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

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    /**
     * ViewHolder class for vehicle items.
     */
    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleName, vehicleDetails, vehicleLicensePlate;
        ImageView vehiclePhoto;

        /**
         * Constructs a new VehicleViewHolder.
         *
         * @param itemView the view of the vehicle item
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
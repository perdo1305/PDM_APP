package ipleiria.eec.pdm.pdm_app.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;

/**
 * Adapter class for displaying a list of maintenance records in a RecyclerView.
 */
public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MaintenanceViewHolder> {
    private List<Maintenance> maintenanceList;
    private OnMaintenanceClickListener onMaintenanceClickListener;

    /**
     * Constructs a new MaintenanceAdapter with the specified list of maintenance records.
     *
     * @param maintenanceList the list of maintenance records to display
     */
    public MaintenanceAdapter(List<Maintenance> maintenanceList) {
        this.maintenanceList = maintenanceList;
    }

    @NonNull
    @Override
    public MaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintenance_list_item, parent, false);
        return new MaintenanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceViewHolder holder, int position) {
        Maintenance maintenance = maintenanceList.get(position);
        holder.serviceType.setText(maintenance.getServiceType());
        holder.serviceDate.setText("Date: " + maintenance.getServiceDate());
        holder.serviceCost.setText("Cost: " + maintenance.getServiceCost());

        holder.itemView.setOnClickListener(v -> {
            if (onMaintenanceClickListener != null) {
                onMaintenanceClickListener.onEditMaintenance(position, maintenance);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onMaintenanceClickListener != null) {
                onMaintenanceClickListener.onDeleteMaintenance(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return maintenanceList.size();
    }

    /**
     * Sets the listener for maintenance click events.
     *
     * @param listener the listener to set
     */
    public void setOnMaintenanceClickListener(OnMaintenanceClickListener listener) {
        this.onMaintenanceClickListener = listener;
    }

    /**
     * Interface for handling maintenance click events.
     */
    public interface OnMaintenanceClickListener {
        /**
         * Called when a maintenance record is clicked for editing.
         *
         * @param position the position of the clicked maintenance record
         * @param maintenance the clicked maintenance record
         */
        void onEditMaintenance(int position, Maintenance maintenance);

        /**
         * Called when a maintenance record is long-clicked for deletion.
         *
         * @param position the position of the long-clicked maintenance record
         */
        void onDeleteMaintenance(int position);
    }

    /**
     * ViewHolder class for maintenance items.
     */
    static class MaintenanceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceType, serviceDate, serviceCost;

        /**
         * Constructs a new MaintenanceViewHolder.
         *
         * @param itemView the view of the maintenance item
         */
        public MaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceType = itemView.findViewById(R.id.service_type);
            serviceDate = itemView.findViewById(R.id.service_date);
            serviceCost = itemView.findViewById(R.id.service_cost);
        }
    }
}
package ipleiria.eec.pdm.pdm_app.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MaintenanceViewHolder> {
    private List<Maintenance> maintenanceList;

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

        // Tap to edit maintenance record
        holder.itemView.setOnClickListener(v -> {
            if (onMaintenanceClickListener != null) {
                onMaintenanceClickListener.onEditMaintenance(position, maintenance);
            }
        });

        // Long press to delete maintenance record
        holder.itemView.setOnLongClickListener(v -> {
            if (onMaintenanceClickListener != null) {
                onMaintenanceClickListener.onDeleteMaintenance(position);
            }
            return true;
        });
    }

    private OnMaintenanceClickListener onMaintenanceClickListener;

    public void setOnMaintenanceClickListener(OnMaintenanceClickListener listener) {
        this.onMaintenanceClickListener = listener;
    }

    public interface OnMaintenanceClickListener {
        void onEditMaintenance(int position, Maintenance maintenance);
        void onDeleteMaintenance(int position);
    }


    @Override
    public int getItemCount() {
        return maintenanceList.size();
    }

    static class MaintenanceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceType, serviceDate, serviceCost;

        public MaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceType = itemView.findViewById(R.id.service_type);
            serviceDate = itemView.findViewById(R.id.service_date);
            serviceCost = itemView.findViewById(R.id.service_cost);
        }
    }
}

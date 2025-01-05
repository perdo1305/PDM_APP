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
 * Classe Adapter para exibir uma lista de registos de manutenção em um RecyclerView.
 */
public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MaintenanceViewHolder> {
    private List<Maintenance> maintenanceList;
    private OnMaintenanceClickListener onMaintenanceClickListener;

    /**
     * Constrói um novo MaintenanceAdapter com a lista especificada de registos de manutenção.
     *
     * @param maintenanceList a lista de registos de manutenção a ser exibida
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
        holder.serviceDate.setText("Data: " + maintenance.getServiceDate());
        holder.serviceCost.setText("Custo: " + maintenance.getServiceCost());

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
     * Define o listener para eventos de clique nos registos de manutenção.
     *
     * @param listener o listener a ser definido
     */
    public void setOnMaintenanceClickListener(OnMaintenanceClickListener listener) {
        this.onMaintenanceClickListener = listener;
    }

    /**
     * Interface para manipular eventos de clique nos registos de manutenção.
     */
    public interface OnMaintenanceClickListener {
        /**
         * Chamado quando um registo de manutenção é clicado para edição.
         *
         * @param position a posição do registo de manutenção clicado
         * @param maintenance o registo de manutenção clicado
         */
        void onEditMaintenance(int position, Maintenance maintenance);

        /**
         * Chamado quando um registo de manutenção é clicado longamente para exclusão.
         *
         * @param position a posição do registo de manutenção clicado longamente
         */
        void onDeleteMaintenance(int position);
    }

    /**
     * Classe ViewHolder para itens de manutenção.
     */
    static class MaintenanceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceType, serviceDate, serviceCost;

        /**
         * Constrói um novo MaintenanceViewHolder.
         *
         * @param itemView a view do item de manutenção
         */
        public MaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceType = itemView.findViewById(R.id.service_type);
            serviceDate = itemView.findViewById(R.id.service_date);
            serviceCost = itemView.findViewById(R.id.service_cost);
        }
    }
}

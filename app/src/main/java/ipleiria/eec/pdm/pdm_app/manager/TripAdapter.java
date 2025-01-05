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
 * Classe Adapter para exibir uma lista de viagens em um RecyclerView.
 */
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> tripList;
    private OnItemClickListener listener;

    /**
     * Constrói um novo TripAdapter com a lista especificada de viagens.
     *
     * @param tripList a lista de viagens a ser exibida
     */
    public TripAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);

        // Preenche o ViewHolder com os dados da viagem
        holder.destinationTextView.setText(trip.getDestination());
        holder.distanceTextView.setText(trip.getDistance());
        holder.fuelCostTextView.setText(trip.getFuelCost());

        // Define o listener de clique
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    /**
     * Classe ViewHolder para itens de viagem.
     */
    static class TripViewHolder extends RecyclerView.ViewHolder {
        public TextView fuelCostTextView;
        public TextView distanceTextView;
        public TextView destinationTextView;

        /**
         * Constrói um novo TripViewHolder.
         *
         * @param itemView a view do item de viagem
         */
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.trip_destination);
            distanceTextView = itemView.findViewById(R.id.trip_distance);
            fuelCostTextView = itemView.findViewById(R.id.trip_fuel_cost);
        }
    }

    /**
     * Interface para manipular eventos de clique nos itens de viagem.
     */
    public interface OnItemClickListener {
        /**
         * Chamado quando um item de viagem é clicado.
         *
         * @param trip a viagem clicada
         */
        void onItemClick(Trip trip);
    }

    /**
     * Define o listener para eventos de clique nos itens de viagem.
     *
     * @param listener o listener a ser definido
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Atualiza a lista de viagens e notifica o adapter.
     *
     * @param newList a nova lista de viagens
     */
    public void updateList(List<Trip> newList) {
        this.tripList = newList; // Atualiza a referência da lista
        notifyDataSetChanged();  // Notifica o RecyclerView sobre as mudanças
    }
}

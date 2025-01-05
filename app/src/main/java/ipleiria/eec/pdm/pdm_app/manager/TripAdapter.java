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
 * Adapter class for displaying a list of trips in a RecyclerView.
 */
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> tripList;
    private OnItemClickListener listener;

    /**
     * Constructs a new TripAdapter with the specified list of trips.
     *
     * @param tripList the list of trips to display
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

        // Populate the ViewHolder with trip data
        holder.destinationTextView.setText(trip.getDestination());
        holder.distanceTextView.setText(trip.getDistance());
        holder.fuelCostTextView.setText(trip.getFuelCost());

        // Set click listener
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
     * ViewHolder class for trip items.
     */
    static class TripViewHolder extends RecyclerView.ViewHolder {
        public TextView fuelCostTextView;
        public TextView distanceTextView;
        public TextView destinationTextView;

        /**
         * Constructs a new TripViewHolder.
         *
         * @param itemView the view of the trip item
         */
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.trip_destination);
            distanceTextView = itemView.findViewById(R.id.trip_distance);
            fuelCostTextView = itemView.findViewById(R.id.trip_fuel_cost);
        }
    }

    /**
     * Interface for handling trip item click events.
     */
    public interface OnItemClickListener {
        /**
         * Called when a trip item is clicked.
         *
         * @param trip the clicked trip
         */
        void onItemClick(Trip trip);
    }

    /**
     * Sets the listener for trip item click events.
     *
     * @param listener the listener to set
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the list of trips and notifies the adapter.
     *
     * @param newList the new list of trips
     */
    public void updateList(List<Trip> newList) {
        this.tripList = newList; // Update the list reference
        notifyDataSetChanged();  // Notify RecyclerView of changes
    }
}
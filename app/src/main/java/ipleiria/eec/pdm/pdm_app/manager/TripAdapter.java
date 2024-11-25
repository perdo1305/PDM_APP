package ipleiria.eec.pdm.pdm_app.manager;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> tripList;

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
        holder.tripDestination.setText("Destination: " + trip.getDestination());
        holder.tripDistance.setText("Distance: " + trip.getDistance());
        holder.tripFuelCost.setText("Fuel Cost: " + trip.getFuelCost());

        // Long press to delete
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Trip")
                    .setMessage("Are you sure you want to delete this trip?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        tripList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, tripList.size());
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tripDestination, tripDistance, tripFuelCost;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripDestination = itemView.findViewById(R.id.trip_destination);
            tripDistance = itemView.findViewById(R.id.trip_distance);
            tripFuelCost = itemView.findViewById(R.id.trip_fuel_cost);
        }
    }
}

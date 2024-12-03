package ipleiria.eec.pdm.pdm_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;
import ipleiria.eec.pdm.pdm_app.manager.Trip;
import ipleiria.eec.pdm.pdm_app.manager.TripAdapter;

import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TripMenuFragment extends Fragment {
    private RecyclerView tripRecyclerView;
    private TripAdapter tripAdapter;
    private List<Trip> tripList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_menu, container, false);


        // Initialize RecyclerView
        tripRecyclerView = view.findViewById(R.id.trip_recycler_view);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Populate sample data
        tripList = new ArrayList<>();
        tripList.add(new Trip("Torres Novas to Leira", "58 km", "5.75"));
        tripList.add(new Trip("Leira to Facho", "40 km", "7.50"));

        // Set adapter
        tripAdapter = new TripAdapter(tripList);
        tripRecyclerView.setAdapter(tripAdapter);

        FloatingActionButton fabAddTrip = view.findViewById(R.id.fab_add_trip);
        fabAddTrip.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add New Trip");

            // Input fields
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            EditText inputDestination = new EditText(getContext());
            inputDestination.setHint("Destination");
            layout.addView(inputDestination);

            EditText inputDistance = new EditText(getContext());
            inputDistance.setHint("Distance");
            inputDistance.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(inputDistance);

            EditText inputFuelCost = new EditText(getContext());
            inputFuelCost.setHint("Fuel Cost");
            inputFuelCost.setInputType(InputType.TYPE_CLASS_TEXT);
            layout.addView(inputFuelCost);

            builder.setView(layout);

            // Add buttons
            builder.setPositiveButton("Add", (dialog, which) -> {
                String destination = inputDestination.getText().toString();
                String distance = inputDistance.getText().toString();
                String fuelCost = inputFuelCost.getText().toString();

                if (!destination.isEmpty() && !distance.isEmpty() && !fuelCost.isEmpty()) {
                    tripList.add(new Trip(destination, distance, fuelCost));
                    tripAdapter.notifyItemInserted(tripList.size() - 1);
                } else {
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        // SearchView setup
        SearchView searchView = view.findViewById(R.id.search_view_trip);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No need to handle submit
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTrips(newText);
                return true;
            }
        });

        return view;
    }

    /**
     * filtra as viagens com base na pesquisa
     * @param query texto de pesquisa
     */
    private void filterTrips(String query) {
        List<Trip> filteredList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (trip.getDestination().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(trip);
            }
        }
        tripAdapter = new TripAdapter(filteredList);
        tripRecyclerView.setAdapter(tripAdapter);
    }
}


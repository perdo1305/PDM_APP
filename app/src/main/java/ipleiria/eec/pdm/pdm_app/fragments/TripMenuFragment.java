package ipleiria.eec.pdm.pdm_app.fragments;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TripMenuFragment extends Fragment implements OnMapReadyCallback {
    private RecyclerView tripRecyclerView;
    private TripAdapter tripAdapter;
    private List<Trip> tripList;

    private GoogleMap mMap;
    private Marker startMarker, endMarker;
    private Polyline routePolyline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_menu, container, false);

        // Initialize RecyclerView
        tripRecyclerView = view.findViewById(R.id.trip_recycler_view);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Populate sample data
        tripList = new ArrayList<>();
        tripList.add(new Trip("Torres Novas to Leiria", "58 km", "5.75"));
        tripList.add(new Trip("Leiria to Facho", "40 km", "7.50"));

        // Set adapter
        tripAdapter = new TripAdapter(tripList);
        tripRecyclerView.setAdapter(tripAdapter);

        // Initialize FloatingActionButton
        FloatingActionButton fabAddTrip = view.findViewById(R.id.fab_add_trip);
        fabAddTrip.setOnClickListener(v -> showAddTripDialog());

        // Initialize SearchView
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

        // Initialize Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng startLocation = new LatLng(39.600672, -8.433824);
        LatLng endLocation = new LatLng(39.743623, -8.807050);

        startMarker = mMap.addMarker(new MarkerOptions().position(startLocation).title("Start: Torres Novas"));
        endMarker = mMap.addMarker(new MarkerOptions().position(endLocation).title("End: Leiria"));

        // Draw a line between start and end
        routePolyline = mMap.addPolyline(new PolylineOptions()
                .add(startLocation, endLocation)
                .width(5)
                .color(android.graphics.Color.BLUE));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10));
    }


    private void showAddTripDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.add_new_trip);

        // Input fields
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputDestination = new EditText(getContext());
        inputDestination.setHint(R.string.destination);
        layout.addView(inputDestination);

        EditText inputDistance = new EditText(getContext());
        inputDistance.setHint(R.string.distance);
        inputDistance.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputDistance);

        EditText inputFuelCost = new EditText(getContext());
        inputFuelCost.setHint(R.string.fuel_cost);
        inputFuelCost.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputFuelCost);

        builder.setView(layout);

        // Add buttons
        builder.setPositiveButton(R.string.add, (dialog, which) -> {
            String destination = inputDestination.getText().toString();
            String distance = inputDistance.getText().toString();
            String fuelCost = inputFuelCost.getText().toString();

            if (!destination.isEmpty() && !distance.isEmpty() && !fuelCost.isEmpty()) {
                tripList.add(new Trip(destination, distance, fuelCost));
                tripAdapter.notifyItemInserted(tripList.size() - 1);
            } else {
                Toast.makeText(getContext(), R.string.all_fields_are_required, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Filters trips based on search query.
     * @param query Search text.
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

    /**
     * Updates start and end markers dynamically.
     * @param newStart New start location.
     * @param newEnd New end location.
     */
    public void updateMarkers(LatLng newStart, LatLng newEnd) {
        if (startMarker != null) startMarker.setPosition(newStart);
        if (endMarker != null) endMarker.setPosition(newEnd);

        // Update route line
        if (routePolyline != null) {
            routePolyline.setPoints(List.of(newStart, newEnd));
        }

        // Adjust camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                new LatLngBounds.Builder().include(newStart).include(newEnd).build(), 100));
    }
}

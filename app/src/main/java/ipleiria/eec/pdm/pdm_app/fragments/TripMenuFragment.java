package ipleiria.eec.pdm.pdm_app.fragments;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
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
import ipleiria.eec.pdm.pdm_app.manager.MapSelectionActivity;
import ipleiria.eec.pdm.pdm_app.manager.Trip;
import ipleiria.eec.pdm.pdm_app.manager.TripAdapter;

import android.app.AlertDialog;
import android.text.InputType;
import android.widget.Button;
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

    private LatLng selectedStartLocation;
    private LatLng selectedEndLocation;
    private boolean isSelectingStart = true; // Tracks whether the user is selecting the start or end location

    private EditText inputStartLat, inputStartLng, inputEndLat, inputEndLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_menu, container, false);

        // Initialize RecyclerView
        tripRecyclerView = view.findViewById(R.id.trip_recycler_view);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tripList = new ArrayList<>();
        tripList.add(new Trip("Torres Novas to Leiria", "58 km", "5.75", new LatLng(39.600672, -8.433824), new LatLng(39.743623, -8.807050)));
        tripList.add(new Trip("Leiria to Facho", "40 km", "7.50", new LatLng(39.743623, -8.807050), new LatLng(39.792678, -9.083916)));

        tripAdapter = new TripAdapter(tripList);
        tripRecyclerView.setAdapter(tripAdapter);


        // Set click listener for trips
        tripAdapter.setOnItemClickListener(position -> {
            Trip selectedTrip = tripList.get(position);
            updateMarkers(selectedTrip.getStartLocation(), selectedTrip.getEndLocation());
        });

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

        // Enable map clicks for selecting locations
        mMap.setOnMapClickListener(latLng -> {
            if (isSelectingStart) {
                // Set the start location
                selectedStartLocation = latLng;
                if (startMarker != null) startMarker.remove(); // Remove existing marker
                startMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Start Location"));
                Toast.makeText(getContext(), "Start location selected.", Toast.LENGTH_SHORT).show();
            } else {
                // Set the end location
                selectedEndLocation = latLng;
                if (endMarker != null) endMarker.remove(); // Remove existing marker
                endMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("End Location"));
                Toast.makeText(getContext(), "End location selected.", Toast.LENGTH_SHORT).show();
            }

            // Adjust the camera to show the selected markers
            if (selectedStartLocation != null && selectedEndLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                        new LatLngBounds.Builder()
                                .include(selectedStartLocation)
                                .include(selectedEndLocation)
                                .build(), 100));
            }
        });
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

        // Coordinates fields (start and end locations)
        EditText inputStartLat = new EditText(getContext());
        inputStartLat.setHint("Start Latitude");
        inputStartLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputStartLat.setEnabled(false); // Make it non-editable
        layout.addView(inputStartLat);

        EditText inputStartLng = new EditText(getContext());
        inputStartLng.setHint("Start Longitude");
        inputStartLng.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputStartLng.setEnabled(false); // Make it non-editable
        layout.addView(inputStartLng);

        EditText inputEndLat = new EditText(getContext());
        inputEndLat.setHint("End Latitude");
        inputEndLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputEndLat.setEnabled(false); // Make it non-editable
        layout.addView(inputEndLat);

        EditText inputEndLng = new EditText(getContext());
        inputEndLng.setHint("End Longitude");
        inputEndLng.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputEndLng.setEnabled(false); // Make it non-editable
        layout.addView(inputEndLng);

        builder.setView(layout);

        // Buttons for location selection
        Button btnStartLocation = new Button(getContext());
        btnStartLocation.setText("Select Start Location");
        layout.addView(btnStartLocation);

        Button btnEndLocation = new Button(getContext());
        btnEndLocation.setText("Select End Location");
        layout.addView(btnEndLocation);

        // Coordinates for the selected locations
        final LatLng[] startLocation = {null};
        final LatLng[] endLocation = {null};

        // Start location selection
        btnStartLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapSelectionActivity.class);
            startActivityForResult(intent, 101); // Request code for start location
        });

        // End location selection
        btnEndLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapSelectionActivity.class);
            startActivityForResult(intent, 102); // Request code for end location
        });

        builder.setPositiveButton(R.string.add, (dialog, which) -> {
            String destination = inputDestination.getText().toString();
            String distance = inputDistance.getText().toString();
            String fuelCost = inputFuelCost.getText().toString();

            if (startLocation[0] != null && endLocation[0] != null &&
                    !destination.isEmpty() && !distance.isEmpty() && !fuelCost.isEmpty()) {
                tripList.add(new Trip(destination, distance, fuelCost, startLocation[0], endLocation[0]));
                tripAdapter.notifyItemInserted(tripList.size() - 1);

                // Update the map with the new trip
                updateMarkers(startLocation[0], endLocation[0]);
            } else {
                Toast.makeText(getContext(), "Please select both start and end locations.", Toast.LENGTH_SHORT).show();
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

    // Handle the results from MapSelectionActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            LatLng selectedLocation = new LatLng(latitude, longitude);

            if (requestCode == 101) { // For Start Location
                selectedStartLocation = selectedLocation;
                inputStartLat.setText(String.valueOf(latitude));
                inputStartLng.setText(String.valueOf(longitude));
                Toast.makeText(getContext(), "Start location selected!", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 102) { // For End Location
                selectedEndLocation = selectedLocation;
                inputEndLat.setText(String.valueOf(latitude));
                inputEndLng.setText(String.valueOf(longitude));
                Toast.makeText(getContext(), "End location selected!", Toast.LENGTH_SHORT).show();
            }
        }
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

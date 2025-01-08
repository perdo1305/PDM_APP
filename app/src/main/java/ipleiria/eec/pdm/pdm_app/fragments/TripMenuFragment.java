package ipleiria.eec.pdm.pdm_app.fragments;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;



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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;

/**
 * Fragmento para exibir e gerenciar o menu de viagens.
 */
public class TripMenuFragment extends Fragment implements OnMapReadyCallback {
    private static final String SHARED_PREFS_NAME = "trip_prefs";
    private static final String TRIP_LIST_KEY = "trip_list";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null && mMap != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                    }
                }
            }
        };
    }
    /**
     * Chamado para criar a hierarquia de views associada ao fragmento.
     *
     * @param inflater o LayoutInflater usado para inflar qualquer view no fragmento
     * @param container o ViewGroup pai ao qual a view do fragmento será anexada
     * @param savedInstanceState o estado salvo da instância
     * @return a view para o fragmento
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_menu, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize RecyclerView
        tripRecyclerView = view.findViewById(R.id.trip_recycler_view);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load trips from SharedPreferences
        tripList = loadTrips();
        if (tripList == null) {
            tripList = new ArrayList<>();
        }

        tripAdapter = new TripAdapter(tripList);
        tripRecyclerView.setAdapter(tripAdapter);

        // Set click listener for trips
        tripAdapter.setOnItemClickListener(trip -> {
            updateMarkers(trip.getStartLocation(), trip.getEndLocation());
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

    /**
     * Salva a lista de viagens nas SharedPreferences.
     *
     * @param trips a lista de viagens a ser salva
     */
    private void saveTrips(List<Trip> trips) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(trips);
        editor.putString(TRIP_LIST_KEY, json);
        editor.apply();
    }

    /**
     * Carrega a lista de viagens das SharedPreferences.
     *
     * @return a lista de viagens carregada
     */
    private List<Trip> loadTrips() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(TRIP_LIST_KEY, null);
        Type type = new TypeToken<List<Trip>>() {}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Chamado quando o fragmento é destruído.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        saveTrips(tripList);
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Chamado quando o mapa está pronto para ser usado.
     *
     * @param googleMap a instância do GoogleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        // Enable map clicks for selecting locations
        mMap.setOnMapClickListener(latLng -> {
            if (isSelectingStart) {
                // Set the start location
                selectedStartLocation = latLng;
                if (startMarker != null) startMarker.remove(); // Remove existing marker
                startMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.start_location)));
                Toast.makeText(getContext(), R.string.start_location_selected, Toast.LENGTH_SHORT).show();
            } else {
                // Set the end location
                selectedEndLocation = latLng;
                if (endMarker != null) endMarker.remove(); // Remove existing marker
                endMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.end_location)));
                Toast.makeText(getContext(), R.string.end_location_selected, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    startLocationUpdates();
                }
            } else {
                Toast.makeText(requireContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }



    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        }
                    }
                });
    }
    /**
     * Exibe um diálogo para adicionar uma nova viagem.
     */
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
    inputStartLat = new EditText(getContext());
    inputStartLat.setHint(R.string.start_latitude);
    inputStartLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    inputStartLat.setEnabled(false); // Make it non-editable
    layout.addView(inputStartLat);

    inputStartLng = new EditText(getContext());
    inputStartLng.setHint(R.string.start_longitude);
    inputStartLng.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    inputStartLng.setEnabled(false); // Make it non-editable
    layout.addView(inputStartLng);

    inputEndLat = new EditText(getContext());
    inputEndLat.setHint(R.string.end_latitude);
    inputEndLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    inputEndLat.setEnabled(false); // Make it non-editable
    layout.addView(inputEndLat);

    inputEndLng = new EditText(getContext());
    inputEndLng.setHint(R.string.end_longitude);
    inputEndLng.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    inputEndLng.setEnabled(false); // Make it non-editable
    layout.addView(inputEndLng);

    builder.setView(layout);

    // Buttons for location selection
    Button btnStartLocation = new Button(getContext());
    btnStartLocation.setText(R.string.select_start_location);
    layout.addView(btnStartLocation);

    Button btnEndLocation = new Button(getContext());
    btnEndLocation.setText(R.string.select_end_location);
    layout.addView(btnEndLocation);

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

        if (selectedStartLocation == null || selectedEndLocation == null ||
                destination.isEmpty() || distance.isEmpty() || fuelCost.isEmpty()) {
            Toast.makeText(getContext(), R.string.please_fill_in_all_fields_and_select_both_start_and_end_locations, Toast.LENGTH_SHORT).show();
        } else {
            // Add the new trip to the original list
            Trip newTrip = new Trip(destination, distance, fuelCost, selectedStartLocation, selectedEndLocation);

            tripList.add(newTrip);

            // Notify the adapter about the new item
            tripAdapter.notifyItemInserted(tripList.size() - 1);

            // Update the map with the new trip
            updateMarkers(selectedStartLocation, selectedEndLocation);
        }
    });

    builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

    builder.show();
}

    /**
     * Filtra as viagens com base na consulta de pesquisa.
     *
     * @param query o texto de pesquisa
     */
    private void filterTrips(String query) {
        List<Trip> filteredList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (trip.getDestination().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(trip);
            }
        }
        // Update the adapter with the filtered list
        tripAdapter.updateList(filteredList);
    }

    /**
     * Atualiza a lista de viagens e notifica o adapter.
     *
     * @param newList a nova lista de viagens
     */
    public void updateList(List<Trip> newList) {
        this.tripList = newList; // Replace the adapter's list
        tripAdapter.notifyDataSetChanged(); // Notify the adapter
    }

    /**
     * Manipula os resultados da atividade MapSelectionActivity.
     *
     * @param requestCode o código de solicitação
     * @param resultCode o código de resultado
     * @param data os dados retornados
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra(getString(R.string.latitude), 0);
            double longitude = data.getDoubleExtra(getString(R.string.longitude), 0);
            LatLng selectedLocation = new LatLng(latitude, longitude);

            if (requestCode == 101) { // For Start Location
                selectedStartLocation = selectedLocation;
                inputStartLat.setText(String.valueOf(latitude));
                inputStartLng.setText(String.valueOf(longitude));
                Toast.makeText(getContext(), R.string.start_location_selected, Toast.LENGTH_SHORT).show();
            } else if (requestCode == 102) { // For End Location
                selectedEndLocation = selectedLocation;
                inputEndLat.setText(String.valueOf(latitude));
                inputEndLng.setText(String.valueOf(longitude));
                Toast.makeText(getContext(), R.string.end_location_selected, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Atualiza dinamicamente os marcadores de início e fim.
     *
     * @param newStart a nova localização de início
     * @param newEnd a nova localização de fim
     */
    public void updateMarkers(LatLng newStart, LatLng newEnd) {
        if (startMarker != null) startMarker.setPosition(newStart);
        else startMarker = mMap.addMarker(new MarkerOptions().position(newStart).title(getString(R.string.start_location_)));

        if (endMarker != null) endMarker.setPosition(newEnd);
        else endMarker = mMap.addMarker(new MarkerOptions().position(newEnd).title(getString(R.string.end_location_)));

        // Create or update route line
        if (routePolyline != null) {
            routePolyline.setPoints(List.of(newStart, newEnd));
        } else {
            routePolyline = mMap.addPolyline(new PolylineOptions()
                    .add(newStart, newEnd)
                    .width(5)
                    .color(Color.RED));
        }

        // Adjust camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                new LatLngBounds.Builder().include(newStart).include(newEnd).build(), 100));
    }
}
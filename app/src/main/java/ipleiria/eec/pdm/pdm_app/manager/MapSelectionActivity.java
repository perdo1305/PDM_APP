package ipleiria.eec.pdm.pdm_app.manager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import ipleiria.eec.pdm.pdm_app.R;

public class MapSelectionActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Allow the user to select a location
        mMap.setOnMapClickListener(latLng -> {
            if (selectedMarker != null) selectedMarker.remove();
            selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            // Pass the selected location back when the user confirms
            findViewById(R.id.btn_confirm).setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", latLng.latitude);
                resultIntent.putExtra("longitude", latLng.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        });
    }

    private void drawLine(LatLng start, LatLng end) {
        mMap.addPolyline(new PolylineOptions()
                .add(start, end)
                .width(5)
                .color(Color.RED));
    }

    public void onItemClick(Trip trip) {
        LatLng startLocation = new LatLng(trip.getStartLatitude(), trip.getStartLongitude());
        LatLng endLocation = new LatLng(trip.getEndLatitude(), trip.getEndLongitude());
        drawLine(startLocation, endLocation);
    }
}
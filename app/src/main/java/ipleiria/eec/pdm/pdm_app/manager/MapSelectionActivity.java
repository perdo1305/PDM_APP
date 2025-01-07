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

/**
 * Atividade para selecionar uma localização no mapa.
 */
public class MapSelectionActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker selectedMarker;

    /**
     * Chamado quando a atividade é criada.
     *
     * @param savedInstanceState o estado salvo da instância
     */
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

    /**
     * Chamado quando o mapa está pronto para ser usado.
     *
     * @param googleMap a instância do GoogleMap
     */
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

    /**
     * Desenha uma linha entre duas localizações no mapa.
     *
     * @param start a localização inicial
     * @param end a localização final
     */
    private void drawLine(LatLng start, LatLng end) {
        mMap.addPolyline(new PolylineOptions()
                .add(start, end)
                .width(5)
                .color(Color.RED));
    }

    /**
     * Manipula o clique em um item de viagem.
     *
     * @param trip a viagem clicada
     */
    public void onItemClick(Trip trip) {
        LatLng startLocation = new LatLng(trip.getStartLatitude(), trip.getStartLongitude());
        LatLng endLocation = new LatLng(trip.getEndLatitude(), trip.getEndLongitude());
        drawLine(startLocation, endLocation);
    }
}
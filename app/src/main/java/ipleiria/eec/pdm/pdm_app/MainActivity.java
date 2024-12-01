package ipleiria.eec.pdm.pdm_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ipleiria.eec.pdm.pdm_app.fragments.LiveDataMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.MaintenanceMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.TripMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.VehicleMenuFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BottomNavigationView e NavController
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Fragment Manager para troca manual de fragments
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_vehicle) {
                selectedFragment = new VehicleMenuFragment();
            } else if (item.getItemId() == R.id.nav_maintenance) {
                selectedFragment = new MaintenanceMenuFragment();
            } else if (item.getItemId() == R.id.nav_trip) {
                selectedFragment = new TripMenuFragment();
            } else if (item.getItemId() == R.id.nav_live_data) {
                selectedFragment = new LiveDataMenuFragment();
            }


            if(selectedFragment == null) {
                return false;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, selectedFragment)
                    .commit();

            return true;
        });

        // Define o fragment inicial
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_vehicle);
        }
    }
}

package ipleiria.eec.pdm.pdm_app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import ipleiria.eec.pdm.pdm_app.fragments.LiveDataMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.MaintenanceMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.TripMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.VehicleMenuFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

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

            if (selectedFragment == null) {
                return false;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, selectedFragment)
                    .commit();

            return true;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_vehicle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        } else if (id == R.id.action_help) {
            showHelpDialog();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showSettingsDialog() {
        String[] options = {"\uD83C\uDF0D Change Language", "☯\uFE0E Toggle Dark Mode"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Settings")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showLanguageDialog();
                    } else if (which == 1) {
                        boolean isNightMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
                        AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
                    }
                });
        builder.create().show();
    }

    private void showLanguageDialog() {
        String[] languages = {"\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F English", "\uD83C\uDDF5\uD83C\uDDF9 Portuguese", "\uD83C\uDDE9\uD83C\uDDEA German","\uD83C\uDDE8\uD83C\uDDF3 Chinese(simplied)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("\uD83C\uDF0D Select Language")
                .setItems(languages, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            setLocale("en");
                            break;
                        case 1:
                            setLocale("pt");
                            break;
                        case 2:
                            setLocale("de");
                            break;
                        case 3:
                            setLocale("cn");
                            break;
                    }
                });
        builder.create().show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help")
                .setMessage(R.string.possible_interactions_tap_select_long_press_delete_swipe_return)
                .setPositiveButton("OK", null);
        builder.create().show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");

        // Inflate the custom layout
        View view = getLayoutInflater().inflate(R.layout.dialog_about, null);
        builder.setView(view);

        // Make the link clickable
        TextView aboutText = view.findViewById(R.id.about_text);
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());

        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}
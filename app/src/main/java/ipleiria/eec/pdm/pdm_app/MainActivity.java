package ipleiria.eec.pdm.pdm_app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import ipleiria.eec.pdm.pdm_app.fragments.LiveDataMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.MaintenanceMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.TripMenuFragment;
import ipleiria.eec.pdm.pdm_app.fragments.VehicleMenuFragment;

/**
 * Main activity of the application.
 */
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

    /**
     * Shows the settings dialog.
     */
    private void showSettingsDialog() {
        String[] options = {"\uD83C\uDF0D Change Language", "☯\uFE0E Toggle Dark Mode", "🔒 Log Out"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showLanguageDialog();
                    } else if (which == 1) {
                        boolean isNightMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
                        AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
                        recreate(); // Recreate the activity to apply the mode change immediately
                    } else if (which == 2) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                });
        builder.create().show();
    }

    /**
     * Shows the language selection dialog.
     */
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

    /**
     * Sets the locale for the application.
     *
     * @param lang the language code
     */
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }

    /**
     * Shows the help dialog.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help")
                .setMessage(R.string.possible_interactions_tap_select_long_press_delete_swipe_return)
                .setPositiveButton("OK", null);
        builder.create().show();
    }

    /**
     * Shows the about dialog with project information and a link to the GitHub repository.
     */
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");

        // Inflate the custom layout
        View view = getLayoutInflater().inflate(R.layout.dialog_about, null);
        builder.setView(view);

        // Set the project information
        TextView aboutText = view.findViewById(R.id.about_text);
        aboutText.setText("This project is a vehicle management app developed by Bernardo Santos and Pedro Ferreira.\n\nFor more information, visit our GitHub repository:");
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());

        // Set the GitHub link
        TextView githubLink = view.findViewById(R.id.github_link);
        githubLink.setText("https://github.com/your-repo");
        githubLink.setMovementMethod(LinkMovementMethod.getInstance());

        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}
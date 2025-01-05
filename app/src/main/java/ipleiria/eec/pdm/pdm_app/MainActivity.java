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
 * Atividade principal da aplicação.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragmentSelecionado = null;
            if (item.getItemId() == R.id.nav_vehicle) {
                fragmentSelecionado = new VehicleMenuFragment();
            } else if (item.getItemId() == R.id.nav_maintenance) {
                fragmentSelecionado = new MaintenanceMenuFragment();
            } else if (item.getItemId() == R.id.nav_trip) {
                fragmentSelecionado = new TripMenuFragment();
            } else if (item.getItemId() == R.id.nav_live_data) {
                fragmentSelecionado = new LiveDataMenuFragment();
            }

            if (fragmentSelecionado == null) {
                return false;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragmentSelecionado)
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
            mostrarDialogoAjuda();
            return true;
        } else if (id == R.id.action_about) {
            mostrarDialogoSobre();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Exibe o diálogo de configurações.
     */
    private void showSettingsDialog() {
        String[] options = {"\uD83C\uDF0D Change Language", "☯\uFE0E Toggle Dark Mode", "🔒 Log Out"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        mostrarDialogoIdioma();
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
     * Exibe o diálogo de seleção de idioma.
     */
    private void mostrarDialogoIdioma() {
        String[] idiomas = {getString(R.string.ingl_s), getString(R.string.portugu_s)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_language)
                .setItems(idiomas, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            setIdioma("en");
                            break;
                        case 1:
                            setIdioma("pt");
                            break;
                    }
                });
        builder.create().show();
    }

    /**
     * Define o idioma da aplicação.
     *
     * @param idioma o código do idioma
     */
    private void setIdioma(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }

    /**
     * Exibe o diálogo de ajuda.
     */
    private void mostrarDialogoAjuda() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_)
                .setMessage(R.string.possible_interactions_tap_select_long_press_delete_swipe_return)
                .setPositiveButton("OK", null);
        builder.create().show();
    }

    /**
     * Exibe o diálogo "Sobre" com informações do projeto e um link para o repositório GitHub.
     */
    private void mostrarDialogoSobre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about);

        // Infla o layout personalizado
        View view = getLayoutInflater().inflate(R.layout.dialog_about, null);
        builder.setView(view);

        // Define as informações do projeto
        TextView sobreTexto = view.findViewById(R.id.about_text);
        sobreTexto.setText(R.string.this_project_is_an_application_that_manages_vehicles_developed_by_bernardo_santos_and_pedro_ferreira_for_more_information_visit_our_github_repository);
        sobreTexto.setMovementMethod(LinkMovementMethod.getInstance());
        // Define o link para o GitHub
        TextView linkGitHub = view.findViewById(R.id.github_link);
        linkGitHub.setText("https://github.com/your-repo");
        linkGitHub.setMovementMethod(LinkMovementMethod.getInstance());

        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}

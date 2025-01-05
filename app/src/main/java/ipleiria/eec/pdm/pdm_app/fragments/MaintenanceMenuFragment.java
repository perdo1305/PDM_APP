package ipleiria.eec.pdm.pdm_app.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ipleiria.eec.pdm.pdm_app.R;
import ipleiria.eec.pdm.pdm_app.manager.Maintenance;
import ipleiria.eec.pdm.pdm_app.manager.MaintenanceAdapter;
import ipleiria.eec.pdm.pdm_app.manager.Vehicle;
import ipleiria.eec.pdm.pdm_app.manager.VehicleDatabaseHelper;

public class MaintenanceMenuFragment extends Fragment {
    private RecyclerView maintenanceRecyclerView;
    private MaintenanceAdapter maintenanceAdapter;
    private List<Maintenance> maintenanceList;



    /**
     * mostrar um dialogo para adicionar um novo registro de manutenção.
     */
    private void showAddMaintenanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.add_maintenance__record);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputServiceType = new EditText(getContext());
        inputServiceType.setHint(R.string.service_type_e_g_oil_change);
        layout.addView(inputServiceType);

        EditText inputServiceDate = new EditText(getContext());
        inputServiceDate.setHint(R.string.service_date_yyyy_mm_dd);
        layout.addView(inputServiceDate);

        EditText inputServiceCost = new EditText(getContext());
        inputServiceCost.setHint(R.string.service_cost);
        layout.addView(inputServiceCost);

        builder.setView(layout);

        builder.setPositiveButton(R.string.addd, (dialog, which) -> {
            String serviceType = inputServiceType.getText().toString();
            String serviceDate = inputServiceDate.getText().toString();
            String serviceCost = inputServiceCost.getText().toString();

            if (!serviceType.isEmpty() && !serviceDate.isEmpty() && !serviceCost.isEmpty()) {
                maintenanceList.add(new Maintenance(serviceType, serviceDate, serviceCost));
                maintenanceAdapter.notifyItemInserted(maintenanceList.size() - 1);
            } else {
                Toast.makeText(getContext(), R.string.all___fields_are_required, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancell, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * mostrar um dialogo para editar um registro de manutenção.
     * @param position
     * @param maintenance
     */
    private void showEditMaintenanceDialog(int position, Maintenance maintenance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.edit_maintenance);

        // Input fields
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputServiceType = new EditText(getContext());
        inputServiceType.setHint(R.string.service_type_);
        inputServiceType.setText(maintenance.getServiceType());
        layout.addView(inputServiceType);

        EditText inputServiceDate = new EditText(getContext());
        inputServiceDate.setHint(R.string.service_date_yyyy_mm_dd_);
        inputServiceDate.setText(maintenance.getServiceDate());
        layout.addView(inputServiceDate);

        EditText inputServiceCost = new EditText(getContext());
        inputServiceCost.setHint(R.string.service_cost_);
        inputServiceCost.setText(maintenance.getServiceCost());
        layout.addView(inputServiceCost);

        builder.setView(layout);

        // Add buttons
        builder.setPositiveButton(R.string.save_, (dialog, which) -> {
            String serviceType = inputServiceType.getText().toString();
            String serviceDate = inputServiceDate.getText().toString();
            String serviceCost = inputServiceCost.getText().toString();

            if (!serviceType.isEmpty() && !serviceDate.isEmpty() && !serviceCost.isEmpty()) {
                maintenance.setServiceType(serviceType);
                maintenance.setServiceDate(serviceDate);
                maintenance.setServiceCost(serviceCost);
                maintenanceAdapter.notifyItemChanged(position);
            } else {
                Toast.makeText(getContext(), R.string.all___fields_are_required, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancell, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * mostrar um dialogo de confirmação para excluir um registro de manutenção.
     * @param position
     */
    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_maintenance_record)
                .setMessage(R.string.are_you_sure_you_want_to_delete_this_maintenance_record)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    maintenanceList.remove(position);
                    maintenanceAdapter.notifyItemRemoved(position);
                    Toast.makeText(getContext(), R.string.maintenance_record_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void showSelectVehicleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_vehicle);

        List<Vehicle> vehicleList = new VehicleDatabaseHelper(getContext()).getAllVehicles();
        String[] vehicleNames = new String[vehicleList.size()];
        for (int i = 0; i < vehicleList.size(); i++) {
            vehicleNames[i] = vehicleList.get(i).getName();
        }

        builder.setItems(vehicleNames, (dialog, which) -> {
            Vehicle selectedVehicle = vehicleList.get(which);
            // Use the selectedVehicle object as needed
            FloatingActionButton fabAddMaintenance = getView().findViewById(R.id.fab_add_maintenance);
            fabAddMaintenance.setVisibility(View.VISIBLE);
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_menu, container, false);

        TextView tvSelectedVehicle = view.findViewById(R.id.tv_selected_vehicle);
        maintenanceRecyclerView = view.findViewById(R.id.maintenance_recycler_view);
        maintenanceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        maintenanceList = new ArrayList<>();
        maintenanceAdapter = new MaintenanceAdapter(maintenanceList);
        maintenanceRecyclerView.setAdapter(maintenanceAdapter);

        Button btnSelectVehicle = view.findViewById(R.id.btn_select_vehicle);
        btnSelectVehicle.setOnClickListener(v -> showSelectVehicleDialog(tvSelectedVehicle));

        FloatingActionButton fabAddMaintenance = view.findViewById(R.id.fab_add_maintenance);
        fabAddMaintenance.setOnClickListener(v -> showAddMaintenanceDialog());

        return view;
    }

    private void showSelectVehicleDialog(TextView tvSelectedVehicle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_vehicle);

        List<Vehicle> vehicleList = new VehicleDatabaseHelper(getContext()).getAllVehicles();
        String[] vehicleNames = new String[vehicleList.size()];
        for (int i = 0; i < vehicleList.size(); i++) {
            vehicleNames[i] = vehicleList.get(i).getName();
        }

        builder.setItems(vehicleNames, (dialog, which) -> {
            Vehicle selectedVehicle = vehicleList.get(which);
            tvSelectedVehicle.setText(getString(R.string.selected_vehicle) + selectedVehicle.getName());
            FloatingActionButton fabAddMaintenance = getView().findViewById(R.id.fab_add_maintenance);
            fabAddMaintenance.setVisibility(View.VISIBLE);
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}

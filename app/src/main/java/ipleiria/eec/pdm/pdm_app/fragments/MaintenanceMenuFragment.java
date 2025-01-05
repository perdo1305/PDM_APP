package ipleiria.eec.pdm.pdm_app.fragments;

import android.text.Html;
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
    private int selectedVehicleId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_menu, container, false);

        TextView tvSelectedVehicle = view.findViewById(R.id.tv_selected_vehicle);
        maintenanceRecyclerView = view.findViewById(R.id.maintenance_recycler_view);
        maintenanceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        maintenanceList = new ArrayList<>();
        maintenanceAdapter = new MaintenanceAdapter(maintenanceList);
        maintenanceRecyclerView.setAdapter(maintenanceAdapter);

        maintenanceAdapter.setOnMaintenanceClickListener(new MaintenanceAdapter.OnMaintenanceClickListener() {
            @Override
            public void onEditMaintenance(int position, Maintenance maintenance) {
                showEditMaintenanceDialog(position, maintenance);
            }

            @Override
            public void onDeleteMaintenance(int position) {
                showDeleteConfirmationDialog(position);
            }
        });

        Button btnSelectVehicle = view.findViewById(R.id.btn_select_vehicle);
        btnSelectVehicle.setOnClickListener(v -> showSelectVehicleDialog(tvSelectedVehicle));

        FloatingActionButton fabAddMaintenance = view.findViewById(R.id.fab_add_maintenance);
        fabAddMaintenance.setOnClickListener(v -> {
            if (selectedVehicleId == -1) {
                Toast.makeText(getContext(), "Please select a vehicle first!", Toast.LENGTH_SHORT).show();
            } else {
                showAddMaintenanceDialog(selectedVehicleId);
            }
        });

        // Load maintenance records based on the selected vehicle
        if (selectedVehicleId != -1) {
            loadMaintenanceRecords(selectedVehicleId);
        } else {
            loadAllMaintenanceRecords();
        }

        return view;
    }

    private void loadAllMaintenanceRecords() {
        maintenanceList.clear();
        maintenanceList.addAll(new VehicleDatabaseHelper(getContext()).getAllMaintenance());
        maintenanceAdapter.notifyDataSetChanged();
    }

    private void loadMaintenanceRecords(int vehicleId) {
        maintenanceList.clear();
        maintenanceList.addAll(new VehicleDatabaseHelper(getContext()).getMaintenanceByVehicleId(vehicleId));
        maintenanceAdapter.notifyDataSetChanged();
    }

    private void showSelectVehicleDialog(TextView tvSelectedVehicle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Vehicle");

        List<Vehicle> vehicleList = new VehicleDatabaseHelper(getContext()).getAllVehicles();
        String[] vehicleNames = new String[vehicleList.size()];
        for (int i = 0; i < vehicleList.size(); i++) {
            vehicleNames[i] = vehicleList.get(i).getName();
        }

        builder.setItems(vehicleNames, (dialog, which) -> {
            Vehicle selectedVehicle = vehicleList.get(which);
            tvSelectedVehicle.setText(Html.fromHtml("Selected Vehicle: <i><big>" + selectedVehicle.getName() + "</big></i>"));
            selectedVehicleId = selectedVehicle.getVehicleId();
            loadMaintenanceRecords(selectedVehicleId);
            FloatingActionButton fabAddMaintenance = getView().findViewById(R.id.fab_add_maintenance);
            fabAddMaintenance.setVisibility(View.VISIBLE);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void showAddMaintenanceDialog(int vehicleId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Maintenance Record");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputServiceType = new EditText(getContext());
        inputServiceType.setHint(R.string.service_type_e_g_oil_change);
        layout.addView(inputServiceType);

        EditText inputServiceDate = new EditText(getContext());
        inputServiceDate.setHint("Service Date (dd - mm - yyyy)");
        layout.addView(inputServiceDate);

        EditText inputServiceCost = new EditText(getContext());
        inputServiceCost.setHint("Service Cost €");
        layout.addView(inputServiceCost);

        builder.setView(layout);

        builder.setPositiveButton(R.string.addd, (dialog, which) -> {
            String serviceType = inputServiceType.getText().toString();
            String serviceDate = inputServiceDate.getText().toString();
            String serviceCost = inputServiceCost.getText().toString();

            if (!serviceType.isEmpty() && !serviceDate.isEmpty() && !serviceCost.isEmpty()) {
                Maintenance newMaintenance = new Maintenance(vehicleId, serviceType, serviceDate, serviceCost);
                maintenanceList.add(newMaintenance);
                maintenanceAdapter.notifyItemInserted(maintenanceList.size() - 1);

                // Save to database
                new VehicleDatabaseHelper(getContext()).insertMaintenance(newMaintenance);
            } else {
                Toast.makeText(getContext(), R.string.all___fields_are_required, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancell, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditMaintenanceDialog(int position, Maintenance maintenance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.edit_maintenance);

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
                maintenance.setVehicleId(maintenance.getVehicleId()); // Ensure vehicleId is set
                maintenanceAdapter.notifyItemChanged(position);

                // Update in database
                new VehicleDatabaseHelper(getContext()).insertMaintenance(maintenance);
            } else {
                Toast.makeText(getContext(), R.string.all___fields_are_required, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancell, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_maintenance_record)
                .setMessage(R.string.are_you_sure_you_want_to_delete_this_maintenance_record)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    Maintenance maintenance = maintenanceList.get(position);
                    new VehicleDatabaseHelper(getContext()).deleteMaintenance(maintenance.getMaintenanceId());
                    maintenanceList.remove(position);
                    maintenanceAdapter.notifyItemRemoved(position);
                    // Reload the maintenance records
                    if (selectedVehicleId != -1) {
                        loadMaintenanceRecords(selectedVehicleId);
                    } else {
                        loadAllMaintenanceRecords();
                    }
                    Toast.makeText(getContext(), R.string.maintenance_record_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }


}

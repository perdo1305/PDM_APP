package ipleiria.eec.pdm.pdm_app.fragments;

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

import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ipleiria.eec.pdm.pdm_app.R;
import ipleiria.eec.pdm.pdm_app.manager.Vehicle;
import ipleiria.eec.pdm.pdm_app.manager.VehicleAdapter;


public class VehicleMenuFragment extends Fragment {
    private RecyclerView vehicleRecyclerView;
    private VehicleAdapter vehicleAdapter;
    private List<Vehicle> vehicleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_menu, container, false);

        // Initialize RecyclerView
        vehicleRecyclerView = view.findViewById(R.id.vehicle_recycler_view);
        vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data
        vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle("Toyota Corolla", "Last maintenance: 500km ago"));
        vehicleList.add(new Vehicle("Tesla Model S", "Last maintenance: 1200km ago"));

        // Set adapter
        vehicleAdapter = new VehicleAdapter(vehicleList);
        vehicleRecyclerView.setAdapter(vehicleAdapter);

        vehicleAdapter.setOnVehicleClickListener(new VehicleAdapter.OnVehicleClickListener() {
            @Override
            public void onEditVehicle(int position, Vehicle vehicle) {
                showEditVehicleDialog(position, vehicle);
            }

            @Override
            public void onDeleteVehicle(int position) {
                showDeleteConfirmationDialog(position);
            }
        });

        // FAB for adding vehicles
        FloatingActionButton fabAddVehicle = view.findViewById(R.id.fab_add_vehicle);
        fabAddVehicle.setOnClickListener(v -> showAddVehicleDialog());

        return view;
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Vehicle")
                .setMessage("Are you sure you want to delete this vehicle?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    vehicleList.remove(position);
                    vehicleAdapter.notifyItemRemoved(position);
                    Toast.makeText(getContext(), "Vehicle deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEditVehicleDialog(int position, Vehicle vehicle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Vehicle");

        // Input fields
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputName = new EditText(getContext());
        inputName.setHint("Vehicle Name");
        inputName.setText(vehicle.getName());
        layout.addView(inputName);

        EditText inputDetails = new EditText(getContext());
        inputDetails.setHint("Vehicle Details");
        inputDetails.setText(vehicle.getDetails());
        layout.addView(inputDetails);

        builder.setView(layout);

        // Add buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = inputName.getText().toString();
            String details = inputDetails.getText().toString();

            if (!name.isEmpty() && !details.isEmpty()) {
                vehicle.setName(name);
                vehicle.setDetails(details);
                vehicleAdapter.notifyItemChanged(position);
            } else {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void showAddVehicleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Vehicle");

        // Input fields
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputName = new EditText(getContext());
        inputName.setHint("Vehicle Name");
        layout.addView(inputName);

        EditText inputDetails = new EditText(getContext());
        inputDetails.setHint("Vehicle Details (e.g., Last maintenance: 500km ago)");
        layout.addView(inputDetails);

        builder.setView(layout);

        // Add buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = inputName.getText().toString();
            String details = inputDetails.getText().toString();

            if (!name.isEmpty() && !details.isEmpty()) {
                vehicleList.add(new Vehicle(name, details));
                vehicleAdapter.notifyItemInserted(vehicleList.size() - 1);
            } else {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}

package ipleiria.eec.pdm.pdm_app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ipleiria.eec.pdm.pdm_app.R;
import ipleiria.eec.pdm.pdm_app.manager.Vehicle;
import ipleiria.eec.pdm.pdm_app.manager.VehicleAdapter;
import ipleiria.eec.pdm.pdm_app.manager.VehicleDatabaseHelper;

public class VehicleMenuFragment extends Fragment {
    private RecyclerView vehicleRecyclerView;
    private VehicleAdapter vehicleAdapter;
    private List<Vehicle> vehicleList;
    private ImageView selectedImageView;
    private VehicleDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_menu, container, false);

        dbHelper = new VehicleDatabaseHelper(getContext());
        vehicleList = dbHelper.getAllVehicles();

        vehicleRecyclerView = view.findViewById(R.id.vehicle_recycler_view);
        vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        FloatingActionButton fabAddVehicle = view.findViewById(R.id.fab_add_vehicle);
        fabAddVehicle.setOnClickListener(v -> showAddVehicleDialog());

        return view;
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Vehicle")
                .setMessage("Are you sure you want to delete this vehicle?")
                .setPositiveButton("Yes", (dialog, which) -> {
    dbHelper.deleteVehicle(Integer.parseInt(String.valueOf(vehicleList.get(position).getVehicleId())));
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

        EditText inputLicensePlate = new EditText(getContext());
        inputLicensePlate.setHint("License Plate");
        inputLicensePlate.setText(vehicle.getLicensePlate());
        layout.addView(inputLicensePlate);

        FrameLayout photoContainer = new FrameLayout(getContext());
        ImageView vehiclePhoto = new ImageView(getContext());
        vehiclePhoto.setLayoutParams(new FrameLayout.LayoutParams(200, 200));
        vehiclePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (vehicle.getPhotoUri() != null && !vehicle.getPhotoUri().isEmpty()) {
            Glide.with(this).load(vehicle.getPhotoUri()).into(vehiclePhoto);
        } else {
            vehiclePhoto.setImageResource(R.drawable.ic_vehicle_placeholder);
        }

        Button editPhotoButton = new Button(getContext());
        editPhotoButton.setText("Edit Photo");
        editPhotoButton.setOnClickListener(v -> selectPhoto(vehiclePhoto));

        photoContainer.addView(vehiclePhoto);
        photoContainer.addView(editPhotoButton);
        layout.addView(photoContainer);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = inputName.getText().toString();
            String details = inputDetails.getText().toString();
            String licensePlate = inputLicensePlate.getText().toString();
            String photoUri = (String) vehiclePhoto.getTag();

            if (!name.isEmpty() && !details.isEmpty() && !licensePlate.isEmpty()) {
                vehicle.setName(name);
                vehicle.setDetails(details);
                vehicle.setLicensePlate(licensePlate);
                vehicle.setPhotoUri(photoUri);
                vehicle.setVehicleId(vehicle.getVehicleId());
                dbHelper.updateVehicle(vehicle);
                vehicleAdapter.notifyItemChanged(position);
            } else {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void selectPhoto(ImageView imageView) {
        selectedImageView = imageView;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null && selectedImageView != null) {
                selectedImageView.setTag(selectedImageUri.toString());
                Glide.with(this).load(selectedImageUri).into(selectedImageView);
            }
        }
    }

    private void showAddVehicleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Vehicle");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputName = new EditText(getContext());
        inputName.setHint("Vehicle Name");
        layout.addView(inputName);

        EditText inputDetails = new EditText(getContext());
        inputDetails.setHint("Vehicle Details");
        layout.addView(inputDetails);

        EditText inputLicensePlate = new EditText(getContext());
        inputLicensePlate.setHint("License Plate");
        layout.addView(inputLicensePlate);

        ImageView vehiclePhoto = new ImageView(getContext());
        vehiclePhoto.setImageResource(R.drawable.ic_vehicle_placeholder);
        vehiclePhoto.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        vehiclePhoto.setPadding(0, 16, 0, 16);
        layout.addView(vehiclePhoto);

        builder.setView(layout);

        vehiclePhoto.setOnClickListener(v -> selectPhoto(vehiclePhoto));

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = inputName.getText().toString();
            String details = inputDetails.getText().toString();
            String licensePlate = inputLicensePlate.getText().toString();
            String photoUri = (String) vehiclePhoto.getTag();

            if (!name.isEmpty() && !details.isEmpty() && !licensePlate.isEmpty()) {
                Vehicle newVehicle = new Vehicle(0, name, details, photoUri, licensePlate);
                dbHelper.addVehicle(newVehicle);
                vehicleList.add(newVehicle);
                vehicleAdapter.notifyItemInserted(vehicleList.size() - 1);
            } else {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
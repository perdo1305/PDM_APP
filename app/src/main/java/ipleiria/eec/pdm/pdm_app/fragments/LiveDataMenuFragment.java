package ipleiria.eec.pdm.pdm_app.fragments;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ipleiria.eec.pdm.pdm_app.R;

public class LiveDataMenuFragment extends Fragment {

    private TextView mStatusBlueTv, mPairedTv;
    private ListView lvNewDevices;
    private BluetoothAdapter mBlueAdapter;
    private ArrayList<String> mBTDevicesString = new ArrayList<>();
    private ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    private ActivityResultLauncher<Intent> enableBluetooth;
    private boolean isReceiverRegistered = false;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!hasBluetoothPermissions()) return;

            final String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !mBTDevicesString.contains(device.getName() + "\n" + device.getAddress())) {
                    mBTDevices.add(device);
                    mBTDevicesString.add(device.getName() + "\n" + device.getAddress());

                    ArrayAdapter<String> mDeviceListAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_list_item_1, mBTDevicesString);
                    lvNewDevices.setAdapter(mDeviceListAdapter);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_data_menu, container, false);

        mStatusBlueTv = view.findViewById(R.id.statusBluetoothTv);
        mPairedTv = view.findViewById(R.id.pairedTv);
        lvNewDevices = view.findViewById(R.id.listViewDevices);

        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.GONE);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        initializeBluetoothLauncher();
        setupBluetoothStatus();
        checkBTPermissions();

        Button btnTurnOn = view.findViewById(R.id.btnTurnOn);
        btnTurnOn.setOnClickListener(this::onClickOn);

        Button btnTurnOff = view.findViewById(R.id.btnTurnOff);
        btnTurnOff.setOnClickListener(this::onClickOff);

        Button btnDiscover = view.findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(this::btnDiscover);

        lvNewDevices.setOnItemClickListener((parent, itemView, position, id) -> connectToDevice(mBTDevices.get(position)));

        return view;
    }

    private void initializeBluetoothLauncher() {
        enableBluetooth = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    showToast("Bluetooth is on");
                } else {
                    showToast("Could not turn on Bluetooth");
                }
            }
        });
    }

    private void setupBluetoothStatus() {
        if (mBlueAdapter == null) {
            mStatusBlueTv.setText("Bluetooth is not available");
        } else {
            mStatusBlueTv.setText("Bluetooth is available");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean hasBluetoothPermissions() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && !hasBluetoothPermissions()) {
            requestPermissions(new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1001);
        }
    }

    public void onClickOn(View view) {
        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.GONE);
        if (!mBlueAdapter.isEnabled()) {
            showToast("Turning On Bluetooth...");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetooth.launch(intent);
        } else {
            showToast("Bluetooth is already on");
        }
    }

    @SuppressLint("MissingPermission")
    public void onClickOff(View view) {
        if (!hasBluetoothPermissions()) return;

        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.GONE);
        if (mBlueAdapter.isEnabled()) {
            mBlueAdapter.disable();
            showToast("Turning Bluetooth Off");
        } else {
            showToast("Bluetooth is already off");
        }
    }
    @SuppressLint("MissingPermission")
    public void btnDiscover(View view) {
        if (!hasBluetoothPermissions()) return;

        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.VISIBLE);
        mBTDevices.clear();
        mBTDevicesString.clear();

        if (mBlueAdapter.isDiscovering()) {
            mBlueAdapter.cancelDiscovery();
        }

        mBlueAdapter.startDiscovery();
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        requireContext().registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        isReceiverRegistered = true;

        showToast("Discovering devices...");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isReceiverRegistered) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            requireContext().registerReceiver(mBroadcastReceiver, filter);
            isReceiverRegistered = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isReceiverRegistered) {
            requireContext().unregisterReceiver(mBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }
    @SuppressLint("MissingPermission")
    private void connectToDevice(BluetoothDevice device) {
        if (!hasBluetoothPermissions()) {
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1002);
            return;
        }

        try {
            if (mBlueAdapter.isDiscovering()) {
                mBlueAdapter.cancelDiscovery();
            }

            if (device.getUuids() == null || device.getUuids().length == 0) {
                showToast("Device does not provide UUIDs");
                return;
            }

            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            socket.connect();
            showToast("Connected to " + device.getName());

        } catch (Exception e) {
            Log.e("Bluetooth", "Connection failed", e);
            showToast("Failed to connect to " + device.getName());
        }
    }
}

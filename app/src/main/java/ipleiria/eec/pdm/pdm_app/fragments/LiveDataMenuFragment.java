package ipleiria.eec.pdm.pdm_app.fragments;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.ImageView;
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
    private ImageView mBlueIv;
    private ListView lvNewDevices;
    private BluetoothAdapter mBlueAdapter;
    private ArrayList<String> mBTDevicesString = new ArrayList<>();
    private ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    private ActivityResultLauncher<Intent> enableBluetooth;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R &&
                    ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            final String action = intent.getAction();
            if (mBTDevicesString.isEmpty())
                mBTDevicesString.add("List of devices: ");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !mBTDevices.contains(device)) {
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
        btnTurnOn.setOnClickListener(v -> onClickOn(v));

        Button btnTurnOff = view.findViewById(R.id.btnTurnOff);
        btnTurnOff.setOnClickListener(v -> onClickOff(v));

        Button btnDiscover = view.findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(v -> btnDiscover(v));

        lvNewDevices.setOnItemClickListener((parent, itemView, position, id) -> {
            BluetoothDevice device = mBTDevices.get(position - 1); // Adjust for "List of devices:" header
            connectToDevice(device);
        });

        return view;
    }

    private void initializeBluetoothLauncher() {
        enableBluetooth = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                   // mBlueIv.setImageResource(R.drawable.ic_action_on);
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
            if (mBlueAdapter.isEnabled()) {
                //mBlueIv.setImageResource(R.drawable.ic_action_on);
            } else {
                //mBlueIv.setImageResource(R.drawable.ic_action_off);
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            int permissionCheck =
                    requireContext().checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) +
                            requireContext().checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) +
                            requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) +
                            requireContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionCheck != 0) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        } else {
            Log.d("Bluetooth", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
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

    public void onClickOff(View view) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.GONE);
        if (mBlueAdapter.isEnabled()) {
            mBlueAdapter.disable();
            showToast("Turning Bluetooth Off");
            //mBlueIv.setImageResource(R.drawable.ic_action_off);
        } else {
            showToast("Bluetooth is already off");
        }
    }

    public void btnDiscover(View view) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        requireContext().unregisterReceiver(mBroadcastReceiver);
        mBlueAdapter.cancelDiscovery();
    }

    private void connectToDevice(BluetoothDevice device) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1002);
            return;
        }

        try {
            // Cancel discovery to improve connection speed
            if (mBlueAdapter.isDiscovering()) {
                mBlueAdapter.cancelDiscovery();
            }

            // Create a socket and connect
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            socket.connect();
            showToast("Connected to " + device.getName());
            // Use the socket to communicate with the device
        } catch (Exception e) {
            Log.e("Bluetooth", "Connection failed", e);
            showToast("Failed to connect to " + device.getName());
        }
    }

}

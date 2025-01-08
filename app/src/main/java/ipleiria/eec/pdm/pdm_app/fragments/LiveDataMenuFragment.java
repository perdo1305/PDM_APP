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
import android.graphics.Color;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import ipleiria.eec.pdm.pdm_app.R;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

/**
 * Fragmento para exibir e gerenciar o menu de dados ao vivo.
 */
public class LiveDataMenuFragment extends Fragment {

    private TextView receivedString1Tv, receivedString2Tv;

    private TextView mStatusBlueTv, mPairedTv;
    private ListView lvNewDevices;
    private BluetoothAdapter mBlueAdapter;
    private ArrayList<String> mBTDevicesString = new ArrayList<>();
    private ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();


    private ActivityResultLauncher<Intent> enableBluetooth;
    private boolean isReceiverRegistered = false;

    private BluetoothSocket mBluetoothSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private boolean isReading = false;

    private LineChart lineChart;
    private LineDataSet rpmDataSet, kmhDataSet;
    private LineData lineData;
    private int lastX = 0;

    // UUID for the serial port service
    private static final UUID SERIAL_PORT_SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


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

    /**
     * Chamado para criar a hierarquia de views associada ao fragmento.
     *
     * @param inflater o LayoutInflater usado para inflar qualquer view no fragmento
     * @param container o ViewGroup pai ao qual a view do fragmento será anexada
     * @param savedInstanceState o estado salvo da instância
     * @return a view para o fragmento
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_data_menu, container, false);

        mStatusBlueTv = view.findViewById(R.id.statusBluetoothTv);
        mPairedTv = view.findViewById(R.id.pairedTv);
        lvNewDevices = view.findViewById(R.id.listViewDevices);

        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.GONE);

        // Initialize TextViews
        receivedString1Tv = view.findViewById(R.id.receivedString1);
        receivedString2Tv = view.findViewById(R.id.receivedString2);

        // Initialize LineChart
        lineChart = view.findViewById(R.id.lineChart);
        rpmDataSet = new LineDataSet(new ArrayList<>(), "RPM");
        kmhDataSet = new LineDataSet(new ArrayList<>(), "KM/H");
        lineData = new LineData(rpmDataSet, kmhDataSet);
        lineChart.setData(lineData);

// Set colors for the datasets
rpmDataSet.setColor(Color.parseColor("#88D498")); // Red color in hex
kmhDataSet.setColor(Color.parseColor("#F3E9D2")); // Blue color in hex

        // Customize LineChart
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE); // Set X axis text color to white

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE); // Set left Y axis text color to white

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

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

    /**
     * Inicializa o lançador de atividade para habilitar o Bluetooth.
     */
    private void initializeBluetoothLauncher() {
        enableBluetooth = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    showToast(getString(R.string.bluetooth_is_on));
                } else {
                    showToast(getString(R.string.could_not_turn_on_bluetooth));
                }
            }
        });
    }

    /**
     * Configura o status do Bluetooth.
     */
    private void setupBluetoothStatus() {
        if (mBlueAdapter == null) {
            mStatusBlueTv.setText(R.string.bluetooth_is_not_available);
        } else {
            mStatusBlueTv.setText(R.string.bluetooth_is_available);
        }
    }

    /**
     * Exibe uma mensagem Toast.
     *
     * @param msg a mensagem a ser exibida
     */
    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Verifica se as permissões de Bluetooth foram concedidas.
     *
     * @return true se as permissões foram concedidas, false caso contrário
     */
    private boolean hasBluetoothPermissions() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Verifica e solicita permissões de Bluetooth, se necessário.
     */
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

    /**
     * Manipula o clique no botão para desligar o Bluetooth.
     *
     * @param view a view que foi clicada
     */
    public void onClickOn(View view) {
        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.GONE);
        if (!hasBluetoothPermissions()) {
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1002);
            return;
        }
        if (!mBlueAdapter.isEnabled()) {
            showToast(getString(R.string.turning_on_bluetooth));
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetooth.launch(intent);
            mStatusBlueTv.setText("Bluetooth: ON");
        } else {
            showToast(getString(R.string.bluetooth_is_already_on));
            mStatusBlueTv.setText("Bluetooth: ON");
        }
    }

    /**
     * Manipula o clique no botão para descobrir dispositivos Bluetooth.
     *
     * @param view a view que foi clicada
     */
    @SuppressLint("MissingPermission")
    public void onClickOff(View view) {
        if (!hasBluetoothPermissions()) return;

        mPairedTv.setVisibility(View.GONE);
        lvNewDevices.setVisibility(View.GONE);
        if (mBlueAdapter.isEnabled()) {
            mBlueAdapter.disable();
            if (mBluetoothSocket != null && mBluetoothSocket.isConnected()) {
                stopReading();
            }
            showToast(getString(R.string.turning_bluetooth_off));
            mStatusBlueTv.setText("Bluetooth: OFF");
        } else {
            showToast(getString(R.string.bluetooth_is_already_off));
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

        mStatusBlueTv.setText("Bluetooth: Discovering devices...");
        showToast(getString(R.string.discovering_devices));
    }

    /**
     * Chamado quando o fragmento é iniciado.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (!isReceiverRegistered) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            requireContext().registerReceiver(mBroadcastReceiver, filter);
            isReceiverRegistered = true;
        }
    }

    /**
     * Chamado quando o fragmento é parado.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (isReceiverRegistered) {
            requireContext().unregisterReceiver(mBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    /**
     * Conecta-se a um dispositivo Bluetooth.
     *
     * @param device o dispositivo Bluetooth ao qual se conectar
     */
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

            mBluetoothSocket = device.createRfcommSocketToServiceRecord(SERIAL_PORT_SERVICE_UUID);
            mBluetoothSocket.connect();
            mInputStream = mBluetoothSocket.getInputStream();
            mOutputStream = mBluetoothSocket.getOutputStream();
            isReading = true;
            showToast(getString(R.string.connected_to) + device.getName());

            // Start a new thread to read data
            new Thread(this::readData).start();

        } catch (IOException e) {
            Log.e(getString(R.string.bluetooth), getString(R.string.connection_failed), e);
            showToast(getString(R.string.failed_to_connect_to) + device.getName());
        }
    }


    /**
     * Método para ler dados do dispositivo Bluetooth.
     */
    private void readData() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (isReading) {
            try {
                if (mInputStream.available() > 0) {
                    bytes = mInputStream.read(buffer);
                    String data = new String(buffer, 0, bytes);
                    Log.d("BluetoothData", "Received: " + data);

                    // Assuming the data is received as two strings separated by a comma
                    String[] receivedStrings = data.split(",");
                    if (receivedStrings.length >= 2) {
                        String string1 = receivedStrings[0].trim();
                        String string2 = receivedStrings[1].trim();

                        getActivity().runOnUiThread(() -> {
                            receivedString1Tv.setText("RPM: " + string1);
                            receivedString2Tv.setText("KM/H: " + string2);
                            addEntry(Double.parseDouble(string1), Double.parseDouble(string2));
                        });
                    }
                }
            } catch (IOException e) {
                Log.e("BluetoothData", "Error reading data", e);
                isReading = false;
            }
        }
    }


    /**
     * Método para parar de ler dados e fechar a conexão.
     */

    private void stopReading() {
        isReading = false;
        try {
            if (mInputStream != null) mInputStream.close();
            if (mOutputStream != null) mOutputStream.close();
            if (mBluetoothSocket != null) mBluetoothSocket.close();
        } catch (IOException e) {
            Log.e("BluetoothData", "Error closing connection", e);
        }
    }


/**
 * Método para atualizar o gráfico com novos dados.
 */
    private void addEntry(double rpmValue, double kmhValue) {
        rpmDataSet.addEntry(new Entry(lastX, (float) rpmValue));
        kmhDataSet.addEntry(new Entry(lastX++, (float) kmhValue));
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.setVisibleXRangeMaximum(50);
        lineChart.moveViewToX(lineData.getEntryCount());
    }

    // Method to update the graph with new data
//    private void addEntry(double value) {
//        series.appendData(new DataPoint(lastX++, value), true, 100);
//    }

    // Example method to handle Bluetooth data
//    private void onBluetoothDataReceived(double value) {
//        addEntry(value);
//    }

}

package com.example.bryan.androidcontrol;

/**
 * Created by Bryan on 1/28/2018.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Set;


public class BluetoothFragment extends Fragment {

    private static TextView deviceTypeTextView;
    private static TextView deviceSelectedTextView;

    private static Button showPairedDevicesBtn;
    private static Button showAvailableDevicesBtn;
    private static Button pairUnpairBtn;
    private static Button connectBtn;

    private static ListView bluetoothDevicesListView;

    public static ArrayAdapter<String> availableDevicesAdapter;
    public static ArrayAdapter<String> pairedDevicesAdapter;

    private static BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedBluetoothDevice;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                Log.v("MainActivity", "BroadcastReceiver device found");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                availableDevicesAdapter.add(device.getName()+"\n"+device.getAddress());

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                //Toast.makeText(getContext(), "Finish scanning for bluetooth devices", Toast.LENGTH_SHORT).show();
                if (availableDevicesAdapter.isEmpty()) {
                    availableDevicesAdapter.add("No bluetooth devices found");
                }
                deviceTypeTextView.setText("Available Bluetooth Devices");
                showAvailableDevicesBtn.setEnabled(true);
                showPairedDevicesBtn.setEnabled(true);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                //Toast.makeText(getContext(), "Scanning bluetooth devices", Toast.LENGTH_SHORT).show();
                availableDevicesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
                bluetoothDevicesListView.setAdapter(availableDevicesAdapter);
                deviceTypeTextView.setText("Scanning...");

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(getContext(), "Paired", Toast.LENGTH_SHORT);
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(getContext(), "Unpaired", Toast.LENGTH_SHORT);
                }
            }

            availableDevicesAdapter.notifyDataSetChanged();
        }
    };

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public BluetoothFragment() {}

    public static BluetoothFragment newInstance(BluetoothAdapter adapter) {
        BluetoothFragment fragment = new BluetoothFragment();
        bluetoothAdapter = adapter;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        bluetoothAdapter = MainActivity.bluetoothAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        deviceTypeTextView = view.findViewById(R.id.deviceTypeTextView);

        deviceSelectedTextView = view.findViewById(R.id.deviceSelectedTextView);

        pairUnpairBtn = view.findViewById(R.id.pairUnpairBtn);
        pairUnpairBtn.setEnabled(false);
        pairUnpairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedBluetoothDevice.getBondState()==BluetoothDevice.BOND_BONDED) {
                    unpairDevice(selectedBluetoothDevice);

                } else if (selectedBluetoothDevice.getBondState()==BluetoothDevice.BOND_NONE) {
                    pairDevice(selectedBluetoothDevice);
                }
                loadPairedDevices();

            }
        });

        connectBtn = view.findViewById(R.id.connectBtn);
        connectBtn.setEnabled(false);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectDevice(selectedBluetoothDevice);
            }
        });

        showPairedDevicesBtn = view.findViewById(R.id.showPairedDevicesBtn);
        showPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPairedDevices();
                showPairedDevicesBtn.setEnabled(false);
                pairUnpairBtn.setEnabled(false);
                connectBtn.setEnabled(false);
            }
        });

        showAvailableDevicesBtn = view.findViewById(R.id.showAvailableDevicesBtn);
        showAvailableDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                scanAvailableDevices();
                pairUnpairBtn.setEnabled(false);
                connectBtn.setEnabled(false);
            }
        });


        bluetoothDevicesListView = view.findViewById(R.id.bluetoothDevicesListView);
        bluetoothDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String[] deviceData = adapterView.getItemAtPosition(position).toString().split("\n");
                if (deviceData[0] != "No paired devices found" && deviceData[0] != "No bluetooth devices found") {

                    deviceSelectedTextView.setText("Device selected: " + deviceData[0]);
                    selectedBluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceData[1]);
                    pairUnpairBtn.setEnabled(true);

                    if (selectedBluetoothDevice.getBondState()==BluetoothDevice.BOND_BONDED) {
                        pairUnpairBtn.setText("Unpair");
                        connectBtn.setEnabled(true);
                    } else if (selectedBluetoothDevice.getBondState()==BluetoothDevice.BOND_NONE) {
                        pairUnpairBtn.setText("Pair");
                        connectBtn.setEnabled(false);
                    }
                }

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bluetoothAdapter!=null)
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        // getContext().unregisterReceiver(receiver);
    }


    private void scanAvailableDevices() {

        if (bluetoothAdapter!=null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }

            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            getActivity().registerReceiver(receiver, filter);

            bluetoothAdapter.startDiscovery();
        }
    }

    private void loadPairedDevices() {

        pairedDevicesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size()>0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesAdapter.add(device.getName()+"\n"+device.getAddress());
            }
        }

        // If there are no devices, add an item that states so. It will be handled in the view.
        if (pairedDevicesAdapter.isEmpty()) {
            pairedDevicesAdapter.add("No paired devices found");
        }

        bluetoothDevicesListView.setAdapter(pairedDevicesAdapter);
        deviceTypeTextView.setText("Paired Bluetooth Devices");
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectDevice(BluetoothDevice selectedBluetoothDevice) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.connectDevice(selectedBluetoothDevice);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    public interface OnFragmentInteractionListener {

        void connectDevice(BluetoothDevice selectedBluetoothDevice);

    }

}
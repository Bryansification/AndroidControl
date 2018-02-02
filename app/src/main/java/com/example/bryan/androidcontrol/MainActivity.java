package com.example.bryan.androidcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        BluetoothFragment.OnFragmentInteractionListener{

    // Intent request codes //
    //private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 3;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private FragmentTabHost mTabHost;
    private static BluetoothFragment bluetoothFragment;

    private static GridView gridView;
    private static String mdfExploredString = "";
    private static String mdfObstacleString = "";

    private static TextView bluetoothStatusTextView;

    public static boolean isWaypointToggled = false;
    public static boolean isAutoUpdateToggled = false;
    public static boolean isRobotToggled = false;

    public static String connectedDeviceName;

    public static BluetoothAdapter bluetoothAdapter;
    private static BluetoothChatService bluetoothService;

    private static StringBuffer inputStringBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("Bluetooth").setIndicator("Bluetooth", null),
                BluetoothFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Direction").setIndicator("Direction", null),
                DirectionFragment.class, null);



        gridView = (GridView) findViewById(R.id.mapGridView);

        bluetoothStatusTextView = (TextView) findViewById(R.id.bluetoothStatusTextView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the chat session
            } else {
                if (bluetoothService == null)
                    setupChat();
            }
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        if (bluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (bluetoothService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                bluetoothService.start();
            }
        }

    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onStop(){
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothService != null) {
            bluetoothService.stop();
        }
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment attachedFragment) {
        super.onAttachFragment(attachedFragment);

        if (attachedFragment.getClass().equals((BluetoothFragment.class))) {
            bluetoothFragment = (BluetoothFragment) attachedFragment;
        }
    }




    @Override
    public void connectDevice(BluetoothDevice device) {
        Log.v("MainActivity", "connectDevice()" + device.getName());
        // Get the device MAC address

        bluetoothService.connect(device, true);
    }
    public void sendMessage(String message) {

        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();

            bluetoothService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            inputStringBuffer.setLength(0);

        }
    }


    private void setupChat() {
        Log.v("MainActivity", "setupChat()");

        // Initialize the BluetoothChatService to perform bluetooth connections //
        bluetoothService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages //
        inputStringBuffer = new StringBuffer("");
    }

    private void setStatus(String status) {
        bluetoothStatusTextView.setText(status);

    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DEVICE_NAME:
                    // save the connected device name
                    connectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + connectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;

                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private String hexToBinary(String hex) {
        int pointer = 0;
        String binary = "";
        String partial;
        // 1 Hex digits each time to prevent overflow and recognize leading 0000
        while (hex.length() - pointer > 0) {
            partial = hex.substring(pointer, pointer + 1);
            String bin;
            if (!partial.equals("\n" )){
                bin = Integer.toBinaryString(Integer.parseInt(partial, 16));
            } else{
                bin = "";
            }
            for (int i = 0; i < 4 - bin.length(); i++) {
                binary = binary.concat("0");
            }
            binary = binary.concat(bin);
            pointer += 1;
        }
        return binary;
    }


}

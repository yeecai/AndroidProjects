package com.example.chattingui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AppCompatActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;

    private UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    UUID MY_UUID_INSECURE = UUID.fromString("dfasdf");

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMsgs();

        displayMsgs(this);


        // a litte test for if device support bluetooth


        if(bluetoothAdapter == null) {
            Log.i(TAG, "not supported");
        } else {
            Log.i(TAG, "supported");
        }

        // check whether bluetooth is enabled
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableInent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableInent,REQUEST_ENABLE_BT);
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // discover bluetooth
        Intent discoverableIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE
        );
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);


        //  connection

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryFinishReceiver, filter);

        // Pairing


    }




    private class  AcceptThread extends Thread {
        private final  BluetoothServerSocket serverSocket;
        private String socketType;
        private int state;



        public AcceptThread(boolean secure) {
            BluetoothServerSocket temp = null;
            socketType = secure ? "Secure" : "Insecure";


            try{
                if (secure) {
                    String NAME_SECURE = "so secure";
                    temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
                } else {
                    String NAME_INSECURE = "no secure";

                    temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            serverSocket = temp;
        }

     public void  run() {
            setName("AcceptThread" +socketType);

         BluetoothSocket socket = null;

         while (state != STATE_CONNECTED) {
             try{
                 socket = serverSocket.accept();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

     public void cancel() {
            try{
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
     }

    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket socket;

        private final BluetoothDevice device;

        private String socketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            this.device = device;
            BluetoothSocket tmp = null;
            socketType = secure ? "Secure" : "Insecure";

            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                } else {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            socket = tmp;
        }

        @Override
        public void run() {
            setName("ConnectThread" + socketType);

            // always cancel discovery cus slow down a connection
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread cus we're done
            synchronized (ChatService.this) {
                connectionThread = null;
            }

            connected(socket, device, socketType);

        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                /*if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
        //            newDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
               // if (newDevicesArrayAdapter.getCount() == 0) {
             //       String noDevices = getResources().getText(R.string.none_found).toString();
              //      newDevicesArrayAdapter.add(noDevices);
                }*/

                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
            }
        }

    };

    private void displayMsgs(Context context) {
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send_button);

        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");
                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("hi", Msg.TPYE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("oh hi, what's up.", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("All good, you?", Msg.TPYE_RECEIVED);
        msgList.add(msg3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(discoveryFinishReceiver);
    }
}


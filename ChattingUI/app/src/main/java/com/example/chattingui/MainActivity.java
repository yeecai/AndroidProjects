

package com.example.chattingui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

public class MainActivity extends AppCompatActivity {

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String TOAST = "toast";


    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    private static final String TAG = "AppCompatActivity";
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private StringBuffer outStringBuffer;
    private ChatService chatService = null;


    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_STATE_CHANGE :
                    switch (message.arg1) {
                        //set status in AppBar
                        case ChatService.STATE_CONNECTED:
                           // setStatus("Connected to ${device_name}");
                            break;
                        case ChatService.STATE_CONNECTING:
                            break;
                        case ChatService.STATE_LISTEN:
                            break;
                        case ChatService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) message.obj;
                    String readMessage = new String(readBuf, 0, message.arg1);
                    msgList.add(new Msg(readMessage, Msg.TPYE_RECEIVED));

                    inputText.setText("");
                    break;

                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) message.obj;

                    String writeMessage = new String(writeBuf);
                    msgList.add(new Msg(writeMessage,Msg.TYPE_SENT));
                    break;

                case MESSAGE_DEVICE_NAME:

                    //connectedDeviceName = message.getData().getString(MESSAGE_DEVICE_NAME);
                   // Toast.makeText(getApplicationContext(),"")
                    break;

                case MESSAGE_TOAST:
                    break;

            }
            return false;
        }
    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(bluetoothAdapter == null) {
            Toast.makeText(this,"Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initMsgs();

        displayMsgs(this);
    }

/*     // check whether bluetooth is enabled



        // discover bluetooth
        Intent discoverableIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE
        );
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);


        //  connection

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
      //  registerReceiver(discoveryFinishReceiver, filter);

        // Pairing
*/


/*
*  When is onActivityResult be called?? when startActivityForResult()
* */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                   setupChat();
                } else {
                    // TODO: make a toast that bt is not enabled
                    finish();
                }
                break;
            }
        }

    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(
                DeviceListActivity.DEVICE_ADDRESS);
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        chatService.connect(device, secure);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(!bluetoothAdapter.isEnabled()) {
            Intent enableInent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableInent,REQUEST_ENABLE_BT);
        } else {
            if(chatService == null) {
                setupChat();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( chatService != null) {
            if (chatService.getState() == ChatService.STATE_NONE) {
                try {
                    chatService.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setupChat() {
        chatService = new ChatService(this, handler);
        outStringBuffer = new StringBuffer("");
    }

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
      //  unregisterReceiver(discoveryFinishReceiver);
        if( chatService != null) {
            try {
                chatService.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


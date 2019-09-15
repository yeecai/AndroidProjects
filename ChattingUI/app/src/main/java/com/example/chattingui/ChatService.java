/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.example.chattingui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

public class ChatService {

    private ConnectThread connectThread;
    private int state;
    private ConnectedThread connectedThread;
    private final Handler handler;
    private AcceptThread secureAcceptThread;


    public ChatService(Context context, Handler handler) {
       // this.bluetoothAdapter = bluetoothAdapter;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        state = STATE_NONE;
        this.handler = handler;
    }

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE = UUID
            .fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE = UUID
            .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1; // listening connection
    public static final int STATE_CONNECTING = 2; // initiate outgoing
    public static final int STATE_CONNECTED = 3; // connected to remote device

    private final BluetoothAdapter bluetoothAdapter;

    
    public void connect(BluetoothDevice device, boolean secure){
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }
        
        if (connectedThread != null) {
            try {
                connectedThread.cancel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        connectThread = new ConnectThread(device, secure);
        setState(STATE_CONNECTING);
        
    }

    private void setState(int state) {
        this.state = state;


        handler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE, state, -1)
                .sendToTarget();

    }

    // connection


    public void write(byte[] out){
        ConnectedThread r;
        synchronized (this) {
            if (state != STATE_CONNECTED) {
                return;
            }
            r = connectedThread;
        }
        r.write(out);
    }

    public int getState() {
        return state;
    }

    public void stop() throws IOException {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (secureAcceptThread != null) {
            secureAcceptThread.cancel();
            secureAcceptThread = null;
        }
    }

    public void start() throws IOException {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        setState(STATE_LISTEN);

        if (secureAcceptThread == null) {
            secureAcceptThread = new AcceptThread(true);
            secureAcceptThread.start();
        }
    }

    // TODO connectionFailed() {}

    // TODO connectLost() {}
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
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
               // connectionFailed();
                return;
            }

            // Reset the ConnectThread cus we're done
            synchronized (ChatService.this) {
                //connectionThread = null;
            }

            connected(socket, device, socketType);

        }

        private void connected(BluetoothSocket socket, BluetoothDevice device, String socketType) {
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

    // read and write from connected remote device
    private class ConnectedThread extends Thread {

        private final BluetoothSocket socket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }


            inputStream = tmpIn;
            outputStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = inputStream.read(buffer);

                    handler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {

                /**/
                    try {
                        connectLost();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        ChatService.this.start();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                    break;
                }
            }

        }

        public void write(byte[] buffer/**/) {
            try {
                outputStream.write(buffer);
                handler.obtainMessage(MainActivity.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() throws IOException {
            socket.close();
        }
    }

    private void connectLost() throws IOException {
        Message message = handler.obtainMessage(MainActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Device connection is lost");
        message.setData(bundle);
        handler.sendMessage(message);
        ChatService.this.start();
    }



}

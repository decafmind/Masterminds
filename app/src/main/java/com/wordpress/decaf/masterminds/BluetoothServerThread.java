package com.wordpress.decaf.masterminds;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by decaf on 9/29/15.
 */
public class BluetoothServerThread extends Thread {
    private final BluetoothServerSocket mServerSocket;
    private final BluetoothAdapter mBluetoothAdapter;
    private static final String NAME = "";
    private UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothServerThread(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket tmp = null;

        try
        {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, DEFAULT_UUID);
        }catch(IOException ex){ }

        mServerSocket = tmp;
    }

    @Override
    public void run() {

        BluetoothSocket socket;

        while(true){
            try{
                socket = mServerSocket.accept();
            }catch(IOException ex){
                break;
            }

            try
            {
                if (socket != null){
                    manageConnectedSocket(socket);
                    mServerSocket.close();
                    break;
                }
            }catch(IOException ex){
                break;
            }
        }
    }

    public void cancel(){
        try{
            mServerSocket.close();
        }catch(IOException e){ }
    }

    private void manageConnectedSocket(BluetoothSocket socket){
        BluetoothConnectedThread bluetoothConnectedThread = new BluetoothConnectedThread(socket);
        bluetoothConnectedThread.run();
    }
}

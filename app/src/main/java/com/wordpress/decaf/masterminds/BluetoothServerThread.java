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
    private static final String NAME = "mastermind";
    private UUID MY_UUID = UUID.fromString("ca25edd6-5a21-45e9-8dea-9b57abe44cee");

    // TODO: 9/29/15
    public BluetoothServerThread(){

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket tmp = null;

        try
        {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        }catch(IOException ex){ }

        mServerSocket = tmp;
    }

    // TODO: 9/29/15
    @Override
    public void run() {
        BluetoothSocket socket = null;

        while(true){
            try{
                if(mServerSocket != null) socket = mServerSocket.accept();
            }catch(IOException iox){ break; }

            try{
                if (socket != null){
                    manageConnectedSocket(socket);
                    mServerSocket.close();
                    break;
                }
            }catch(IOException ex){ break; }
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

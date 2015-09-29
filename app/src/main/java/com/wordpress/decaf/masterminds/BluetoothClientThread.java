package com.wordpress.decaf.masterminds;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by decaf on 9/29/15.
 */
public class BluetoothClientThread extends Thread {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmBluetoothAdapter;
    private UUID MY_UUID = UUID.fromString("ca25edd6-5a21-45e9-8dea-9b57abe44cee");

    // TODO: 9/29/15
    public BluetoothClientThread(BluetoothDevice device){
        mmBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        }catch (IOException iox){ }

        mmSocket = tmp;
    }

    // TODO: 9/29/15
    @Override
    public void run(){
        mmBluetoothAdapter.cancelDiscovery();

        try{
            mmSocket.connect();
        } catch(IOException ex){
            try{
                mmSocket.close();
            }catch(IOException iex){

            }
        }

        manageConnectedSocket(mmSocket);
    }

    private void manageConnectedSocket(BluetoothSocket socket){
        BluetoothConnectedThread bluetoothConnectedThread = new BluetoothConnectedThread(socket);
        bluetoothConnectedThread.run();
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}

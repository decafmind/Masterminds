package com.wordpress.decaf.masterminds;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by decaf on 9/29/15.
 */
public class BluetoothClientThread extends Thread {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmBluetoothAdapter;
    private UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothClientThread(BluetoothDevice device){
        mmBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mmDevice = device;
        UUID uudi;

        try {
            // Use the UUID of the device that discovered // TODO Maybe need extra device object
            if (mmDevice != null){
                if (Build.VERSION.SDK_INT >= 15){
                    tmp = device.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
                }else{

                }
            }

        }
        catch (NullPointerException e)
        {
            try {
                tmp = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) { }

        mmSocket = tmp;
    }

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

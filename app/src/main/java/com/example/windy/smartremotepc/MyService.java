package com.example.windy.smartremotepc;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Windy on 16/04/2015.
 */
public class MyService extends Service {

    private final IBinder mBinder=new LocalBinder();

    private Socket socket=null;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private ObjectInputStream objectInputStream=null;
    private InputStream inputStream=null;
    private OutputStream outputStream=null;
    private String address=null;
    private int port=0;
    private UUID uuid=UUID.fromString("00001562-0000-1000-8000-00805F9B34FB");
    String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MyService","onBind");
        return mBinder;
    }

    public void setInfoServer(String inAddress,int inPort){
        address=inAddress;
        port=inPort;
    }

    public boolean isConnect(){
        if(socket==null)return false;
        return true;
    }

    public void stopConnect(){
        try {
            inputStream.close();
            outputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket onConnect(){
        if(address==null||port==0)return null;
        Log.i("MyService","connect to "+address+" on "+port);
        ConnectThread connectThread=new ConnectThread();
        new Thread(connectThread).start();
        return socket;
    }

    public void onBluetoothConnect(String mac){
        address=mac;
        Log.i("MyService","connect to mac "+address);
        ConnectThread connectThread=new ConnectThread();
        new Thread(connectThread).start();
    }

    public byte[] getScreen(){
        Log.i("MyService","get Screen");
        byte[] data=null;
        try {
            data= (byte[]) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void sendByte(int data){
        Log.i("MyService","send Byte");
        if(!checkConnect())return;
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] data){
        Log.i("MyService","send Message");
        if(!checkConnect())return;
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder {
        // Trả về đối tượng MyService để cho client có thể gọi public method
        MyService getService(){
            Log.i("MyService","get Service");
            return MyService.this;
        }
    }

    public boolean checkIsIPv4(String ip){
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        return pattern.matcher(ip).matches();
    }

    class ConnectThread implements Runnable{

        @Override
        public void run() {
            if(socket!=null){
                stopConnect();
            }
            try {
                //Bluetooth
                if(!checkIsIPv4(address)){
                    Log.i("Bluetooth Connect", "connect socket");
                    bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device=bluetoothAdapter.getRemoteDevice(address);
                    BluetoothSocket btSocket=device.createRfcommSocketToServiceRecord(uuid);
                    btSocket.connect();
                    outputStream=btSocket.getOutputStream();
                    inputStream=btSocket.getInputStream();
                    objectInputStream=new ObjectInputStream(inputStream);
                }
                else {
                    socket = new Socket(InetAddress.getByName(address), port);
                    socket.setTcpNoDelay(true);
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    objectInputStream = new ObjectInputStream(inputStream);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean checkConnect(){
        if(outputStream==null)return false;
        return true;
    }
}

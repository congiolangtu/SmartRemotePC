package com.example.windy.smartremotepc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Windy on 05/05/2015.
 */
public class BluetoothActivity extends Activity{
    BluetoothAdapter btAdapter=null;
    ListView listBluetooth=null;
    private static final int REQUEST_ENABLE_BT = 1;
    private ArrayAdapter<String> bluetoothArray=null;
    private ArrayList<BluetoothDevice> listDevice=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        listBluetooth= (ListView) findViewById(R.id.listB);
        listDevice=new ArrayList<BluetoothDevice>();
        bluetoothArray=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listBluetooth.setAdapter(bluetoothArray);
        Button btRefresh= (Button) findViewById(R.id.btRefreshB);
        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDevice();
            }
        });
        btAdapter=BluetoothAdapter.getDefaultAdapter();
        CheckBluetooth();
        findDevice();
        listBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=getIntent();
                intent.putExtra("Mac",listDevice.get(position).getAddress());
                setResult(1000,intent);
                finish();
            }
        });
    }
//
//    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                listDevice.add(device);
//                bluetoothArray.add(device.getName() + "\n" + device.getAddress());
//                bluetoothArray.notifyDataSetChanged();
//            }
//        }
//    };

    public void findDevice() {
//        if (btAdapter.isDiscovering()) {
//            btAdapter.cancelDiscovery();
//        }
//        else {
//            bluetoothArray.clear();
//            listDevice.clear();
//            btAdapter.startDiscovery();
//            registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
//        }
        bluetoothArray.clear();
        listDevice.clear();
        Set<BluetoothDevice> pairedDevice=btAdapter.getBondedDevices();
        for(BluetoothDevice device:pairedDevice){
            listDevice.add(device);
            bluetoothArray.add(device.getName() + "\n" + device.getAddress());
            bluetoothArray.notifyDataSetChanged();
        }
    }


    private void CheckBluetooth() {
        if(btAdapter==null) {
            Toast.makeText(this,"Bluetooth Not Support!",Toast.LENGTH_LONG);
        } else {
            if (btAdapter.isEnabled()) {
                Toast.makeText(this,"Bluetooth already!",Toast.LENGTH_SHORT);
            } else {
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }
}

package com.example.windy.smartremotepc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import Mode.CodeIntent;
import adapter.MenuItemPager;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {


    MenuItemPager mMenuItemPager;
    ViewPager mViewPager;
    MyService mService;
    String ipAddress=null;
    int port=0;

    public static final int MO_ADD_ITEM = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mMenuItemPager = new MenuItemPager(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.viewMain);
        mViewPager.setAdapter(mMenuItemPager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        Button connect= (Button) findViewById(R.id.btConnectMain);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(MainActivity.this,RemotePCActivity.class);
//                intent.putExtra("ipAddress",ipAddress);
//                intent.putExtra("port",port);
//                startActivity(intent);
                  Intent intent = new Intent(MainActivity.this, WifiConnectActivity.class);
                  startActivityForResult(intent, CodeIntent.REQUEST_WIFI_CONNECT);
            }
        });

        Button setting= (Button) findViewById(R.id.btSettingMain);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,KeyboardActivity.class);
                startActivity(intent);
            }
        });

        Button btHelp= (Button) findViewById(R.id.btHelpMain);
        btHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,EditButtonActivity.class);
                startActivity(intent);
            }
        });

        Button btBluetooth= (Button) findViewById(R.id.btBluetoothMain);
        btBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,BluetoothActivity.class);
                startActivityForResult(intent,1001);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.

        actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_app)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_form)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_game)).setTabListener(this));


//        Button bt=new Button(this);
//        bt.setText("trung");
//        bt.setBackgroundColor(Color.BLUE);
//        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        param.setMargins(50,50,50,50);
//        this.addContentView(bt, param);
//        bt.setPadding(5, 5, 10, 5);
//        bt.setX(300);bt.setY(300);bt.setScaleX(5);bt.setScaleY(10);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,MyService.class);
        if(bindService(intent, mConnection, Context.BIND_AUTO_CREATE))
            Log.i("MyService","bind accept");
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==CodeIntent.RESULT_WIFI_CONNECT){
            ipAddress=data.getStringExtra("ipAddress");
            port=data.getIntExtra("port",0);
            mService.setInfoServer(ipAddress,port);
            Log.i("MyServer","bindConnect");
            mService.onConnect();
        }
        if(resultCode==1000){
            String address=data.getStringExtra("Mac");
            Log.i("MyServer","connect BLuetooth on "+address);
            mService.onBluetoothConnect(address);
        }
    }
}

package com.example.windy.smartremotepc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Windy on 24/11/2015.
 */
public class SlideShowActivity extends Activity{

    MyService mService=null;
    Button btLaser=null;
    Button btBack=null;
    Button btNext=null;
    EditText etWidth;
    EditText etHeight;
    int xMouse;
    int yMouse;
    long timeMove=0;
    float ratioWidthLaserScreen;
    float ratioHeightLaserScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        btLaser= (Button) findViewById(R.id.btLaser);
        btBack= (Button) findViewById(R.id.btBackSlide);
        btNext= (Button) findViewById(R.id.btNextSlide);
        etWidth= (EditText) findViewById(R.id.editTextWidth);
        etHeight= (EditText) findViewById(R.id.editTextHeight);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] message = new byte[4];
                message[0] = (byte) 254;
                message[1] = (byte) 37;
                message[2] = (byte) 253;
                message[3] = (byte) 37;
                mService.sendMessage(message);
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] message=new byte[4];
                message[0]=(byte)254;
                message[1]=(byte)39;
                message[2]=(byte)253;
                message[3]=(byte)39;
                mService.sendMessage(message);
            }
        });
        btLaser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN: {
                        timeMove=0;
                        ratioWidthLaserScreen = ((float)Integer.valueOf(etWidth.getText().toString())) / ((float)btLaser.getWidth());
                        ratioHeightLaserScreen = ((float)Integer.valueOf(etHeight.getText().toString())) / ((float)btLaser.getHeight());
                        xMouse = (int) (event.getX() * ratioWidthLaserScreen);
                        yMouse = (int) (event.getY() * ratioHeightLaserScreen);
                        byte[] message = new byte[14];
                        message[0] = (byte) 254;
                        message[1] = (byte) 17;
                        message[2] = (byte) 254;
                        message[3] = (byte) 76;
                        message[4] = (byte) 253;
                        message[5] = (byte) 76;
                        message[6] = (byte) 253;
                        message[7] = (byte) 17;
                        message[8] = (byte) 252;
                        message[9] = (byte) 11;
                        message[10] = (byte) (xMouse >> 8);
                        message[11] = (byte) xMouse;
                        message[12] = (byte) (yMouse >> 8);
                        message[13] = (byte) yMouse;
                        mService.sendMessage(message);
                    }
                        break;
                    case MotionEvent.ACTION_MOVE: {
                        long time=System.currentTimeMillis();
                        if(time-timeMove>16)timeMove=time;
                        else break;
                        xMouse = (int) (event.getX() * ratioWidthLaserScreen);
                        yMouse = (int) (event.getY() * ratioHeightLaserScreen);
                        byte[] message = new byte[6];
                        message[0] = (byte) 252;
                        message[1] = (byte) 11;
                        message[2] = (byte) (xMouse >> 8);
                        message[3] = (byte) xMouse;
                        message[4] = (byte) (yMouse >> 8);
                        message[5] = (byte) yMouse;
                        mService.sendMessage(message);
                    }
                    break;
                    case MotionEvent.ACTION_UP:{
                        int x=Integer.valueOf(etWidth.getText().toString());
                        int y=Integer.valueOf(etHeight.getText().toString());
                        byte[] message = new byte[14];
                        message[0] = (byte) 252;
                        message[1] = (byte) 11;
                        message[2] = (byte) (x >> 8);
                        message[3] = (byte) x;
                        message[4] = (byte) (y >> 8);
                        message[5] = (byte) y;
                        message[6] = (byte) 254;
                        message[7] = (byte) 17;
                        message[8] = (byte) 254;
                        message[9] = (byte) 76;
                        message[10] = (byte) 253;
                        message[11] = (byte) 76;
                        message[12] = (byte) 253;
                        message[13] = (byte) 17;
                        mService.sendMessage(message);
                    }

                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,MyService.class);
        if(bindService(intent, mConnection, Context.BIND_AUTO_CREATE))
            Log.i("MyService", "bind accept 1");
        else Log.i("MyService","bind fault 1");
    }



    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i("RemotePC","onServiceConnected");
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };
}

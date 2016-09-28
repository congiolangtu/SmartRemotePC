package com.example.windy.smartremotepc.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.windy.smartremotepc.mode.CodeIntent;

import com.example.windy.smartremotepc.MyService;
import com.example.windy.smartremotepc.R;
import com.example.windy.smartremotepc.item.MyKeyboard;
import com.example.windy.smartremotepc.item.MyKeyboardView;

/**
 * Created by Windy on 01/04/2015.
 */
 public class KeyboardActivity extends Activity {

    private MyKeyboardView kbv=null;
    private MyKeyboard kb=null;
    private MyKeyboard kbFn=null;
    private int keycodeFirst=0;
    MyService mService=null;
    View rootView=null;
    long firstTime=0;
    long lastTime=0;
    long timeMove=0;
    static final long DOUBLE_CLICK_TIME=200;
    static final int NONE=0;
    static final int ONE=1;
    static final int DOUBLE_CLICK=2;
    static final int DOUBLE_MOVE=3;
    static final int MOVE=4;
    PointF startPoint;
    PointF midPoint;
    int mode=NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.keybroad, null);
        setContentView(rootView);
        kbv = new MyKeyboardView(this,null);
        RelativeLayout.LayoutParams param=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        kbv.setLayoutParams(param);
        RelativeLayout relativeLayout= (RelativeLayout) findViewById(R.id.root_keyboard);
        relativeLayout.addView(kbv);
        kb = new MyKeyboard(this, R.xml.qwerty);
        kbFn = new MyKeyboard(this, R.xml.fn);
        kbv.setKeyboard(kb);
        kbv.invalidateAllKeys();
        startPoint=new PointF();
        midPoint=new PointF();
        Button btLeft= (Button) findViewById(R.id.leftMouse);
        btLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        byte[] data = new byte[2];
                        data[0] = (byte) 252;
                        data[1] = (byte) 1;
                        mService.sendMessage(data);
                        break;
                    case MotionEvent.ACTION_UP:
                        byte[] data2 = new byte[2];
                        data2[0] = (byte) 252;
                        data2[1] = (byte) 2;
                        mService.sendMessage(data2);
                        break;
                }
                return false;
            }
        });
        Button btRight=(Button)findViewById(R.id.rightMouse);
        btRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        byte[] data = new byte[2];
                        data[0] = (byte) 252;
                        data[1] = (byte) 3;
                        mService.sendMessage(data);
                        break;
                    case MotionEvent.ACTION_UP:
                        byte[] data2 = new byte[2];
                        data2[0] = (byte) 252;
                        data2[1] = (byte) 4;
                        mService.sendMessage(data2);
                        break;
                }
                return false;
            }
        });
        setTouchControl();
    }

    //Chuyển Keyboard0p-;
    private void switchKeyboard(){
        if (kbv.getKeyboard()==kb)
            kbv.setKeyboard(kbFn);
        else kbv.setKeyboard(kb);
        kbv.invalidateAllKeys();
    }

    private void result(byte[] data){
        Intent intent=getIntent();
        intent.putExtra("code",data);
        setResult(CodeIntent.RESULT_KEYBOARD,intent);
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
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,MyService.class);
        if(bindService(intent, mConnection, Context.BIND_AUTO_CREATE))
            Log.i("MyService","bind accept 1");
        else Log.i("MyService","bind fault 1");
    }

    public void setTouchControl(){

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchPad(event);
                return true;
            }
        });
        kbv.setOnKeyboardActionListener(new MyKeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {
                keycodeFirst = primaryCode;
                //message press key
                if(primaryCode>0){
                    byte[] message=new byte[2];
                    message[0]=(byte)254;
                    message[1]=(byte)primaryCode;
                    mService.sendMessage(message);
                }
                for (MyKeyboard.Key key : kbv.getKeyboard().getKeys()) {
                    if (key.codes[0] == primaryCode) {
                        Log.i("key",key.x+" - "+key.y+" | "+key.width+" - "+key.height);
                    }
                }
                Log.i("Keyboard", "press " + primaryCode);
            }

            @Override
            public void onRelease(int primaryCode) {
                if(keycodeFirst>0){
                    byte[] message=new byte[2];
                    message[0]=(byte)253;
                    message[1]=(byte)primaryCode;
                    mService.sendMessage(message);
                }
                if (primaryCode != keycodeFirst) return;
                if (primaryCode == -1) switchKeyboard();
                Log.i("Keyboard", "release " + primaryCode);
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                String string="";
                for(int a:keyCodes)string=string+a;
                Log.i("Keyboard", "onKey " + primaryCode+" : "+string);
            }

            @Override
            public void onText(CharSequence text) {
                Log.i("Keyboard", "onText " + text);
            }

            @Override
            public void swipeLeft() {
                switchKeyboard(); Log.i("Keyboard", "swipe left ");
            }

            @Override
            public void swipeRight() {
                switchKeyboard();Log.i("Keyboard", "swipe right ");
            }

            @Override
            public void swipeDown() {Log.i("Keyboard", "swipe left ");
            }

            @Override
            public void swipeUp() {Log.i("Keyboard", "swipe up ");
            }
        });

    }

    //xử lý event touch touchPad
    private void touchPad(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                firstTime= System.currentTimeMillis();
                startPoint.set(event.getX(), event.getY());
                if(firstTime-lastTime<=DOUBLE_CLICK_TIME) {
                    mode = DOUBLE_CLICK;
                    byte[] data = new byte[2];
                    data[0] = (byte) 252;
                    data[1] = (byte) 1;
                    mService.sendMessage(data);
                }
                else
                    mode = ONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(mode==ONE)
                    mode=DOUBLE_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                lastTime=System.currentTimeMillis();
                if(mode==ONE){
                    Handler myHandler = new Handler() {
                        public void handleMessage(Message m) {
                            if (mode == NONE) {
                                byte[] data = new byte[4];
                                data[0] = (byte) 252;
                                data[1] = (byte) 1;
                                data[2] = (byte) 252;
                                data[3] = (byte) 2;
                                mService.sendMessage(data);
                            }
                        }
                    };
                    Message m = new Message();
                    myHandler.sendMessageDelayed(m, DOUBLE_CLICK_TIME);
                 }
            case MotionEvent.ACTION_POINTER_UP:
                if(mode==DOUBLE_CLICK){
                    byte[] data=new byte[2];
                    data[0]=(byte)252;
                    data[1]=(byte)2;
                    mService.sendMessage(data);
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                long time=System.currentTimeMillis();
                if(time-timeMove>100)timeMove=time;
                else break;
                if(mode==MOVE||mode==DOUBLE_CLICK){
                    midPoint.set(event.getX(),event.getY());
                    byte x=(byte)(midPoint.x-startPoint.x);
                    byte y=(byte)(midPoint.y-startPoint.y);
                    if((x|y)!=0) {
                        byte[] data = new byte[4];
                        data[0] = (byte) 252;
                        data[1] = (byte) 10;
                        data[2] = x;
                        data[3] = y;
                        mService.sendMessage(data);
                    }
                    startPoint.set(midPoint);
                }
                else if(mode==DOUBLE_MOVE){
                    midPoint.set(event.getX(),event.getY());
                    byte y=(byte)(midPoint.y-startPoint.y);
                    if(y!=0) {
                        byte[] data = new byte[3];
                        data[0] = (byte) 252;
                        data[1] = (byte) 7;
                        data[2] = y;
                        mService.sendMessage(data);
                    }
                    startPoint.set(midPoint);
                }
                else if(mode==ONE){
                    float x=event.getX() - startPoint.x;
                    float y=event.getY() - startPoint.y;
                    if(Math.abs(x)>2|Math.abs(y)>2) {
                        mode = MOVE;
                    }
                }
                break;
        }
    }

}

package com.example.windy.smartremotepc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import Mode.CodeIntent;

/**
 * Created by Windy on 19/03/2015.
 */
public class RemotePCActivity extends Activity {

    ImageView screen;
    Button btMenu, btKeyboard;
    Handler handler=null;
    Bitmap bmp=null;
    MyService mService=null;
    boolean stop=true;
    boolean connectService=false;
    ReceiveScreen receiveScreen=null;
    Thread threadReceive;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int CLICK = 3;
    static final int DOUBLE_CLICK = 4;
    static final int DOUBLE_MOVE = 5;
    static final int LONG_CLICK=6;
    static final int ONE=11;
    static final int TWO=12;
    boolean DOUBLE_CLICK_MOVE=false;
    static final long DOUBLE_CLICK_TIME=200;
    int mode = NONE;
    float[] valueMatrix=new float[9];
    float screenWidth=0;
    float screenHeight =0;
    float screenX=0;
    float screenY=0;
    int heightDScreen=480;
    int widthDScreen=800;
    float xM=0;
    float yM=0;
    long firstTime=0;
    long lastTime=0;
    long timeMove=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout);
        screen= (ImageView) findViewById(R.id.screenPC);
        screen.setScaleType(ImageView.ScaleType.MATRIX);
        screen.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                screen.getViewTreeObserver().removeOnPreDrawListener(this);
                screenWidth = (float) screen.getWidth();
                screenHeight = (float) screen.getHeight();
                screenX=screen.getX();
                screenY=screen.getY();
                matrix = screen.getImageMatrix();
                repairScreen();
                screen.setImageMatrix(matrix);
                return true;
            }
        });
        btMenu= (Button) findViewById(R.id.menuPC);
        btKeyboard = (Button) findViewById(R.id.btKeyboard);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.sendByte(250);
                stop=false;
                finish();
            }
        });
        btKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKB=new Intent(RemotePCActivity.this,KeyboardActivity.class);
                startActivityForResult(intentKB, CodeIntent.REQUEST_KEYBOARD);
            }
        });
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                screenTouch(event);
               return true;
            }
        });


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("handler receive screen", "screen");
                screen.setImageBitmap(bmp);
                Log.d("handler receive screen2", "screen "+bmp.getByteCount());
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,MyService.class);
        if(bindService(intent, mConnection, Context.BIND_AUTO_CREATE))
            Log.i("MyService","bind accept 1");
        else Log.i("MyService","bind fault 1");
        receiveScreen=new ReceiveScreen();
        threadReceive = new Thread(receiveScreen);
        threadReceive.start();
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

    @Override
    protected void onStop() {
        super.onStop();
        stop=false;
    }

    public void screenTouch(final MotionEvent event){
        Log.i("touch","touch");
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                firstTime= System.currentTimeMillis();
                savedMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                if(firstTime-lastTime<=DOUBLE_CLICK_TIME) {
                    mode = DOUBLE_CLICK;
                    Point point=getLocationMouse(event);
                    if(point.x!=-1) {
                        byte[] data = new byte[8];
                        data[0] = (byte) 252;
                        data[1] = (byte) 11;
                        data[2] = (byte) (point.x >> 8);
                        data[3] = (byte) (point.x);
                        data[4] = (byte) (point.y >> 8);
                        data[5] = (byte) (point.y);
                        data[6] = (byte) 252;
                        data[7] = (byte) 1;
                        mService.sendMessage(data);
                    }
                }
                else
                    mode = ONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(mode==ONE)
                    mode=TWO;
                else
                    break;
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(midPoint, event);
                }
                else
                    mode=NONE;
                break;
            case MotionEvent.ACTION_UP:
                lastTime=System.currentTimeMillis();
                if(mode==ONE){
                    if(lastTime-firstTime>DOUBLE_CLICK_TIME){
                        Log.i("touch", "long click");
                        Point point=getLocationMouse(event);
                        if(point.x!=-1) {
                            byte[] data = new byte[10];
                            data[0] = (byte) 252;
                            data[1] = (byte) 11;
                            data[2] = (byte) (point.x >> 8);
                            data[3] = (byte) (point.x);
                            data[4] = (byte) (point.y >> 8);
                            data[5] = (byte) (point.y);
                            data[6] = (byte) 252;
                            data[7] = (byte) 3;
                            data[8] = (byte) 252;
                            data[9] = (byte) 4;
                            mService.sendMessage(data);
                        }
                    }
                    else {
                        final Point point=getLocationMouse(event);
                        Handler myHandler = new Handler() {
                            public void handleMessage(Message m) {
                                if (mode == NONE) {
                                    Log.i("touch", "click");
                                    if(point.x!=-1) {
                                        byte[] data = new byte[10];
                                        data[0] = (byte) 252;
                                        data[1] = (byte) 11;
                                        data[2] = (byte) (point.x >> 8);
                                        data[3] = (byte) (point.x);
                                        data[4] = (byte) (point.y >> 8);
                                        data[5] = (byte) (point.y);
                                        data[6] = (byte) 252;
                                        data[7] = (byte) 1;
                                        data[8] = (byte) 252;
                                        data[9] = (byte) 2;
                                        mService.sendMessage(data);
                                    }
                                }
                            }
                        };
                        Message m = new Message();
                        myHandler.sendMessageDelayed(m, DOUBLE_CLICK_TIME);
                    }
                }
                break;
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
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                }
                else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
                else if(mode==DOUBLE_CLICK){
                    Log.i("touch", "double click");
                    Point point=getLocationMouse(event);
                    if(point.x!=-1) {
                        byte[] data = new byte[6];
                        data[0] = (byte) 252;
                        data[1] = (byte) 11;
                        data[2] = (byte) (point.x >> 8);
                        data[3] = (byte) (point.x);
                        data[4] = (byte) (point.y >> 8);
                        data[5] = (byte) (point.y);
                        mService.sendMessage(data);
                    }
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
                    Log.i("touch", "drag");
                    float x=event.getX() - startPoint.x;
                    float y=event.getY() - startPoint.y;
                    if(Math.abs(x)>2||Math.abs(y)>2) {
                        mode = DRAG;
                        matrix.set(savedMatrix);
                        matrix.postTranslate(x, y);
                    }
                }
                else if(mode==TWO){
                    float newDist=spacing(event);
                    if(newDist>10f){
                        if(Math.abs(newDist-oldDist)<1){
                            Log.i("touch", "double move");
                            mode=DOUBLE_MOVE;
                            midPoint.set(event.getX(),event.getY());
                            byte[] data=new byte[3];
                            data[0]=(byte)252;
                            data[1]=(byte)7;
                            data[2]=(byte)(midPoint.y-startPoint.y);
                            mService.sendMessage(data);
                            startPoint.set(midPoint);
                        }
                        else{
                            Log.i("touch", "zoom");
                            mode=ZOOM;
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                }
                break;
        }
        repairScreen();
        screen.setImageMatrix(matrix);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    private boolean repairScreen(){
        matrix.getValues(valueMatrix);
        float x=valueMatrix[Matrix.MTRANS_X];
        float y=valueMatrix[Matrix.MTRANS_Y];
        float heightImg=heightDScreen*valueMatrix[Matrix.MSCALE_Y];
        float widthImg=widthDScreen*valueMatrix[Matrix.MSCALE_X];
        if(widthImg<screenWidth){
            x=(screenWidth-widthImg)/2;
        }
        else if(x>0)
            x=0;
        else if((widthImg-screenWidth+x)<0)
            x=screenWidth-widthImg;
        if(heightImg< screenHeight){
            y=(screenHeight -heightImg)/2;
        }
        else if(y>0)
             y=0;
        else if((heightImg-screenHeight+y)<0)
             y=screenHeight-heightImg;
        x=x-valueMatrix[Matrix.MTRANS_X];
        y=y-valueMatrix[Matrix.MTRANS_Y];
        if(x==0&&y==0)return false;
        matrix.postTranslate(x,y);
        return true;
    }
    private Point getLocationMouse(MotionEvent event){
        screen.getImageMatrix().getValues(valueMatrix);
        int x=(int)((event.getX()-valueMatrix[Matrix.MTRANS_X])/valueMatrix[Matrix.MSCALE_X]);
        int y=(int)((event.getY()-screenY-valueMatrix[Matrix.MTRANS_Y])/valueMatrix[Matrix.MSCALE_Y]);
        if(x<0||x>widthDScreen||y<0||y>heightDScreen)x=-1;
        Point point=new Point(x,y);
        Log.i("Mouse location",x+" - "+y);
        return point;
    }


    class ReceiveScreen implements Runnable{

        ReceiveScreen() {

        }

        @Override
        public void run() {
            int numberPacket = 2;
            byte[] data = null;
            Log.i("ReceiveScreen","run");
            while(mService==null){
                SystemClock.sleep(100);
            }
            mService.sendByte(255);
            while (stop) {
                Log.i("ReceiveScreen","run while");
                data = mService.getScreen();
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                heightDScreen=bmp.getHeight();
                widthDScreen=bmp.getWidth();
                handler.sendMessage(handler.obtainMessage());
                if (++numberPacket > 4) {
                    mService.sendByte(255);
                    numberPacket = 0;
                }
            }
        }
    }
}

package com.example.windy.smartremotepc;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Mode.CodeIntent;
import Mode.Item;
import Mode.ModeButton;
import myItem.MyButton;

import static android.view.View.OnTouchListener;

/**
 * Created by Windy on 06/03/2015.
 */
public class AbsoluteActivity extends Activity implements OnTouchListener {

    public Item item=null;
    View rootview=null;
    private SensorManager sensorManager;
    private Sensor sensor;
    public int heightPad=0;
    public int widthPad=0;
    TextView textView;
    public float xTouchSave=0;
    public float yTouchSave=0;
    ArrayList<ModeButton> listButton=null;
    ModeButton buttonTouch=null;
    public long timeTouch=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getIntent().getBundleExtra("item").getSerializable("item");
        listButton = new ArrayList<ModeButton>();
        setIndexItem();
        if (item.isTitleBar()) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setIcon(item.getIcon());
                actionBar.setTitle(item.getName());
            }
        }
        //3 chế độ (chơi, create, chỉnh sửa button)
        if (true) {

        }
        rootview = new RelativeLayout(this);
        if (rootview != null) rootview.setOnTouchListener(this);
        rootview.post(new Runnable() {
            @Override
            public void run() {
                heightPad = rootview.getHeight();
                widthPad = rootview.getWidth();
            }
        });
        this.addContentView(rootview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    public void onClickColor(View v){
        Log.i("on Click","t");
    }

    void setIndexItem(){
        if(item.isWhiteTheme()){
            if(item.isTitleBar()){
                setTheme(android.R.style.Theme_Holo_Light);
            }
            else {
                setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
            }
        }
        else{
            if(item.isTitleBar()){
                setTheme(android.R.style.Theme_Holo);
            }
            else {
                setTheme(android.R.style.Theme_Holo_NoActionBar);
            }
        }
        if(item.isFullScreen())getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v!=null) {
            Log.i("on Touch", "View " + v.getClass().getSimpleName());
        }
        if(ModeButton.class.isInstance(v)){
            //sự kiện khi chạm vào button
            return ((ModeButton)v).touchListen(event);
        }
        else if(RelativeLayout.class.isInstance(v)){
            if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_UP){
                //nếu tgian chạm vào layout <200 thì thêm button
                if(System.currentTimeMillis()-timeTouch>200)return true;
                Intent intent=new Intent(this,EditButtonActivity.class);
                intent.putExtra("width",widthPad);intent.putExtra("height",heightPad);
                startActivityForResult(intent,1);
                xTouchSave=event.getX();yTouchSave=event.getY();
            }
            else if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_DOWN){
                //lấy thời gian kiểm tra có phải chạm nhanh không!
                timeTouch=System.currentTimeMillis();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            MyButton myButton = new MyButton(this);
            listButton.add(myButton);
            ((RelativeLayout)rootview).addView(myButton);
            int height=data.getIntExtra("height",100);
            int width=data.getIntExtra("width",100);
            myButton.setHeight(height);
            myButton.setWidth(width);
            myButton.setName(data.getStringExtra("name"));
            myButton.setX(xTouchSave-width/2);myButton.setY(yTouchSave-height/2);
            myButton.setOnTouchListener(this);
        }
    }
}

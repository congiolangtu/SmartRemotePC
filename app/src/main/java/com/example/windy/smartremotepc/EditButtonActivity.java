package com.example.windy.smartremotepc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Mode.FunctionItem;
import Mode.ModeButton;
import adapter.ListFunctionAdapter;
import myItem.MyButton;

/**
 * Created by Windy on 12/02/2016.
 */
public class EditButtonActivity extends Activity{

    LinearLayout rootView;
    FrameLayout viewItem;
    ImageButton backType,nextType,icon1,icon2;
    TextView zoomOut,touchView,textType,editName;
    CheckBox check1,check2,check3;
    Button btOk,btCancel;
    ListView listFunction;
    View pickColor=null;
    View viewPickColor;
    ImageButton iconIsPick;
    ModeButton buttonChange=null;
    MyButton myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_button);
        findViewAll();
        Log.i("test screen", Configuration.SCREEN_WIDTH_DP_UNDEFINED+"");
        final ArrayList<FunctionItem> arrayItem= new ArrayList<>();
        arrayItem.add(new FunctionItem(R.drawable.menu0));
        arrayItem.add(new FunctionItem(R.drawable.menu1));
        arrayItem.add(new FunctionItem(R.drawable.menu2));
        arrayItem.add(new FunctionItem(R.drawable.menu3));
        arrayItem.add(new FunctionItem(R.drawable.menu4));
        listFunction.post(new Runnable() {
            @Override
            public void run() {
                listFunction.setAdapter(new ListFunctionAdapter(EditButtonActivity.this,R.layout.choose_function_button,arrayItem));
            }
        });
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconIsPick=icon1;
                int[] location1={0,0};
                int[] location2={0,0};
                icon1.getLocationInWindow(location1);
                pickColor.getLocationInWindow(location2);
                pickColor.setX(pickColor.getX()+location1[0]-location2[0]);
                pickColor.setY(pickColor.getY()+location1[1]-location2[1]+icon1.getHeight());
                viewPickColor.setVisibility(View.VISIBLE);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconIsPick=icon2;
                int[] location1={0,0};
                int[] location2={0,0};
                icon2.getLocationInWindow(location1);
                pickColor.getLocationInWindow(location2);
                pickColor.setX(pickColor.getX()+location1[0]-location2[0]);
                pickColor.setY(pickColor.getY()+location1[1]-location2[1]+icon2.getHeight());
                viewPickColor.setVisibility(View.VISIBLE);
            }
        });
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("test touch",event.getX()+" - "+event.getY());
                return true;
            }
        });
        zoomOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("test touch",event.getX()+" - "+event.getY());
                return false;
            }
        });
        myButton=new MyButton(this);
        myButton.setMode(ModeButton.MODE_VIEW_RESIZE_CREATE);
        myButton.setHeight(100);myButton.setWidth(200);
        viewItem.addView(myButton,new FrameLayout.LayoutParams(myButton.getWidthButton(),myButton.getHeightButton(), Gravity.CENTER));
        myButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return myButton.touchListen(event);
            }
        });
        buttonChange=myButton;
        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
    }


    void findViewAll(){
        rootView = (LinearLayout) findViewById(R.id.layoutRootEB);
        viewItem = (FrameLayout) findViewById(R.id.viewItemEB);
        backType = (ImageButton) findViewById(R.id.backTypeEB);
        nextType = (ImageButton) findViewById(R.id.nextTypeEB);
        icon1 = (ImageButton) findViewById(R.id.icon1EB);
        icon2 = (ImageButton) findViewById(R.id.icon2EB);
        zoomOut = (TextView) findViewById(R.id.zoomOutEB);
        touchView = (TextView) findViewById(R.id.touchViewEB);
        textType = (TextView) findViewById(R.id.textTypeEB);
        editName = (TextView) findViewById(R.id.editNameEB);
        check1 = (CheckBox) findViewById(R.id.check1EB);
        check2 = (CheckBox) findViewById(R.id.check2EB);
        check3 = (CheckBox) findViewById(R.id.check3EB);
        btOk = (Button) findViewById(R.id.btOkEB);
        btCancel = (Button) findViewById(R.id.btCancelEB);
        listFunction = (ListView) findViewById(R.id.listFunctionEB);
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                buttonChange.setName(s+"");
                buttonChange.invalidate();
            }
        });
        icon1.post(new Runnable() {
            @Override
            public void run() {
                int height = editName.getHeight()+20;
                icon1.getLayoutParams().height = height;
                icon1.getLayoutParams().width=height;
                icon2.getLayoutParams().height = height;
                icon2.getLayoutParams().width=height;
            }
        });
        if(getResources().getConfiguration().orientation==android.content.res.Configuration.ORIENTATION_LANDSCAPE)
            rootView.setOrientation(LinearLayout.HORIZONTAL);
        else
            rootView.setOrientation(LinearLayout.VERTICAL);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPickColor=vi.inflate(R.layout.pick_icon_color,null);
        EditButtonActivity.this.addContentView(viewPickColor, params);
        pickColor =findViewById(R.id.viewPIC);
        for(int i=0; i<3; i++) {
            View nextChild=((ViewGroup) pickColor).getChildAt(i);
            for (int j = 0; j < 3; j++) {
                ((ViewGroup) nextChild).getChildAt(j).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iconIsPick.setImageDrawable((ColorDrawable) ((ImageButton) v).getDrawable());
                        }
                    });
            }
        }
        ((ViewGroup) pickColor).getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditButtonActivity.this, "get Image", Toast.LENGTH_SHORT).show();
            }
        });
        ((SeekBar)((ViewGroup) pickColor).getChildAt(4)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        viewPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPickColor.setVisibility(View.INVISIBLE);
            }
        });
        viewPickColor.setVisibility(View.INVISIBLE);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=getIntent();
                data.putExtra("name",myButton.getName());
                data.putExtra("width",myButton.getWidth());
                data.putExtra("height",myButton.getHeight());
                setResult(1,data);
                finish();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

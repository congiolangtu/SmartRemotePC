package com.example.windy.smartremotepc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import Mode.DimenPer;
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
    MyButton myButton;
    int widthPad=480,heightPad=800;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_button);
        findViewAll();
        widthPad=getIntent().getIntExtra("width",widthPad);
        heightPad=getIntent().getIntExtra("height",heightPad);
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
        listFunction.setDividerHeight(DimenPer.getToHeight(0.02f)-10);
        int padding=DimenPer.getToHeight(0.01f);
        ((LinearLayout.LayoutParams)listFunction.getLayoutParams()).setMargins(padding,padding,padding,padding);
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconIsPick=icon1;
                int[] location1={0,0};
                int[] location2={0,0};
                icon1.getLocationInWindow(location1);
                pickColor.getLocationInWindow(location2);
                pickColor.setX(pickColor.getX()+location1[0]-location2[0]-10);
                pickColor.setY(pickColor.getY()+location1[1]-location2[1]+icon1.getHeight()-10);
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
                pickColor.setX(pickColor.getX()+location1[0]-location2[0]-10);
                pickColor.setY(pickColor.getY()+location1[1]-location2[1]+icon2.getHeight()-10);
                viewPickColor.setVisibility(View.VISIBLE);
            }
        });
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        zoomOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        myButton=new MyButton(this);
        myButton.setLayoutParams(new FrameLayout.LayoutParams(myButton.getWidthButton(),myButton.getHeightButton(), Gravity.CENTER));
        myButton.setMode(ModeButton.MODE_VIEW_RESIZE_CREATE);
        myButton.setPad(widthPad,heightPad);
        myButton.setPerHeight(0.2f);myButton.setPerWidth(0.2f);
        viewItem.addView(myButton);
        myButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return myButton.touchListen(event);
            }
        });
        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
    }


    void findViewAll(){
        float sizeText=15;int sizeHeightBt=30;
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
                myButton.setName(s+"");
                myButton.invalidate();
            }
        });
        if(getResources().getConfiguration().orientation==android.content.res.Configuration.ORIENTATION_LANDSCAPE){
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            DimenPer.setStart(getWindowManager().getDefaultDisplay().getWidth()/2,getWindowManager().getDefaultDisplay().getHeight());
        }
        else {
            rootView.setOrientation(LinearLayout.VERTICAL);
            DimenPer.setStart(getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight()/2);
        }

        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPickColor=vi.inflate(R.layout.pick_icon_color,null);
        EditButtonActivity.this.addContentView(viewPickColor, params);

        //cài đặt viewPicColor
        pickColor =findViewById(R.id.viewPIC);
        int onePer=DimenPer.getToWidth(0.015f);
        onePer+=6;pickColor.setPadding(onePer,onePer,onePer,onePer);onePer-=6;
        pickColor.getLayoutParams().width=onePer*17+12;
        pickColor.getLayoutParams().height=onePer*27+12;
        View nextChild=null;
        for(int i=0; i<3; i++) {
            nextChild=((ViewGroup) pickColor).getChildAt(i);
            for (int j = 0; j < 3; j++) {
                ((ViewGroup) nextChild).getChildAt(j).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iconIsPick.setImageDrawable((ColorDrawable) ((ImageButton) v).getDrawable());
                            viewPickColor.setVisibility(View.INVISIBLE);
                        }
                    });
            }
        }
        nextChild=((ViewGroup) pickColor).getChildAt(3);
        ((TextView)nextChild).setTextSize(onePer);
        nextChild.setOnClickListener(new View.OnClickListener() {
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

        //cài đặt lại kích thước text, button
        sizeText=DimenPer.getToHeightF(0.04f);
        editName.setTextSize(DimenPer.getToHeightF(0.032f));
        check1.setTextSize(DimenPer.getToHeightF(0.03f));
        btOk.setTextSize(sizeText);
        btCancel.setTextSize(sizeText);
//        sizeText=DimenPer.getToHeightF(0.02f);
//        zoomOut.setTextSize(sizeText);
//        touchView.setTextSize(sizeText);

//        sizeHeightBt=DimenPer.getToHeight(0.1f);
//        zoomOut.getLayoutParams().height=sizeHeightBt;
//        zoomOut.getLayoutParams().width=sizeHeightBt;
//        touchView.getLayoutParams().height=sizeHeightBt;
//        touchView.getLayoutParams().width=sizeHeightBt;
//        ((FrameLayout.LayoutParams)touchView.getLayoutParams()).setMargins(sizeHeightBt,0,0,0);
        editName.getLayoutParams().height=DimenPer.getToHeight(0.1f);
        sizeHeightBt=DimenPer.getToHeight(0.12f);
        btOk.getLayoutParams().height=sizeHeightBt;
        btCancel.getLayoutParams().height=sizeHeightBt;

        sizeHeightBt = sizeHeightBt+DimenPer.getToHeight(0.01f);
        icon1.getLayoutParams().height = sizeHeightBt;
        icon1.getLayoutParams().width=sizeHeightBt;
        icon2.getLayoutParams().height = sizeHeightBt;
        icon2.getLayoutParams().width=sizeHeightBt;

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

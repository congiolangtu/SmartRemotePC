package com.example.windy.smartremotepc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.windy.smartremotepc.mode.CodeIntent;
import com.example.windy.smartremotepc.mode.DimenPer;
import com.example.windy.smartremotepc.mode.FunctionItem;
import com.example.windy.smartremotepc.mode.ModeButton;
import com.example.windy.smartremotepc.adapter.ListFunctionAdapter;

import com.example.windy.smartremotepc.R;
import com.example.windy.smartremotepc.item.MyButton;

/**
 * Created by Windy on 12/02/2016.
 */
public class EditButtonActivity extends Activity{

    LinearLayout rootView,contentSetup;
    FrameLayout viewItem;
    ImageButton backType,nextType,icon1,icon2;
    TextView zoomOut,touchView,textType,editName;
    Button btOk,btCancel;
    ListView listFunction;
    View pickColor=null;
    View viewPickColor;
    ImageButton iconIsPick;
    MyButton myControl;
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
        listFunction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(EditButtonActivity.this,KeyboardFunctionActivity.class);
                startActivity(intent);
            }
        });
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
                int delta=(int)(8*getResources().getDisplayMetrics().density);
                pickColor.setX(pickColor.getX()+location1[0]-location2[0]-delta);
                pickColor.setY(pickColor.getY()+location1[1]-location2[1]+icon1.getHeight()-delta);
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
                int delta=(int)(8*getResources().getDisplayMetrics().density);
                pickColor.setX(pickColor.getX()+location1[0]-location2[0]-delta);
                pickColor.setY(pickColor.getY()+location1[1]-location2[1]+icon2.getHeight()-delta);
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
        myControl =new MyButton(this);
        myControl.setLayoutParams(new FrameLayout.LayoutParams(myControl.getWidthButton(), myControl.getHeightButton(), Gravity.CENTER));
        myControl.setMode(ModeButton.MODE_VIEW_RESIZE_CREATE);
        myControl.setPad(widthPad,heightPad);
        myControl.setPerHeight(0.2f);
        myControl.setPerWidth(0.2f);
        viewItem.addView(myControl);
        myControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return myControl.touchListen(event);
            }
        });
    }


    void findViewAll(){
        float sizeText=15;int sizeHeightBt=30;
        rootView = (LinearLayout) findViewById(R.id.layoutRootEB);
        contentSetup= (LinearLayout) findViewById(R.id.content_setup);
        viewItem = (FrameLayout) findViewById(R.id.viewItemEB);
        backType = (ImageButton) findViewById(R.id.backTypeEB);
        nextType = (ImageButton) findViewById(R.id.nextTypeEB);
        icon1 = (ImageButton) findViewById(R.id.icon1EB);
        icon2 = (ImageButton) findViewById(R.id.icon2EB);
        zoomOut = (TextView) findViewById(R.id.zoomOutEB);
        touchView = (TextView) findViewById(R.id.touchViewEB);
        textType = (TextView) findViewById(R.id.textTypeEB);
        editName = (TextView) findViewById(R.id.editNameEB);
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
                myControl.setName(s+"");
                myControl.invalidate();
            }
        });
        int onePer;
        if(getResources().getConfiguration().orientation==android.content.res.Configuration.ORIENTATION_LANDSCAPE){
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            DimenPer.setStart(getWindowManager().getDefaultDisplay().getWidth()/2,getWindowManager().getDefaultDisplay().getHeight());
            onePer=DimenPer.getToHeight(0.015f);
        }
        else {
            rootView.setOrientation(LinearLayout.VERTICAL);
            DimenPer.setStart(getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight()/2);
            onePer=DimenPer.getToWidth(0.015f);
        }

        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPickColor=vi.inflate(R.layout.pick_icon_color,null);
        EditButtonActivity.this.addContentView(viewPickColor, params);

        //cài đặt viewPicColor
        pickColor =findViewById(R.id.viewPIC);
        float diviTextsize=getResources().getDisplayMetrics().density/1.25f;
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
                            int color=((ColorDrawable) ((ImageButton) v).getDrawable()).getColor();
                            if(iconIsPick==icon1) myControl.setColorBound1(color);
                            else myControl.setColorBound2(color);
                            if(ColorDrawable.class.isInstance(iconIsPick.getDrawable())) {
                                iconIsPick.setImageDrawable(new ColorDrawable(color));
                                if(iconIsPick==icon1) myControl.setIcon1(new ColorDrawable(color));
                                else myControl.setIcon2(new ColorDrawable(color));
                            }
                            else{
                                iconIsPick.setBackgroundColor(color);
                                if(iconIsPick==icon1) myControl.setColorBound1(color);
                                else myControl.setColorBound2(color);
                            }
                            myControl.invalidate();
                            viewPickColor.setVisibility(View.INVISIBLE);
                        }
                    });
            }
        }
        nextChild=((ViewGroup) pickColor).getChildAt(3);
        ((TextView)nextChild).setTextSize(onePer/diviTextsize);
        nextChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CodeIntent.REQUEST_PICK_IMAGE);
            }
        });
        ((SeekBar)((ViewGroup) pickColor).getChildAt(4)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value=progress*255/100;
                if(iconIsPick==icon1){
                    myControl.setAlpha1(value);
                }
                else {
                    myControl.setAlpha2(value);
                }
                myControl.invalidate();
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
        sizeText=DimenPer.getToHeightF(0.04f)/diviTextsize;
        editName.setTextSize(DimenPer.getToHeightF(0.032f)/diviTextsize);
        btOk.setTextSize(sizeText);
        btCancel.setTextSize(sizeText);
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
                data.putExtra("name", myControl.getName());
                data.putExtra("width", myControl.getWidth());
                data.putExtra("height", myControl.getHeight());
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
        nextType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myControl.setTypeViewControl(1);
            }
        });
        backType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myControl.setTypeViewControl(-1);
            }
        });
        setupContent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeIntent.REQUEST_PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap=BitmapFactory.decodeFile(picturePath);
            iconIsPick.setImageBitmap(bitmap);
            if(iconIsPick==icon1){
                myControl.setIcon1(new BitmapDrawable(bitmap));
            }
            else {
                myControl.setIcon2(new BitmapDrawable(bitmap));
            }
        }
    }

    void setupContent(){
        int type=this.getIntent().getIntExtra("type",0);
        float diviTextsize=DimenPer.getToHeightF(0.03f)/(getResources().getDisplayMetrics().density/1.25f);
        if(type==ModeButton.TYPE_BUTTON){
            CheckBox check=new CheckBox(this);
            check.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            check.setText("View bound");
            check.setTextSize(diviTextsize);
            check.setChecked(true);
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    myControl.setViewBound(isChecked);
                }
            });
            contentSetup.addView(check);
        }
    }
}

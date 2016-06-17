package com.example.windy.smartremotepc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import Mode.CodeIntent;
import Mode.Item;

/**
 * Created by Windy on 02/03/2015.
 */
public class AddItemActivity extends Activity {

    private EditText editName;
    private RadioButton radioWhite;
    private ImageView imageIcon;
    private Spinner spinnerScreen;
    private CheckBox checkFullScreen;
    private CheckBox checkBar;
    private CheckBox checkLayout;
    private CheckBox checkTitle;
    private Button btOK;
    private Button btBack;
    private Item item=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        item = new Item();
        getWidgets();
        addEvent();
    }

    void getWidgets(){
        editName= (EditText) findViewById(R.id.editNameAI);
        radioWhite= (RadioButton) findViewById(R.id.radioWhiteAI);
        imageIcon= (ImageView) findViewById(R.id.pickIconAI);
        spinnerScreen= (Spinner) findViewById(R.id.spinnerAI);
        checkFullScreen= (CheckBox) findViewById(R.id.checkFullScreenAI);
        checkBar= (CheckBox) findViewById(R.id.checkMenuBarAI);
        checkLayout= (CheckBox) findViewById(R.id.checkLayoutAI);
        checkTitle= (CheckBox) findViewById(R.id.checkTitleBarAI);
        btOK= (Button) findViewById(R.id.btOkAI);
        btBack= (Button) findViewById(R.id.btBackAI);

    }

    void addEvent(){
        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddItemActivity.this,PickImageActivity.class);
                intent.putExtra("idImage",R.drawable.menu0);
                intent.putExtra("count",10);
                startActivityForResult(intent, CodeIntent.REQUEST_PICK_IMAGE);
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okEvent();
            }
        });

    }

    void okEvent(){
        if(editName.getText().toString().matches("")){
            Toast.makeText(this,"Enter the name!",Toast.LENGTH_SHORT).show();
        }
        else{
            item.setName(editName.getText() + "");
            item.setRotateScreen(spinnerScreen.getSelectedItemPosition());
            item.setWhiteTheme(radioWhite.isChecked());
            item.setFullScreen(checkFullScreen.isChecked());
            item.setMenuBar(checkBar.isChecked());
            item.setLayout(checkLayout.isChecked());
            item.setTitleBar(checkTitle.isChecked());
            Bundle bundle= new Bundle();
            bundle.putSerializable("item",item);
            Intent intent=new Intent(this,AbsoluteActivity.class);
            intent.putExtra("item",bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CodeIntent.REQUEST_PICK_IMAGE) {
            int idImage = data.getIntExtra("idImage", 0);
            imageIcon.setImageResource(idImage);
            item.setIcon(idImage);
        }
    }
}
